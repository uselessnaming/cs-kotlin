package map

class CustomMap<K, V>(private var capacity: Int) {
    private class Entry<K, V>(val key: K, var value: V)

    val size
        get() = keys.size

    private var entries = arrayOfNulls<Entry<K, V>>(capacity)

    val keys
        get() = entries.filterNotNull().map { it.key }

    val values
        get() = entries.filterNotNull().map { it.value }

    operator fun get(key: K): V? {
        for (i in 0 until capacity) {
            val entry = entries[i]
            if (entry?.key == key) return entry.value
        }

        return null
    }

    fun put(key: K, value: V) {
        for (i in 0 until capacity) {
            val entry = entries[i]

            if (entry?.key == key) {
                entry.value = value
                return
            }
        }

        if (isFull()) {
            capacity *= 2
            entries = entries.copyOf(capacity)
        }

        for (idx in 0 until capacity) {
            val entry = entries[idx]

            if (entry == null) {
                entries[idx] = Entry(key, value)
                return
            }
        }
    }

    operator fun set(key: K, value: V) = put(key, value)

    fun sortedMap(): CustomMap<K, V> {
        val hashIndex = keys.withIndex().map { it.index to it.value.hashCode() }
            .sortedBy { it.second }
            .map { it.first }
        val newMap = CustomMap<K, V>(this.keys.size)

        for (idx in hashIndex) {
            val key = keys[idx]
            val value = values[idx]
            newMap[key] = value
        }

        return newMap
    }

    private fun isFull(): Boolean = keys.size == entries.size
}