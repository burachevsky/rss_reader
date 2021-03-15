package com.burachevsky.rssfeedreader.di

import android.content.Context
import androidx.room.Room
import com.burachevsky.rssfeedreader.data.AppDatabase
import com.burachevsky.rssfeedreader.data.daos.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext.applicationContext,
            AppDatabase::class.java,
            "news"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideNewsChannelDao(appDatabase: AppDatabase): NewsChannelDao {
        return appDatabase.newsChannelDao()
    }

    @Provides
    fun provideNewsItemDao(appDatabase: AppDatabase): NewsItemDao {
        return appDatabase.newsItemDao()
    }

    @Provides
    fun provideFavoriteItemDao(appDatabase: AppDatabase): FavoriteItemDao {
        return appDatabase.favoriteItemDao()
    }

    @Provides
    fun provideReadItemDao(appDatabase: AppDatabase): ReadItemDao {
        return appDatabase.readItemDao()
    }

    @Provides
    fun provideNewsCategoryDao(appDatabase: AppDatabase): NewsCategoryDao {
        return appDatabase.newsCategoryDao()
    }
}