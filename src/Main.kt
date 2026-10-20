import thread_coroutine.MyThread
import thread_coroutine.createThread

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val lock = Object()
    var count = 0

    val runnable = Runnable {
        synchronized(lock) {
            repeat(100_000) {
                count++
            }
        }
    }

    val threads = listOf(
        MyThread(runnable),
        MyThread(runnable),
        MyThread(runnable),
        MyThread(runnable),
        MyThread(runnable),
    )

    threads.forEach { thread -> thread.start() }
    threads.forEach { thread -> thread.join() }

    println("예상 값 : 500000 / 실제 값 : $count")
}