import map.CustomMap

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val name = "Kotlin"
    //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
    // to see how IntelliJ IDEA suggests fixing it.
    println("Hello, " + name + "!")

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
}