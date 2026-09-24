import map.CustomMap
import map.CustomTreeMap

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val cm = CustomMap<Int, Int>(30)
    var befTime = System.currentTimeMillis()
    repeat(50000) {
        cm[200000 - it] = it * 2
    }
    var afterTime = System.currentTimeMillis()
    println("put time : ${afterTime - befTime}")
    println("map")
//    println(cm.keys)
//    println(cm.values)

    println("sorted")
    val sorted = cm.sortedMap()
//    println(sorted.keys)
//    println(sorted.values)

    println("tree map")
    val ctm = CustomTreeMap<Int, Int>()
    befTime = System.currentTimeMillis()
    repeat(50000) {
        ctm[200000 - it] = it * 2
    }
    afterTime = System.currentTimeMillis()
    println("put time : ${afterTime - befTime}")
//    ctm.printTree()
//    println("keys : ${ctm.getKeys()}")
//    println("values : ${ctm.values}")
}