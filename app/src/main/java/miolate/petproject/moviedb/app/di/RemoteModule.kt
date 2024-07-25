package miolate.petproject.moviedb.app.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import miolate.petproject.moviedb.data.remote.HttpClient
import miolate.petproject.moviedb.data.remote.MovieApi
import miolate.petproject.moviedb.data.remote.MovieApiImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RemoteModule {

    @Binds
    fun bindsMovieApi(movieApi: MovieApiImpl): MovieApi

    companion object {

        @Provides
        @Singleton
        fun providesMovieApiImpl(): MovieApiImpl {
            return MovieApiImpl(HttpClient.getHttpClient())
        }
    }
}