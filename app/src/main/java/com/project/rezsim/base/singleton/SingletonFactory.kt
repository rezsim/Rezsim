package com.project.rezsim.base.singleton

import com.project.rezsim.ui.screen.activity.MainActivityViewModel
import com.project.rezsim.ui.screen.footer.FooterViewModel
import com.project.rezsim.ui.screen.header.HeaderViewModel
import com.project.rezsim.ui.screen.household.HouseholdViewModel
import com.project.rezsim.ui.screen.login.LoginViewModel
import com.project.rezsim.ui.screen.splash.SplashViewModel
import com.project.rezsim.server.UserModel
import com.project.rezsim.ui.screen.main.MainViewModel
import kotlin.reflect.KClass

object SingletonFactory {

    fun <T : Singleton> createInstance(clazz: KClass<T>) : Singleton? = when (clazz) {

        MainActivityViewModel::class -> MainActivityViewModel()

        SplashViewModel::class -> SplashViewModel()

        HeaderViewModel::class -> HeaderViewModel()

        FooterViewModel::class -> FooterViewModel()

        LoginViewModel::class -> LoginViewModel()

        HouseholdViewModel::class -> HouseholdViewModel()

        MainViewModel::class -> MainViewModel()

        UserModel::class -> UserModel()

        else -> null
    }

}