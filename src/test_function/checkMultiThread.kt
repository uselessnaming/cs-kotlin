package test_function

import test_function.test_task.countTask
import test_function.test_task.sortTask
import test_function.test_task.sumAllPrime
import kotlin.concurrent.thread

fun checkMultiThread() {
    val startTime = System.nanoTime()

    val threads = listOf(
        thread(start = false) { countTask() },
        thread(start = false) { sumAllPrime(MAX_N / 50) },
        thread(start = false) { sortTask() }
    )

    threads.forEach { it.start() }

    threads.forEach { it.join() }

    println("multi thread duration : ${(System.nanoTime() - startTime) / 1_000_000}")
}