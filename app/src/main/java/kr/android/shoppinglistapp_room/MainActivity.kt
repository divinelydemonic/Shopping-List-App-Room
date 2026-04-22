package kr.android.shoppinglistapp_room

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import kr.android.shoppinglistapp_room.navigation.Navigation
import kr.android.shoppinglistapp_room.ui.theme.ShoppingListApp_RoomTheme
import kr.android.shoppinglistapp_room.ui.theme.ThemeMode
import kr.android.shoppinglistapp_room.util.LocationPermissionHandler
import kr.android.shoppinglistapp_room.util.LocationUtil
import kr.android.shoppinglistapp_room.viewmodel.LocationViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            //to track the theme of device
            var themeMode by rememberSaveable {
                mutableStateOf(ThemeMode.SYSTEM)
            }

            //dark mode toggle
            val isDark = when (themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }

            val locationViewModel : LocationViewModel = viewModel()
            val navController = rememberNavController()
            val context = LocalContext.current
            val locationUtil = LocationUtil(context)

            ShoppingListApp_RoomTheme (darkTheme = isDark) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LocationPermissionHandler(
                        locationViewModel = locationViewModel,
                        locationUtil = locationUtil,
                        navController = navController
                    ) {
                        Navigation(
                            modifier = Modifier.padding(innerPadding),
                            themeMode = themeMode,
                            isDark = isDark,
                            onThemeChange = { themeMode = it },
                            locationViewModel = locationViewModel,
                            navController = navController,
                            context = context,
                            locationUtil = locationUtil
                        )
                    }
                }
            }
        }
    }
}
