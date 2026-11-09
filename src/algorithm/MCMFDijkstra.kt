package algorithm

import java.util.PriorityQueue

class MCMFDijkstra(private val n: Int) {
    private val graph = Array(n) { ArrayList<Edge>() }

    private val h = IntArray(n)

    fun solve(s: Int, t: Int): Pair<Int, Int> {
        var totalFlow = 0
        var totalCost = 0

        initPotential(s)

        while (true) {
            val (dist, prevNode, prevEdge) = dijkstra(s)
            if (dist[t] == INF) break

            for (v in 0 until n) {
                if (dist[v] != INF) h[v] += dist[v]
            }

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
            totalCost += f * h[t]
            println("보냄: $f 개, 경로 비용 ${h[t]} -> 누적 유량 $totalFlow, 누적 비용 $totalCost")
        }

        return Pair(totalFlow, totalCost)
    }

    fun addEdge(u: Int, v: Int, cap: Int, cost: Int) {
        val forward = Edge(v, cap, cost, graph[v].size)
        val backward = Edge(u, 0, -cost, graph[u].size)
        graph[u].add(forward)
        graph[v].add(backward)
    }

    private fun initPotential(s: Int) {
        h.fill(INF)
        val inQueue = BooleanArray(n)
        val q = ArrayDeque<Int>()
        h[s] = 0
        q.add(s)
        inQueue[s] = true

        while (q.isNotEmpty()) {
            val u = q.removeFirst()
            inQueue[u] = false
            for (e in graph[u]) {
                if (e.cap > 0 && h[u] + e.cost < h[e.to]) {
                    h[e.to] = h[u] + e.cost
                    if (!inQueue[e.to]) {
                        q.add(e.to)
                        inQueue[e.to] = true
                    }
                }
            }
        }
    }

    private fun dijkstra(s: Int): Triple<IntArray, IntArray, IntArray> {
        val dist = IntArray(n) { INF }
        val prevNode = IntArray(n) { -1 }
        val prevEdge = IntArray(n) { -1 }
        val pq = PriorityQueue<Pair<Int, Int>>(compareBy { it.first })

        dist[s] = 0
        pq.add(Pair(0, s))

        while(pq.isNotEmpty()) {
            val (d, u) = pq.poll()
            if (d > dist[u]) continue

            for ((i, e) in graph[u].withIndex()) {
                if (e.cap <= 0) continue

                val reduced = e.cost + h[u] - h[e.to]
                if (dist[u] + reduced < dist[e.to]) {
                    dist[e.to] = dist[u] + reduced
                    prevNode[e.to] = u
                    prevEdge[e.to] = i
                    pq.add(Pair(dist[e.to], e.to))
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