package com.library.core

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.library.core.event.FragmentNavigationDirection
import com.library.core.event.HandleOnActivityResult
import com.library.core.event.ShowToastEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.ref.WeakReference

abstract class BaseActivity(val layoutRes: Int) : AppCompatActivity() {

    protected lateinit var navController: NavController

    protected abstract fun getNavRes(): Int

    private var onActivityResultHandler: WeakReference<OnActivityResultHandler>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
        init()
    }

    fun init() {
        if (getNavRes() != 0) {
            navController = Navigation.findNavController(this, getNavRes())
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    fun navigate(event: FragmentNavigationDirection) {
        navController.navigate(event.nav)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: HandleOnActivityResult) {
        onActivityResultHandler = WeakReference(event.handler)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showToast(event: ShowToastEvent) {
        Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        registerEventBus()
    }

    override fun onPause() {
        unregisterEventBus()
        super.onPause()
    }

    private fun registerEventBus() {
//        log("registerEventBus call for: " + this::class.simpleName)
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
//            log("event bus registered for: " + this::class.simpleName)
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        onActivityResultHandler?.let {
            onActivityResultHandler?.get()?.onActivityResult(requestCode, resultCode, data)
            onActivityResultHandler = null
        } ?: run {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun unregisterEventBus() {
        EventBus.getDefault().unregister(this)
//        log("event bus UNregistered for: " + this::class.simpleName)
    }

}