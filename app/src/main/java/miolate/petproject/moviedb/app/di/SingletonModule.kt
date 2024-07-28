package miolate.petproject.moviedb.app.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import miolate.petproject.moviedb.data.local.AppDatabase
import miolate.petproject.moviedb.data.remote.HttpClient
import miolate.petproject.moviedb.data.remote.RemoteDataSource
import miolate.petproject.moviedb.data.remote.RemoteDataSourceImpl
import miolate.petproject.moviedb.domain.AuthState
import miolate.petproject.moviedb.domain.impl.AuthStateImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface SingletonModule {

    @Binds
    fun bindsAuthProvides(authRepo: AuthStateImpl): AuthState

    @Binds
    fun bindsRemoteDataSourceImpl(remoteDataSource: RemoteDataSourceImpl): RemoteDataSource

    companion object {

        @Provides
        @Singleton
        fun providesAppDatabase(@ApplicationContext context: Context): AppDatabase {
            return AppDatabase.getInstance(context)
        }

        @Provides
        @Singleton
        fun providesAuthStateImpl(): AuthStateImpl {
            return AuthStateImpl()
        }

        @Provides
        @Singleton
        fun providesRemoteDataSourceImpl(): RemoteDataSourceImpl {
            return RemoteDataSourceImpl(HttpClient.getHttpClient())
        }
    }
}