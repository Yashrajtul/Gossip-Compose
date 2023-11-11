package com.example.gossip.di

import android.content.ContentProvider
import com.example.gossip.contentproviders.MyContentProvider
import com.example.gossip.firebaseRealtimeDb.repository.RealtimeDbRepository
import com.example.gossip.firebaseRealtimeDb.repository.RealtimeRepository
import com.example.gossip.firebaseauth.repository.AuthRepository
import com.example.gossip.firebaseauth.repository.AuthRepositoryImpl
import com.example.gossip.firestoredb.repository.FirestoreDbRepositoryImpl
import com.example.gossip.firestoredb.repository.FirestoreRepository
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
    ): RealtimeRepository

    @Binds
    abstract fun providesFirestoreRepository(
        repo: FirestoreDbRepositoryImpl
    ): FirestoreRepository

    @Binds
    abstract fun providesFirebaseAuthRepository(
        repo: AuthRepositoryImpl
    ): AuthRepository

}