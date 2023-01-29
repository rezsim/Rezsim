package com.project.rezsim.base.singleton

import com.project.rezsim.ui.MainActivityViewModel
import com.project.rezsim.ui.footer.FooterViewModel
import com.project.rezsim.ui.header.HeaderViewModel
import com.project.rezsim.ui.login.LoginViewModel
import com.project.rezsim.ui.splash.SplashViewModel
import com.project.server.UserModel
import kotlin.reflect.KClass

object SingletonFactory {

    fun <T : Singleton> createInstance(clazz: KClass<T>) : Singleton? = when (clazz) {

        MainActivityViewModel::class -> MainActivityViewModel()

        SplashViewModel::class -> SplashViewModel()

        HeaderViewModel::class -> HeaderViewModel()

        FooterViewModel::class -> FooterViewModel()

        LoginViewModel::class -> LoginViewModel()

        UserModel::class -> UserModel()

        else -> null
    }

}