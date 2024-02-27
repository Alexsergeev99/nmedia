package ru.netology.nmedia.util

object ToShortString {
    fun Int.toShortString(): String = when (this) {
        in 0..<1_000 -> this.toString()
        in 1_000..<10_000 -> "${(this / 100) / 10.0}K"
        in 10_000..<1_000_000 -> "${this / 1000}K"
        in 1_000_000..<10_000_000 -> "${(this / 100_000) / 10.0}M"
        in 10_000_000..<1_000_000_000 -> "${this / 1_000_000}M"
        else -> "MANY"
    }
}