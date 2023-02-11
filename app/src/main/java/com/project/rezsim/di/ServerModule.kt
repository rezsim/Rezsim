package com.project.rezsim.di

import com.madhava.keyboard.vario.base.Singletons
import com.project.rezsim.server.register.RegisterRepository
import com.project.rezsim.server.UserModel
import com.project.rezsim.server.login.LoginRepository
import com.project.rezsim.server.user.UserRepository
import org.koin.dsl.module

val serverModule = module {

    single { Singletons.instance(UserModel::class) as UserModel }

    factory { RegisterRepository() }

    factory { LoginRepository() }

    factory { UserRepository() }

}