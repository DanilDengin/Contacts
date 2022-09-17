package com.example.lessons


import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val txt: TextView=findViewById(R.id.contact1)
        txt.setOnClickListener() {
            this.switch()
        }
    }

    private fun switch() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .replace(R.id.fragment_container, FragmentDetails.newInstance(R.id.contact1.toString()))
            .addToBackStack(null)
            .commit()
    }
}

