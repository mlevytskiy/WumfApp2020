package krafts.alex.tg

import android.content.Context
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import krafts.alex.tg.entity.Chat
import org.drinkless.td.libcore.BuildConfig
import org.drinkless.td.libcore.telegram.TdApi
import java.util.*

@ExperimentalCoroutinesApi
class TgClient(private val context: Context) : TelegramFlow(),
    CoroutineScope by CoroutineScope(Dispatchers.IO + SupervisorJob()) {


    private val authState = TdApi.UpdateAuthorizationState().mapAsFlow { it.authorizationState }

    init {
        authState
            .onEach {
                delay(200)
                "{authState $it}".log()
                when (it) {
                    is TdApi.AuthorizationStateWaitTdlibParameters -> {
                        TdApi.SetTdlibParameters(getParams(context)).launch()
                    }

                    is TdApi.AuthorizationStateWaitEncryptionKey -> {
                        TdApi.CheckDatabaseEncryptionKey().launch()
                    }
                    is TdApi.AuthorizationStateWaitPhoneNumber -> {
                        loginStateMutable.postValue(EnterPhone)
                    }
                    is TdApi.AuthorizationStateReady -> {
                        loginStateMutable.postValue(AuthOk)
                    }
                    is TdApi.AuthorizationStateWaitCode -> {
                        loginStateMutable.postValue(EnterCode)
                    }
                    is TdApi.AuthorizationStateWaitPassword -> {
                        loginStateMutable.postValue(EnterPassword(it.passwordHint))
                    }
                }
            }.launchIn(this)
    }

    private val loginStateMutable = MutableLiveData<AuthState?>()
    val loginState: LiveData<AuthState?> = loginStateMutable

    val haveAuthorization: Boolean get() = loginState.value == AuthOk

    suspend fun init() {
        TdApi.SetTdlibParameters(getParams(context)).launch()
    }

    suspend fun sendAuthPhone(phone: String) =
        TdApi.SetAuthenticationPhoneNumber(phone, null).expect<TdApi.Ok>()

    suspend fun sendAuthCode(code: String) =
        TdApi.CheckAuthenticationCode(code).expect<TdApi.Ok>()

    suspend fun sendAuthPassword(password: String) =
        TdApi.CheckAuthenticationPassword(password).expect<TdApi.Ok>()

    val newMessageFlow = TdApi.UpdateNewMessage().asFlow()

    val userStatusFlow = TdApi.UpdateUserStatus().asFlow().apply {
        onEach {
            "userStatusFlow $it.status".log()
        }
    }

    val updateNewChatFlow = TdApi.UpdateNewChat().asFlow()
        .mapNotNull {
            Chat.fromTg(it.chat)
        }.onEach {
            if (it.photoBig?.downloaded == false)
                TdApi.DownloadFile(it.photoBig.fileId, 32, 0, 0, true).launch()
        }

    val updateMessageFlow = TdApi.UpdateMessageContent().asFlow().onEach {
        "updated $it".log()
    }

    val deleteMessageFlow = TdApi.UpdateDeleteMessages().asFlow().onEach {
        "deleted $it".log()
    }

    val userFlow = TdApi.UpdateUser().asFlow()

    suspend fun downloadFile(fileId: Int) =
        TdApi.DownloadFile(fileId, 32, 0, 0, true).async<TdApi.File>()

    fun registerFirebaseNotifications(token: String) {
        TdApi.RegisterDevice(
            TdApi.DeviceTokenFirebaseCloudMessaging(token, false), null
        )
    }

    suspend fun getMe(): TdApi.User {
        return TdApi.GetMe().async()
    }

    suspend fun getFile() {
        TdApi.DownloadFile()
    }

    suspend fun logOut(): TdApi.Object {
        return TdApi.LogOut().async()
    }

    suspend fun destroy(): TdApi.Object {
        return TdApi.Destroy().async()
    }

    suspend fun getContacts(): TdApi.Users {
        return TdApi.GetContacts().async()
    }

    suspend fun getUser(userId: Int): TdApi.User {
        "get user userId=$userId".log()
        return TdApi.GetUser(userId).async()
    }

    fun getChatInfo(chatId: Long) {
        //        sendClient(TdApi.GetChat(chatId))
    }

    fun getUserInfo(userId: Int) {
        //        sendClient(TdApi.GetUser(userId))
    }

    fun loadImage(id: Int) {
        //        sendClient(TdApi.DownloadFile(id, 32, 0, 0, false))
    }

    data class Vpn(
        val ip: String,
        val port: Int,
        val username: String,
        val password: String
    )

    suspend fun addProxy(vpn: Vpn) {
        //TODO: use vpn if needed
        TdApi.AddProxy(
            vpn.ip,
            vpn.port,
            true,
            TdApi.ProxyTypeSocks5(vpn.username, vpn.password)
        ).launch()
    }

    companion object {
        fun getParams(context: Context): TdApi.TdlibParameters {
            return TdApi.TdlibParameters().apply {
                apiId = 1043772
                apiHash = "9a7362f6ec33bc23187f757ecee5e83c"
                enableStorageOptimizer = true
                useMessageDatabase = false//Разрешаем кэшировать чаты и сообщения
                useFileDatabase = false//Разрешаем кэшировать файлы
                useChatInfoDatabase = false
                useSecretChats = false
                useTestDc = false
                filesDirectory =
                    context.getExternalFilesDir(null)!!.getAbsolutePath() + "/"//Путь к файлам
                databaseDirectory =
                    context.getExternalFilesDir(null)!!.getAbsolutePath() + "/"//Пусть к базе данных
                systemVersion = Build.VERSION.RELEASE//Версия ос
                deviceModel = Build.DEVICE//Модель устройства
                systemLanguageCode = Locale.getDefault().language
                applicationVersion = BuildConfig.VERSION_NAME
            }
        }
//        private val parameters = TdApi.TdlibParameters().apply {
////            databaseDirectory = "/data/user/0/krafts.alex.backupgram.app/files/tdlib"
////            useMessageDatabase = false
////            useSecretChats = false
////            apiId = BuildConfig.apiId
////            apiHash = BuildConfig.apiHash
////            useFileDatabase = true
////            systemLanguageCode = "en"
////            deviceModel = "Desktop"
////            systemVersion = "Undegram"
////            applicationVersion = "1.0"
////            enableStorageOptimizer = true
//        }
    }
}