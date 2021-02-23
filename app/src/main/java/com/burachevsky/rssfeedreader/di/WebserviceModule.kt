package com.burachevsky.rssfeedreader.di

import com.burachevsky.rssfeedreader.network.NewsWebservice
import com.burachevsky.rssfeedreader.network.NewsWebserviceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WebserviceModule {

    @Binds
    abstract fun bindWebservice(impl: NewsWebserviceImpl): NewsWebservice
}