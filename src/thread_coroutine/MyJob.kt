package thread_coroutine

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.coroutines.Continuation
import kotlin.coroutines.cancellation.CancellationException

class MyJob {
    private var outcome: Result<Int?>? = null
    private var state = State.NEW

    private val waitingQueue = ArrayDeque<Continuation<Int?>>()
    private val lock = ReentrantLock()

    private var cancelCause: CancellationException? = null

    fun tryStart(): Boolean {
        return lock.withLock {
            if (state == State.NEW) {
                state = State.ACTIVE
                true
            } else {
                false
            }
        }
    }

    fun tryCancel(): CancelResult {
        return lock.withLock {
            when(state) {
                State.NEW -> {
                    cancelCause = CancellationException()
                    state = State.CANCELLING
                    CancelResult.FINISH_NOW
                }
                State.ACTIVE -> {
                    cancelCause = CancellationException()
                    state = State.CANCELLING
                    CancelResult.REQUESTED
                }
                else -> {
                    CancelResult.IGNORED
                }
            }
        }
    }

    fun complete(result: Result<Int?>): CompleteResult {
        return lock.withLock {
            val dq = ArrayDeque<Continuation<Int?>>()
            when(state) {
                State.ACTIVE -> {
                    outcome = result
                    if (result.isSuccess) {
                        state = State.COMPLETED
                    } else {
                        state = State.CANCELLED
                    }
                }
                State.CANCELLING -> {
                    val cause = cancelCause!!
                    if (result.isSuccess) {
                        outcome = Result.failure(cause)
                    } else {
                        cause.addSuppressed(result.exceptionOrNull()!!)
                        outcome = Result.failure(cause)
                    }
                    state = State.CANCELLED
                }
                State.NEW -> throw IllegalStateException("not started")
                else -> throw IllegalStateException("already done")
            }

            while (waitingQueue.isNotEmpty()) dq.add(waitingQueue.removeFirst())

            CompleteResult(outcome!!, dq)
        }
    }

    fun tryRegister(waiter: Continuation<Int?>): Result<Int?>? {
        return lock.withLock {
            if (state == State.COMPLETED || state == State.CANCELLED) {
                outcome
            } else {
                waitingQueue.add(waiter)
                null
            }
        }
    }

    fun getCancelCause(): CancellationException? = lock.withLock { cancelCause }

    data class CompleteResult(
        val outcome: Result<Int?>,
        val waiters: List<Continuation<Int?>>
    )

    enum class State {
        NEW, ACTIVE, CANCELLING, CANCELLED, COMPLETED
    }

    enum class CancelResult {
        FINISH_NOW, REQUESTED, IGNORED
    }
}