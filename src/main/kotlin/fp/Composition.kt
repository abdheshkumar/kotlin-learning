package fp

import basic.fp.Fun

object Composition {
    // https://docs.vavr.io/
    operator fun invoke(a: String) {
        println(a)
    }

    private fun double(x: Int): Int = x * 2

    private fun square(x: Int): Int = x * x

    private infix fun <A, B, C> ((A) -> B).compose(g: (B) -> C): (A) -> C =
        { a ->
            g(this(a))
        }

    private inline infix fun <A, B, C> Fun<A, B>.after(crossinline g: Fun<B, C>): Fun<A, C> =
        { a ->
            g(this(a))
        }

    @JvmStatic
    fun main(args: Array<String>) {
        val result = Composition::square compose Composition::double
        println(result(2))
        val result1 = Composition::square after Composition::double
        // ::square.after(::double)
        println(result1(9))
        Composition("12") // Calling invoke function
        val f: Fun<Int, Int> = Composition::square
        val g: Fun<Int, Int> = Composition::double
        val `gof` = f.after(g)
        println(gof(2))
    }

    fun strToIntResult(string: String): Result<Int> =
        try {
            Result.success(string.toInt())
        } catch (nfe: NumberFormatException) {
            Result.failure(nfe)
        }
}
