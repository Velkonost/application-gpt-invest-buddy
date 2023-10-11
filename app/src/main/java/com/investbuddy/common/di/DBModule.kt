package com.investbuddy.common.di

import android.content.Context
import androidx.room.Room
import com.investbuddy.common.room.MainRoomDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        MainRoomDB::class.java,
        "investbuddy_data"
    )
        .allowMainThreadQueries()
        .build()

    @Singleton
    @Provides
    fun provideInvestDataDao(db: MainRoomDB) = db.investDataDao()

    @Singleton
    @Provides
    fun provideChatDataDao(db: MainRoomDB) = db.chatDataDao()
}