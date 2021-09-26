package com.epikron.catzwiki.injection

import android.app.Application
import android.app.SearchManager
import android.content.Context
import dagger.Module
import dagger.Provides
import com.epikron.catzwiki.utils.AppPreferences
import com.epikron.catzwiki.utils.BaseSchedulerProvider
import com.epikron.catzwiki.utils.ResourceProvider
import com.epikron.catzwiki.utils.SchedulerProvider
import dagger.Binds

import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideAppContext(app: Application): Context = app.applicationContext

    @Provides
    @Singleton
    fun provideSearchManager(context: Context) =
        context.getSystemService(Context.SEARCH_SERVICE) as SearchManager

    @Provides
    @Singleton
    fun provideResourceProvider(context: Context) = ResourceProvider(context)

    @Provides
    @Singleton
    fun provideAppPreferences(context: Context) = AppPreferences(context)
}
