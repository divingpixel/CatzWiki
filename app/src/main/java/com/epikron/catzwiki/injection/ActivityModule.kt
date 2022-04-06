package com.epikron.catzwiki.injection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.epikron.catzwiki.MainActivity
import com.epikron.catzwiki.presentation.*
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import com.epikron.catzwiki.ui.fragments.*

@Module
@Suppress ("unused")
abstract class ActivityModule{
    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CatListViewModel::class)
    internal abstract fun bindCatListViewModel(catListViewModel: CatListViewModel): ViewModel

    @ContributesAndroidInjector
    abstract fun catListFragment(): CatListFragment
}
