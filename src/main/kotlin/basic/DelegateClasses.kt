package basic

object DelegateClasses {
    interface AttackType {
        fun getAttackType(): String
    }

    class Ranged : AttackType {
        override fun getAttackType(): String = "Ranged"
    }

    interface HeroType {
        fun getAttributeType(): String
    }

    class Strength : HeroType {
        override fun getAttributeType(): String = "Strength"
    }

    class Huskar : AttackType by Ranged(), HeroType by Strength()

    @JvmStatic
    fun main(args: Array<String>) {
        val huskar = Huskar()
        println(huskar.getAttackType())
        println(huskar.getAttributeType())
    }
}
