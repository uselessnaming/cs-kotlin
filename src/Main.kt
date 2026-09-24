import map.CustomMap
import map.CustomTreeMap

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
//    val map = CustomMap<String, Int>(5)

    val map = mapOf("value" to 1, "next" to 2, "null" to null)

    println("exist value : ${map["value"]}")
    println("non exist value : ${map["a"]}")
    println("null value : ${map["null"]}")
    println("is same? ${map["null"] == map["a"]}")

    val cm = CustomMap<String, Int>(5)
    cm["a"] = 2
    cm["b"] = 3
    println(cm["a"]!! + cm["b"]!!)
    println(cm["eng"])
    println(cm.keys)
    println(cm.values)

    val ctm = CustomTreeMap<Int, Int>()
    ctm[15] = 32
    ctm[31] = 63
    ctm[0] = 1
    ctm.printTree()
    println("keys : ${ctm.getKeys()}")
    println("values : ${ctm.values}")
}