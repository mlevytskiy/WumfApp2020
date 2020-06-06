package com.library.core

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation

abstract class BaseActivity(val layoutRes: Int) : AppCompatActivity() {

    protected lateinit var navController: NavController

    protected abstract fun getNavRes(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
        init()
    }

    fun init() {
        if (getNavRes() != 0) {
            navController = Navigation.findNavController(this, getNavRes())
            Log.i("testr", "current=" + navController.currentDestination)
        }
    }

}