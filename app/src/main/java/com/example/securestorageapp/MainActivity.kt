package com.example.securestorageapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.securestorageapp.databinding.ActivityMainBinding
import com.example.securestorageapp.fragments.DatabaseFragment

import com.example.securestorageapp.fragments.ExternalStorageFragment
import com.example.securestorageapp.fragments.InternalStorageFragment
import com.example.securestorageapp.fragments.ScopedStorageFragment
import com.example.securestorageapp.fragments.SecurityRisksFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 6

            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> InternalStorageFragment()
                    1 -> ExternalStorageFragment()
                    2 -> DatabaseFragment()
                    3 -> ScopedStorageFragment()
                    4 -> EncryptedFileFragment()
                    5 -> SecurityRisksFragment()
                    else -> InternalStorageFragment()
                }
            }
        }

        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Interne"
                1 -> "Externe"
                2 -> "BDD"
                3 -> "Media"
                4 -> "Chiffré"
                5 -> "Risques"
                else -> null
            }
        }.attach()
    }
}
