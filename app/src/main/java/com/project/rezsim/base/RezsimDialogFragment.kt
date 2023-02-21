package com.project.rezsim.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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
            inflater.inflate(contentId, container, false)
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
        requireDialog().window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.width = width
            setAttributes(attributes)
        }
    }

    fun setProgress(show: Boolean) {
        view?.let {
            if (it is ConstraintLayout) {
                if (show) {
                    if (cover == null) cover = createCover()
                    it.addView(cover)
                } else {
                    it.removeView(cover)
                }
            }
        }
    }

    private fun createCover() = FrameLayout(requireContext()).apply {
//        layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
        val a = dialog?.window?.attributes
//        layoutParams = ConstraintLayout.LayoutParams(100, )
        background = ColorDrawable(Color.RED)
        isClickable = true
    }


}