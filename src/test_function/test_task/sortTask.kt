package test_function.test_task

import test_function.MAX_N
import kotlin.random.Random

fun sortTask() {
    val arr = IntArray(MAX_N / 50) { Random.nextInt() }

    for (i in arr.indices) {
        for (j in 0 until arr.size - i + 1) {
            if (arr[j] < arr[j + 1]) {
                val tmp = arr[j]
                arr[j] = arr[j + 1]
                arr[j + 1] = tmp
            }
        }
    }
}