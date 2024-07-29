package miolate.petproject.moviedb.app.base

//Data State includes Loading
sealed interface DataState<out T, out E : RootError> {

    data class Failure<T, E : RootError>(val e: E? = null, val message: String? = null) :
        DataState<T, E>

    data class Success<T, E : RootError>(val body: T) : DataState<T, E>

    data class Loading<T, E : RootError>(val body: T? = null) : DataState<T, E>


    fun getOrNull() =
        when (this) {
            is Failure -> null
            is Success -> this.body
            is Loading -> null
        }

    fun <R> map(
        map: (T) -> R,
    ): DataState<R, E> =
        when (this) {
            is Failure -> Failure(this.e, this.message)
            is Success -> Success(
                map(this.body)
            )

            is Loading -> Loading()
        }

    fun isSuccess(): Boolean =
        this is Success
}