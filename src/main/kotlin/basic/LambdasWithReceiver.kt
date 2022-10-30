package basic

object LambdasWithReceiver {

    private fun htmlTag(sb: StringBuilder, attr: String, action: (StringBuilder) -> Unit): String {
        sb.append("<$attr>")
        action(sb)
        sb.append("</$attr>")
        return sb.toString()
    }

    private fun htmlTagV1(sb: StringBuilder, attr: String, action: StringBuilder.() -> Unit): String {
        sb.append("<$attr>")
        // action(sb) // It also works
        sb.action()
        sb.append("</$attr>")
        return sb.toString()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val xml = htmlTag(StringBuilder(), "attr") {
            it.append("MyAttribute")
        }
        println(xml)

        val xmlV1 = htmlTagV1(StringBuilder(), "attr") {
            append("MyAttribute") // this.append("MyAttribute")
        }
        println(xmlV1)
    }
}
