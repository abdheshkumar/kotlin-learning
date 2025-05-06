package dsl

@DslMarker
annotation class HtmlDsl

@DslMarker
//@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
annotation class CssDsl

@HtmlDsl
class HtmlTag {
    fun div(block: HtmlTag.() -> Unit) = HtmlTag().apply(block)
    fun style(block: CssStyle.() -> Unit) = CssStyle().apply(block)
    fun text(value: String) = println("Text: $value")
}

@CssDsl
class CssStyle {
    fun color(value: String) = println("Color: $value")
    fun margin(value: String) = println("Margin: $value")
    //fun text(value: String) = println("CSS text: $value")
}

fun html(init: HtmlTag.() -> Unit) = HtmlTag().apply(init)


fun main() {

    html {
        div {
            text("Hello")

            style {
                color("red")
                text("Oops")
                text("HTML version")
            }
        }
    }
}
