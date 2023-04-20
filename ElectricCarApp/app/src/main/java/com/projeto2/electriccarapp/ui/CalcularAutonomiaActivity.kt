package com.projeto2.electriccarapp.ui

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.projeto2.electriccarapp.R

class CalcularAutonomiaActivity : AppCompatActivity() {

    lateinit var preco: EditText
    lateinit var kmpercorrido: EditText
    lateinit var btncalcular: Button
    lateinit var resultado: TextView
    lateinit var btnClose: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calcular_autonomia)
        setupView()
        calculaAutonomia_Listener()
        setupCacheResult()


    }

    private fun setupCacheResult() {
        val valorCalculado = getSharedPref()
        resultado.text = valorCalculado.toString()
    }

    fun setupView() {
        preco = findViewById(R.id.et_preco_kwh)
        kmpercorrido = findViewById(R.id.et_km_percorrido)
        btncalcular = findViewById(R.id.btn_calcular)
        resultado = findViewById(R.id.tv_resultado)
        btnClose = findViewById(R.id.iv_close)

    }

    fun calculaAutonomia_Listener() {
        btncalcular.setOnClickListener {
            calcular()
        }
        btnClose.setOnClickListener {
            finish()
        }

    }

    fun calcular() {
        val preco = preco.text.toString().toFloat()
        val km = kmpercorrido.text.toString().toFloat()

        val autonomia = preco / km

        resultado.text = autonomia.toString()
        saveSharedPref(autonomia)
    }

    fun saveSharedPref(resultado: Float) {
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putFloat(getString(R.string.saved_calc), resultado)
            apply()
        }
    }

    fun getSharedPref (): Float{
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getFloat(getString(R.string.saved_calc), 0.0f)

    }

}
