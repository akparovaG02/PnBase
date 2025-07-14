package com.example.pnbase

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.work.BackoffPolicy
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.pnbase.auth.presentation.AuthViewModel
import com.example.pnbase.navigation.MyAppNavigation
import com.example.pnbase.ui.theme.PnBaseTheme
import com.example.pnbase.userprofile.workmanager.DataSyncWorker
import dalvik.system.DexClassLoader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.reflect.Method
import java.time.Duration
import java.util.concurrent.TimeUnit
import kotlin.getValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadClassFromAndroidTestApp(this)

        // для фото
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
            0
        )
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)


        enableEdgeToEdge()

        val authViewModel : AuthViewModel by viewModels()
        setContent {
            PnBaseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyAppNavigation(
                        modifier = Modifier.padding(innerPadding),
                        authViewModel = authViewModel
                    )
                }
            }
        }
    }

    fun loadClassFromAndroidTestApp(context: Context) {
        val externalApk = File("/sdcard/Download/app-release.apk")
        val targetApk = File(context.codeCacheDir, "plugin.apk")

        if (!targetApk.exists()) {
            externalApk.copyTo(targetApk, overwrite = true)

            targetApk.setReadable(true, true)
            targetApk.setWritable(false)
            targetApk.setExecutable(false)
        }

        val optimizedDir = File(context.codeCacheDir, "dex_opt").apply { mkdirs() }

        val classLoader = DexClassLoader(
            targetApk.absolutePath,
            optimizedDir.absolutePath,
            null,
            context.classLoader
        )

        try {
            val clazz = classLoader.loadClass("com.example.testandroid.Test")
            val instance = clazz.getDeclaredConstructor().newInstance()
            val method = clazz.getMethod("test", Context::class.java)
            method.invoke(instance, context)
/*
            val clazz = classLoader.loadClass("com.example.testandroid.Test")
            val instance = clazz.getDeclaredConstructor().newInstance()
            val method = clazz.getMethod("test")
            method.invoke(instance)*/

            Log.d("TestAndroidGIGI", "Worked")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("TestAndroidGIGI", "Ошибка", e)

        }
    }


}

