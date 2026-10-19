package test_function.test_task

private fun sumAllPrime(limit: Int) {
    var sum = 2

    for (i in 3 until limit) {
        if (isPrime(i)) {
            sum += i
        }
    }

    println("sum all prime value : ${sum}")
}

private fun isPrime(n: Int): Boolean {
    for (i in 2 until n / 2) {
        if (n % i == 0) return false
    }
    return true
}