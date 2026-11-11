package test_function.test_algorithm

import algorithm.Dijkstra
import kotlin.math.log2
import kotlin.random.Random

fun checkDijkstraTimeTest() {
    println()
    println(String.format("%10s %10s %12s %22s", "V", "E", "평균(ms)", "시간 / (E * log V) (ns)"))

    var v = 10_000
    repeat(5) {
        val e = v * 10
        val g = makeGraph(v, e, seed = v)

        repeat(3) { Dijkstra(v).solve(g, 0, v - 1) }

        val times = 5
        val start = System.nanoTime()
        repeat(times) { Dijkstra(v).solve(g, 0, v - 1) }
        val avgNs = (System.nanoTime() - start).toDouble() / times

        val ratio = avgNs / (e * log2(v.toDouble()))
        println(String.format("%10d %10d %12.1f %22.2f", v, e, avgNs / 1_000_000, ratio))

        v *= 2
    }
}

private fun makeGraph(v: Int, e: Int, seed: Int): Array<IntArray> {
    val rnd = Random(seed)
    val edges = ArrayList<IntArray>(e)
    for (i in 0 until v - 1) edges.add(intArrayOf(i, i + 1, rnd.nextInt(1, 1001)))
    while (edges.size < e) edges.add(intArrayOf(rnd.nextInt(v), rnd.nextInt(v), rnd.nextInt(1, 1001)))
    return edges.toTypedArray()
}