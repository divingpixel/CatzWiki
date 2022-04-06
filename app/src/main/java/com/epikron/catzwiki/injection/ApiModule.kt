package com.epikron.catzwiki.injection

import com.epikron.catzwiki.remote.CatApiService
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Module that provides all dependencies from the remote package/layer.
 */
@Module
class ApiModule {
	companion object {
		const val CATS_URL = "https://api.thecatapi.com/v1/"
	}

	@Provides
	fun provideRetrofit(): Retrofit {
		val httpClient = OkHttpClient.Builder()
			.connectTimeout(10, TimeUnit.SECONDS)
			.readTimeout(10, TimeUnit.SECONDS)

		return Retrofit.Builder()
			.baseUrl(CATS_URL)
			.addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
			.addCallAdapterFactory(RxJava3CallAdapterFactory.create())
			.client(httpClient.build())
			.build()
	}

	@Provides
	fun provideBreedsService(retrofit: Retrofit): CatApiService = retrofit.create(CatApiService::class.java)
}
