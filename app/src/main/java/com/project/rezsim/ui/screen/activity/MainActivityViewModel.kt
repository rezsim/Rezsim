package com.project.rezsim.ui.screen.activity

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.project.rezsim.R
import com.project.rezsim.base.RezsimViewModel
import com.project.rezsim.ui.screen.household.HouseholdFragment
import com.project.rezsim.ui.screen.login.LoginFragment
import com.project.rezsim.ui.screen.login.LoginViewModel
import com.project.rezsim.ui.screen.main.MainFragment
import com.project.rezsim.ui.screen.splash.SplashFragment
import com.project.rezsim.ui.screen.splash.SplashViewModel
import com.project.rezsim.ui.view.message.MessageSeverity
import com.project.rezsim.ui.view.message.MessageType
import com.project.rezsim.server.UserModel
import com.project.rezsim.teszt.FragmentA
import com.project.rezsim.teszt.FragmentB
import com.project.rezsim.ui.screen.household.HouseholdViewModel
import com.project.rezsim.ui.screen.main.MainViewModel
import org.koin.core.component.inject

class MainActivityViewModel : RezsimViewModel() {

    val headerVisbileLiveData = MutableLiveData<Boolean>()
    val footerVisbileLiveData = MutableLiveData<Boolean>()
    val fabIconLiveData = MutableLiveData<Int?>()
    val loadWorkFragmentLiveData = MutableLiveData<String>()
    val showProgressLiveData = MutableLiveData<Boolean>()
    val messageLiveData: MutableLiveData<MainActivity.MessageData> = MutableLiveData()
    val fabPressedLiveData = MutableLiveData<Boolean>()

    private val splashViewModel: SplashViewModel by inject()
    private val loginViewModel: LoginViewModel by inject()
    private val mainViewModel: MainViewModel by inject()
    private val householdViewModel: HouseholdViewModel by inject()
    private val userModel: UserModel by inject()

    init {
        mainViewModel.addHouseholdLiveData.observeForever { addNewHousehold() }
    }

    private var currentFragmentTag: String = ""
        set(value) {
            field = value
            setWorkFragment(value)
        }

    private val fabImages = HashMap<String, Int>().apply {
        put(MainFragment.TAG, R.drawable.ic_edit)
        put(HouseholdFragment.TAG, R.drawable.ic_floppy)
        put(FragmentA.TAG, R.drawable.ic_plus)
        put(FragmentB.TAG, R.drawable.ic_minus)
    }

    init {
        Log.d("DEBINFO", "MainActivityViewModel.init")
        currentFragmentTag = SplashFragment.TAG
        splashViewModel.finishedLiveData.observeForever { splashFinished() }
        loginViewModel.finishedLiveData.observeForever { loginFinished() }
    }

    fun currentFragmentTag() = currentFragmentTag

    fun showProgress() = showProgressLiveData.postValue(true)

    fun hideProgress() = showProgressLiveData.postValue(false)


    fun showMessage(messageResId: Int, type: MessageType = MessageType.SNACKBAR_CLOSEABLE_AND_MANUALCLOSE, severity: MessageSeverity = MessageSeverity.INFO, runnable: Runnable? = null) {
        showMessage(context.resources.getString(messageResId), type, severity, runnable)
    }

    fun showMessage(message: String, type: MessageType = MessageType.SNACKBAR_CLOSEABLE_AND_MANUALCLOSE, severity: MessageSeverity = MessageSeverity.INFO, runnable: Runnable? = null) {
        val messageData = MainActivity.MessageData(message, type, severity, runnable)
        messageLiveData.postValue(messageData)
    }

    fun fabPressed() {
/*        if (currentFragmentTag != FragmentA.TAG) {
            currentFragmentTag = FragmentA.TAG
        } else {
            currentFragmentTag = FragmentB.TAG
        }
*/


        when (currentFragmentTag) {
            MainFragment.TAG -> {
                currentFragmentTag = HouseholdFragment.TAG
                householdViewModel.houseHold = userModel.getUser()?.households?.get(mainViewModel.getCurrentHousehold())
            }
            HouseholdFragment.TAG -> {
                Log.d("DEBINFO-R", "MainActivityModel.fabPressedLiveData set value:true")
                fabPressedLiveData.value = true
            }
        }

    }

    fun switchToFragment(fragmentTag: String) {
        currentFragmentTag = fragmentTag
    }

    private fun setWorkFragment(fragmentTag: String) {
        loadWorkFragmentLiveData.value = fragmentTag
        headerVisbileLiveData.value = needHeader(fragmentTag)
        footerVisbileLiveData.value = needFooter(fragmentTag)
        fabIconLiveData.value = fabIcon(fragmentTag)
    }

    private fun splashFinished() {
        if (currentFragmentTag == SplashFragment.TAG) {
            currentFragmentTag = if (!userModel.isLoggedIn()) LoginFragment.TAG else startScreen()
        }
    }

    private fun loginFinished() {
        if (currentFragmentTag == LoginFragment.TAG) {
            currentFragmentTag = startScreen()
        }
    }

    private fun startScreen() = if (userModel.hasHousehold()) MainFragment.TAG else HouseholdFragment.TAG

    private fun needHeader(fragmentId: String) =
        fragmentId in listOf(MainFragment.TAG, HouseholdFragment.TAG, FragmentA.TAG, FragmentB.TAG)

    private fun needFooter(fragmentId: String) =
        fragmentId in listOf(MainFragment.TAG, FragmentA.TAG, FragmentB.TAG)

    private fun fabIcon(fragmentId: String) =
        fabImages[fragmentId]

    private fun addNewHousehold() {
        if (currentFragmentTag == MainFragment.TAG) {
            currentFragmentTag = HouseholdFragment.TAG
            householdViewModel.houseHold = null
        }
    }


}