package com.burachevsky.rssfeedreader.di

import com.burachevsky.rssfeedreader.utils.parser.RssParser
import com.burachevsky.rssfeedreader.utils.parser.RssParserImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ParserModule {

    @Binds
    abstract fun bindRssParser(parser: RssParserImpl): RssParser
}