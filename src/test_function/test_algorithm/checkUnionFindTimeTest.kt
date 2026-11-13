package test_function.test_algorithm

import algorithm.UnionFind

fun checkUnionFindLarge() {
    println()
    println(String.format("%-10s %10s %8s %8s %10s %16s", "그래프", "N", "결과", "예상", "평균(ms)", "시간/(N+M) (ns)"))

    for (n in listOf(250_000, 500_000, 1_000_000, 2_000_000)) {
        runLarge("순서 사슬", n, chain(n), 0, n - 1, true)
        runLarge("역순 사슬", n, reverseChain(n), 0, n - 1, true)
        runLarge("별 모양", n, star(n), 1, n - 1, true)
        runLarge("두 덩어리", n, evenOdd(n), 0, 1, false)
    }
}

private fun runLarge(name: String, n: Int, set: Array<IntArray>, a: Int, b: Int, expected: Boolean) {
    try {
        repeat(2) { UnionFind(n).solve(set, a, b) }   // 워밍업 (측정 제외)

        val times = 5
        var result = false
        val start = System.nanoTime()
        repeat(times) { result = UnionFind(n).solve(set, a, b) }
        val avgNs = (System.nanoTime() - start).toDouble() / times

        val ratio = avgNs / (n + set.size)
        val mark = if (result == expected) "" else "  ← 실패"
        println(
            String.format(
                "%-10s %10d %8s %8s %10.1f %16.2f%s",
                name,
                n,
                result,
                expected,
                avgNs / 1_000_000,
                ratio,
                mark
            )
        )
    } catch (e: StackOverflowError) {
        println(String.format("%-10s %10d  StackOverflowError (재귀가 너무 깊음)", name, n))
    }
}

// 순서대로 사슬 구조 -> 최적화 없을 경우 일자 사슬이 되는 경우
private fun chain(n: Int) = Array(n - 1) { intArrayOf(it, it + 1) }

// 역순 사슬
private fun reverseChain(n: Int) = Array(n - 1) { intArrayOf(n - 1 - it, n - 2 - it) }

// 별 모양 -> 0과 나머지 모두 연결
private fun star(n: Int) = Array(n - 1) { intArrayOf(0, it + 1) }

// 두 덩어리 -> 짝수끼리, 홀수끼리만 연결 (0과 1은 다른 그룹
private fun evenOdd(n: Int) = Array(n - 2) { intArrayOf(it, it + 2) }