package fp

object ValueClass {

    @JvmInline
    value class NonZeroIntSmart private constructor(val value: Int) {
        companion object {
            operator fun invoke(value: Int): NonZeroIntSmart? {
                return when (value) {
                    0 -> null
                    else -> NonZeroIntSmart(value)
                }
            }
        }
    }

    @JvmInline
    value class NonZeroInt(val value: Int) {
        init {
            require(value != 0) { "O is not a value for this type!" }
        }
    }

    /** Calculate 1 over x */
    private fun oneOver(x: NonZeroInt): Double = 1.0 / x.value

    /** Calculate 1 over x */
    private fun oneOver(x: NonZeroIntSmart): Double = 1.0 / x.value

    @JvmStatic
    fun main(a: Array<String>) {
        println("1/3 = ${oneOver(NonZeroInt(3))}")
        println("1/0 = ${NonZeroIntSmart(0)?.let { oneOver(it) }}")
        println("1/1 = ${NonZeroIntSmart(1)?.let(ValueClass::oneOver)}")
    }
}
