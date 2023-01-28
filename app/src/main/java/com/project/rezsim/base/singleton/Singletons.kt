package com.madhava.keyboard.vario.base

import com.project.rezsim.base.singleton.Singleton
import com.project.rezsim.base.singleton.SingletonFactory
import kotlin.reflect.KClass

object Singletons {

        private val instances = HashMap<String, Singleton>()

        fun <T : Singleton> instance(clazz: KClass<T>) : Singleton = instances[clazz.qualifiedName]
                ?: SingletonFactory.createInstance(clazz)?.also { instances[clazz.qualifiedName ?: ""] = it }
                ?: error ("${clazz.qualifiedName} not found in SingletonFactory")

        fun clearAll() {
                instances.clear()
        }
}