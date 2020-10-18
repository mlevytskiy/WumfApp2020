package com.core.wumfapp2020

import android.animation.ArgbEvaluator
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionLayout.TransitionListener
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import com.core.wumfapp2020.di.injector
import com.library.core.log
import io.opencensus.stats.Stats.setState

class FastActivity : AppCompatActivity() {

    private val WELCOME = "<p>Hi friend!</p>\n" +
            "<p>Here you can:</p>\n" +
            "<p>• Create collection with your favourite apps and share them with friends. <a href=\"http://google.com/\">Details...</a></p>\n" +
            "<p>• Install and use different plugins&nbsp;<a href=\"http://google.com/\">Details...</a></p>"

    private val memory by lazy {
        injector.provideUserInfoRepository()
    }


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        window.apply {
            val currentStatusBarColor = Color.TRANSPARENT
            val currentNavBarColor = Color.TRANSPARENT
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = currentStatusBarColor
            navigationBarColor = currentNavBarColor
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
        super.onCreate(savedInstanceState)
        if (!memory.isEmpty()) {
            nextScreen()
            return
        }
        setContentView(R.layout.activity_fast)
        val bgView = findViewById<View>(R.id.bg_view)
        "FastActivity onCreate()".log()
        val root = findViewById<View>(R.id.root)
        findViewById<MotionLayout>(R.id.root).setTransitionListener(object:TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {

            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
//                "p1=$p1 p2=$p2 p3=$p3".log()
                setWindowsColor(p3)
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {

            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {

            }

        })
        val rotate = AnimationUtils.loadAnimation(this, R.anim.splash_screen_rotate)
        rotate.interpolator = WaveInterpolator()

        rotate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) { }

            override fun onAnimationEnd(animation: Animation?) {
                findViewById<TextView>(R.id.message).text = Html.fromHtml(WELCOME)
                val rootView = findViewById<MotionLayout>(R.id.root)
                rootView.transitionToEnd()
            }

            override fun onAnimationStart(animation: Animation?) { }

        })
        val image = findViewById<View>(R.id.image)
        image.startAnimation(rotate)
    }

    fun onClickGo(view: View) {
        nextScreen()
    }

    fun nextScreen() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    val evaluator = ArgbEvaluator()
    val startStatusBarColor = Color.TRANSPARENT
    val endStatusBarColor = Color.WHITE
    val startNavColor = Color.TRANSPARENT
    val endNavColor by lazy { resources.getColor(R.color.green) }

    fun setWindowsColor(changeIndicator: Float) {
        window.apply {
            val currentStatusBarColor = evaluator.evaluate(changeIndicator, startStatusBarColor, endStatusBarColor) as Int
            val currentNavBarColor = evaluator.evaluate(changeIndicator, startNavColor, endNavColor) as Int
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = currentStatusBarColor
            navigationBarColor = currentNavBarColor
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }


}