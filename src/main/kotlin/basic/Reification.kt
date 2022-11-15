package basic

object Reification {
    private inline fun <reified T> doSomethingWithType(obj: T) {
        val typeName = T::class.java
        println(typeName)
        println(obj)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        doSomethingWithType("hello")
    }
}
