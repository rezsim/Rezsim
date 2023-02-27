package com.project.rezsim.ui.screen.activity

import android.app.Activity
import android.os.IBinder
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.MutableLiveData
import com.madhava.keyboard.vario.base.Singletons
import com.project.rezsim.R
import com.project.rezsim.base.RezsimViewModel
import com.project.rezsim.device.StringRepository
import com.project.rezsim.ui.screen.household.HouseholdFragment
import com.project.rezsim.ui.screen.login.LoginFragment
import com.project.rezsim.ui.screen.login.LoginViewModel
import com.project.rezsim.ui.screen.main.MainFragment
import com.project.rezsim.ui.screen.splash.SplashFragment
import com.project.rezsim.ui.screen.splash.SplashViewModel
import com.project.rezsim.ui.view.message.MessageSeverity
import com.project.rezsim.ui.view.message.MessageType
import com.project.rezsim.server.UserModel
import com.project.rezsim.server.dto.measurement.Utility
import com.project.rezsim.ui.screen.dialog.DialogParameter
import com.project.rezsim.ui.screen.dialog.user.UserDialogFragment
import com.project.rezsim.ui.screen.footer.FooterButton
import com.project.rezsim.ui.screen.footer.FooterViewModel
import com.project.rezsim.ui.screen.header.HeaderViewModel
import com.project.rezsim.ui.screen.household.HouseholdViewModel
import com.project.rezsim.ui.screen.main.MainViewModel
import com.project.rezsim.ui.screen.overview.OverviewFragment
import com.project.rezsim.ui.screen.overview.OverviewViewModel
import com.project.rezsim.ui.view.message.Message
import com.project.rezsim.ui.view.message.MessageParameter
import org.koin.android.ext.android.inject
import org.koin.core.component.inject

class MainActivityViewModel : RezsimViewModel() {

    val headerVisbileLiveData = MutableLiveData<Boolean>()
    val footerVisbileLiveData = MutableLiveData<Boolean>()
    val fabIconLiveData = MutableLiveData<Int?>()
    val loadWorkFragmentLiveData = MutableLiveData<String>()
    val showProgressLiveData = MutableLiveData<Boolean>()
    val messageLiveData: MutableLiveData<MessageParameter> = MutableLiveData()
    val fabPressedLiveData = MutableLiveData<Boolean>()
    val dialogLiveData = MutableLiveData<DialogParameter>()
    val quitLiveData = MutableLiveData<Boolean>()
    val hideKeyboardLiveData = MutableLiveData<IBinder?>()

    private val splashViewModel: SplashViewModel by inject()
    private val loginViewModel: LoginViewModel by inject()
    private val mainViewModel: MainViewModel by inject()
    private val householdViewModel: HouseholdViewModel by inject()
    private val userModel: UserModel by inject()
    private val headerViewModel: HeaderViewModel by inject()
    private val footerViewModel: FooterViewModel by inject()
    private val stringRepository: StringRepository by inject()
    private val overviewViewModel: OverviewViewModel by inject()

    private var currentFragmentTag: String = ""
        set(value) {
            field = value
            setWorkFragment(value)
        }

    private val fabImages = HashMap<String, Int>().apply {
        put(MainFragment.TAG, R.drawable.ic_edit)
        put(HouseholdFragment.TAG, R.drawable.ic_floppy)
        put(OverviewFragment.TAG, R.drawable.ic_add)
    }

    private val headerButtons = HashMap<String, IntArray>().apply {
        put(MainFragment.TAG, intArrayOf(R.id.ivUser))
        put(HouseholdFragment.TAG, intArrayOf(R.id.ivUser))
        put(OverviewFragment.TAG, intArrayOf(R.id.ivCalendar, R.id.ivUser))
    }


    init {
        Log.d("DEBINFO", "MainActivityViewModel.init")
        currentFragmentTag = SplashFragment.TAG
        splashViewModel.finishedLiveData.observeForever { splashFinished() }
        loginViewModel.finishedLiveData.observeForever { loginFinished() }
        headerViewModel.buttonPressedLiveData.observeForever { headerButtonPressed(it) }
        headerViewModel.backLiveData.observeForever { goBack(it) }
        mainViewModel.addHouseholdLiveData.observeForever { addNewHousehold() }
        userModel.logoutLiveData.observeForever { logout() }
        footerViewModel.selectButtonLiveData.observeForever { shortTo(it) }
    }

    fun currentFragmentTag() = currentFragmentTag

    fun showProgress() = showProgressLiveData.postValue(true)

    fun hideProgress() = showProgressLiveData.postValue(false)


