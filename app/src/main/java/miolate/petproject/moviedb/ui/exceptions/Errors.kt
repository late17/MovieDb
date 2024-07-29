package miolate.petproject.moviedb.ui.exceptions

import android.content.Context
import miolate.petproject.moviedb.R
import miolate.petproject.moviedb.app.base.DataError

fun DataError?.localizedErrorMessage(context: Context): String {

    // This can be customized to return any new error, now only Network ones.
    //
    return when(this){
        DataError.Network.REQUEST_TIMEOUT -> context.getString(R.string.unknown_error)
        DataError.Network.TOO_MANY_REQUESTS -> context.getString(R.string.unknown_error)
        DataError.Network.INVALID_PARAMETERS -> context.getString(R.string.unknown_error)
        DataError.Network.NO_INTERNET -> context.getString(R.string.unknown_error)
        DataError.Network.SERVER_RESPONSE_ERROR -> context.getString(R.string.unknown_error)
        DataError.Network.CLIENT_REQUEST_ERROR -> context.getString(R.string.unknown_error)
        DataError.Network.SERIALIZATION -> context.getString(R.string.unknown_error)
        DataError.Network.UNKNOWN -> context.getString(R.string.unknown_error)
        null -> context.getString(R.string.unknown_error)
    }
}