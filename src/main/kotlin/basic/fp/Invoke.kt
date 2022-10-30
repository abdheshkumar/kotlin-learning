package basic.fp

object Invoke {
    operator fun invoke(a: Int) {
        println(a)
    }
    operator fun invoke(a: String): String {
        println(a)
        return a
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val a: Unit = Invoke(10)
        val b: String = Invoke("hello")
        println(a)
        println(b)
    }
}
