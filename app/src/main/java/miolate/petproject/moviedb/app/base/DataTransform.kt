package miolate.petproject.moviedb.app.base

fun <R, T, E : RootError> DataResult.Success<T, E>.map(
    map: (T) -> R,
): DataResult.Success<R, E> =
    DataResult.Success(
        map(this.body)
    )

fun <R, T, E : RootError> DataResult<T, E>.mapIfSuccess(
    map: (T) -> R,
): DataResult<R, E> =
    when (this) {
        is DataResult.Failure -> DataResult.Failure(this.e, this.message)
        is DataResult.Success -> DataResult.Success(
            map(this.body)
        )
    }