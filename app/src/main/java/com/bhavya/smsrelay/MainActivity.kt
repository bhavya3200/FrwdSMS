package com.bhavya.smsrelay

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bhavya.smsrelay.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var b: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 3
            override fun createFragment(position: Int) = when (position) {
                0 -> HistoryFragment()
                1 -> FiltersFragment()
                else -> SettingsFragment()
            }
        }
        TabLayoutMediator(b.tabLayout, b.viewPager) { tab, pos ->
            tab.text = when (pos) {
                0 -> getString(R.string.tab_history)
                1 -> getString(R.string.tab_filters)
                else -> getString(R.string.tab_settings)
            }
        }.attach()

        val hasNotifPerm = ensurePostNotificationsPermission()
        if (!isNotifListenerEnabled()) {
            promptEnableNotifListener()
        }

        if (hasNotifPerm) {
            ForwardService.start(this)
        }
    }

    private fun isNotifListenerEnabled(): Boolean {
        val cn = android.content.ComponentName(this, SmsNotificationListener::class.java)
        val enabled = android.provider.Settings.Secure
            .getString(contentResolver, "enabled_notification_listeners")
            ?.split(":")
            ?.any { it.equals(cn.flattenToString(), ignoreCase = true) } == true
        return enabled
    }

    private fun promptEnableNotifListener() {
        val dlg = androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Enable Notification Access")
            .setMessage(
                "To forward SMS using notification reading (Play Store–friendly), please enable notification access for SMS Relay."
            )
            .setPositiveButton("Open Settings") { _, _ ->
                startActivity(android.content.Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
            }
            .setNegativeButton("Later", null)
            .create()
        dlg.show()
    }

    private fun ensurePostNotificationsPermission(): Boolean {
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            val perm = android.Manifest.permission.POST_NOTIFICATIONS
            if (checkSelfPermission(perm) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(perm), 1001)
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001 && grantResults.isNotEmpty() &&
            grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            ForwardService.start(this)
        }
    }
}

