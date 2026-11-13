package algorithm

class UnionFind(private val n: Int) {
    private val parents = IntArray(n)
    private val size = IntArray(n)

    fun solve(set: Array<IntArray>, targetA: Int, targetB: Int): Boolean {
        makeSet()
        union(set)
        val rootA = find(targetA)
        val rootB = find(targetB)
        return rootA == rootB
    }

    private fun makeSet() {
        for (i in 0 until n) {
            parents[i] = i
            size[i] = i
        }
    }

    private fun union(set: Array<IntArray>) {
        for ((a, b) in set) {
            var rootA = find(a)
            var rootB = find(b)
            if (rootA == rootB) continue
            if (size[rootA] > size[rootB]) {
                val tmp = rootA
                rootA = rootB
                rootB = tmp
            }

            parents[rootA] = rootB
            size[rootB] += size[rootA]
        }
    }

    private fun find(target: Int): Int {
        if (target != parents[target]) parents[target] = find(parents[target])

        return parents[target]
    }
}