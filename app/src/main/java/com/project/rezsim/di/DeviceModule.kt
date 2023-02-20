package com.project.rezsim.di

import com.project.rezsim.device.DrawableRepository
import com.project.rezsim.device.ScreenRepository
import com.project.rezsim.device.SettingsRepository
import com.project.rezsim.device.StringRepository
import org.koin.dsl.module

val deviceModule = module {

    factory { SettingsRepository() }

    factory { ScreenRepository() }

    factory { StringRepository() }

    factory { DrawableRepository() }

}