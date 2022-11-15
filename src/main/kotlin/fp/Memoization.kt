package fp

import basic.fp.Fun

object Memoization {

    /** Memo version of a function */
    private fun <A, B> Fun<A, B>.memo(): Fun<A, B> {
        val cache by lazy { mutableMapOf<A, B>() }
        return { a: A ->
            val cached = cache[a]
            if (cached == null) {
                cache[a] = this(a)
            }
            cache[a]!!
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val testFunction = { a: Int -> println("Evaluating... $a"); a * 2 }
        println("Running testFunction 4 times")
        testFunction(2)
        testFunction(2)
        testFunction(2)
        testFunction(3)
        val memoTestingFunction: (Int) -> Int = testFunction.memo()
        println("Running memoTestingFunction 4 times")
        memoTestingFunction(2)
        memoTestingFunction(2)
        memoTestingFunction(2)
        memoTestingFunction(3)
    }
}
