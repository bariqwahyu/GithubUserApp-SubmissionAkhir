package com.bariq.bariqgithub3.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ThemeViewModel(private val pref: SettingPreferences) : ViewModel() {
    fun getThemeSetting(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeOn: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeOn)
        }
    }
}