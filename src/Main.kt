import thread_coroutine.MyThread
import thread_coroutine.createThread

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val lock = Object()
    var count = 0

    val runnable1 = Runnable {
        synchronized(lock) {
            repeat(100_000) {
                count++
            }
        }
    }

    val thread1 = Thread(runnable1)
    var startTime = System.nanoTime()
    thread1.start()
    thread1.join()
    println("repeat in lock duration : ${System.nanoTime() - startTime}")

    val runnable2 = Runnable {
        repeat(100_000) {
            synchronized(lock) {
                count++
            }
        }
    }

    val thread2 = Thread(runnable2)
    startTime = System.nanoTime()
    thread2.start()
    thread2.join()
    println("repeat out lock duration : ${System.nanoTime() - startTime}")
}