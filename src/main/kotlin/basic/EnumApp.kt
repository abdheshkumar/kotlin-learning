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
                println("Not yet implemented")
                return true
            }
        }, CORN {
            override fun isExotic(): Boolean {
                println("Not yet implemented")
                return true
            }
        }, CASSAVA {
            override fun isExotic(): Boolean {
                println("Not yet implemented")
                return true
            }
        }
    }

    enum class FlourV2 {
        WHEAT {
            override fun isExotic(): Boolean {
                println("Not yet implemented")
                return true
            }
        }, CORN {
            override fun isExotic(): Boolean {
                println("Not yet implemented")
                return true
            }
        }, CASSAVA {
            override fun isExotic(): Boolean {
                println("Not yet implemented")
                return true
            }
        };

        abstract fun isExotic(): Boolean
    }

    enum class FlourV3 {
        WHEAT ,
        CORN,
        CASSAVA;
        var weight: Int = 0;
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val en: Flour = Flour.CASSAVA
        when (en) {
            Flour.WHEAT -> println(en)
            Flour.CORN -> println(en)
            Flour.CASSAVA -> println(en)
        }

        FlourV3.WHEAT.weight = 100
        println(FlourV3.WHEAT.weight) // 100
        FlourV3.CORN.weight = 200
        println(FlourV3.CORN.weight) // 200
        println(FlourV3.CASSAVA.weight) // 0, not set
    }
}
