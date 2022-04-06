package com.epikron.catzwiki.injection

import com.epikron.catzwiki.presentation.CatApiRepoImpl
import com.epikron.catzwiki.remote.CatApiRepository
import dagger.Binds
import dagger.Module

@Module
abstract class DataModule {

	@Binds
	abstract fun providesCatApiRepository(catApiRepoImpl: CatApiRepoImpl): CatApiRepository

}
