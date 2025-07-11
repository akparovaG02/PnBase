package com.example.pnbase.userprofile.domain

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.pnbase.userprofile.data.LocationClient
import com.example.pnbase.utils.LogFileWriter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

class DefaultLocationClient(
    private val context: Context,
    private val client: FusedLocationProviderClient
): LocationClient {

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Location {
        if (!context.hasLocationPermission()) {
            LogFileWriter.writeLog("APPLOG", "Нет разрешения на доступ к геолокации")
            throw SecurityException("Нет разрешения на доступ к геолокации")
        }

        return suspendCancellableCoroutine { cont ->
            client.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        LogFileWriter.writeLog("APPLOG", "Получена текущая геолокация: ${location.latitude}, ${location.longitude}")
                        cont.resume(location, null)
                    } else {
                        LogFileWriter.writeLog("APPLOG", "Ошибка: Текущее местоположение = null")
                        cont.resumeWithException(IllegalStateException("Location is null"))
                    }
                }
                .addOnFailureListener { e ->
                    LogFileWriter.writeLog("APPLOG", "Ошибка при получении геолокации: ${e.message}")
                    cont.resumeWithException(e)
                }
        }
    }


    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(interval: Long): Flow<Location> {
        return callbackFlow {
            if (!context.hasLocationPermission()) {
                LogFileWriter.writeLog("APPLOG", "Нет разрешения на получение обновлений геолокации")
                throw LocationClient.LocationException("Missing location permission")
            }

            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGpsEnabled && !isNetworkEnabled) {
                Log.e("APPLOG", "GPS и сеть отключены")
                throw LocationClient.LocationException("GPS is disabled")
            }

            val request = LocationRequest.create()
                .setInterval(interval)
                .setFastestInterval(interval)

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    result.locations.lastOrNull()?.let { location ->
                        LogFileWriter.writeLog("APPLOG", "Обновление геолокации: ${location.latitude}, ${location.longitude}")
                        launch { send(location) }
                    }
                }
            }

            LogFileWriter.writeLog("APPLOG", "Запущено получение обновлений геолокации (интервал = $interval мс)")

            client.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )

            awaitClose {
                LogFileWriter.writeLog("APPLOG", "Остановлено получение обновлений геолокации")
                client.removeLocationUpdates(locationCallback)
            }
        }
    }

}


fun Context.hasLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
}