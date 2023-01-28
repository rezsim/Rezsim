package com.project.rezsim.tool

import androidx.lifecycle.MutableLiveData

class Timer(private val runnable: Runnable) {

    private var cancelled = false

    private val liveData = MutableLiveData<Boolean>()

    init {
        liveData.observeForever {
            if (!cancelled) {
                runnable.run()
            }
        }
    }

    fun cancel() {
        cancelled = true
    }

    companion object {
        fun runDelayed(runnable: Runnable, delay: Long): Timer = Timer(runnable).apply {
            Thread {
                try {
                    Thread.sleep(delay)
                    liveData.postValue(true)
                } catch (e: Exception) {}
            }.start()
        }


    }

}