package com.kitlabs.aiapp.base

import android.Manifest.permission.*
import android.os.Build
import androidx.annotation.RequiresApi

sealed class MyPermission(vararg val permissions: String) {

    // Individual permissions
    data object Camera : MyPermission(CAMERA)
    data object FileStorage : MyPermission(READ_EXTERNAL_STORAGE)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    data object Storage13 : MyPermission(READ_MEDIA_IMAGES)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    data object Notification13 : MyPermission(POST_NOTIFICATIONS)

    // Grouped permissions
    data object Location : MyPermission(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
    data object Storage : MyPermission(WRITE_EXTERNAL_STORAGE)

    companion object {
        fun from(permission: String) = when (permission) {
            CAMERA -> Camera
            READ_EXTERNAL_STORAGE -> FileStorage
            READ_MEDIA_IMAGES -> Storage13
            POST_NOTIFICATIONS -> Notification13
            ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION -> Location
            WRITE_EXTERNAL_STORAGE -> Storage
            else -> throw IllegalArgumentException("Unknown permission: $permission")
        }
    }
}