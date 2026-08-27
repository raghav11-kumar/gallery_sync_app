package com.example.gallery_sync_app.screens.bottomNavImp

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.ActivityHomeScreenBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeScreen : AppCompatActivity() {
    /*well this screen start with the flow of showing the use of bottomnavigation  which help to shift blw the
     fragments
    * */
    private lateinit var binding: ActivityHomeScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bottom_id=findViewById<BottomNavigationView>(R.id.bottom_nav)
        showFragment(ChatScreen())
        bottom_id.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.settings -> {
                    Toast.makeText(this,"You On Setting Screen",Toast.LENGTH_LONG).show()
                    showFragment(SettingScreen())
                    true
                }
                R.id.home -> {
                    Toast.makeText(this,"You On Home Screen",Toast.LENGTH_LONG).show()

                    showFragment(HomeFragScreen())

                    true
                }
                R.id.chats -> {
                    Toast.makeText(this,"You On Chat Screen",Toast.LENGTH_LONG).show()

                    showFragment(ChatScreen())

                    true
                }
                else -> false
            }
        }

    }
    private fun showFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout,fragment)
                .commit()
        }
    }
}