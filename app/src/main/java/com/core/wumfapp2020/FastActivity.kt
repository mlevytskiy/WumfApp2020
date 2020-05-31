package com.core.wumfapp2020

//import android.content.Intent
//import android.graphics.Color
//import android.os.Build
//import android.os.Bundle
//import android.text.Html
//import android.view.View
//import android.view.ViewGroup
//import android.view.WindowManager
//import android.view.animation.Animation
//import android.view.animation.AnimationUtils
//import android.widget.TextView
//import androidx.annotation.RequiresApi
//import androidx.appcompat.app.AppCompatActivity
//import androidx.constraintlayout.motion.widget.MotionLayout
//
//class FastActivity : AppCompatActivity() {
//
//    private val WELCOME = "<p>Hi friend!</p>\n" +
//            "<p>Here you can:</p>\n" +
//            "<p>• Create collection with your favourite apps and share them with friends. <a href=\"http://google.com/\">Details...</a></p>\n" +
//            "<p>• Install and use different plugins&nbsp;<a href=\"http://google.com/\">Details...</a></p>"
//
//
//    @RequiresApi(Build.VERSION_CODES.P)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        window.apply {
//            val currentStatusBarColor = getColor(R.color.white)
//            val currentNavBarColor = Color.BLACK
//            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//            statusBarColor = currentStatusBarColor
//            navigationBarColor = currentNavBarColor
//            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//        }
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_fast)
//        val rotate = AnimationUtils.loadAnimation(this, R.anim.splash_screen_rotate)
//        rotate.interpolator = WaveInterpolator()
//
//        rotate.setAnimationListener(object : Animation.AnimationListener {
//            override fun onAnimationRepeat(animation: Animation?) { }
//
//            override fun onAnimationEnd(animation: Animation?) {
//                findViewById<TextView>(R.id.message).text = Html.fromHtml(WELCOME)
//                val rootView = findViewById<MotionLayout>(R.id.root)
//                rootView.transitionToEnd()
//                window.apply {
//                    val currentStatusBarColor = Color.WHITE
//                    val currentNavBarColor = Color.BLACK
//                    setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//                    clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//                    addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//                    statusBarColor = currentStatusBarColor
//                    navigationBarColor = currentNavBarColor
//                }
//            }
//
//            override fun onAnimationStart(animation: Animation?) { }
//
//        })
//        val image = findViewById<View>(R.id.image)
//        image.startAnimation(rotate)
//    }
//
//    fun onClickGo(view: View) {
//        startActivity(Intent(this, MainActivity::class.java))
//    }
//}