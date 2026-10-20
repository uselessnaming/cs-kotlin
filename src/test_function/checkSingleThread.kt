package test_function

import test_function.test_task.countTask
import test_function.test_task.sortTask
import test_function.test_task.sumAllPrime
import kotlin.concurrent.thread

const val MAX_N = 5_000_000

fun checkSingleThread() {
    val taskThread = thread {
        val startTime = System.nanoTime()
        countTask()
        sumAllPrime(MAX_N/10)
        sortTask()
        println("single thread duration : ${(System.nanoTime() - startTime) / 1_000_000}")
    }

    taskThread.join()
}