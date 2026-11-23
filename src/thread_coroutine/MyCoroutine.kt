package thread_coroutine

import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext

class MyCoroutine(
    private val dispatcher: MyDispatcher,
    private val completion: Continuation<Int?>,
    private val awaitValue: (Continuation<Int?>) -> Unit
) : Continuation<Int?> {
    override val context: CoroutineContext = completion.context
    private val continuation = DispatchedContinuation(delegate = this, dispatcher = dispatcher)
    private val started = AtomicBoolean(false)

    private val done = CountDownLatch(1)
    private var outcome: Result<Int?>? = null

    private val lock = ReentrantLock()
    private val waitingQueue = ArrayDeque<Continuation<Int?>>()

    private var label = START
    private var a: Int = 0
    private var y: Int = 0

    fun start() {
        if (!started.compareAndSet(false, true)) throw IllegalStateException("already started")
        continuation.resumeWith(Result.success(null))
    }

    fun resume(value: Int) {
        continuation.resumeWith(Result.success(value))
    }

    override fun resumeWith(result: Result<Int?>) {
        if (label >= DONE) throw IllegalStateException("Coroutine is already done")

        if (result.isFailure) {
            label = DONE
            finish(Result.failure(result.exceptionOrNull()!!))
            return
        }

        val value = result.getOrThrow()

        if (label == START && value != null) throw IllegalStateException("Coroutine is not started")
        if (label != START && value == null) throw IllegalStateException("Coroutine is already started")

        var res: Int? = null

        try {
            when (label) {
                START -> {
                    a = 10
                    println("구간1: a=$a")
                    label = 1
                    awaitValue(continuation)
                }

                1 -> {
                    y = value!!
                    println("구간2: y=$y")
                    label = 2
                    awaitValue(continuation)
                }

                2 -> {
                    val z = a + y + value!!
                    println("구간3: z=$z")
                    label = DONE
                    res = z
                }

                else -> {
                    throw IllegalStateException("label value is not available")
                }
            }
        } catch (e: Exception) {
            label = DONE
            finish(Result.failure(e))
            return
        }

        if (res != null) {
            finish(Result.success(res))
        }
    }

    private fun finish(result: Result<Int?>) {
        val toWake = lock.withLock {
            outcome = result
            done.countDown()

            val jobs = mutableListOf<Continuation<Int?>>()

            while(waitingQueue.isNotEmpty()) jobs.add(waitingQueue.removeFirst())

            return@withLock jobs
        }

        try {
            completion.resumeWith(result)
        } catch (e: Exception) {
            println("Error occur : ${e.localizedMessage}")
            throw e
        } finally {
            toWake.forEach { job ->
                try {
                    job.resumeWith(result)
                } catch (e: Exception) {
                    println("Error : ${e.localizedMessage}")
                }
            }
        }
    }

    fun join(waiter: Continuation<Int?>) {
        val finished: Result<Int?>? = lock.withLock {
            if (outcome != null) {
                return@withLock outcome
            } else {
                waitingQueue.add(waiter)
                return@withLock null
            }
        }

        if (finished != null) waiter.resumeWith(finished)
    }

    companion object {
        const val DONE = 3
        const val START = 0
    }
}