package miolate.petproject.moviedb.app.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import miolate.petproject.moviedb.domain.AuthState
import miolate.petproject.moviedb.domain.impl.AuthStateImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AuthModule {

    @Binds
    fun bindsAuthProvides(authRepo: AuthStateImpl): AuthState

    companion object {

        @Provides
        @Singleton
        fun providesAuthStateImpl(): AuthStateImpl {
            return AuthStateImpl()
        }
    }
}