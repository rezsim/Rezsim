package com.project.rezsim.teszt

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.rezsim.R

class FragmentB : Fragment() {

    init {
        Log.d("DEBINFO-RT", "FragmentB.init")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("DEBINFO-RT", "FragmentB.onAttach()")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("DEBINFO-RT", "FragmentB.onCreate() savedInstanceState:$savedInstanceState")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("DEBINFO-RT", "FragmentB.onCreateView()")
        return inflater.inflate(R.layout.b_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("DEBINFO-RT", "FragmentB.onViewCreated() savedInstanceState:$savedInstanceState")
    }

    override fun onStart() {
        super.onStart()
        Log.d("DEBINFO-RT", "FragmentB.onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.d("DEBINFO-RT", "FragmentB.onResume()")
    }

    override fun onPause() {
        super.onPause()
        Log.d("DEBINFO-RT", "FragmentB.onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.d("DEBINFO-RT", "FragmentB.onStop()")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("DEBINFO-RT", "FragmentB.onDestroyView()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("DEBINFO-RT", "FragmentB.onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("DEBINFO-RT", "FragmentB.onDetach()")
    }

    companion object {
        const val TAG = "FragmentB"
        fun newInstance() = FragmentB()
    }

}