package com.example.dicodingevent

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dicodingevent.data.local.SettingPreferences
import com.example.dicodingevent.data.local.dataStore
import com.example.dicodingevent.databinding.ActivityMainBinding
import com.example.dicodingevent.ui.settings.SettingsViewModel
import com.example.dicodingevent.ui.settings.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifications permission rejected", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        supportActionBar?.hide()
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_active_event, R.id.navigation_finished_event, R.id.navigation_settings
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    // set the dark mode theme based on the saved preferences
    private fun setTheme() {
        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingsViewModel = ViewModelProvider(
            this,
            ViewModelFactory(pref)
        )[SettingsViewModel::class.java]

        settingsViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}