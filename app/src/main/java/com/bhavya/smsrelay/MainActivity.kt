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

        ForwardService.start(this)
    }
}