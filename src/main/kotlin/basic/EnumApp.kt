package basic

object EnumApp {
    interface Exotic {
        fun isExotic(): Boolean
    }

    enum class Flour {
        WHEAT, CORN, CASSAVA
    }

    enum class FlourV1 : Exotic {
        WHEAT {
            override fun isExotic(): Boolean {
                TODO("Not yet implemented")
            }
        }, CORN {
            override fun isExotic(): Boolean {
                TODO("Not yet implemented")
            }
        }, CASSAVA {
            override fun isExotic(): Boolean {
                TODO("Not yet implemented")
            }
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val en: Flour = Flour.CASSAVA
        when (en) {
            Flour.WHEAT -> TODO()
            Flour.CORN -> TODO()
            Flour.CASSAVA -> TODO()
        }
    }
}
