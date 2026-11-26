package thread_coroutine

import thread_coroutine.MyJob.StartResult
import java.util.concurrent.CountDownLatch
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.cancellation.CancellationException

class MyCoroutine(
    private val dispatcher: MyDispatcher,
    private val completion: Continuation<Int?>,
    private val awaitValue: (Continuation<Int?>) -> Unit
) : Continuation<Int?> {
    override val context: CoroutineContext = completion.context

    private val job = MyJob()
    private val continuation = DispatchedContinuation(delegate = this, dispatcher = dispatcher)

    private var label = START
    private var a: Int = 0
    private var y: Int = 0

    fun start() {
        when(job.tryStart()) {
            StartResult.ALREADY_STARTED -> throw IllegalStateException("already started")
            StartResult.CANCELLED -> throw CancellationException("coroutine is already cancelled")
            StartResult.STARTED -> continuation.resumeWith(Result.success(null))
        }
    }

    fun resume(value: Int) {
        continuation.resumeWith(Result.success(value))
    }

    override fun resumeWith(result: Result<Int?>) {
        if (job.isCancelRequested()) {
            if (result.isSuccess || label >= DONE) return
        }

        if (label >= DONE) throw IllegalStateException("Coroutine is already done")

        if (result.isFailure) {
            label = DONE
            finish(Result.failure(result.exceptionOrNull()!!))
            return
        }

        val value = result.getOrThrow()

        if (label == START && value != null) throw IllegalStateException("Coroutine is not started")
        if (label != START && value == null) throw IllegalStateException("Coroutine is already started")

        var res: Int? = null

        try {
            when (label) {
                START -> {
                    a = 10
                    println("구간1: a=$a")
                    label = 1
                    awaitValue(continuation)
                }

                1 -> {
                    y = value!!
                    println("구간2: y=$y")
                    label = 2
                    awaitValue(continuation)
                }

                2 -> {
                    val z = a + y + value!!
                    println("구간3: z=$z")
                    label = DONE
                    res = z
                }

                else -> {
                    throw IllegalStateException("label value is not available")
                }
            }
        } catch (e: Throwable) {
            label = DONE
            finish(Result.failure(e))
            return
        }

        if (res != null) {
            finish(Result.success(res))
        }
    }

    private fun finish(result: Result<Int?>) {
        var first: Throwable? = null
        fun record(e: Throwable) {
            if (first == null) first = e
            else first.addSuppressed(e)
        }

        val (outcome, toWake) = job.complete(result)

        try {
            completion.resumeWith(outcome)
        } catch (e: Throwable) {
            println("Error occur : ${e.localizedMessage}")
            record(e)
        }

        toWake.forEach { waiter ->
            try {
                waiter.resumeWith(outcome)
            } catch (e: Throwable) {
                record(e)
            }
        }

        if (first != null) throw first
    }

    fun join(waiter: Continuation<Int?>) {
        val finished: Result<Int?>? = job.tryRegister(waiter)

        if (finished != null) waiter.resumeWith(finished)
    }

    fun joinBlocking(): Result<Int?> {
        val latch = CountDownLatch(1)
        var holder: Result<Int?>? = null
        val waiter = Continuation<Int?>(EmptyCoroutineContext) { res ->
            holder = res
            latch.countDown()
        }
        join(waiter)
        latch.await()

        return holder!!
    }

    fun cancel() {
        when (job.tryCancel()) {
            MyJob.CancelResult.REQUESTED -> { continuation.resumeWith(Result.failure(job.getCancelCause()!!)) }
            MyJob.CancelResult.FINISH_NOW -> { finish(Result.failure(job.getCancelCause()!!)) }
            MyJob.CancelResult.IGNORED -> { /** 무시되면 그냥 넘어간다 ***/ }
        }
    }

    companion object {
        const val DONE = 3
        const val START = 0
    }
}