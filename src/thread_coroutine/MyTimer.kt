package thread_coroutine

import java.util.concurrent.DelayQueue
import kotlin.coroutines.Continuation

object MyTimer {
    private val queue = DelayQueue<TimeEntry>()

    private val worker: Thread = Thread {
        while (true) {
            try {
                val entry = queue.take()
                entry.cont.resumeWith(Result.success(entry.value))
            } catch (e: Exception) {
                println(e.localizedMessage)
            }
        }
    }

    init {
        worker.isDaemon = true
        worker.start()
    }

    fun schedule(delayMillis: Long, cont: Continuation<Int?>, value: Int) {
        queue.add(TimeEntry(delayMillis, cont, value))
    }
}