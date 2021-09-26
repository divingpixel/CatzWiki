package com.epikron.catzwiki.injection

import com.epikron.catzwiki.presentation.CatApiRepoImpl
import com.epikron.catzwiki.presentation.UserRepoImpl
import com.epikron.catzwiki.remote.CatApiRepository
import com.epikron.catzwiki.remote.UserRepository
import dagger.Binds
import dagger.Module

@Module
abstract class DataModule {

	@Binds
	abstract fun providesCatApiRepository(catApiRepoImpl: CatApiRepoImpl): CatApiRepository

	@Binds
	abstract fun providesUserRepository(userRepoImpl: UserRepoImpl): UserRepository

}
