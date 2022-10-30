package basic

object PatternMatch {
    sealed class MyOneof {
        class StringOneof(val s: String) : MyOneof() {
            override fun toString(): String = "StringOneof($s)"
        }

        class IntOneof(val i: Int) : MyOneof() {
            override fun toString(): String = "IntOneof(i)"
        }

        class DoubleOneof(val d: Double) : MyOneof() {
            override fun toString(): String = "DoubleOneof($d)"
        }

        object NotSet : MyOneof() {
            override fun toString(): String = "NotSet"
        }
    }

    fun k(myOne: MyOneof): String = when (myOne) {
        is MyOneof.StringOneof -> "string"
        is MyOneof.IntOneof -> "int"
        is MyOneof.NotSet -> "Not Set"
        // MyOneof.NotSet -> "Not Set"
        is MyOneof.DoubleOneof -> "double"
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println(k(MyOneof.NotSet))
    }
}
