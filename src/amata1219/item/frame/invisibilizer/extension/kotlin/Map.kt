package amata1219.item.frame.invisibilizer.extension.kotlin

internal fun <K, V> MutableMap<K, V> .putIfUnset(key: K, value: V): V {
    return get(key) ?: run {
        put(key, value)
        return value
    }
}