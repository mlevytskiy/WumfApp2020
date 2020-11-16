package com.core.dynamicfeature.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.core.dynamicfeature.R
import com.core.dynamicfeature.databinding.FrgDetectingPhoneNumberBinding
import com.core.dynamicfeature.di.featureInjector
import com.core.dynamicfeature.viewmodel.DetectingYourPhoneNumberViewModel
import com.core.wumfapp2020.activities.MainActivity
import com.core.wumfapp2020.base.AppBaseFragment
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.gms.common.api.GoogleApiClient
import com.library.core.OnActivityResultHandler
import com.library.core.lazyViewModel
import java.lang.ref.WeakReference

class DetectingYourPhoneNumberFragment: AppBaseFragment<FrgDetectingPhoneNumberBinding, DetectingYourPhoneNumberViewModel>(R.layout.frg_detecting_phone_number),
    OnActivityResultHandler {

    private var googleApiClient: GoogleApiClient? = null
    private val REQUEST_PHONE_NUMBER_CODE = 565

    override val viewModel by lazyViewModel { featureInjector.detectingViewModelFactory.create() }

    override fun setViewModelInBinding(binding: FrgDetectingPhoneNumberBinding, viewModel: DetectingYourPhoneNumberViewModel) {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel.isNeedDetectPhoneNumber()) {
            googleApiClient = GoogleApiClient
                .Builder(requireContext())
                .enableAutoManage(
                    requireActivity()
                ) { viewModel.cantDetectPhoneNumber() }
                .addApi(Auth.CREDENTIALS_API)
                .build()
        } else {
            viewModel.nextScreen()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState(viewModel.showDetectPhoneNumber) {
            showDetectPhoneNumberHint()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_PHONE_NUMBER_CODE && resultCode == Activity.RESULT_OK) {
            val credentials = data?.getParcelableExtra<Credential>(Credential.EXTRA_KEY)
            credentials?.id?.let {
                viewModel.detectPhoneNumber(it)
                return
            }
        }
        viewModel.cantDetectPhoneNumber()
    }

    override fun onResume() {
        super.onResume()
        googleApiClient?.connect()
        if (!viewModel.isPhoneNumberDetectingStarted()) {
            viewModel.startPhoneNumberDetecting()
        } else {
            viewModel.resumePhoneNumberDetecting()
        }
    }

    override fun onPause() {
        super.onPause()
        googleApiClient?.stopAutoManage(requireActivity())
        googleApiClient?.disconnect()
        viewModel.interruptPhoneNumberDetecting()
    }

    fun showDetectPhoneNumberHint() {
        val hintRequest = HintRequest.Builder()
            .setPhoneNumberIdentifierSupported(true)
            .build()
        val intent = Auth.CredentialsApi.getHintPickerIntent(googleApiClient, hintRequest)
        val mainActivity = activity as MainActivity
        mainActivity.onActivityResultHandler = WeakReference(this)
        activity?.startIntentSenderForResult(intent.intentSender,
            REQUEST_PHONE_NUMBER_CODE, null, 0, 0, 0,  null)
    }
}