package dsl

@DslMarker
annotation class MyDsl

@MyDsl
class Outer {
    fun greet() = println("Hello from Outer")
    fun inner(block: Inner.() -> Unit) {
        Inner().block()
    }
}


@MyDsl
class Inner {
    fun greet() = println("Hello from Inner")
}

fun outer(block: Outer.() -> Unit) {
    Outer().block()
}

fun main() {
    outer {
        greet() // ✅ Calls Outer.greet()
        inner {
            greet() // ✅ Calls Inner.greet()
            //greet() // ❌ If Inner didn't have greet(), this would cause a compile-time error due to @DslMarker
        }
    }
}

/*
Without @DslMarker: Both Outer and Inner's greet() functions would be accessible in the innermost scope, potentially leading to ambiguity.

With @DslMarker: When both Outer and Inner are annotated with the same DSL marker, Kotlin restricts access to the outer receiver (Outer) within the inner lambda. This means that inside the inner { ... } block, only Inner's members are accessible by default.

Accessing Outer Receiver Explicitly:

If you need to access the outer receiver (Outer) from within the inner lambda, you can do so explicitly using a labeled this:
this@outer.greet()
 */