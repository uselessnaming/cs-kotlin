package test_function

import map.CustomHashMap
import map.CustomMap
import map.CustomTreeMap
import kotlin.random.Random
import kotlin.random.nextInt

fun checkDurationAmongMap() {
    val cm = CustomMap<Int, Int>(30)
    var befTime = System.currentTimeMillis()
    repeat(100000) {
        cm[200000 - it] = it * 2
    }
    var afterTime = System.currentTimeMillis()
    println("put time : ${afterTime - befTime}")
    println("map")

    println(cm.keys)
    println(cm.values)

    println("sorted")
    val sorted = cm.sortedMap()
    println(sorted.keys)
    println(sorted.values)

    println("tree map")
    val ctm = CustomTreeMap<Int, Int>()
    befTime = System.currentTimeMillis()
    repeat(10000000) {
        val n = Random.nextInt(0 until 10000000)
        ctm[n] = n
    }
    afterTime = System.currentTimeMillis()
    println("put time : ${afterTime - befTime}")

    var keys = ctm.getKeys()
    befTime = System.currentTimeMillis()
    for (key in keys) {
        ctm[key]
    }
    afterTime = System.currentTimeMillis() - befTime
    println("get time : ${afterTime}")

    ctm.printTree()
    println("keys : ${ctm.getKeys()}")
    println("values : ${ctm.values}")

    println("hash map")
    val chm = CustomHashMap<Int, Int>(capacity = 100000)
    befTime = System.currentTimeMillis()
    repeat(10000000){
        val n = Random.nextInt(0 until 10000000)
        chm[n] = n
    }
    afterTime = System.currentTimeMillis()
    println("put time : ${afterTime - befTime}")

    keys = chm.getKeys()
    befTime = System.currentTimeMillis()
    for(key in keys) {
        chm[key]
    }
    afterTime = System.currentTimeMillis() - befTime
    println("get time : ${afterTime}")
}