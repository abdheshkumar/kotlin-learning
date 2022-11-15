package fp

import kotlin.reflect.KProperty

object Laziness {
    private fun testDelegate() {
        var variable by object {

            var localInt: Int? = null

            operator fun getValue(
                thisRef: Any?,
                property: KProperty<*>
            ): Int? {
                println("Getter Invoked returning $localInt")
                return localInt
            }

            operator fun setValue(
                thisRef: Any?,
                property: KProperty<*>,
                value: Int?
            ) {
                println("Setter Invoked with value $value")
                localInt = value
            }
        }
        variable = 10
        println("Reading $variable")
    }

    private fun multiLazy() {
        val multiLambda by lazy { println("I'm MultiLambda") }
        multiLambda
        multiLambda
        multiLambda
        multiLambda
    }

    @JvmStatic
    fun main(args: Array<String>) {
        multiLazy()
        testDelegate()
    }
}
