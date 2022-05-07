package com.example.extensions

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationServices
import timber.log.Timber

object LocationUtils {

    fun Fragment.getLocationPermissions(onPermissionGained: () -> Unit, onPermissionDenied: () -> Unit) {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                when {
                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                        Timber.d("Location permission gained...")
                        onPermissionGained()
                    }
                    else -> {
                        Timber.d("Location permission denied...")
                        onPermissionDenied()
                    }
                }
            }
        }
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    fun Fragment.getLastLocation(successCallback: (location: Location) -> Unit, locationNullCallback: () -> Unit) {
        Timber.d("Getting last location...")
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        successCallback(it)
                    } ?: locationNullCallback()
                }
        }
    }
}