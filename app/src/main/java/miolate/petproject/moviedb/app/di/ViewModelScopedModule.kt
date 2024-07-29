package miolate.petproject.moviedb.app.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import miolate.petproject.moviedb.data.local.AppDatabase
import miolate.petproject.moviedb.data.local.MoviesDatabase
import miolate.petproject.moviedb.data.local.MoviesDatabaseImpl
import miolate.petproject.moviedb.data.local.dao.MovieDao
import miolate.petproject.moviedb.data.remote.RemoteDataSource
import miolate.petproject.moviedb.domain.MoviesRepository
import miolate.petproject.moviedb.domain.impl.MoviesRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
interface ViewModelScopedModule {

    @Binds
    fun bindsMoviesRepository(moviesRepository: MoviesRepositoryImpl): MoviesRepository

    @Binds
    fun bindsMovieDatabase(moviesDatabaseImpl: MoviesDatabaseImpl): MoviesDatabase

    companion object {

        @Provides
        @ViewModelScoped
        fun providesMoviesRepositoryImpl(remoteDataSource: RemoteDataSource, moviesDatabaseImpl: MoviesDatabaseImpl): MoviesRepositoryImpl {
            return MoviesRepositoryImpl(remoteDataSource, moviesDatabaseImpl)
        }

        @Provides
        @ViewModelScoped
        fun providesMovieDao(appDatabase: AppDatabase): MovieDao {
            return appDatabase.movieDao()
        }

        @Provides
        @ViewModelScoped
        fun providesMovieDatabase(movieDao: MovieDao): MoviesDatabaseImpl {
            return MoviesDatabaseImpl(movieDao)
        }
    }
}