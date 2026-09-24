package map

class CustomTreeMap<K, V> {
    private var head: Entry<K, V>? = null
    private val keys: MutableSet<K> = mutableSetOf()

    private data class Entry<K, V>(
        val key: K,
        val value: V,
        var type: Color,
        var parent: Entry<K, V>? = null,
        var leftEntry: Entry<K, V>? = null,
        var rightEntry: Entry<K, V>? = null
    )

    private enum class Color {
        Red, Black
    }

    operator fun get(key: K): V? {
        if (head == null) return null

        var currentEntry = head
        val targetValue = key.hashCode()

        while (currentEntry != null) {
            val midValue = currentEntry.key.hashCode()

            if (midValue == targetValue) return currentEntry.value
            else if (midValue > targetValue) currentEntry = currentEntry.leftEntry
            else currentEntry = currentEntry.rightEntry
        }

        return null
    }

    fun put(key: K, value: V): V? {
        println("=====================")
        if (keys.contains(key)) {
            println("해당 key 값은 이미 map에 존재합니다.")
            return null
        }

        keys.add(key)

        val newEntry = Entry(
            key = key,
            value = value,
            type = Color.Red
        )

        if (head == null) {
            newEntry.type = Color.Black
            head = newEntry
            return value
        }

        var current = head
        val keyHash = key.hashCode()

        while (current != null) {
            if (current.key.hashCode() > keyHash) {
                if (current.leftEntry == null) {
                    newEntry.parent = current
                    current.leftEntry = newEntry
                    // 여기서 삼촌 node type을 확인하고 restructuring or recoloring 진행
                    updateTree(newEntry)
                    break
                }
                current = current.leftEntry
            } else {
                if (current.rightEntry == null) {
                    newEntry.parent = current
                    current.rightEntry = newEntry
                    // 여기서 삼촌 node type을 확인하고 restructuring or recoloring 진행
                    updateTree(newEntry)
                    break
                }
                current = current.rightEntry
            }
        }

        return value
    }

    private fun updateTree(newEntry: Entry<K, V>) {
        var current = newEntry

        while (current.parent != null) {
            if (current.type == Color.Red && current.parent!!.type == Color.Red) {
                val grandNode = current.parent!!.parent!!
                val rightType = grandNode.rightEntry?.type ?: Color.Black
                val leftType = grandNode.leftEntry?.type ?: Color.Black

                if (rightType == leftType) {
                    recoloring(current)
                } else {
                    reconstructing(current)
                }
            }

            if (current.parent == null) break

            current = current.parent!!
        }

        head!!.type = Color.Black
    }

    private fun recoloring(newEntry: Entry<K, V>) {
        val parent = newEntry.parent ?: return
        val grand = parent.parent ?: return

        grand.type = Color.Red
        if (grand.rightEntry != null) grand.rightEntry!!.type = Color.Black
        if (grand.leftEntry != null) grand.leftEntry!!.type = Color.Black
    }

    private fun reconstructing(newEntry: Entry<K, V>) {
        val child = newEntry
        val parent = child.parent!!.apply {
            if (this.leftEntry != null && this.leftEntry == child) {
                this.leftEntry = null
            }
            if (this.rightEntry != null && this.rightEntry == child) {
                this.rightEntry = null
            }
        }
        val grand = parent.parent!!.apply {
            if (this.leftEntry != null && this.leftEntry == parent) {
                this.leftEntry = null
            }
            if (this.rightEntry != null && this.rightEntry == parent) {
                this.rightEntry = null
            }
        }
        val greatest = grand.parent

        val sorted = listOf(child, parent, grand).sortedWith(compareBy<Entry<K, V>> { it.key.hashCode() })

        child.parent = null
        parent.parent = null
        grand.parent = null

        sorted[1].leftEntry = sorted[0]
        sorted[1].rightEntry = sorted[2]
        sorted[0].parent = sorted[1]
        sorted[2].parent = sorted[1]

        sorted[1].type = Color.Black
        sorted[0].type = Color.Red
        sorted[2].type = Color.Red

        if (greatest == null) {
            head = sorted[1]
            return
        }

        if (greatest.key.hashCode() < grand.key.hashCode()) {
            greatest.rightEntry = sorted[1]
        } else {
            greatest.leftEntry = sorted[1]
        }
        sorted[1].parent = greatest
    }

    private fun getKeys(): List<K> = keys.toList()

    operator fun set(key: K, value: V): V? = put(key, value)

    fun printTree() {
        if (head == null) {
            println("tree is empty")
            return
        }

        printNode(head!!)
    }

    private fun printNode(entry: Entry<K, V>) {
        if (entry.leftEntry != null) {
            printNode(entry.leftEntry!!)
        }

        println("key value: ${entry.key} ${entry.value}")

        if (entry.rightEntry != null) {
            printNode(entry.rightEntry!!)
        }
    }
}