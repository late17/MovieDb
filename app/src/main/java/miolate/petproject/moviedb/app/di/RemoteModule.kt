package miolate.petproject.moviedb.app.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import miolate.petproject.moviedb.data.remote.HttpClient
import miolate.petproject.moviedb.data.remote.RemoteDataSource
import miolate.petproject.moviedb.data.remote.RemoteDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RemoteModule {

    @Binds
    fun bindsMovieApi(movieApi: RemoteDataSourceImpl): RemoteDataSource

    companion object {

        @Provides
        @Singleton
        fun providesMovieApiImpl(): RemoteDataSourceImpl {
            return RemoteDataSourceImpl(HttpClient.getHttpClient())
        }
    }
}