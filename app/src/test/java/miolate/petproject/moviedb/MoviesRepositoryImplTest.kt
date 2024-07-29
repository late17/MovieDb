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
import org.mockito.kotlin.mock
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
//        remoteDataSource = mock()
//        moviesDatabase = mock()
        moviesRepository = MoviesRepositoryImpl(remoteDataSource, moviesDatabase)
    }

    // Add test cases here
    @Test
    fun testAddAndRemoveFavorites() = runBlocking {
        // Given
        val movie1 = Movie(
            adult = false,
            backdropPath = "backdropPath1",
            genreIds = listOf(1, 2, 3),
            id = 1,
            originalLanguage = "en",
            originalTitle = "Original Title 1",
            overview = "Overview 1",
            popularity = 10.0,
            posterPath = "posterPath1",
            releaseDate = LocalDate.of(2022, 7, 29),
            yearAndMonth = YearMonth.of(2022, 7),
            title = "Title 1",
            video = false,
            voteAverage = 8.0,
            voteCount = 1000,
            isFavourite = IsFavorite.NOT_FAVORITE,
            isCashed = false
        )

        val movie2 = Movie(
            adult = false,
            backdropPath = "backdropPath2",
            genreIds = listOf(4, 5, 6),
            id = 2,
            originalLanguage = "en",
            originalTitle = "Original Title 2",
            overview = "Overview 2",
            popularity = 12.0,
            posterPath = "posterPath2",
            releaseDate = LocalDate.of(2022, 8, 29),
            yearAndMonth = YearMonth.of(2022, 8),
            title = "Title 2",
            video = false,
            voteAverage = 8.5,
            voteCount = 1500,
            isFavourite = IsFavorite.NOT_FAVORITE,
            isCashed = false
        )

        val movie3 = Movie(
            adult = false,
            backdropPath = "backdropPath3",
            genreIds = listOf(7, 8, 9),
            id = 3,
            originalLanguage = "en",
            originalTitle = "Original Title 3",
            overview = "Overview 3",
            popularity = 15.0,
            posterPath = "posterPath3",
            releaseDate = LocalDate.of(2022, 9, 29),
            yearAndMonth = YearMonth.of(2022, 9),
            title = "Title 3",
            video = false,
            voteAverage = 9.0,
            voteCount = 2000,
            isFavourite = IsFavorite.NOT_FAVORITE,
            isCashed = false
        )

        // Add movies to favorites
        moviesRepository.addToFavorites(movie1)
        moviesRepository.addToFavorites(movie2)
        moviesRepository.addToFavorites(movie3)

        // Verify the movies are added to the database
        verify(moviesDatabase).insert(movie1.copy(isFavourite = IsFavorite.FAVORITE))
        verify(moviesDatabase).insert(movie2.copy(isFavourite = IsFavorite.FAVORITE))
        verify(moviesDatabase).insert(movie3.copy(isFavourite = IsFavorite.FAVORITE))

        // Remove some movies from favorites
        moviesRepository.removeFromFavorites(movie1)
        moviesRepository.removeFromFavorites(movie3)

        // Verify the movies are removed from the database
        verify(moviesDatabase).delete(movie1)
        verify(moviesDatabase).delete(movie3)

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
        val movie1 = Movie(
            adult = false,
            backdropPath = "backdropPath1",
            genreIds = listOf(1, 2, 3),
            id = 1,
            originalLanguage = "en",
            originalTitle = "Original Title 1",
            overview = "Overview 1",
            popularity = 10.0,
            posterPath = "posterPath1",
            releaseDate = LocalDate.of(2022, 7, 29),
            yearAndMonth = YearMonth.of(2022, 7),
            title = "Title 1",
            video = false,
            voteAverage = 8.0,
            voteCount = 1000,
            isFavourite = IsFavorite.FAVORITE,
            isCashed = false
        )

        val movie2 = Movie(
            adult = false,
            backdropPath = "backdropPath2",
            genreIds = listOf(4, 5, 6),
            id = 2,
            originalLanguage = "en",
            originalTitle = "Original Title 2",
            overview = "Overview 2",
            popularity = 12.0,
            posterPath = "posterPath2",
            releaseDate = LocalDate.of(2022, 8, 29),
            yearAndMonth = YearMonth.of(2022, 8),
            title = "Title 2",
            video = false,
            voteAverage = 8.5,
            voteCount = 1500,
            isFavourite = IsFavorite.NOT_FAVORITE,
            isCashed = false
        )

        val movie3 = Movie(
            adult = false,
            backdropPath = "backdropPath3",
            genreIds = listOf(4, 5, 6),
            id = 3,
            originalLanguage = "en",
            originalTitle = "Original Title 3",
            overview = "Overview 3",
            popularity = 12.0,
            posterPath = "posterPath3",
            releaseDate = LocalDate.of(2022, 8, 29),
            yearAndMonth = YearMonth.of(2022, 8),
            title = "Title 3",
            video = false,
            voteAverage = 8.5,
            voteCount = 1500,
            isFavourite = IsFavorite.FAVORITE,
            isCashed = false
        )

        val movie4 = Movie(
            adult = false,
            backdropPath = "backdropPath4",
            genreIds = listOf(4, 5, 6),
            id = 4,
            originalLanguage = "en",
            originalTitle = "Cashed not Favorite Should Be deleted 4",
            overview = "Overview 3",
            popularity = 12.0,
            posterPath = "posterPath3",
            releaseDate = LocalDate.of(2022, 8, 29),
            yearAndMonth = YearMonth.of(2022, 8),
            title = "Title 4",
            video = false,
            voteAverage = 8.5,
            voteCount = 1500,
            isFavourite = IsFavorite.NOT_FAVORITE,
            isCashed = true
        )

        val discoverResponse = DiscoverResponse(
            page = 1,
            results = listOf(movie1, movie2).map { it.toResultResponse() },
            totalPages = 1,
            totalResults = 2
        )

        whenever(remoteDataSource.getMovies(1)).thenReturn(DataResult.Success(discoverResponse))

        //Adding previously cashed
        moviesDatabase.insert(movie4)
        //Adding to favorite before cashing
        moviesRepository.addToFavorites(movie1)
        moviesRepository.addToFavorites(movie3)
        assert(moviesDatabase.getById(movie1.id) == movie1)

        // When
        val result = moviesRepository.getMovies(1)

        // Then
        assert(result is DataResult.Success)
        // Assert that movie Database Contains both movies as cashed
        // Assert that movie 1 is still favorite and cashed
        // Assert that movie 2 is not favorite and cashed
        // Assert that movie 3 is favorite and not cashed
        // Assert that movie 4 doesn't exist
        val all = moviesDatabase.getAll()
        assert(all.any {
            it.id == movie1.id && it.isFavourite == IsFavorite.FAVORITE && it.isCashed
        })
        assert(all.any {
            it.id == movie2.id && it.isFavourite == IsFavorite.NOT_FAVORITE && it.isCashed
        })
        assert(all.any {
            it.id == movie3.id && it.isFavourite == IsFavorite.FAVORITE && !it.isCashed
        })
        assert(!all.any {
            it.id == movie4.id
        })
    }

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
}