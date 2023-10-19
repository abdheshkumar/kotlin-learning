package basic

import com.google.gson.Gson

object Reification {
    private inline fun <reified T> doSomethingWithType(obj: T) {
        val typeName = T::class.java
        println(typeName)
        println(obj)
    }

    private inline fun <reified T> Gson.fromJson(json: String): T {
        return fromJson(json, T::class.java)
    }

    private fun parseJsonResponse(json: String): ApiResponse {
        return Gson().fromJson(json, ApiResponse::class.java)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        doSomethingWithType("hello")

        val json = """{"name": "my-name"}"""
        println(parseJsonResponse(json))
        val res: ApiResponse = Gson().fromJson(json)
        println(res)
    }
}
