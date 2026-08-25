package com.example.gallery_sync_app.screens.main_app

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.gallery_sync_app.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_screen)
        val bottom_nav=findViewById<BottomNavigationView>(R.id.mainBott)
        val navhost=supportFragmentManager.findFragmentById(R.id.frag_cont) as NavHostFragment
        val navController =navhost.navController
        navController.addOnDestinationChangedListener { controller, destination, bundle ->
            if(destination.id == R.id.loginScreen){
                bottom_nav.visibility= View.GONE
            }else{
                bottom_nav.visibility=View.VISIBLE
            }

        }
        bottom_nav.setOnItemSelectedListener { item->
            when(item.itemId){
                R.id.chats ->{
                    Toast.makeText(this,"on Chats",Toast.LENGTH_LONG).show()
                     true
                }
                else ->  false
            }

        }





    }
}