package com.abtech.coroutine.examples

import kotlinx.coroutines.*
import java.util.concurrent.Executors



suspend fun main() {
    /*    Runtime.getRuntime().addShutdownHook(
            Thread {
                dispatcher.close()
            },
        )*/
    /*
    Dispatcher with a fixed pool of threads
     */
    val NUMBER_OF_THREADS = 20
    val dispatcher =
        Executors
            .newFixedThreadPool(NUMBER_OF_THREADS)
            .asCoroutineDispatcher()

    coroutineScope {
        launch(dispatcher) {
            println(Thread.currentThread().name)
        }
    }
}
/*
The biggest problem with this approach is that a dispatcher created with ExecutorService.asCoroutineDispatcher() needs to be closed manually by calling close() on it.
Developers often forget about this, which leads to leaking threads.
 */

/*
class DiscUserRepository(
    private val discReader: DiscReader,
    private val dispatcher: CoroutineContext = Dispatchers.IO,
) : UserRepository {
    override suspend fun getUser(): UserData =
        withContext(dispatcher) {
            UserData(discReader.read("userName"))
        }
}

class DiscUserRepository(
    private val discReader: DiscReader,
) : UserRepository {
    override suspend fun getUser(): UserData =
        withContext(Dispatchers.IO) {
            UserData(discReader.read("userName"))
        }
}
*/
