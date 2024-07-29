package miolate.petproject.moviedb.ui.toast

import android.content.Context
import android.widget.Toast
import miolate.petproject.moviedb.app.base.DataError
import miolate.petproject.moviedb.ui.exceptions.localizedErrorMessage

fun Context.showToast(dataError: DataError?) {
    Toast.makeText(this, dataError.localizedErrorMessage(this), Toast.LENGTH_LONG).show()
}
