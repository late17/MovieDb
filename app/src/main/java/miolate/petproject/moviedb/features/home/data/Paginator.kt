package miolate.petproject.moviedb.features.home.data

interface Paginator<Key, Item> {

    suspend fun loadNextItems()
    suspend fun reset()
}