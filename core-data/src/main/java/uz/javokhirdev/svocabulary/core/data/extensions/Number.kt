package uz.javokhirdev.svocabulary.core.data.extensions

fun Long?.orNotId(): Long {
    return if (this != null && this >= 0) this else -1L
}

fun Long?.orNullId(): Long? {
    return if (this != null && this >= 0) this else null
}

fun Int?.orZero(): Int = this ?: 0