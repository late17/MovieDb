package miolate.petproject.moviedb.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> runOnDefault(block: suspend () -> T): T {
    return withContext(Dispatchers.Default) {
        block()
    }
}