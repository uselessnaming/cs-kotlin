import test_function.MAX_N
import test_function.test_task.countTask
import test_function.test_task.sortTask
import test_function.test_task.sumAllPrime
import kotlin.concurrent.thread

fun checkSingleThreadOverCPU() {
    val repeatThread = thread(start = false) {
        repeat(5) {
            countTask()
            sumAllPrime(MAX_N / 10)
            sortTask()
        }
    }

    val startTime = System.nanoTime()
    repeatThread.start()
    repeatThread.join()
    println("single duration over cpu : ${(System.nanoTime() - startTime) / 1_000_000}")
}