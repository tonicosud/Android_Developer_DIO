package com.projeto2.electriccarapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TableLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.projeto2.electriccarapp.R
import com.projeto2.electriccarapp.data.CarFactory
import com.projeto2.electriccarapp.ui.adapter.CarAdapter
import com.projeto2.electriccarapp.ui.adapter.TabsAdapter

class MainActivity : AppCompatActivity() {

    lateinit var btn_tela_calculo: Button
    lateinit var listaCarros: RecyclerView
    lateinit var tabLayout : TabLayout
    lateinit var viewPager : ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup_View()
        gotoAutonomia()
        setupLista()
        setupTabs()
    }

    fun setup_View(){
        btn_tela_calculo = findViewById(R.id.btn_tela_calculo)
        listaCarros = findViewById(R.id.rv_lista_carros)
        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.vp_view_pager)

    }

    fun setupTabs(){
        val tabsAdapter = TabsAdapter(this)
        viewPager.adapter = tabsAdapter
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