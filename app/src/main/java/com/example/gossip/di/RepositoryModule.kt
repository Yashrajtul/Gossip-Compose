package com.example.gossip.di

import com.example.gossip.firebaseauth.repository.AuthRepository
import com.example.gossip.firebaseauth.repository.AuthRepositoryImpl
import com.example.gossip.firestoredb.repository.ChatRepository
import com.example.gossip.firestoredb.repository.ChatRepositoryImpl
import com.example.gossip.firestoredb.repository.FirestoreDbRepositoryImpl
import com.example.gossip.firestoredb.repository.FirestoreRepository
import com.example.gossip.firestoredb.repository.HomeRepository
import com.example.gossip.firestoredb.repository.HomeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun providesFirestoreRepository(
        repo: FirestoreDbRepositoryImpl
    ): FirestoreRepository

    @Binds
    abstract fun providesFirebaseAuthRepository(
        repo: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    abstract fun providesChatRepository(
        repo: ChatRepositoryImpl
    ): ChatRepository

    @Binds
    abstract fun providesHomeRepository(
        repo: HomeRepositoryImpl
    ): HomeRepository

}