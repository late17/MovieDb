package miolate.petproject.moviedb.app.base

sealed interface DataError : Error {

    enum class Network : DataError {
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        INVALID_PARAMETERS,
        NO_INTERNET,
        SERVER_RESPONSE_ERROR,
        CLIENT_REQUEST_ERROR,
        SERIALIZATION,
        UNKNOWN
    }
}