package thread_coroutine

import test_function.test_task.countTask
import java.util.concurrent.Executors
import kotlin.concurrent.thread

fun createThread() {
    val method1 = thread {
        countTask()
    }

    val method2 = Thread2()
    method2.start()

    val method3 = Runnable {
        countTask()
    }
    Thread(method3).start()

    val method4 = Thread {
        countTask()
    }
    method4.start()

    val executor = Executors.newFixedThreadPool(1)
    executor.submit {
        countTask()
    }
    executor.shutdown()
}

class Thread2 : Thread() {
    override fun run() {
        countTask()
    }
}