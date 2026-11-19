import thread_coroutine.MyCoroutine
import thread_coroutine.MyDispatcher
import java.util.concurrent.CountDownLatch
import kotlin.concurrent.thread
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.resume

fun main() {
    val values = ArrayDeque(listOf(100, 200))
    val latch = CountDownLatch(1)
    val completion = object : Continuation<Int?> {
        override val context: CoroutineContext = EmptyCoroutineContext
        override fun resumeWith(result: Result<Int?>) {
            println("(completion) $result on ${Thread.currentThread().name}")
            latch.countDown()
        }
    }
    val dispatcher = MyDispatcher()
    val awaitValue: (Continuation<Int?>) -> Unit = { cont ->
        val v = values.removeFirst()
        thread(name = "timer-$v") {
            Thread.sleep(200)
            println("[${Thread.currentThread().name}] resume($v)")
            cont.resume(v)
        }
    }
    val coroutine = MyCoroutine(dispatcher, completion, awaitValue)

    println("[main] start 호출")
    coroutine.start()
    println("[main] start 반환 (아직 완료 전)")
    latch.await()
    println("[main] 종료")
}