package com.project.rezsim.di

import com.project.rezsim.server.register.RegisterRepository
import com.project.rezsim.server.UserModel
import com.project.rezsim.server.calculation.CalculationRepository
import com.project.rezsim.server.household.HouseholdRepository
import com.project.rezsim.server.login.LoginRepository
import com.project.rezsim.server.measurement.MeasurementRepository
import com.project.rezsim.server.user.UserRepository
import org.koin.dsl.module

val serverModule = module {

    single { UserModel() }

    factory { RegisterRepository() }

    factory { LoginRepository() }

    factory { UserRepository() }

    factory { HouseholdRepository() }

    factory { MeasurementRepository() }

    factory { CalculationRepository() }

}