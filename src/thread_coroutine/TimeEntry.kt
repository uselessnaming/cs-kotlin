package thread_coroutine

import java.util.concurrent.Delayed
import java.util.concurrent.TimeUnit
import kotlin.coroutines.Continuation

class TimeEntry(
    delayMillis: Long,
    val cont: Continuation<Int?>,
    val value: Int
): Delayed {
    private val triggerAtNanos = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(delayMillis)

    override fun getDelay(unit: TimeUnit): Long {
        return unit.convert(triggerAtNanos - System.nanoTime(), TimeUnit.NANOSECONDS)
    }

    override fun compareTo(other: Delayed): Int {
        if (other !is TimeEntry) throw IllegalArgumentException("other is not TimeEntry")
        val diff = this.triggerAtNanos - other.triggerAtNanos
        return when {
            (diff < 0L) -> -1
            (diff == 0L) -> 0
            else -> 1
        }
    }
}