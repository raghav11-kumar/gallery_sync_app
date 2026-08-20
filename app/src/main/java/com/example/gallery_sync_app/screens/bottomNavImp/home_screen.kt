package com.example.gallery_sync_app.screens.bottomNavImp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.gallery_sync_app.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class home_screen : AppCompatActivity() {
    /*well this screen start with the flow of showing the use of bottomnavigation  which help to shift blw the
     fragments
    * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_screen)
        val bottom_id=findViewById<BottomNavigationView>(R.id.bottom_nav)
        showFragment(chat_Screen())
        bottom_id.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.settings -> {
                    showFragment(Settings_screen())
                    true
                }
                R.id.home -> {
                    showFragment(homeFrag_screen())

                    true
                }
                R.id.chats -> {
                    showFragment(chat_Screen())

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