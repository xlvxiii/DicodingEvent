package com.example.dicodingevent.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.dicodingevent.R
import com.example.dicodingevent.data.local.SettingPreferences
import com.example.dicodingevent.data.local.dataStore
import com.example.dicodingevent.utilities.ReminderWorkManager
import com.google.android.material.switchmaterial.SwitchMaterial
import java.util.UUID
import java.util.concurrent.TimeUnit

class SettingsFragment : Fragment() {

    private lateinit var workManager: WorkManager
    private lateinit var periodicWorkRequest: PeriodicWorkRequest
    private var workState: String = ""
    private var prefWorkState: String = ""
    private lateinit var workId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        workManager = WorkManager.getInstance(this@SettingsFragment.requireContext())

        val switchTheme = view.findViewById<SwitchMaterial>(R.id.switch_theme)
        val switchReminder = view.findViewById<SwitchMaterial>(R.id.switch_reminder)

        val pref = SettingPreferences.getInstance(requireActivity().application.dataStore)
        val settingsViewModel = ViewModelProvider(
            this,
            ViewModelFactory(pref)
        )[SettingsViewModel::class.java]

        settingsViewModel.getReminderStatus().observe(viewLifecycleOwner) { status: Set<String> ->
            prefWorkState = try {
                status.first()
            } catch (e: Exception) {
                "empty"
            }
        }

        settingsViewModel.getWorkId().observe(viewLifecycleOwner) { id: Set<String> ->
            workId = try {
                id.first()
            } catch (e: Exception) {
                "empty"
            }
        }

        settingsViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            switchTheme.isChecked = isDarkModeActive
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingsViewModel.saveThemeSetting(isChecked)
        }

        settingsViewModel.getReminderSettings().observe(viewLifecycleOwner) { isReminderActive: Boolean ->
            switchReminder.isChecked = isReminderActive
            setReminder(isReminderActive, settingsViewModel)
        }

        switchReminder.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingsViewModel.saveReminderSetting(isChecked)
        }

    }

    override fun onStart() {
        super.onStart()

//        val pref = SettingPreferences.getInstance(requireActivity().application.dataStore)
//        val settingsViewModel = ViewModelProvider(
//            this,
//            ViewModelFactory(pref)
//        )[SettingsViewModel::class.java]
//
//        prefWorkState = settingsViewModel.getReminderStatus().toString()
//        settingsViewModel.getReminderStatus().observe(viewLifecycleOwner) { status: Set<String> ->
//            prefWorkState = try {
//                status.first()
//            } catch (e: Exception) {
//                "empty"
//            }
//        }
    }

    private fun setReminder(isChecked: Boolean, settingsViewModel: SettingsViewModel) {

        if (isChecked) {

            if (prefWorkState != "ACTIVE") {
                val data = Data.Builder()
                    .putString(ReminderWorkManager.EXTRA_EVENT, "Event")
                    .build()

                val constraint = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

                periodicWorkRequest = PeriodicWorkRequest.Builder(ReminderWorkManager::class.java, 1, TimeUnit.DAYS)
                    .setInputData(data)
                    .setConstraints(constraint)
                    .addTag(TAG)
                    .build()

                workManager.enqueue(periodicWorkRequest)
                workManager.getWorkInfoByIdLiveData(periodicWorkRequest.id)
                    .observe(viewLifecycleOwner) { workInfo ->
                        val status = workInfo.state.name
                        workId = workInfo.id.toString()
                        workState = workInfo.state.name
                        Toast.makeText(context, "Reminder set to $status", Toast.LENGTH_SHORT).show()

                        settingsViewModel.saveReminderStatus("ACTIVE", workId)
                    }
            }
        } else {
            if (prefWorkState == "ACTIVE") {
                workManager.cancelWorkById(UUID.fromString(workId))
                settingsViewModel.saveReminderStatus("CANCELLED", workId)
            }
        }
    }

    companion object {
        const val TAG = "reminder task"
    }
}