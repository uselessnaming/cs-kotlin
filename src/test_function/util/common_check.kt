package test_function.util

fun check(name: String, actual: Any, expected: Any) {
    val result = if (actual == expected) "통과" else "실패"
    println("$name: 결과 = $actual, 예상 = $expected -> $result")
}