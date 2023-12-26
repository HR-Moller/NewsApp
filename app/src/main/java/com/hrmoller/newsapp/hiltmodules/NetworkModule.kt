package com.hrmoller.newsapp.hiltmodules

import com.hrmoller.newsapp.hiltmodules.Constants.API_KEY
import com.hrmoller.newsapp.hiltmodules.Constants.BASE_URL
import com.hrmoller.newsapp.services.NewsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideBaseUrl(): String {
        return BASE_URL
    }

    @Singleton
    @Provides
    fun provideConvertorFactory(): MoshiConverterFactory {
        return MoshiConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideOkhttpClient(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.addInterceptor(Interceptor { chain ->
            val builder = chain.request().newBuilder()
            builder.header("X-Api-Key", API_KEY)
            return@Interceptor chain.proceed(builder.build())
        })
        return okHttpClient.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        baseUrl: String, converterFactory: MoshiConverterFactory, okHttpClient: OkHttpClient
    ): Retrofit {
        val retrofit = Retrofit.Builder().baseUrl(baseUrl).client(okHttpClient).addConverterFactory(converterFactory)
        return retrofit.build()
    }

    @Singleton
    @Provides
    fun provideNewsApiService(retrofit: Retrofit): NewsApiService {
        return retrofit.create(NewsApiService::class.java)
    }
}

object Constants {
    const val BASE_URL = "https://newsapi.org/v2/"
    const val API_KEY = "29b7e257316545179f6d0f237ff2a80a"
}
