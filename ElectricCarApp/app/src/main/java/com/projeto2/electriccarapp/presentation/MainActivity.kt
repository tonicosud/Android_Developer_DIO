package com.projeto2.electriccarapp.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.projeto2.electriccarapp.R

class MainActivity : AppCompatActivity() {

    lateinit var btn_tela_calculo: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup_View()
        gotoAutonomia()
    }

    fun setup_View(){
        btn_tela_calculo = findViewById(R.id.btn_tela_calculo)

    }

    fun gotoAutonomia(){

        btn_tela_calculo.setOnClickListener{
            //calcular()
            startActivity(Intent(this,CalcularAutonomiaActivity::class.java))


        }

    }


}