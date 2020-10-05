package com.core.wumfapp2020.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.core.dynamicfeature.fragment.EnterTelegramPasswordFragmentArgs
import com.core.wumfapp2020.R
import com.core.wumfapp2020.base.AppBaseFragment
import com.core.wumfapp2020.base.showAppDialog
import com.core.wumfapp2020.base.showAppDialogFromMyCollection
import com.core.wumfapp2020.databinding.FrgFriendDetailBinding
import com.core.wumfapp2020.di.injector
import com.core.wumfapp2020.util.showInGooglePlay
import com.core.wumfapp2020.viewmodel.FriendDetailViewModel
import com.library.core.lazySavedStateViewModel

class FriendDetailFragment : AppBaseFragment<FrgFriendDetailBinding, FriendDetailViewModel>(R.layout.frg_friend_detail) {

    override val viewModel by lazySavedStateViewModel {
        val args: FriendDetailFragmentArgs by navArgs()
        injector.friendDetailViewModelFactory.create(friend = args.friend)
    }

    override fun setViewModelInBinding(binding: FrgFriendDetailBinding, viewModel: FriendDetailViewModel) {
        binding.viewModel = viewModel
        binding.toolbar.setupWithNavController(navController, null)
        binding.toolbar.navigationIcon = requireContext().getDrawable(R.drawable.ic_arrow_white)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var dialog : DialogInterface? = null
        val context = view.context
        binding.appsRecycleView.setItemClick { app, likes ->
            dialog = showAppDialog(appContainer = app, context = context,
                showInGPBlock = {
                    dialog?.dismiss()
                    context.showInGooglePlay(app.packageName)
                }, peopleWhoLikes = emptyList(), showPeopleWhoLikesBlock = { })
        }
    }

}