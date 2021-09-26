package com.epikron.catzwiki.injection

import com.epikron.catzwiki.utils.BaseSchedulerProvider
import com.epikron.catzwiki.utils.SchedulerProvider
import dagger.Binds
import dagger.Module

@Module
abstract class SchedulersModule {

	@Binds
	abstract fun provideAppSchedulers(schedulerProvider: SchedulerProvider): BaseSchedulerProvider

}