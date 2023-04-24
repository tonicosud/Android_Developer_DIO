package com.projeto2.electriccarapp.ui

import android.media.CamcorderProfile.getAll
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.projeto2.electriccarapp.R
import com.projeto2.electriccarapp.data.local.CarRepository
import com.projeto2.electriccarapp.domain.Carro
import com.projeto2.electriccarapp.ui.adapter.CarAdapter

class FavFragment: Fragment() {

    lateinit var listaCarrosFavoritos : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fav_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        val carlist = getCarsOnLocalDb()
        setupLista()
    }

    private fun getCarsOnLocalDb(): List<Carro> {
        val repository = CarRepository(requireContext())
        val carlist = repository.getAll()
        return carlist
    }

    fun setupView(view: View) {
        view?.apply {

            listaCarrosFavoritos = findViewById(R.id.rv_lista_carros_favoritos)

        }
    }

    fun setupLista(){
        val cars = getCarsOnLocalDb()
        val carrosAdapter = CarAdapter(cars, isFavoriteScreen = true)
        listaCarrosFavoritos.apply {
            isVisible = true
            adapter = carrosAdapter

        }

        carrosAdapter.carItemListner = { carro ->
            //val isSaved = CarRepository(requireContext()).saveIfNotExist(carro)
        }

    }
}