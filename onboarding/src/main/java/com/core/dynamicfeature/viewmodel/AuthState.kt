package com.core.dynamicfeature.viewmodel

sealed class AuthState
object EnterPhone : AuthState()
object EnterCode : AuthState()
data class EnterPassword(val hint: String) : AuthState()
object AuthTelegramOk : AuthState()

//array AuthStates - EnterPhone (success, inProgress, failed), EnterCode(success, inProgress, failed),
// EnterPassword(success, dont_need, inProgress, failed)
// GetContactsListFromTelegram
// CheckPhoneInWumfBE(success, inProgress, failed)
// OnBoarding(success, inProgress, failed)