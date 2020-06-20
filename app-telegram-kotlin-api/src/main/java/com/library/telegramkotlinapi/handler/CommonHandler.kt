package com.library.telegramkotlinapi.handler

import android.content.Context
import android.os.Build
import android.util.Log
import org.drinkless.td.libcore.BuildConfig
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import java.util.*

class CommonHandler(private val context: Context, var client: Client? = null): Client.ResultHandler {

    override fun onResult(obj: TdApi.Object?) {
        when(obj?.getConstructor()) {
            TdApi.UpdateAuthorizationState.CONSTRUCTOR -> {
                Log.i("testr", "TdApi.UpdateAuthorizationState.CONSTRUCTOR state=" + (obj as TdApi.UpdateAuthorizationState).authorizationState)
                onUpdateAuthorizationState((obj as TdApi.UpdateAuthorizationState).authorizationState)

            }
            TdApi.AuthorizationStateWaitEncryptionKey.CONSTRUCTOR -> {
                Log.i("testr", "TdApi.AuthorizationStateWaitEncryptionKey.CONSTRUCTOR")
                TdApi.SetDatabaseEncryptionKey()

            }
            TdApi.CheckAuthenticationCode.CONSTRUCTOR -> {
                Log.i("testr", "TdApi.CheckAuthenticationCode.CONSTRUCTOR")
            }
        }
    }

    fun onUpdateAuthorizationState(authorizationState: TdApi.AuthorizationState) {
        when (authorizationState.constructor) {
            TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR -> {
                val parameters = TdApi.TdlibParameters()
                parameters.apiId = 1043772
                parameters.apiHash = "9a7362f6ec33bc23187f757ecee5e83c"
                parameters.enableStorageOptimizer = true
                parameters.useMessageDatabase = false//Разрешаем кэшировать чаты и сообщения
                parameters.useFileDatabase = false//Разрешаем кэшировать файлы
                parameters.useChatInfoDatabase = false
                parameters.useSecretChats = false
                parameters.useTestDc = false
                parameters.filesDirectory =
                    context.getExternalFilesDir(null)!!.getAbsolutePath() + "/"//Путь к файлам
                parameters.databaseDirectory =
                    context.getExternalFilesDir(null)!!.getAbsolutePath() + "/"//Пусть к базе данных
                parameters.systemVersion = Build.VERSION.RELEASE//Версия ос
                parameters.deviceModel = Build.DEVICE//Модель устройства
                parameters.systemLanguageCode = Locale.getDefault().language
                parameters.applicationVersion = BuildConfig.VERSION_NAME
                client?.send(TdApi.SetTdlibParameters(parameters), this)
            }
            TdApi.AuthorizationStateWaitEncryptionKey.CONSTRUCTOR -> client?.send(
                TdApi.SetDatabaseEncryptionKey(),
                this
            )
            TdApi.CheckAuthenticationCode.CONSTRUCTOR -> {
                Log.i("testr", "TdApi.CheckAuthenticationCode.CONSTRUCTOR 2")
            }
        }
    }

}