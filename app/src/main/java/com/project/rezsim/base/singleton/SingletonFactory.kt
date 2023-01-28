package com.project.rezsim.base.singleton

import com.project.rezsim.ui.MainActivityViewModel
import com.project.rezsim.ui.splash.SplashViewModel
import kotlin.reflect.KClass

object SingletonFactory {

    fun <T : Singleton> createInstance(clazz: KClass<T>) : Singleton? = when (clazz) {

        MainActivityViewModel::class -> MainActivityViewModel()

        SplashViewModel::class -> SplashViewModel()

        else -> null
    }

}