package basic

typealias Client = String

//Extension property
val Client.consoleString: String
    get() = "Hello, $this"

//Extension function
fun Client.toConsoleString(): String {
    return "Hello, $this"
}

fun main() {
    val client: Client = "World"
    println(client.consoleString)
    println(client.toConsoleString())
}