package algorithm

import java.util.PriorityQueue

class Dijkstra(private val n: Int) {
    private val graph = mutableMapOf<Int, MutableList<Edge>>().apply {
        for (i in 0 until n) {
            this[i] = mutableListOf()
        }
    }

    fun solve(g: Array<IntArray>, s: Int, t: Int): Int {
        graph.clear()
        initGraph(g)

        val costs = IntArray(n) { INF }
        val visited = BooleanArray(n)
        costs[s] = 0

        val pq = PriorityQueue<Pair<Int, Int>>(compareBy { it.second })
        pq.add(s to 0)

        while (pq.isNotEmpty()) {
            val (cur, cost) = pq.poll()
            if (visited[t]) continue
            visited[cur] = true

            for ((next, c) in graph[cur]!!) {
                if (visited[next]) continue

                val newCost = c + cost
                if (newCost < costs[next]) {
                    costs[next] = newCost
                    pq.add(next to newCost)
                }
            }
        }

        return costs[t]
    }

    private fun initGraph(graph: Array<IntArray>) {
        for ((s, t, c) in graph) {
            this.graph[s]!!.add(Edge(t, c))
        }
    }

    data class Edge(
        val to: Int,
        val cost: Int
    )

    companion object {
        const val INF = Int.MAX_VALUE
    }
}