package basic

object ExtensionFunctions {
    // Extension function
    private fun Int.doubleValue() = this * 2

    // Extension Property
    private val Int.doubleValueV1: Int
        get() {
            return this * 2
        }

    @JvmStatic
    fun main(args: Array<String>) {
        println(2.doubleValue())
        println(2.doubleValueV1)
    }
}
