package com.preetanshumishra.patternkit.android.mvvmcleanxml

import android.app.Application
import com.preetanshumishra.patternkit.android.mvvmcleanxml.di.AppComponent
import com.preetanshumishra.patternkit.android.mvvmcleanxml.di.DaggerAppComponent

class PatternKitApp : Application() {
    lateinit var component: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.create()
    }
}
