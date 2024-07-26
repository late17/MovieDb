package miolate.petproject.moviedb.features.home

interface Paginator<Key, Item> {

    suspend fun loadNextItems()
    suspend fun reset()
}