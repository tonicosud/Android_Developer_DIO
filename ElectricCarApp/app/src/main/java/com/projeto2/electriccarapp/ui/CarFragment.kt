package com.projeto2.electriccarapp.ui

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
import android.widget.ProgressBar
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.loader.content.AsyncTaskLoader
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.projeto2.electriccarapp.R
import com.projeto2.electriccarapp.data.CarFactory
import com.projeto2.electriccarapp.domain.Carro
import com.projeto2.electriccarapp.ui.adapter.CarAdapter
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
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
        setupView(view)
        setupListeners()
        checkForInternet(context)
        callService()
    }

    fun setupView(view: View) {
        view?.apply {
            fabCalcular = findViewById(R.id.fab_calcular)
            listaCarros = findViewById(R.id.rv_lista_carros)
            progress = findViewById(R.id.pb_loader)
        }
    }

    fun setupLista(){
        val carrosAdapter = CarAdapter(carrosArray)

        listaCarros.apply {

            visibility = View.VISIBLE
            adapter = carrosAdapter

        }

    }

    fun setupListeners(){
        fabCalcular.setOnClickListener{
            startActivity(Intent(context,CalcularAutonomiaActivity::class.java))

        }

    }

    fun callService(){
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

    inner class MyTask : AsyncTask<String, String, String>(){

        override fun onPreExecute() {
            super.onPreExecute()
            Log.d("MyTask","Iniciando...")
            progress.visibility = View.VISIBLE
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
                        urlPhoto = urlPhoto

                    )
                    carrosArray.add(model)
                    progress.visibility = View.GONE


                }
                setupLista()
            }catch (ex: Exception){
                Log.e("Erro", ex.message.toString())
            }
        }

    }

}