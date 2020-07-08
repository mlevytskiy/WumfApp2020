package com.core.wumfapp2020.testdi

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.core.wumfapp2020.R

class WumfActivity : AppCompatActivity() {

//    private val viewModel by lazyViewModel { injector.preOnBoardingViewModel }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wumf)
    }

    fun onClick(v: View) {
        onDownload()
    }

    fun onDownload() {
//        val request = SplitInstallRequest.newBuilder()
//            .addModule("onboarding")
//            .build()
//        manager.registerListener {
//            when (it.status()) {
//                SplitInstallSessionStatus.DOWNLOADING -> showToast("Downloading feature")
//                SplitInstallSessionStatus.INSTALLED -> {
//                    showToast("Feature ready to be used")
//                }
//                else -> { showToast("${it.status()}") }
//            }
//        }
//        manager.startInstall(request).addOnSuccessListener {
//            showToast("success")
//            val intent = Intent()
//            intent.setClassName("com.core.wumfapp2020", "com.core.dynamicfeature.OnBoardingActivity2")
//            startActivity(intent)
//        }.addOnFailureListener {
//            val sw = StringWriter()
//            it.printStackTrace(PrintWriter(sw))
//            val exceptionAsString: String = sw.toString()
//            Log.e("testr", "", it)
//            showToast("failed:" + exceptionAsString)
//        }
    }

    private fun showToast(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show()
    }
}