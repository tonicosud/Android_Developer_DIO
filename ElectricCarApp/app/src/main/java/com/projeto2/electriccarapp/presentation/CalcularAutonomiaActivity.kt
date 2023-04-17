package com.projeto2.electriccarapp.presentation

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.projeto2.electriccarapp.R

class CalcularAutonomiaActivity : AppCompatActivity() {

    lateinit var preco: EditText
    lateinit var kmpercorrido: EditText
    lateinit var btncalcular: Button
    lateinit var resultado : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calcular_autonomia)
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