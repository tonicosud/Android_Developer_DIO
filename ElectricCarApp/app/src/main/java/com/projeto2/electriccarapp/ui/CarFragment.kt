package com.projeto2.electriccarapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.projeto2.electriccarapp.R
import com.projeto2.electriccarapp.data.CarFactory
import com.projeto2.electriccarapp.ui.adapter.CarAdapter


class CarFragment : Fragment () {

    lateinit var btn_tela_calculo: Button
    lateinit var listaCarros: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.car_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setupLista()
        setupListeners()
    }

    fun setupView(view: View) {
        view?.apply {
            btn_tela_calculo = findViewById(R.id.btn_tela_calculo)
            listaCarros = findViewById(R.id.rv_lista_carros)
        }
    }

    fun setupLista(){
        val adapter = CarAdapter(CarFactory.list)
        listaCarros.adapter = adapter

    }

    fun setupListeners(){
        btn_tela_calculo.setOnClickListener{
            //calcular()
            //startActivity(Intent(this,CalcularAutonomiaActivity::class.java))

        }

    }

}