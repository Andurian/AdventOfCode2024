package meow.andurian.aoc2024.day_21

import meow.andurian.aoc2024.utils.Direction
import java.util.*

enum class KeyType {
    Number, Direction, Activate, Gap
}

abstract class Key<T>(val type: KeyType) {
    abstract fun value(): T?
}

class NumberKey(private val _value: Int) : Key<Int>(KeyType.Number) {
    override fun value() = _value
    override fun hashCode() = Objects.hashCode(this._value)
    override fun toString() = "${value()}"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is NumberKey) return false

        return this._value == other._value
    }

}

class DirectionKey(private val dir: Direction) : Key<Direction>(KeyType.Direction) {
    override fun value() = dir
    override fun hashCode() = Objects.hashCode(this.dir)
    override fun toString() = "${dir.toChar()}"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DirectionKey) return false

        return this.dir == other.dir
    }
}

class ActivateKey<T> : Key<T>(KeyType.Activate) {
    override fun value() = null
    override fun hashCode() = Objects.hashCode("Activate")
    override fun toString() = "A"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return other is ActivateKey<T>
    }
}

class Gap<T> : Key<T>(KeyType.Gap) {
    override fun value() = null
    override fun hashCode() = Objects.hashCode("Gap")
    override fun toString() = "X"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return other is Gap<T>
    }
}

fun toNumberKey(c: Char): Key<Int> {
    return when (c) {
        in '0'..'9' -> NumberKey(c - '0')
        'A' -> ActivateKey<Int>()
        else -> throw IllegalArgumentException()
    }
}

fun toArrowKey(c: Char): Key<Direction> {
    if (c == 'A') return ActivateKey<Direction>()
    return DirectionKey(Direction.fromChar(c))
}