package com.project.rezsim.di

import com.madhava.keyboard.vario.base.Singletons
import com.project.server.UserModel
import org.koin.dsl.module

val serverModule = module {

    single { Singletons.instance(UserModel::class) as UserModel }

}