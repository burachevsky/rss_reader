package com.burachevsky.rssfeedreader.network

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

interface NewsRetrofitService {

    @GET
    suspend fun requestFeed(@Url url: String): ResponseBody
}