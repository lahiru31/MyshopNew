package com.khadar3344.myshop.ui.maps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.CameraPositionState
import com.khadar3344.myshop.ui.theme.MyShopTheme

@Composable
fun MapScreen(viewModel: MapViewModel) {
    val markers = viewModel.markers

    MyShopTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            val cameraPositionState = rememberCameraPositionState {
                position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(
                    com.google.android.gms.maps.model.LatLng(-34.0, 151.0), 10f
                )
            }

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                markers.forEach { marker ->
                    Marker(
                        position = marker,
                        title = "Marker at ${marker.latitude}, ${marker.longitude}"
                    )
                }
            }
        }
    }
}
