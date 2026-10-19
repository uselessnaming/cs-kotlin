package test_function

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

private fun countTask() {
    var n = 0

    while (n < MAX_N) n++

    println("count task value : $n")
}

private fun sumAllPrime(limit: Int) {
    var sum = 2

    for (i in 3 until limit) {
        if (isPrime(i)) {
            sum += i
        }
    }

    println("sum all prime value : ${sum}")
}

private fun isPrime(n: Int): Boolean {
    for (i in 2 until n / 2) {
        if (n % i == 0) return false
    }
    return true
}