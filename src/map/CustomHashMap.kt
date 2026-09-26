package map

class CustomHashMap<K, V>(private var capacity: Int) {
    private val LOAD_FACTOR = 0.75

    private var keyCnt = 0
    private var hashMap = arrayOfNulls<Entry<K, V>?>(capacity)

    operator fun get(key: K): V? {
        val hashKey = getHash(key)
        var current: Entry<K, V>? = hashMap[hashKey] ?: return null

        do {
            if (current!!.key == key) return current.value
            current = current.next
        } while (current != null)

        return null
    }

    fun put(key: K, value: V): V? {
        if (isCapacityOver()) rehashing()

        val hashKey = getHash(key)
        val head = hashMap[hashKey]

        if (head == null) {
            hashMap[hashKey] = Entry(key = key, value = value)
            keyCnt++
            return value
        }

        var current = head

        do {
            if (current!!.key == key) {
                val befValue = current.value
                current.value = value
                return befValue
            }
            if (current.next == null) {
                current.next = Entry(key = key, value = value)
                keyCnt++
                return value
            }
            current = current.next
        } while(true)
    }

    operator fun set(key: K, value: V): V? = put(key, value)

    private fun getHash(key: K): Int {
        val keyHash = key.hashCode()
        return (keyHash xor (keyHash shr 16)) % capacity
    }

    private fun rehashing() {
        keyCnt = 0
        capacity *= 2
        val tmpHashMap = hashMap.copyOf()
        hashMap = arrayOfNulls<Entry<K, V>>(capacity)

        for(idx in tmpHashMap.indices) {
            var cur = tmpHashMap[idx]

            while(cur != null) {
                put(cur.key, cur.value)
                cur = cur.next
            }
        }
    }

    private fun isCapacityOver(): Boolean = keyCnt > capacity * LOAD_FACTOR

    private data class Entry<K, V>(
        val key: K,
        var value: V,
        var next: Entry<K, V>? = null
    )
}