    fun showMessage(titleResId: Int? = null, messageResId: Int, type: MessageType = MessageType.SNACKBAR_CLOSEABLE_AND_MANUALCLOSE, severity: MessageSeverity = MessageSeverity.INFO, rootView: View? = null, runnable: Runnable? = null) {
        showMessage(titleResId?.let { stringRepository.getById(titleResId) },  stringRepository.getById(messageResId), type, severity, rootView, runnable)
    }

    fun showMessage(title: String?, message: String, type: MessageType = MessageType.SNACKBAR_CLOSEABLE_AND_MANUALCLOSE, severity: MessageSeverity = MessageSeverity.INFO, rootView: View? = null, runnable: Runnable? = null) {
        val messageData = MessageParameter(title, message, type, severity, rootView, runnable)
        messageLiveData.postValue(messageData)
    }

    fun fabPressed() {
        when (currentFragmentTag) {
            MainFragment.TAG -> {
                currentFragmentTag = HouseholdFragment.TAG
                householdViewModel.household = userModel.getUser()?.householdList()?.get(mainViewModel.currentHousehold())
            }
            HouseholdFragment.TAG, OverviewFragment.TAG -> {
                fabPressedLiveData.value = true
            }
        }

    }

    fun switchToFragment(fragmentTag: String) {
        currentFragmentTag = fragmentTag
    }

    fun showDialog(parameter: DialogParameter) {
        dialogLiveData.postValue(parameter)
    }

    fun hideKeyboard(focusedViewToken: IBinder? = null) {
        hideKeyboardLiveData.value = focusedViewToken
    }

    fun showKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    fun goOverviewScreen(householdIndex: Int, utility: Utility): Boolean =
        if (userModel.hasMeasurement(mainViewModel.currentHousehold(), utility)) {
            overviewViewModel.householdIndex = householdIndex
            overviewViewModel.utility = utility
            currentFragmentTag = OverviewFragment.TAG
            true
        } else {
            showMessage(null, R.string.footer_no_meter, MessageType.TOAST_LONG)
            false
        }


    fun clearFabLiveData() {
        fabPressedLiveData.value = false
    }

    private fun shortTo(button: FooterButton) {
        when (button) {
            FooterButton.HOME -> currentFragmentTag = MainFragment.TAG
            FooterButton.GAS -> shortToOverview(Utility.GAS)
            FooterButton.ELECTRICITY -> shortToOverview(Utility.ELECTRICITY_A)
        }
    }

    private fun shortToOverview(utility: Utility) {
        if (goOverviewScreen(mainViewModel.currentHousehold(), utility)) {
            overviewViewModel.reInit()
        }
    }

    private fun setWorkFragment(fragmentTag: String) {
        loadWorkFragmentLiveData.value = fragmentTag
        headerVisbileLiveData.value = needHeader(fragmentTag).also {
            if (it) {
                headerViewModel.setButtons(headerButtons[fragmentTag])
            }
        }
        footerVisbileLiveData.value = needFooter(fragmentTag)
        fabIconLiveData.value = fabIcon(fragmentTag)
        val footerButton = when (fragmentTag) {
            MainFragment.TAG -> FooterButton.HOME
            OverviewFragment.TAG -> if (overviewViewModel.utility == Utility.GAS) FooterButton.GAS else FooterButton.ELECTRICITY
            else -> null
        }
        footerButton?.let {
            footerViewModel.selectButton(it)
        }
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
        fragmentId in listOf(MainFragment.TAG, HouseholdFragment.TAG, OverviewFragment.TAG)

    private fun needFooter(fragmentId: String) =
        fragmentId in listOf(MainFragment.TAG, OverviewFragment.TAG)

    private fun fabIcon(fragmentId: String) =
        fabImages[fragmentId]

    private fun addNewHousehold() {
        if (currentFragmentTag == MainFragment.TAG) {
            currentFragmentTag = HouseholdFragment.TAG
            householdViewModel.household = null
        }
    }

    private fun logout() {
        Message.clear()
        currentFragmentTag = LoginFragment.TAG
    }

    private fun goBack(value: Boolean) {
        if (value && currentFragmentTag == MainFragment.TAG) {
            headerViewModel.clearBackLiveData()
            showMessage(
                titleResId = R.string.main_message_title_exit,
                messageResId = R.string.main_message_exit,
                type = MessageType.DIALOG_YES_CANCEL,
                runnable = { quitLiveData.value = true }
            )
        }
    }

    private fun headerButtonPressed(buttonId: Int) {
        if (buttonId == R.id.ivUser) {
            showDialog(DialogParameter(UserDialogFragment.TAG))
        }
    }

    companion object {
        fun getInstance() = Singletons.instance(MainActivityViewModel::class) as MainActivityViewModel
    }

}