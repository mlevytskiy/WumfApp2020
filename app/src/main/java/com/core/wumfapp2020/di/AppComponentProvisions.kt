package com.core.wumfapp2020.di

import android.content.Context
import com.app.api.api.WumfApi
import com.core.wumfapp2020.base.dialogViewModels.*
import com.core.wumfapp2020.memory.UserInfoRepository
import com.core.wumfapp2020.testdi.Obj1T
import com.core.wumfapp2020.viewmodel.*
import com.google.android.play.core.splitinstall.SplitInstallManager
import krafts.alex.tg.TgClient

interface ViewModelProvision {
    var preOnBoardingViewModel: PreOnBoardingViewModel
    var homeViewModelFactory: HomeViewModel.Factory
    var appsViewModel: AppsViewModel
    var friendsViewModel: FriendsViewModel
    var profileViewModel: ProfileViewModel
    var moreViewModel: MoreViewModel
    var peopleWhoLikesViewModel: PeopleWhoLikesViewModel
    var mainActivityViewModel: MainActivityViewModel
    var sharedViewModel: SharedViewModel
    var addAppInMyCollectionViewModel : AddAppInMyCollectionViewModel
    var checkAppInGooglePlayFactory: CheckAppInGooglePlayViewModel.Factory
    var successLoginViewModelFactory: SuccessLoginViewModel.Factory
    var logoutViewModelFactory: LogOutViewModel.Factory

    //Dialogs
    var appViewModelFactory: AppViewModel.Factory
    var appInMyCollectionViewModelFactory: AppInMyCollectionViewModel.Factory
}

interface ForDeliveryFeaturesProvision {
    fun provideContext(): Context
    fun provideCustomObject(): Obj1T
    fun provideSplitInstallManager(): SplitInstallManager
    fun provideUserInfoRepository(): UserInfoRepository
    fun provideWumfApi(): WumfApi
    fun provideTelegramClient(): TgClient
}