package com.project.rezsim.server.login

import androidx.lifecycle.MutableLiveData
import com.project.rezsim.server.dto.Household
import com.project.rezsim.server.dto.Measurement
import com.project.rezsim.server.dto.User
import org.koin.core.component.KoinComponent
import java.util.concurrent.Executors

class Login : KoinComponent {

    private val testEmail = "test@test.com"
    private val testPassword = "Alma1234"
    private val testToken = "teszt_token"

    private lateinit var resultLiveData: MutableLiveData<LoginResult>

    fun login(email: String, password: String): MutableLiveData<LoginResult> {
        resultLiveData = MutableLiveData()
        Executors.newSingleThreadExecutor().execute {
            Thread.sleep(5000)
            val response = if (email == testEmail && password == testPassword) {
                LoginResponse(200, testToken)
            } else {
                LoginResponse(401)
            }
            val user = testUser()
            resultLiveData.postValue(LoginResult(response, email, password, user))
        }
        return resultLiveData
    }



    fun testUser() = User(
        id = 0,
        email = testEmail,
        password = testPassword,
        name = "Teszt Elek",
        status = true,
        registrationDate = "2022-10-18 21:31:23. 189000",
        lastSignin = "2022-10-18 21:31:37. 000000",
        households = listOf(
            Household(
                id = 1,
                userId = 0,
                name = "Lakás",
                electricityUsage = 0,
                electricityService = 0,
                electricityPricingTypeA = 0,
                electricityPricingTypeB = 0,
                electricityPricingTypeH = 0,
                electricityConsumptionUnit = 0,
                gasService = 0,
                gasChildren = 1,
                gasConsumptionUnit = 0,
                gasHeatingValue = 37,
                measurements = listOf(
                    Measurement(
                        id = 2,
                        userId = 0,
                        householdId = 1,
                        utility = 0,
                        period = 0,
                        date = "2022-10-22 18:52:11. 000000",
                        position = 1234,
                        consumption = 0,
                        level = 0
                    ),
                    Measurement(
                        id = 3,
                        userId = 0,
                        householdId = 1,
                        utility = 0,
                        period = 0,
                        date = "2022-10-22 18:53:11. 000000",
                        position = 4567,
                        consumption = 0,
                        level = 0
                    )
                )
            ),
            Household(
                id = 4,
                userId = 0,
                name = "Nyaraló",
                electricityUsage = 0,
                electricityService = 0,
                electricityPricingTypeA = 0,
                electricityPricingTypeB = 0,
                electricityPricingTypeH = 0,
                electricityConsumptionUnit = 0,
                gasService = 0,
                gasChildren = 1,
                gasConsumptionUnit = 0,
                gasHeatingValue = 38,
                measurements = listOf(
                    Measurement(
                        id = 5,
                        userId = 0,
                        householdId = 4,
                        utility = 0,
                        period = 0,
                        date = "2023-01-05 09:45:44. 000000",
                        position = 1122,
                        consumption = 0,
                        level = 0
                    ),
                    Measurement(
                        id = 6,
                        userId = 0,
                        householdId = 4,
                        utility = 0,
                        period = 0,
                        date = "2023-01-05 09:51:44. 000000",
                        position = 2211,
                        consumption = 0,
                        level = 0
                    )
                )
            ),
        )
    )

}