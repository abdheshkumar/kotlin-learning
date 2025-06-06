package dsl

class Request(val query: String, val contentType: String)
class Response(var statusCode: StatusResult) {
    operator fun invoke(function: StatusResult.() -> Unit) {
    }
}

class StatusResult(var statusCode: Int, var statusText: String)

class RouteHandler(var request: Request, val response: Response)

fun get(path: String, func: RouteHandler.() -> Unit) {}

fun main() {
    get("/") {
        if (request.contentType == "") {
            response {
                statusCode = 405
                statusText = "Blah"
            }
        }
    }
}