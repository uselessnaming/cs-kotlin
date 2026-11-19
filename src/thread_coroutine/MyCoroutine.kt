package thread_coroutine

import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext

class MyCoroutine(
    private val dispatcher: MyDispatcher,
    private val completion: Continuation<Int>,
    private val awaitValue: (Continuation<Int?>) -> Unit
) : Continuation<Int?> {
    override val context: CoroutineContext = completion.context
    private val continuation = DispatchedContinuation(delegate = this, dispatcher = dispatcher)
    private var label = START
    private var a: Int = 0
    private var y: Int = 0

    fun start() {
        continuation.resumeWith(Result.success(null))
    }

    fun resume(value: Int) {
        continuation.resumeWith(Result.success(value))
    }

    override fun resumeWith(result: Result<Int?>) {
        if (label >= DONE) throw IllegalStateException("Coroutine is already done")

        if (result.isFailure) {
            label = DONE
            completion.resumeWith(Result.failure(result.exceptionOrNull()!!))
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
        } catch (e: Exception) {
            label = DONE
            completion.resumeWith(Result.failure(e))
            return
        }

        if (res != null) {
            completion.resumeWith(Result.success(res))
        }
    }

    companion object {
        const val DONE = 3
        const val START = 0
    }
}