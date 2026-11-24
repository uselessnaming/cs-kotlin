package thread_coroutine

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.coroutines.Continuation

class MyJob {
    private val started = AtomicBoolean(false)
    private var outcome: Result<Int?>? = null

    private val waitingQueue = ArrayDeque<Continuation<Int?>>()
    private val lock = ReentrantLock()

    fun tryStart(): Boolean {
        return started.compareAndSet(false, true)
    }

    fun complete(result: Result<Int?>): List<Continuation<Int?>> {
        val dq = ArrayDeque<Continuation<Int?>>()

        lock.withLock {
            if (outcome != null) throw IllegalStateException("already done")

            outcome = result

            while(waitingQueue.isNotEmpty()) dq.add(waitingQueue.removeFirst())
        }

        return dq
    }

    fun tryRegister(waiter: Continuation<Int?>): Result<Int?>? {
        return lock.withLock {
            if (outcome != null) {
                return@withLock outcome
            } else {
                waitingQueue.add(waiter)
                null
            }
        }
    }
}