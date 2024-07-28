package miolate.petproject.moviedb.domain.model

import miolate.petproject.moviedb.R

enum class IsFavorite(val resIconId: Int) {
    FAVORITE(R.drawable.baseline_favorite_24),
    NOT_FAVORITE(R.drawable.baseline_favorite_border_24),
}