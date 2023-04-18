package com.projeto2.electriccarapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.projeto2.electriccarapp.R
import com.projeto2.electriccarapp.data.CarFactory
import com.projeto2.electriccarapp.ui.adapter.CarAdapter

class MainActivity : AppCompatActivity() {

    lateinit var btn_tela_calculo: Button
    lateinit var listaCarros: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup_View()
        gotoAutonomia()
        setupLista()
    }

    fun setup_View(){
        btn_tela_calculo = findViewById(R.id.btn_tela_calculo)
        listaCarros = findViewById(R.id.rv_lista_carros)

    }

    fun setupLista(){
        val adapter = CarAdapter(CarFactory.list)
        listaCarros.adapter = adapter

    }

    fun gotoAutonomia(){

        btn_tela_calculo.setOnClickListener{
            //calcular()
            startActivity(Intent(this,CalcularAutonomiaActivity::class.java))


        }

    }


}