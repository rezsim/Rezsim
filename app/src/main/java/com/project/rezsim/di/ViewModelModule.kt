package com.project.rezsim.di

import com.madhava.keyboard.vario.base.Singletons
import com.project.rezsim.ui.MainActivityViewModel
import com.project.rezsim.ui.splash.SplashViewModel
import org.koin.dsl.module


val viewModelModule = module {

    single { Singletons.instance(MainActivityViewModel::class) as MainActivityViewModel }

    single { Singletons.instance(SplashViewModel::class) as SplashViewModel }

}


