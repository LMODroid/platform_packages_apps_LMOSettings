/*
 * Copyright (C) 2022 FlamingoOS Project
 * Copyright (C) 2024 The LibreMobileOS Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.security.applock;

import android.content.Context

import androidx.preference.Preference
import androidx.preference.PreferenceScreen

import com.android.settings.R
import com.android.settings.core.TogglePreferenceController
import com.android.settingslib.widget.MainSwitchPreference

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

internal val appLockWriteScope = CoroutineScope(
    SupervisorJob() + Dispatchers.IO.limitedParallelism(1)
)

abstract class AppLockTogglePreferenceController(
    context: Context,
    key: String,
) : TogglePreferenceController(context, key) {

    override fun displayPreference(screen: PreferenceScreen) {
        super.displayPreference(screen)
    }

    override fun getSliceHighlightMenuRes() = R.string.menu_key_security
}
