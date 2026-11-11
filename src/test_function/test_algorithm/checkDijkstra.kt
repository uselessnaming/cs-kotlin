package test_function.test_algorithm

import algorithm.Dijkstra
import test_function.util.check

fun checkDijkstra() {
    val tc1 = arrayOf(
        intArrayOf(0, 1, 1),
        intArrayOf(1, 2, 1),
        intArrayOf(0, 2, 5),
    )
    check("TC1", Dijkstra(3).solve(tc1, 0, 2), 2)

    val tc2 = arrayOf(
        intArrayOf(0, 1, 5),
        intArrayOf(0, 2, 1),
        intArrayOf(2, 1, 1),
        intArrayOf(1, 3, 1),
    )
    check("TC2", Dijkstra(4).solve(tc2, 0, 3), 3)

    val tc3 = arrayOf(
        intArrayOf(0, 1, 2),
        intArrayOf(2, 3, 1),
    )
    check("TC3", Dijkstra(4).solve(tc3, 0, 3), Dijkstra.INF)
}

