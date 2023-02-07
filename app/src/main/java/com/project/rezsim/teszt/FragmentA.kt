package com.project.rezsim.teszt

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.rezsim.R
import com.project.rezsim.ui.screen.household.HouseholdFragment

class FragmentA : Fragment() {

    init {
        Log.d("DEBINFO-RT", "FragmentA.init")
   }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("DEBINFO-RT", "FragmentA.onAttach()")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("DEBINFO-RT", "FragmentA.onCreate() savedInstanceState:$savedInstanceState")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("DEBINFO-RT", "FragmentA.onCreateView()")
        return inflater.inflate(R.layout.a_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("DEBINFO-RT", "FragmentA.onViewCreated() savedInstanceState:$savedInstanceState")
    }

    override fun onStart() {
        super.onStart()
        Log.d("DEBINFO-RT", "FragmentA.onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.d("DEBINFO-RT", "FragmentA.onResume()")
    }

    override fun onPause() {
        super.onPause()
        Log.d("DEBINFO-RT", "FragmentA.onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.d("DEBINFO-RT", "FragmentA.onStop()")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("DEBINFO-RT", "FragmentA.onDestroyView()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("DEBINFO-RT", "FragmentA.onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("DEBINFO-RT", "FragmentA.onDetach()")
    }

    companion object {
        const val TAG = "FragmentA"
        fun newInstance() = FragmentA()
    }

}