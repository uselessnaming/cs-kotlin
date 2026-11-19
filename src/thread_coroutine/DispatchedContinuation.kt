package thread_coroutine

import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext

class DispatchedContinuation(
    private val delegate: Continuation<Int?>,
    private val dispatcher: MyDispatcher
) : Continuation<Int?> {
    override val context: CoroutineContext = delegate.context

    override fun resumeWith(result: Result<Int?>) {
        dispatcher.dispatch { delegate.resumeWith(result) }
    }
}