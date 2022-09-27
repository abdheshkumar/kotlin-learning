package com.abtech // ktlint-disable filename

fun main() {
    fun simple(): List<Int> = listOf(1, 2, 3)
    simple().forEach { value -> println(value) }
}
