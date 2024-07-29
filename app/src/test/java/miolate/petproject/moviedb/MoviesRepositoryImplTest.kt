package miolate.petproject.moviedb

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import miolate.petproject.moviedb.app.base.DataResult
import miolate.petproject.moviedb.data.local.MoviesDatabase
import miolate.petproject.moviedb.data.models.DiscoverResponse
import miolate.petproject.moviedb.data.models.ResultResponse
import miolate.petproject.moviedb.data.remote.RemoteDataSource
import miolate.petproject.moviedb.domain.impl.MoviesRepositoryImpl
import miolate.petproject.moviedb.domain.model.IsFavorite
import miolate.petproject.moviedb.domain.model.Movie
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDate
import java.time.YearMonth

@ExperimentalCoroutinesApi
class MoviesRepositoryImplTest {

    @Mock
    private lateinit var remoteDataSource: RemoteDataSource

    private var moviesDatabase: MoviesDatabase = InMemoryMoviesDatabase()

    private lateinit var moviesRepository: MoviesRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        moviesRepository = MoviesRepositoryImpl(remoteDataSource, moviesDatabase)
    }

    @Test
    fun testAddAndRemoveFavorites() = runBlocking {
        // Given
        val movie1 = createMovie(id = 1, title = "Title 1", isFavorite = IsFavorite.NOT_FAVORITE)
        val movie2 = createMovie(id = 2, title = "Title 2", isFavorite = IsFavorite.NOT_FAVORITE)
        val movie3 = createMovie(id = 3, title = "Title 3", isFavorite = IsFavorite.NOT_FAVORITE)

        // Add movies to favorites
        moviesRepository.addToFavorites(movie1)
        moviesRepository.addToFavorites(movie2)
        moviesRepository.addToFavorites(movie3)

        // Verify the movies are added to the database
        verifyDatabaseInsert(movie1.copy(isFavourite = IsFavorite.FAVORITE))
        verifyDatabaseInsert(movie2.copy(isFavourite = IsFavorite.FAVORITE))
        verifyDatabaseInsert(movie3.copy(isFavourite = IsFavorite.FAVORITE))

        // Remove some movies from favorites
        moviesRepository.removeFromFavorites(movie1)
        moviesRepository.removeFromFavorites(movie3)

        // Verify the movies are removed from the database
        verifyDatabaseDelete(movie1)
        verifyDatabaseDelete(movie3)

        // Set the remaining favorite movies in the database
        whenever(moviesDatabase.getAllFavorite()).thenReturn(flowOf(listOf(movie2.copy(isFavourite = IsFavorite.FAVORITE))))

        // When
        val actualMovies = moviesRepository.getFavouritesMovies().first()

        // Then
        val expectedMovies = listOf(movie2.copy(isFavourite = IsFavorite.FAVORITE))
        assertEquals(expectedMovies, actualMovies)
    }

    @Test
    fun testCacheMovies() = runBlocking {
        // Given
        val movie1 = createMovie(id = 1, title = "Title 1", isFavorite = IsFavorite.FAVORITE)
        val movie2 = createMovie(id = 2, title = "Title 2", isFavorite = IsFavorite.NOT_FAVORITE)
        val movie3 = createMovie(id = 3, title = "Title 3", isFavorite = IsFavorite.FAVORITE)
        val movie4 = createMovie(id = 4, title = "Title 4", isFavorite = IsFavorite.NOT_FAVORITE, isCashed = true)

        val discoverResponse = DiscoverResponse(
            page = 1,
            results = listOf(movie1, movie2).map { it.toResultResponse() },
            totalPages = 1,
            totalResults = 2
        )

        whenever(remoteDataSource.getMovies(1)).thenReturn(DataResult.Success(discoverResponse))

        // Adding previously cashed movie
        moviesDatabase.insert(movie4)
        // Adding to favorite before caching
        moviesRepository.addToFavorites(movie1)
        moviesRepository.addToFavorites(movie3)
        assert(moviesDatabase.getById(movie1.id) == movie1)

        // When
        val result = moviesRepository.getMovies(1)

        // Then
        assert(result is DataResult.Success)
        // Assert database contents
        assertDatabaseContains(movie1, IsFavorite.FAVORITE, true)
        assertDatabaseContains(movie2, IsFavorite.NOT_FAVORITE, true)
        assertDatabaseContains(movie3, IsFavorite.FAVORITE, false)
        assertDatabaseNotContains(movie4)
    }

    private fun createMovie(
        id: Int,
        title: String,
        isFavorite: IsFavorite,
        isCashed: Boolean = false
    ): Movie = Movie(
        adult = false,
        backdropPath = "backdropPath$id",
        genreIds = listOf(id, id + 1, id + 2),
        id = id,
        originalLanguage = "en",
        originalTitle = "Original Title $id",
        overview = "Overview $id",
        popularity = 10.0 + id.toDouble(),
        posterPath = "posterPath$id",
        releaseDate = LocalDate.of(2022, 7 + id, 29),
        yearAndMonth = YearMonth.of(2022, 7 + id),
        title = title,
        video = false,
        voteAverage = 8.0 + id.toDouble() / 10,
        voteCount = 1000 + id * 500,
        isFavourite = isFavorite,
        isCashed = isCashed
    )

    private fun Movie.toResultResponse() = ResultResponse(
        adult = this.adult,
        backdropPath = this.backdropPath,
        genreIds = this.genreIds,
        id = this.id,
        originalLanguage = this.originalLanguage,
        originalTitle = this.originalTitle,
        overview = this.overview,
        popularity = this.popularity,
        posterPath = this.posterPath,
        releaseDate = this.releaseDate.toString(),
        title = this.title,
        video = this.video,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount
    )

    private suspend fun verifyDatabaseInsert(movie: Movie) {
        verify(moviesDatabase).insert(movie)
    }

    private suspend fun verifyDatabaseDelete(movie: Movie) {
        verify(moviesDatabase).delete(movie)
    }

    private suspend fun assertDatabaseContains(movie: Movie, isFavorite: IsFavorite, isCashed: Boolean) {
        val all = moviesDatabase.getAll()
        assert(all.any {
            it.id == movie.id && it.isFavourite == isFavorite && it.isCashed == isCashed
        })
    }

    private suspend fun assertDatabaseNotContains(movie: Movie) {
        val all = moviesDatabase.getAll()
        assert(!all.any {
            it.id == movie.id
        })
    }
}
