package com.projeto2.electriccarapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    lateinit var preco: EditText
    lateinit var kmpercorrido: EditText
    lateinit var btncalcular: Button
    lateinit var resultado : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()
        calculaAutonomia()

    }

    fun setupView(){
        preco = findViewById(R.id.et_preco_kmh)
        kmpercorrido = findViewById(R.id.et_km_percorrido)
        btncalcular = findViewById(R.id.btn_calcular)
        resultado = findViewById(R.id.tv_resultado)

    }

    fun calculaAutonomia(){
        btncalcular.setOnClickListener{
            calcular()


        }

    }

    fun calcular(){
        val preco = preco.text.toString().toFloat()
        val km = kmpercorrido.text.toString().toFloat()

        val autonomia = preco / km

        resultado.text = autonomia.toString()
    }
}