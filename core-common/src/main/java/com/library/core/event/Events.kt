package com.library.core.event

import androidx.navigation.NavDirections
import com.library.core.BaseViewModel
import com.library.core.OnActivityResultHandler
import kotlin.reflect.KClass

class FragmentNavigationDirection(val nav: NavDirections) : BaseEvent()

class PopBackTo(val id: Int, val inclusive: Boolean) : BaseEvent()

class ShowToastEvent(val message: String) : BaseEvent()

class ShowBottomNavEvent(val animate: Boolean = false) : BaseEvent()

class HideBottomNavEvent(val animate: Boolean = false) : BaseEvent()

class HandleOnActivityResult(val handler: OnActivityResultHandler) : BaseEvent()