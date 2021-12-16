package com.appschef.hospitalapp.di

import com.appschef.hospitalapp.data.remote.HospitalAPI
import com.appschef.hospitalapp.repositories.HospitalRepositoryImpl
import com.appschef.hospitalapp.util.ConnectivityInteceptor
import com.appschef.hospitalapp.util.Endpoints
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesHospitalApi(): HospitalAPI {
//        val interceptor = HttpLoggingInterceptor()
//            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttp = OkHttpClient.Builder()
            .addInterceptor(ConnectivityInteceptor())
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .client(okHttp)
            .baseUrl(Endpoints.HOSPITAL_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HospitalAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideHospitalRepository(): HospitalRepositoryImpl {
        return HospitalRepositoryImpl(providesHospitalApi())
    }
}