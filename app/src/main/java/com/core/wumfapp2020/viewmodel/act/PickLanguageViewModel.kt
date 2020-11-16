package com.core.wumfapp2020.viewmodel.act

import androidx.lifecycle.LiveData
import com.core.wumfapp2020.memory.UserInfoRepository
import com.core.wumfapp2020.util.CurrentLanguageModel
import com.core.wumfapp2020.util.Languages
import com.library.Event
import com.library.core.BaseViewModel
import com.library.core.SingleLiveEvent
import javax.inject.Inject

class PickLanguageViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
    private val allLanguages: Languages,
    private val currentLanguageModel: CurrentLanguageModel) : BaseViewModel() {
    val languages: List<LanguageViewModel>

    val goBackMutable = SingleLiveEvent<Event<Unit>>()
    val goBack: LiveData<Event<Unit>> = goBackMutable

    val pickLanguageMutable = SingleLiveEvent<Event<String>>()
    val pickLanguage: LiveData<Event<String>> = pickLanguageMutable

    init {
        languages = allLanguages.all.map { LanguageViewModel(it, this::onPickLanguage) }
    }

    class LanguageViewModel constructor(val language: String, private val onClickAction: (String)->Unit) {
        fun onClick() {
            onClickAction(language)
        }
    }

    fun onPickLanguage(language: String) {
        currentLanguageModel.getShortLanguageName(language)?.let {
            pickLanguageMutable.postEvent(Event(it))
        }
        back()
    }

    fun back() {
        goBackMutable.postEvent(Event(Unit))
    }
}