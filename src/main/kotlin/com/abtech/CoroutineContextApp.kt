package com.abtech

import kotlinx.coroutines.* // ktlint-disable no-wildcard-imports
import java.time.Instant
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.coroutineContext

// https://kt.academy/book/coroutines
object CoroutineContextApp {
    /**
     * coroutineName=BACKGROUND, thread=Thread[DefaultDispatcher-worker-1,5,main]
     * BACKGROUND - coroutine name
     * DefaultDispatcher-worker-1 - thread name
     * 5 - thread priority
     * main - thread's group name
     */
    private fun CoroutineScope.log(msg: String) {
        val name = coroutineContext[CoroutineName]?.name
        println("[coroutineName=$name, thread=${Thread.currentThread()}, ${Instant.now()}] $msg")
    }

    @JvmStatic
    fun main(args: Array<String>): Unit = runBlocking {
        val job = Job()
        launch(job) { // the new job replaces one from parent
            delay(1000)
            log("Text 1")
        }
        launch(job) { // the new job replaces one from parent
            delay(2000)
            log("Text 2")
        }

        /*log("Started") // [main] Started
        val v1 = async(CoroutineName("c1")) {
            delay(500)
            log("Running async") // [c1] Running async
            42
        }
        launch(CoroutineName("c2")) {
            delay(1000)
            log("Running launch") // [c2] Running launch
        }
        log("The answer is ${v1.await()}")
        // [main] The answer is 42
        CoroutineScope(EmptyCoroutineContext).launch {
            log("Running on EmptyCoroutineContext")
        }

        launch(CoroutineName("child")) {
            println("My context is $coroutineContext}")
            scopeCheck(this)
        }*/
        // https://medium.com/swlh/how-can-we-use-coroutinescopes-in-kotlin-2210695f0e89
        val scope = CoroutineScope(EmptyCoroutineContext + CoroutineName("BACKGROUND"))
        val repoScope = CoroutineScope(EmptyCoroutineContext + SupervisorJob())
        val response = backGroupProcessing(scope)
        log(response)
        // doThatIn(repoScope)
        doThis(repoScope)
        job.join() // Here we will await forever https://kt.academy/article/cc-job
        println("Will not be printed")
        // delay(1000)
        // switching CoroutineScopes https://medium.com/swlh/how-can-we-use-coroutinescopes-in-kotlin-2210695f0e89
    }

    private suspend fun scopeCheck(scope: CoroutineScope) {
        println(scope.coroutineContext === coroutineContext)
    }

    /*
    Suspending functions can and should wait for all their work to complete before returning to the caller
     */
    private suspend fun backGroupProcessing(scope: CoroutineScope): String = supervisorScope {
        doThis(scope)
        doThatIn(scope)
        log("return response")
        "hello"
    }

    /*
    https://elizarov.medium.com/coroutine-context-and-scope-c8b255d59055
    Suspending functions can and should wait for all their work to complete before returning to the caller
    If you need to launch a coroutine that keeps running after your function returns, then make your function an extension of CoroutineScope or pass scope: CoroutineScope as parameter to make your intent clear in your function signature. Do not make these functions suspending:
     */
    private suspend fun CoroutineScope.doThis(dbScope: CoroutineScope): String {
        val apiRes = dbScope.async(CoroutineName("my")) { // Here we switch from the calling CoroutineScope to the 'dbScope'
            delay(500)
            log("Calling API")
            "res"
        }
            // We're running in the calling CoroutineScope again.
            .await()
        log("Done..")
        return apiRes
    }

    private fun doThatIn(scope: CoroutineScope) {
        scope.launch {
            delay(500)
            log("I'm fine too")
        }
    }
}
