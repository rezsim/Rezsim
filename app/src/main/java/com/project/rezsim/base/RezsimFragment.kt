package com.project.rezsim.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.madhava.keyboard.vario.base.Singletons
import org.koin.android.ext.android.get
import kotlin.reflect.KClass

open class RezsimFragment : Fragment() {

    protected open val contentId: Int = 0

    open fun setupViews() {}
    open fun subscribeObservers() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = if (contentId != 0) {
        inflater.inflate(contentId, container, false)
    } else {
        error("contentId not defined in ${this.javaClass.name}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        subscribeObservers()
    }


}