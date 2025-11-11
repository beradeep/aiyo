package com.beradeep.aiyo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import com.beradeep.aiyo.domain.model.ThemeType
import com.beradeep.aiyo.domain.repository.SettingRepository
import com.beradeep.aiyo.nav.AiyoNavHost
import com.beradeep.aiyo.ui.AiyoTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var settingRepository: SettingRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isSystemThemeDark = isSystemInDarkTheme()
            val isDarkTheme by produceState(
                isSystemThemeDark,
                isSystemThemeDark,
                settingRepository
            ) {
                value = when (settingRepository.getThemeType()) {
                    ThemeType.System -> isSystemThemeDark
                    ThemeType.Light -> false
                    ThemeType.Dark -> true
                }
            }

            AiyoTheme(isDarkTheme) {
                AiyoNavHost(Modifier.fillMaxSize())
            }
        }
    }
}
