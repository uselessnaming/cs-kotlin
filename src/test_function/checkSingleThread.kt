package test_function

import test_function.test_task.countTask
import test_function.test_task.sumAllPrime
import java.lang.Thread.sleep
import kotlin.concurrent.thread

const val MAX_N = 5000000

fun checkSingleThread() {
    val taskThread = thread {
        countTask()
        sleep(1000)
        sumAllPrime(MAX_N/50)
    }

    println("task thread start : ${taskThread.threadId()} / ${taskThread.name}")
    taskThread.join()
}