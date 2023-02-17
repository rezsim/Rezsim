package com.project.rezsim.di

import com.madhava.keyboard.vario.base.Singletons
import com.project.rezsim.ui.screen.dialog.user.UserDialogViewModel
import com.project.rezsim.ui.screen.activity.MainActivityViewModel
import com.project.rezsim.ui.screen.dialog.message.MessageDialogViewModel
import com.project.rezsim.ui.screen.footer.FooterViewModel
import com.project.rezsim.ui.screen.header.HeaderViewModel
import com.project.rezsim.ui.screen.household.HouseholdViewModel
import com.project.rezsim.ui.screen.login.LoginViewModel
import com.project.rezsim.ui.screen.main.MainViewModel
import com.project.rezsim.ui.screen.splash.SplashViewModel
import org.koin.dsl.module


val viewModelModule = module {

    single { Singletons.instance(MainActivityViewModel::class) as MainActivityViewModel }

    single { Singletons.instance(SplashViewModel::class) as SplashViewModel }

    single { Singletons.instance(FooterViewModel::class) as FooterViewModel }

    single { Singletons.instance(HeaderViewModel::class) as HeaderViewModel }

    single { Singletons.instance(LoginViewModel::class) as LoginViewModel }

    single { Singletons.instance(HouseholdViewModel::class) as HouseholdViewModel}

    single { Singletons.instance(MainViewModel::class) as MainViewModel }

    factory { UserDialogViewModel() }

    factory { MessageDialogViewModel() }

}


