package com.example.gossip.di

import com.example.gossip.firebaseRealtimeDb.repository.RealtimeDbRepository
import com.example.gossip.firebaseRealtimeDb.repository.RealtimeRepository
import com.example.gossip.firebaseauth.repository.AuthRepository
import com.example.gossip.firebaseauth.repository.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun providesRealtimeRepository(
        repo: RealtimeDbRepository
    ):RealtimeRepository

    @Binds
    abstract fun providesFirebaseAuthRepository(
        repo: AuthRepositoryImpl
    ):AuthRepository
}