package com.project.rezsim.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import com.project.rezsim.R
import com.project.rezsim.device.ScreenRepository
import com.project.rezsim.device.dp
import com.project.rezsim.ui.screen.activity.MainActivityViewModel
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent
import kotlin.math.max
import kotlin.math.min

open class RezsimDialogFragment : DialogFragment(), KoinComponent {

    protected open val contentId: Int = 0

    var resultLiveData: MutableLiveData<Boolean>? = null

    open fun setupDialog() {}
    open fun setupViews() {}
    open fun subscribeObservers() {}
    open fun start() {}

    protected val screenRepository: ScreenRepository by inject()
    protected val activityViewModel: MainActivityViewModel by inject()

    private var root: FrameLayout? = null
    private var cover: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireDialog().window?.requestFeature(Window.FEATURE_NO_TITLE)

        return if (contentId != 0) {
            inflater.inflate(R.layout.dialog, container, false).apply {
                root = findViewById(R.id.clRootR)
                val cont = findViewById<ConstraintLayout>(R.id.cContainer)
                inflater.inflate(contentId, cont, true)
                cover = findViewById(R.id.clCover)
            }
        } else {
            error("contentId not defined in ${this.javaClass.name}")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDialog()
        setupViews()
    }

    override fun onStart() {
        super.onStart()
        start()
    }

    override fun onResume() {
        super.onResume()
        val minWidth = requireContext().resources.getDimension(R.dimen.dialog_min_width)
        val width = min((screenRepository.screenSize().width * 0.8f), minWidth).toInt()
        val margin = resources.getDimension(R.dimen.dialog_margin).toInt().dp
        root?.layoutParams?.width = width - 2 * margin
        requireDialog().window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.width = width
            setAttributes(attributes)
        }
        root?.requestLayout()
    }

    override fun dismiss() {
        super.dismiss()
        activityViewModel.hideKeyboard()
    }

    fun setProgress(show: Boolean) {
        cover?.visibility = if (show) View.VISIBLE else View.GONE
    }



}