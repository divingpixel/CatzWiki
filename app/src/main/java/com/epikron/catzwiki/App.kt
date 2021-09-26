package com.epikron.catzwiki

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import com.epikron.catzwiki.injection.DaggerAppComponent

class App : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder()
            .create(this)
            .build()
    }
}
