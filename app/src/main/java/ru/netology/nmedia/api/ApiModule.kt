package ru.netology.nmedia.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.auth.AppAuth
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {

    companion object {
        val BASE_URL = "http://10.0.2.2:9999/api/slow/"
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): ApiService = retrofit.create()


    @Provides
    @Singleton
    fun provideRetrofit(appAuth: AppAuth): Retrofit {
        return Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .run {
                        if (BuildConfig.DEBUG) {
                            addInterceptor { chain ->
                                chain.proceed(chain.run {
                                    val token = appAuth.state.value?.token

                                    if (token != null) {
                                        request().newBuilder()
                                            .addHeader("Autorization", token)
                                            .build()
                                    } else {
                                        request()
                                    }
                                }
                                )
                            }.build()
                            addInterceptor(HttpLoggingInterceptor().apply {
                                level = HttpLoggingInterceptor.Level.BODY
                            })
                        } else {
                            this
                        }
                    }
                    .build()
            )
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}