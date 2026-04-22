package kr.android.shoppinglistapp_room.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kr.android.shoppinglistapp_room.model.LocationData

@Composable
fun LocationSelector(
    location: LocationData,
    onLocationSelected : (LocationData) -> Unit
) {

    //live location
    val userLocation = remember {
        mutableStateOf(LatLng(
            location.latitude,
            location.longitude
        ))
    }

    //view of map when location selector opens
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            userLocation.value,
            17f
        )
    }

    val mapProperties by remember {
        (mutableStateOf(
            MapProperties(isMyLocationEnabled = true)   //for the current location blue dot
        ))
    }

    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(myLocationButtonEnabled = true    //for the target button for moving to current location
        ))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Card(
            modifier = Modifier
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )
        ) {

            GoogleMap(
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
                uiSettings = uiSettings,
                onMapClick = { userLocation.value = it }
            ){
                Marker(
                    state = MarkerState(position = userLocation.value),
                    draggable = true,
                    flat = true
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        var newLocation : LocationData

        Button(
            onClick = {
                newLocation = LocationData(
                    latitude = userLocation.value.latitude,
                    longitude = userLocation.value.longitude
                )
                onLocationSelected(newLocation)
            }
        ) {
            Text(
                text = "Update Location",
                fontWeight = FontWeight.Bold
            )
        }
    }
}