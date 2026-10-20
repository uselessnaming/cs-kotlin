package test_function

import test_function.test_task.countTask
import test_function.test_task.sortTask
import test_function.test_task.sumAllPrime
import kotlin.concurrent.thread

fun checkMultiThreadOverCPU() {
    val threads = listOf (
        thread(start = false) { countTask() },
        thread(start = false) { sumAllPrime(MAX_N / 10) },
        thread(start = false) { sortTask() },
        thread(start = false) { countTask() },
        thread(start = false) { sumAllPrime(MAX_N / 10) },
        thread(start = false) { sortTask() },
        thread(start = false) { countTask() },
        thread(start = false) { sumAllPrime(MAX_N / 10) },
        thread(start = false) { sortTask() },
        thread(start = false) { countTask() },
        thread(start = false) { sumAllPrime(MAX_N / 10) },
        thread(start = false) { sortTask() },
        thread(start = false) { countTask() },
        thread(start = false) { sumAllPrime(MAX_N / 10) },
        thread(start = false) { sortTask() },
    )

    val startTime = System.nanoTime()
    threads.forEach { thread -> thread.start() }
    threads.forEach { thread -> thread.join() }
    println("multi duration over cpu : ${(System.nanoTime() - startTime) / 1_000_000}")
}

fun checkMultiThreadUnderCPU12() {
    val threads = listOf (
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
    )

    val startTime = System.nanoTime()
    threads.forEach { thread -> thread.start() }
    threads.forEach { thread -> thread.join() }
    println("multi duration under cpu 12 in same task : ${(System.nanoTime() - startTime) / 1_000_000}")
}

fun checkMultiThreadUnderCPU11() {
    val threads = listOf (
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
    )

    val startTime = System.nanoTime()
    threads.forEach { thread -> thread.start() }
    threads.forEach { thread -> thread.join() }
    println("multi duration under cpu 12 in same task : ${(System.nanoTime() - startTime) / 1_000_000}")
}

fun checkMultiThreadUnderCPU9() {
    val threads = listOf (
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
    )

    val startTime = System.nanoTime()
    threads.forEach { thread -> thread.start() }
    threads.forEach { thread -> thread.join() }
    println("multi duration under cpu 9 in same task : ${(System.nanoTime() - startTime) / 1_000_000}")
}

fun checkMultiThreadUnderCPU6() {
    val threads = listOf (
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
    )

    val startTime = System.nanoTime()
    threads.forEach { thread -> thread.start() }
    threads.forEach { thread -> thread.join() }
    println("multi duration under cpu 6 in same task : ${(System.nanoTime() - startTime) / 1_000_000}")
}

fun checkMultiThreadOverCPUTmp() {
    val threads = listOf (
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() },
        thread(start = false) { sortTask() }
    )

    val startTime = System.nanoTime()
    threads.forEach { thread -> thread.start() }
    threads.forEach { thread -> thread.join() }
    println("multi duration over cpu in same task : ${(System.nanoTime() - startTime) / 1_000_000}")
}