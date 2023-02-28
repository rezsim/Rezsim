package com.project.rezsim.ui.screen.household

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.project.rezsim.R
import com.project.rezsim.base.RezsimFragment
import com.project.rezsim.tool.numericValue
import com.project.rezsim.ui.screen.activity.MainActivityViewModel
import com.project.rezsim.ui.screen.header.HeaderViewModel
import com.project.rezsim.ui.screen.main.MainFragment
import com.project.rezsim.ui.view.message.MessageType
import com.project.rezsim.ui.view.spinner.TextSpinnerAdapter
import com.project.rezsim.ui.view.spinner.TextSpinnerOnItemSelectedListener
import org.koin.android.ext.android.inject

class HouseholdFragment : RezsimFragment() {

    override val contentId = R.layout.household_fragment

    private val viewModel: HouseholdViewModel by inject()
    private val activityViewModel: MainActivityViewModel by inject()
    private val headerViewModel: HeaderViewModel by inject()

    private lateinit var editName: AppCompatEditText
    private lateinit var layoutMeters: ConstraintLayout
    private lateinit var spinnerUsage: AppCompatSpinner
    private lateinit var spinnerPricingTypeA: AppCompatSpinner
    private lateinit var spinnerPricingTypeB: AppCompatSpinner
    private lateinit var buttonChildrenMinus: AppCompatImageButton
    private lateinit var buttonChildrenPlus: AppCompatImageButton
    private lateinit var editChildren: AppCompatEditText
    private lateinit var spinnerGasHeating: AppCompatSpinner
    private lateinit var switchElectricity: SwitchCompat
    private lateinit var switchGas: SwitchCompat
    private lateinit var editElectricityMeter: AppCompatEditText
    private lateinit var editGasMeter: AppCompatEditText
    private lateinit var layoutElectricityMeter: FrameLayout
    private lateinit var layoutGasMeter: FrameLayout
    private lateinit var layoutElectricityParameters: ConstraintLayout
    private lateinit var layoutElectricitySettings: ConstraintLayout
    private lateinit var layoutGasParameters: ConstraintLayout
    private lateinit var layoutGasSettings: ConstraintLayout

    private var content: Content? = null

    private val spinnerItemSelectedListener = TextSpinnerOnItemSelectedListener()

