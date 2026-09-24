package map

class CustomMap<K, V>(capacity: Int) {
    private class Entry<K, V>(val key: K, var value: V)

    var size = 0
        private set

    private var entries = arrayOfNulls<Entry<K, V>>(capacity)

    val keys
        get() = entries.filterNotNull().map { it.key }

    val values
        get() = entries.filterNotNull().map { it.value }

    operator fun get(key: K): V? {
        for (i in 0 until size) {
            val entry = entries[i]!!
            if (entry.key == key) return entry.value
        }
        return null
    }

    fun put(key: K, value: V) {
        for (i in 0 until size) {
            val entry = entries[i]!!

            if (entry.key == key) {
                entry.value = value
                return
            }
        }

        if (size == entries.size) {
            entries = entries.copyOf(entries.size * 2)
        }

        entries[size++] = Entry(key, value)
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
}