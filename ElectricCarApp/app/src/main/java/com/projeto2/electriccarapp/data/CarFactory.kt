package com.projeto2.electriccarapp.data

import com.projeto2.electriccarapp.domain.Carro

object CarFactory {

    val list = listOf(
        Carro(
            id = 1,
            preco = "139.990,00",
            bateria = "30,8 kWh",
            potencia = "61 cv",
            recarga = "5h",
            urlPhoto = "www.google.com.br"
        ),

        Carro(
            id = 2,
            preco = "189.990,00",
            bateria = "40,5 kWh",
            potencia = "75 cv",
            recarga = "6h",
            urlPhoto = "www.google.com.br"
    )
    )
}