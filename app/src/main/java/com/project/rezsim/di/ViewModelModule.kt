package com.project.rezsim.di

import com.project.rezsim.ui.screen.dialog.user.UserDialogViewModel
import com.project.rezsim.ui.screen.activity.MainActivityViewModel
import com.project.rezsim.ui.screen.dialog.message.MessageDialogViewModel
import com.project.rezsim.ui.screen.dialog.meter.MeterDialogViewModel
import com.project.rezsim.ui.screen.footer.FooterViewModel
import com.project.rezsim.ui.screen.header.HeaderViewModel
import com.project.rezsim.ui.screen.household.HouseholdViewModel
import com.project.rezsim.ui.screen.login.LoginViewModel
import com.project.rezsim.ui.screen.main.MainViewModel
import com.project.rezsim.ui.screen.overview.OverviewViewModel
import com.project.rezsim.ui.screen.splash.SplashViewModel
import org.koin.dsl.module


val viewModelModule = module {

    single { MainActivityViewModel() }

    single { SplashViewModel() }

    single { FooterViewModel() }

    single { HeaderViewModel() }

    single { LoginViewModel() }

    single { HouseholdViewModel() }

    single { MainViewModel() }

    single { OverviewViewModel() }

    factory { UserDialogViewModel() }

    factory { MessageDialogViewModel() }

    factory { MeterDialogViewModel() }

}


