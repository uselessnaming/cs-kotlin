package thread_coroutine

import java.lang.AutoCloseable
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class MyDispatcher: AutoCloseable {
    private val queue = LinkedBlockingQueue<Runnable>()
    private var closed = false
    private val stop = Runnable {  }
    private val lock = ReentrantLock()

    private val worker: Thread = Thread {
        while(true) {
            try {
                val job = queue.take()
                if (job === stop) break

                job.run()
            } catch (e: Exception) {
                println("Error : ${e.localizedMessage}")
            }
        }
    }

    init {
        worker.isDaemon = true
        worker.start()
    }

    fun dispatch(runnable: Runnable) {
        lock.withLock {
            if (closed) throw IllegalStateException("this dispatcher is already closed")

            queue.add(runnable)
        }
    }

    override fun close() {
        lock.withLock {
            if (closed) {
                println("dispatcher is already closed")
                return
            }

            closed = true
            queue.add(stop)
        }
    }
}