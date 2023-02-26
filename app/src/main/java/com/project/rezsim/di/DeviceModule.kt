package com.project.rezsim.di

import com.project.rezsim.device.*
import org.koin.dsl.module

val deviceModule = module {

    factory { SettingsRepository() }

    factory { ScreenRepository() }

    factory { StringRepository() }

    factory { DrawableRepository() }

    factory { ColorRepository() }

}