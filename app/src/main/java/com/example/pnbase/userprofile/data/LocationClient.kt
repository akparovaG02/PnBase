package com.example.pnbase.userprofile.data

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationClient {
    suspend fun getCurrentLocation(): Location

    fun getLocationUpdates(interval: Long): Flow<Location>

    class LocationException(message: String): Exception()
}