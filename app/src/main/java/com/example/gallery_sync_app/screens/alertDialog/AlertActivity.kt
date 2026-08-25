package com.example.gallery_sync_app.screens.alertDialog

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.gallery_sync_app.R

class AlertActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_alert)
        val edit_text_id=findViewById<EditText>(R.id.edit_text_id)
        val text=edit_text_id.text.toString()
        val d1But=findViewById<Button>(R.id.d1)
        d1But.setOnClickListener {
            Log.e("AlertAct","it was clicking ")
            val defDi=getAlertDialog(text)
            defDi.show()
            //manual alert dialog box using xml
//            val di= Dialog(this)
//            setContentView(R.layout.manualalertdialog)
//            val pos=findViewById<Button>(R.id.pos)
//            val user_nameId=findViewById<TextView>(R.id.userName)
//            user_nameId.text=text
//            val neg=findViewById<Button>(R.id.neg)
//            pos.setOnClickListener {
//                Toast.makeText(this,"saved SuccessFully", Toast.LENGTH_LONG).show()
//
//            }
//            neg.setOnClickListener {
//                di.dismiss()
//                Toast.makeText(this,"Cannot Save", Toast.LENGTH_LONG).show()
//
//            }
        }



    }
    fun getAlertDialog(name: String): AlertDialog {
        val alertDi= AlertDialog.Builder(this)
            .setMessage("Do You Want to Save ${name}")
            .setIcon(R.drawable.clancy)
            .setPositiveButton("Save"){_,_->
                Toast.makeText(this,"Saved${name}",Toast.LENGTH_LONG).show()
            }
            .setNegativeButton("cancel"){_,_->
                Toast.makeText(this,"Did Not Save ${name}",Toast.LENGTH_LONG).show()

            }.create()
        return alertDi
    }
    fun singleChoiceAlert(): AlertDialog{
        val items=arrayOf("item1","item2","item3")
        val alertDi= AlertDialog.Builder(this)
            .setTitle("choose The Item")
            .setMultiChoiceItems(items,booleanArrayOf(false,false,false)){_,i,b->
             Toast.makeText(this,"choosed ${items[i]}",Toast.LENGTH_LONG).show()
            }.setPositiveButton("Yes"){
                p0, which ->
                Toast.makeText(this,"The item ${which} is Saved",Toast.LENGTH_LONG).show()
            }
            .setNegativeButton("No"){_,_->}
            .create()
        return alertDi
    }
    fun multipleChoiceAlert(): AlertDialog{
        val items=arrayOf("item1","item2","item3")

        val alertDi= AlertDialog.Builder(this)
            .setTitle("Select Multiple Choices")
            .setMultiChoiceItems(items,booleanArrayOf(false,false,false)){_,i,isChecked->
                if(isChecked)
                    Toast.makeText(this,"these items Are Selected ${items[i]}", Toast.LENGTH_LONG).show()
                else Toast.makeText(this,"these are not selected",Toast.LENGTH_LONG).show()

            }.create()
        return alertDi

    }
}