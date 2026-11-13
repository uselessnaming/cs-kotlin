package test_function.test_algorithm

import algorithm.UnionFind
import test_function.util.check

fun checkUnionFind() {
    // TC1: 직접 연결
    check("TC1 직접 연결", UnionFind(2).solve(arrayOf(intArrayOf(0, 1)), 0, 1), true)

    // TC2: 서로 다른 그룹
    check("TC2 다른 그룹", UnionFind(4).solve(arrayOf(intArrayOf(0, 1), intArrayOf(2, 3)), 0, 2), false)

    // TC3: 공통 원소를 통한 간접 연결 (1 - 0 - 2)
    check("TC3 간접 연결", UnionFind(3).solve(arrayOf(intArrayOf(0, 1), intArrayOf(0, 2)), 1, 2), true)

    // TC4: 여러 명짜리 그룹끼리 합치기 ({0,1} + {2,3})
    check("TC4 그룹끼리 합치기", UnionFind(4).solve(arrayOf(intArrayOf(0, 1), intArrayOf(2, 3), intArrayOf(1, 3)), 0, 2), true)

    // TC5: 자기 자신과의 간선이 기존 연결을 끊지 않는지
    check("TC5 자기 자신 간선", UnionFind(2).solve(arrayOf(intArrayOf(0, 1), intArrayOf(0, 0)), 0, 1), true)

    // TC6: 같은 원소끼리 질문
    check("TC6 같은 원소 질문", UnionFind(3).solve(arrayOf(), 1, 1), true)

    // TC7: 간선이 하나도 없음
    check("TC7 간선 없음", UnionFind(3).solve(arrayOf(), 0, 2), false)

    // TC8: 사이클과 중복 간선 (0-1-2-0, 0-1 한 번 더)
    check("TC8 사이클·중복 간선", UnionFind(3).solve(arrayOf(intArrayOf(0, 1), intArrayOf(1, 2), intArrayOf(2, 0), intArrayOf(0, 1)), 0, 2), true)

    // TC9: 큰 번호 → 작은 번호 순서로 연결
    check("TC9 역순 연결", UnionFind(4).solve(arrayOf(intArrayOf(3, 2), intArrayOf(2, 1), intArrayOf(1, 0)), 3, 0), true)

    // TC10: 사슬 끝에서 하나만 떨어져 있음 (0-1-2-3, 4는 혼자)
    check("TC10 하나만 떨어짐", UnionFind(5).solve(arrayOf(intArrayOf(0, 1), intArrayOf(1, 2), intArrayOf(2, 3)), 0, 4), false)
}