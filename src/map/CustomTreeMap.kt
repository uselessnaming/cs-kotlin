package map

class CustomTreeMap<K, V> {
    private var head: Entry<K, V>? = null
    private val keys: MutableSet<K> = mutableSetOf()
    val values: List<V>
        get() = getAllValues()

    private data class Entry<K, V>(
        val key: K,
        val value: V,
        var type: Color,
        var parent: Entry<K, V>? = null,
        var leftEntry: Entry<K, V>? = null,
        var rightEntry: Entry<K, V>? = null
    ) {
        fun isRed(): Boolean = type == Color.Red
    }

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
        if (keys.contains(key)) {
            println("해당 key 값은 이미 map에 존재합니다.")
            return null
        }

        keys.add(key)

        if (head == null) {
            head = Entry(
                key = key,
                value = value,
                type = Color.Black
            )
            return value
        }

        var current = head!!
        val keyHash = key.hashCode()

        while (true) {
            val curHash = current.key.hashCode()
            if (keyHash < curHash) {
                val left = current.leftEntry
                if (left == null) {
                    val newEntry = Entry(
                        key = key,
                        value = value,
                        type = Color.Red,
                        parent = current
                    )
                    current.leftEntry = newEntry
                    fixAfterInsert(newEntry)
                    break
                }
                current = left
            } else {
                val right = current.rightEntry
                if (right == null) {
                    val newEntry = Entry(
                        key = key,
                        value = value,
                        type = Color.Red,
                        parent = current
                    )
                    current.rightEntry = newEntry
                    fixAfterInsert(newEntry)
                    break
                }
                current = right
            }
        }

        return value
    }

    private fun fixAfterInsert(inserted: Entry<K, V>) {
        var z = inserted

        while(isRed(z.parent)) {
            val parent = z.parent!!
            val grand = parent.parent ?: break

            if (parent == grand.leftEntry) {
                val uncle = grand.rightEntry
                if (isRed(uncle)) {
                    parent.type = Color.Black
                    uncle!!.type = Color.Black
                    grand.type = Color.Red
                    z = grand
                } else {
                    if (z === parent.rightEntry) {
                        z = parent
                        rotateLeft(z)
                    }

                    z.parent!!.type = Color.Black
                    z.parent!!.parent!!.type = Color.Red
                    rotateRight(z.parent!!.parent!!)
                }
            } else {
                val uncle = grand.leftEntry
                if (isRed(uncle)) {
                    parent.type = Color.Black
                    uncle!!.type = Color.Black
                    grand.type = Color.Red
                    z = grand
                } else {
                    if (z === parent.leftEntry) {
                        z = parent
                        rotateRight(z)
                    }
                    z.parent!!.type = Color.Black
                    z.parent!!.parent!!.type = Color.Red
                    rotateLeft(z.parent!!.parent!!)
                }
            }
        }

        head!!.type = Color.Black
    }

    private fun rotateLeft(entry: Entry<K, V>) {
        val tmp = entry.rightEntry!!
        entry.rightEntry = tmp.leftEntry
        tmp.leftEntry?.parent = entry

        tmp.parent = entry.parent

        when {
            entry.parent == null -> head = tmp
            entry === entry.parent!!.leftEntry -> entry.parent!!.leftEntry = tmp
            else -> entry.parent!!.rightEntry = tmp
        }

        tmp.leftEntry = entry
        entry.parent = tmp
    }

    private fun rotateRight(entry: Entry<K, V>) {
        val tmp = entry.leftEntry!!
        entry.leftEntry = tmp.rightEntry
        tmp.rightEntry?.parent = entry
        tmp.parent = entry.parent

        when {
            entry.parent == null -> head = tmp
            entry === entry.parent!!.rightEntry -> entry.parent!!.rightEntry = tmp
            else -> entry.parent!!.leftEntry = tmp
        }

        tmp.rightEntry = entry
        entry.parent = tmp
    }

    fun getKeys(): List<K> = keys.toList()

    private fun getAllValues(): List<V> {
        val values = mutableListOf<V>()

        fun putValue(cur: Entry<K, V>) {
            if (cur.leftEntry != null) {
                putValue(cur.leftEntry!!)
            }
            values.add(cur.value)
            if (cur.rightEntry != null) {
                putValue(cur.rightEntry!!)
            }
        }

        if (head != null) {
            putValue(head!!)
        }

        return values
    }

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

    private fun isRed(entry: Entry<K, V>?): Boolean = entry != null && entry.isRed()
}