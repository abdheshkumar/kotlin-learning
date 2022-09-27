package com.abtech // ktlint-disable filename

fun main() {
    fun simple(): Sequence<Int> = sequence { // sequence builder
        for (i in 1..3) {
            Thread.sleep(100) // pretend we are computing it
            yield(i) // yield next value
        }
    }
    simple().forEach { value -> println(value) }
}
