package thread_coroutine

import java.util.concurrent.LinkedBlockingQueue

class MyDispatcher {
    private val queue = LinkedBlockingQueue<Runnable>()
    private val worker: Thread = Thread {
        while(true) {
            try {
                val job = queue.take()
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
        queue.add(runnable)
    }
}