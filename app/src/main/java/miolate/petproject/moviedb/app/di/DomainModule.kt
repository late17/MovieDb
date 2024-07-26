package miolate.petproject.moviedb.app.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import miolate.petproject.moviedb.data.remote.RemoteDataSource
import miolate.petproject.moviedb.domain.MoviesRepository
import miolate.petproject.moviedb.domain.impl.MoviesRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
interface DomainModule {

    @Binds
    fun bindsMoviesRepository(moviesRepository: MoviesRepositoryImpl): MoviesRepository

    companion object {

        @Provides
        @ViewModelScoped
        fun providesMoviesRepositoryImpl(remoteDataSource: RemoteDataSource): MoviesRepositoryImpl {
            return MoviesRepositoryImpl(remoteDataSource)
        }
    }
}