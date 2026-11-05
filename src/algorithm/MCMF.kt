package algorithm

class MCMF(private val n: Int) {
    private val graph = Array(n) { ArrayList<Edge>() }

    fun solve(s: Int, t: Int): Pair<Int, Int> {
        var totalFlow = 0
        var totalCost = 0

        while (true) {
            val (dist, prevNode, prevEdge) = spfa(s)
            if (dist[t] == INF) break

            var f = INF
            var v = t
            while (v != s) {
                val u = prevNode[v]
                val e = graph[u][prevEdge[v]]
                f = minOf(f, e.cap)
                v = u
            }

            v = t
            while (v != s) {
                val u = prevNode[v]
                val e = graph[u][prevEdge[v]]
                e.cap -= f
                graph[v][e.rev].cap += f
                v = u
            }

            totalFlow += f
            totalCost += f * dist[t]
            println(" 보냄: $f 개, 경로 비용 ${dist[t]} -> 누적 유량 $totalFlow, 누적 비용 $totalCost")
        }

        return Pair(totalFlow, totalCost)
    }

    fun addEdge(u: Int, v: Int, cap: Int, cost: Int) {
        val forward = Edge(v, cap, cost, graph[v].size)
        val backward = Edge(u, 0, -cost, graph[u].size)
        graph[u].add(forward)
        graph[v].add(backward)
    }

    private fun spfa(s: Int): Triple<IntArray, IntArray, IntArray> {
        val dist = IntArray(n) { INF }
        val inQueue = BooleanArray(n)
        val prevNode = IntArray(n) { -1 }
        val prevEdge = IntArray(n) { -1 }

        dist[s] = 0
        val q = ArrayDeque<Int>()
        q.add(s)
        inQueue[s] = true

        while (q.isNotEmpty()) {
            val u = q.removeFirst()
            inQueue[u] = false
            for ((i, e) in graph[u].withIndex()) {
                if (e.cap > 0 && dist[u] + e.cost < dist[e.to]) {
                    dist[e.to] = dist[u] + e.cost
                    prevNode[e.to] = u
                    prevEdge[e.to] = i
                    if (!inQueue[e.to]) {
                        q.add(e.to)
                        inQueue[e.to] = true
                    }
                }
            }
        }
        return Triple(dist, prevNode, prevEdge)
    }

    data class Edge(
        val to: Int,
        var cap: Int,
        val cost: Int,
        val rev: Int
    )

    companion object {
        const val INF = Int.MAX_VALUE
    }
}