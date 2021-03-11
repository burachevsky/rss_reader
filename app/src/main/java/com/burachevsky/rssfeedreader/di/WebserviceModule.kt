package com.burachevsky.rssfeedreader.di

import com.burachevsky.rssfeedreader.network.NewsRetrofitService
import com.burachevsky.rssfeedreader.network.NewsWebservice
import com.burachevsky.rssfeedreader.network.NewsRetrofitServiceWrapper
import com.burachevsky.rssfeedreader.utils.parser.RssParser
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface WebserviceModule {

    @Binds
    fun bindNewsWebservice(webservice: NewsRetrofitServiceWrapper): NewsWebservice
}

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun provideNewsRetrofitService(retrofit: Retrofit): NewsRetrofitService {
        return retrofit.create(NewsRetrofitService::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://localhost") // no matter what url
            .build()
    }

    @Provides
    @Singleton
    fun provideClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BASIC)
            .setLevel(HttpLoggingInterceptor.Level.BODY)
            .setLevel(HttpLoggingInterceptor.Level.HEADERS)

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(10000, TimeUnit.MILLISECONDS)
            .readTimeout(10000, TimeUnit.MILLISECONDS)
            .build()
    }
}