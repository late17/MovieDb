package miolate.petproject.moviedb.features.home.data

import miolate.petproject.moviedb.app.base.DataError
import miolate.petproject.moviedb.app.base.DataResult

class DefaultPaginator<Key, Item>(
    private val initialKey: Key,
    private inline val onLoadUpdated: (Boolean) -> Unit,
    private inline val onRequest: suspend (nextKey: Key) -> DataResult<List<Item>, DataError>,
    private inline val getNextKey: suspend (List<Item>) -> Key,
    private inline val onError: suspend (DataResult.Failure<List<Item>, DataError>) -> Unit,
    private inline val onSuccess: suspend (items: List<Item>, newKey: Key) -> Unit,
    private inline val onReset: suspend () -> Unit
) : Paginator<Key, Item> {

    private var currentKey = initialKey
    private var isMakingRequest = false

    override suspend fun loadNextItems() {
        if (isMakingRequest) {
            return
        }
        isMakingRequest = true
        onLoadUpdated(true)
        val result = onRequest(currentKey)
        isMakingRequest = false
        when (result) {
            is DataResult.Success -> {
                currentKey = getNextKey(result.body)
                onSuccess(result.body, currentKey)
                onLoadUpdated(false)
            }

            is DataResult.Failure -> {
                onError(result)
                onLoadUpdated(false)
                return
            }
        }
    }

    override suspend fun reset() {
        currentKey = initialKey
        onReset()
        loadNextItems()
    }
}