    override fun setupViews() {
        super.setupViews()
        view?.let {
            editName = it.findViewById(R.id.etName)
            layoutMeters = it.findViewById(R.id.clMeters)
            layoutElectricityMeter = it.findViewById(R.id.flElectricityMeter)
            layoutGasMeter = it.findViewById(R.id.flGasMeter)
            layoutElectricityParameters = it.findViewById(R.id.clElectricityParameters)
            layoutElectricitySettings = it.findViewById(R.id.clElectricitySettings)
            layoutGasParameters = it.findViewById(R.id.clGasParameters)
            layoutGasSettings = it.findViewById(R.id.clGasSettings)
            editElectricityMeter = it.findViewById(R.id.etElectricityMeter)
            editGasMeter = it.findViewById(R.id.etGasMeter)
            spinnerUsage = it.findViewById<AppCompatSpinner?>(R.id.spUsage).apply {
                adapter = TextSpinnerAdapter(requireContext(), viewModel.usageItems())
                onItemSelectedListener = spinnerItemSelectedListener
            }
            spinnerPricingTypeA = it.findViewById<AppCompatSpinner?>(R.id.spPricingTypeA).apply {
                adapter = TextSpinnerAdapter(requireContext(), viewModel.pricingTypeAItems())
                onItemSelectedListener = spinnerItemSelectedListener
            }
            spinnerPricingTypeB = it.findViewById<AppCompatSpinner?>(R.id.spPricingTypeB).apply {
                adapter = TextSpinnerAdapter(requireContext(), viewModel.pricingTypeBItems())
                onItemSelectedListener = spinnerItemSelectedListener
            }
            buttonChildrenMinus = it.findViewById<AppCompatImageButton>(R.id.btChildrenMinus).apply {
                setOnClickListener { viewModel.childrenButtonClicked(editChildren.text.toString(), -1) }
                isEnabled = false
            }
            buttonChildrenPlus = it.findViewById<AppCompatImageButton>(R.id.btChildrenPlus).apply {
                setOnClickListener { viewModel.childrenButtonClicked(editChildren.text.toString(), 1) }
            }
            editChildren = it.findViewById<AppCompatEditText>(R.id.etChildrenCount).apply {
                addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun afterTextChanged(p0: Editable?) {
                        val strValue = p0?.toString()
                        val value = if (strValue.isNullOrEmpty()) 0 else strValue.toInt()
                        buttonChildrenMinus.isEnabled = value > 0
                    }
                })
            }
            spinnerGasHeating = it.findViewById<AppCompatSpinner?>(R.id.spHeatingValue).apply {
                adapter = TextSpinnerAdapter(requireContext(), viewModel.heatingValueItems())
                onItemSelectedListener = spinnerItemSelectedListener
            }
            switchElectricity = it.findViewById<SwitchCompat>(R.id.swElectricityOn).apply {
                setOnCheckedChangeListener { _, b -> viewModel.electricitySwitchChanged(b) }
            }
            switchGas = it.findViewById<SwitchCompat>(R.id.swGasOn).apply {
                setOnCheckedChangeListener { _, b -> viewModel.gasSwitchChanged(b) }
            }
            it.findViewById<AppCompatImageView>(R.id.ivElectricitySetting).setOnClickListener { toggleElectricitySetting() }
            it.findViewById<AppCompatImageView>(R.id.ivGasSetting).setOnClickListener { toggleGasSetting() }
        }
    }

    override fun subscribeObservers() {
        super.subscribeObservers()
        Log.d("DEBINFO-R", "activityViewModel.fabPressedLiveData start observing")
        activityViewModel.fabPressedLiveData.observe(this) {
            Log.d("DEBINFO-R", "activityViewModel.fabPressedLiveData triggered: $it")
            if (it) {
                activityViewModel.clearFabLiveData()
                save()
            }
        }
        viewModel.childrenValueLiveData.observe(this) { editChildren.setText(it.toString()) }
        viewModel.contentLiveData.observe(this) { setContent(it) }
        viewModel.hasElectricityLiveData.observe(this) { setElectricityState(it) }
        viewModel.hasGasLiveData.observe(this) { setGasState(it) }
        headerViewModel.backLiveData.observeForever {
            goBack(it)
        }
    }

    private fun setContent(content: Content?) {
        headerViewModel.setTitle(if (content == null) R.string.household_title_create else R.string.household_title_change)
        layoutMeters.visibility = if (content == null) View.VISIBLE else View.GONE
        editName.setText(content?.name ?: "")
        editElectricityMeter.setText(null)
        editGasMeter.setText(null)
        spinnerUsage.setSelection((content?.electricityUseMode ?: 0) + 1)
        spinnerPricingTypeA.setSelection((content?.electricityPricingA ?: 0) + 1)
        spinnerPricingTypeB.setSelection((content?.electricityPricingB ?: 0) + 1)
        spinnerGasHeating.setSelection((content?.gasHeating ?: 0) + 1)
        editChildren.setText(content?.gasChildren?.toString())
        switchElectricity.isChecked = (content?.hasElectricity ?: true).also {
            viewModel.electricitySwitchChanged(it)
        }
        switchGas.isChecked = (content?.hasGas ?: true).also {
            viewModel.gasSwitchChanged(it)
        }
        this.content = collectValues()
    }

    private fun save() {
        val content = collectValues()
        viewModel.save(content)
    }

    private fun collectValues(): Content =
        view?.let {
            Content(
                name = it.findViewById<AppCompatEditText>(R.id.etName).text.toString(),
                electricityMeter = it.findViewById<AppCompatEditText>(R.id.etElectricityMeter).numericValue() ?: -1,
                gasMeter = it.findViewById<AppCompatEditText>(R.id.etGasMeter).numericValue() ?: -1,
                electricityUseMode = spinnerUsage.selectedItemPosition - 1,
                electricityPricingA = spinnerPricingTypeA.selectedItemPosition - 1,
                electricityPricingB = spinnerPricingTypeB.selectedItemPosition - 1,
                gasChildren = editChildren.numericValue() ?: -1,
                gasHeating = spinnerGasHeating.selectedItemPosition - 1,
                hasElectricity = switchElectricity.isChecked,
                hasGas = switchGas.isChecked
            )
        } ?: error("View is null when collect values")

    private fun setElectricityState(enabled: Boolean) {
        layoutElectricityMeter.visibility = if (enabled) View.VISIBLE else View.GONE
        editElectricityMeter.isEnabled = enabled
        layoutElectricityParameters.visibility = if (enabled) View.VISIBLE else View.GONE
        switchGas.isEnabled = enabled
    }

    private fun setGasState(enabled: Boolean) {
        layoutGasMeter.visibility = if (enabled) View.VISIBLE else View.GONE
        editGasMeter.isEnabled = enabled
        layoutGasParameters.visibility = if (enabled) View.VISIBLE else View.GONE
        switchElectricity.isEnabled = enabled
    }

    private fun goBack(value: Boolean) {
        if (value && activityViewModel.currentFragmentTag() == TAG) {
            headerViewModel.clearBackLiveData()
            if (content != collectValues()) {
                activityViewModel.showMessage(
                    titleResId = R.string.household_message_go_back_title,
                    messageResId = R.string.household_message_go_back,
                    type = MessageType.DIALOG_YES_CANCEL,
                    runnable = { exit() }
                )
            } else {
                exit()
            }
        }
    }

    private fun toggleElectricitySetting() {
        if (layoutElectricitySettings.visibility == View.VISIBLE) {
            layoutElectricitySettings.visibility = View.GONE
        } else {
            layoutElectricitySettings.visibility = View.VISIBLE
        }
    }

    private fun toggleGasSetting() {
        if (layoutGasSettings.visibility == View.VISIBLE) {
            layoutGasSettings.visibility = View.GONE
        } else {
            layoutGasSettings.visibility = View.VISIBLE
        }
    }

    private fun exit() {
        activityViewModel.switchToFragment(MainFragment.TAG)
    }

    companion object {
        const val TAG = "HouseholdFragment"
        fun newInstance() = HouseholdFragment()
    }

}