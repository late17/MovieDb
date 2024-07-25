package miolate.petproject.moviedb.app.base

sealed interface DataResult<out T, out E : RootError> {

    data class Failure<T, E : RootError>(val e: E? = null, val message: String? = null) :
        DataResult<T, E>

    data class Success<T, E : RootError>(val body: T) : DataResult<T, E>

    fun getOrNull() =
        when (this) {
            is Failure -> null
            is Success -> this.body
        }

    fun isSuccess(): Boolean =
        this is Success

    fun toDataState(): DataState<T, E> =
        when (this) {
            is Failure -> DataState.Failure(this.e, this.message)
            is Success -> DataState.Success(this.body)
        }
}