@file:OptIn(ExperimentalAnimationApi::class)

package kr.android.shoppinglistapp_room.navigation

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.dialog
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import kr.android.shoppinglistapp_room.ui.theme.ThemeMode
import kr.android.shoppinglistapp_room.util.LocationUtil
import kr.android.shoppinglistapp_room.view.AddEditScreen
import kr.android.shoppinglistapp_room.view.HomeScreen
import kr.android.shoppinglistapp_room.view.LocationSelector
import kr.android.shoppinglistapp_room.viewmodel.LocationViewModel

@Composable
fun Navigation (
    modifier : Modifier,
    themeMode : ThemeMode,
    isDark : Boolean,
    onThemeChange : (ThemeMode) -> Unit,
    locationViewModel: LocationViewModel,
    navController : NavHostController,
    context : Context,
    locationUtil: LocationUtil
) {
    NavHost(
        navController = navController,
        startDestination = Screens.HomeScreen.route
    ){

        //home screen
        composable(
            route = Screens.HomeScreen.route,
            enterTransition = { fadeIn( tween(300) ) },
            popEnterTransition = { fadeIn( tween(300) ) },
            exitTransition = { fadeOut( tween(300) ) },
            popExitTransition = { fadeOut( tween(300) ) }
        ){
            HomeScreen(
                themeMode = themeMode,
                isDark = isDark,
                onThemeChange = onThemeChange,
                navController = navController,
                locationViewModel = locationViewModel,
                locationUtil = locationUtil
            )
        }

        //add-edit screen
        composable(
            route = Screens.AddEditScreen.route,
            enterTransition = { fadeIn( tween(300) ) },
            popEnterTransition = { fadeIn( tween(300) ) },
            exitTransition = { fadeOut( tween(300) ) },
            popExitTransition = { fadeOut( tween(300) ) }
        ){
            AddEditScreen(
                id = 0L,
                isDark = isDark,
                themeMode = themeMode,
                onThemeChange = onThemeChange,
                locationUtil = locationUtil,
                locationViewModel = locationViewModel,
                navController = navController,
                onValueChange = {},
                onDecrease = {},
                onIncrease = {},
                onUnitSelect = {}
            )
        }

        //location selection dialog
        dialog( route = Screens.LocationSelector.route ){

            //starts with the last selected location, if not available, starts with the current live location
            val startLocation = locationViewModel.lastSavedLocation.value
                ?: locationViewModel.location.value

            if (startLocation != null){
                LocationSelector(
                    location = startLocation,
                    onLocationSelected = {
                        locationData ->

                        //saving the current location
                        locationViewModel.saveManualLocation(locationData)

                        //fetching the formatted address for the current location
                        locationViewModel.fetchAddress("${locationData.latitude}, ${locationData.longitude}")

                        //goes back to the home screen
                        navController.navigateUp()
                    }
                )
            } else {
                //map loading
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        trackColor = MaterialTheme.colorScheme.primary,
                        strokeCap = StrokeCap.Round,
                        strokeWidth = 8.dp
                    )
                }
            }
        }
    }
}