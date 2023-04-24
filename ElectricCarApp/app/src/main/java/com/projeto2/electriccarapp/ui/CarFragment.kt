package com.projeto2.electriccarapp.ui

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Base64InputStream
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.loader.content.AsyncTaskLoader
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.projeto2.electriccarapp.R
import com.projeto2.electriccarapp.data.CarFactory
import com.projeto2.electriccarapp.data.CarsApi
import com.projeto2.electriccarapp.data.local.CarRepository
import com.projeto2.electriccarapp.data.local.CarrosContract
import com.projeto2.electriccarapp.data.local.CarrosContract.CarEntry.COLUMN_NAME_BATERIA
import com.projeto2.electriccarapp.data.local.CarrosContract.CarEntry.COLUMN_NAME_POTENCIA
import com.projeto2.electriccarapp.data.local.CarrosContract.CarEntry.COLUMN_NAME_PRECO
import com.projeto2.electriccarapp.data.local.CarrosContract.CarEntry.COLUMN_NAME_RECARGA
import com.projeto2.electriccarapp.data.local.CarrosContract.CarEntry.COLUMN_NAME_URL_PHOTO
import com.projeto2.electriccarapp.data.local.CarrosContract.CarEntry.TABLE_NAME
import com.projeto2.electriccarapp.data.local.CarsDbHelper
import com.projeto2.electriccarapp.domain.Carro
import com.projeto2.electriccarapp.ui.adapter.CarAdapter
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL


class CarFragment : Fragment () {

    lateinit var fabCalcular: FloatingActionButton
    lateinit var listaCarros: RecyclerView
    lateinit var progress: ProgressBar
    lateinit var noInternetImage: ImageView
    lateinit var noInternetText: TextView
    lateinit var carsApi: CarsApi

    var carrosArray : ArrayList<Carro> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.car_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRetrofit()
        setupView(view)
        setupListeners()

    }

    override fun onResume() {
        super.onResume()
        if(checkForInternet(context)){
            //callService() -> essa é outra forma de chamar
            getAllCars()
        }else{
            emptyState()
        }
    }

    fun setupRetrofit(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://igorbag.github.io/cars-api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        carsApi = retrofit.create(CarsApi::class.java)

    }

    fun getAllCars(){
        progress.isVisible = true
        carsApi.getAllCars().enqueue(object : Callback<List<Carro>>{
            override fun onResponse(call: Call<List<Carro>>, response: Response<List<Carro>>) {
                if (response.isSuccessful){
                    progress.isVisible = false
                    noInternetImage.isVisible = false
                    noInternetText.isVisible = false
                    response.body()?.let {
                        setupLista(it)
                    }
                }else{
                    Toast.makeText(context, R.string.response_error, Toast.LENGTH_LONG).show()
                    progress.isVisible = false
                }
            }

            override fun onFailure(call: Call<List<Carro>>, t: Throwable) {
                Toast.makeText(context, R.string.response_error, Toast.LENGTH_LONG).show()
                progress.isVisible = false
            }

        })
    }

    fun emptyState(){
        progress.isVisible = false
        listaCarros.isVisible = false
        noInternetImage.isVisible = true
        noInternetText.isVisible = true
    }

    fun setupView(view: View) {
        view?.apply {
            fabCalcular = findViewById(R.id.fab_calcular)
            listaCarros = findViewById(R.id.rv_lista_carros)
            progress = findViewById(R.id.pb_loader)
            noInternetText = findViewById(R.id.tv_no_wifi)
            noInternetImage = findViewById(R.id.iv_empty_state)
        }
    }


    fun setupLista(lista: List<Carro>){
        val carrosAdapter = CarAdapter(lista)
        listaCarros.apply {
            isVisible = true
            adapter = carrosAdapter

        }

        carrosAdapter.carItemListner = { carro ->
            val isSaved = CarRepository(requireContext()).saveIfNotExist(carro)
        }

    }

    fun setupListeners(){
        fabCalcular.setOnClickListener{
            startActivity(Intent(context,CalcularAutonomiaActivity::class.java))

        }

    }

    fun callService(){
        progress.isVisible = true
        val urlBase = "https://igorbag.github.io/cars-api/cars.json"
        MyTask().execute(urlBase)
    }

    fun checkForInternet(context: Context?): Boolean{
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            val network = connectivityManager.activeNetwork ?: return false

            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when{
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }

        }else{
            @Suppress("DEPRECATION")
            val networkinfo = connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkinfo.isConnected

        }
    }


    //Usar o Retrofit como abstraçao do Asynk Task
    inner class MyTask : AsyncTask<String, String, String>(){

        override fun onPreExecute() {
            super.onPreExecute()
            Log.d("MyTask","Iniciando...")
            progress.isVisible = true
        }
        override fun doInBackground(vararg url: String?): String {

            var urlConnection: HttpURLConnection? = null

            try {
                val urlBase = URL(url[0])

                urlConnection = urlBase.openConnection() as HttpURLConnection
                urlConnection.connectTimeout = 60000
                urlConnection.readTimeout = 60000
                urlConnection.setRequestProperty(
                    "Accept",
                    "application/json"
                )

                val responseCode = urlConnection.responseCode

                if(responseCode==HttpURLConnection.HTTP_OK){
                    var response = urlConnection.inputStream.bufferedReader().use{it.readText()}
                    publishProgress(response)

                }else {
                    Log.e("Erro", "Serviço indisponível...")
                }
            } catch (ex:Exception){
                Log.e("Erro", "Erro ao realizar processamento...")
            }finally {
                if(urlConnection != null){
                    urlConnection.disconnect()
                }

                return ""
            }
        }

        override fun onProgressUpdate(vararg values: String?) {
            try {
                val jsonArray = JSONTokener(values[0]).nextValue() as JSONArray

                for (i in 0 until jsonArray.length()){
                    val id = jsonArray.getJSONObject(i).getString("id")
                    Log.d("ID ->", id)

                    val preco = jsonArray.getJSONObject(i).getString("preco")
                    Log.d("Preço ->", preco)

                    val bateria = jsonArray.getJSONObject(i).getString("bateria")
                    Log.d("Bateria ->", bateria)

                    val potencia = jsonArray.getJSONObject(i).getString("potencia")
                    Log.d("Potência ->", potencia)

                    val recarga = jsonArray.getJSONObject(i).getString("recarga")
                    Log.d("Recarga ->", recarga)

                    val urlPhoto = jsonArray.getJSONObject(i).getString("urlPhoto")
                    Log.d("URL ->", urlPhoto)

                    val model = Carro(
                        id = id.toInt(),
                        preco = preco,
                        bateria = bateria,
                        potencia = potencia,
                        recarga = recarga,
                        urlPhoto = urlPhoto,
                        isFavorite = false

                    )
                    carrosArray.add(model)
                }
                progress.isVisible = false
                noInternetImage.isVisible = false
                noInternetText.isVisible = false

                //setupLista()

            }catch (ex: Exception){
                Log.e("Erro", ex.message.toString())
            }
        }

    }

}