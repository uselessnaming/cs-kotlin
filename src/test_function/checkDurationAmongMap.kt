package test_function

import map.CustomHashMap
import map.CustomMap
import map.CustomTreeMap

fun checkDurationAmongMap() {
    val cm = CustomMap<Int, Int>(30)
    var befTime = System.currentTimeMillis()
    repeat(100000) {
        cm[200000 - it] = it * 2
    }
    var afterTime = System.currentTimeMillis()
    println("put time : ${afterTime - befTime}")
    println("map")

    var befT1 = System.currentTimeMillis()
    println("CustomMap key 150000 : ${cm[200000 - 0]}")
    var duration1 = System.currentTimeMillis() - befT1

    var befT2 = System.currentTimeMillis()
    println("CustomMap key 100000 : ${cm[200000 - 50000]}")
    var duration2 = System.currentTimeMillis() - befT2

    var befT3 = System.currentTimeMillis()
    println("CustomMap key 200000 : ${cm[200000 - 99999]}")
    var duration3 = System.currentTimeMillis() - befT3

    println("get duration average : ${(duration1 + duration2 + duration3) / 3.toDouble()}")

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
        ctm[500000 - it] = it * 2
    }
    afterTime = System.currentTimeMillis()
    println("put time : ${afterTime - befTime}")

    befT1 = System.currentTimeMillis()
    println("CustomTreeMap key 150000 : ${ctm[200000 - 0]}")
    duration1 = System.currentTimeMillis() - befT1

    befT2 = System.currentTimeMillis()
    println("CustomTreeMap key 100000 : ${ctm[200000 - 50000]}")
    duration2 = System.currentTimeMillis() - befT2

    befT3 = System.currentTimeMillis()
    println("CustomTreeMap key 200000 : ${ctm[200000 - 99999]}")
    duration3 = System.currentTimeMillis() - befT3

    println("get duration average : ${(duration1 + duration2 + duration3) / 3.toDouble()}")
    ctm.printTree()
    println("keys : ${ctm.getKeys()}")
    println("values : ${ctm.values}")

    println("hash map")
    val chm = CustomHashMap<Int, Int>(capacity = 100000)
    befTime = System.currentTimeMillis()
    repeat(10000000){
        chm[500000 - it] = it * 2
    }
    afterTime = System.currentTimeMillis()
    println("put time : ${afterTime - befTime}")

    befT1 = System.currentTimeMillis()
    println("CustomHashMap key 150000 : ${chm[200000 - 0]}")
    duration1 = System.currentTimeMillis() - befT1

    befT2 = System.currentTimeMillis()
    println("CustomHashMap key 100000 : ${chm[200000 - 50000]}")
    duration2 = System.currentTimeMillis() - befT2

    befT3 = System.currentTimeMillis()
    println("CustomHashMap key 200000 : ${chm[200000 - 99999]}")
    duration3 = System.currentTimeMillis() - befT3

    println("get duration average : ${(duration1 + duration2 + duration3) / 3.toDouble()}")
}