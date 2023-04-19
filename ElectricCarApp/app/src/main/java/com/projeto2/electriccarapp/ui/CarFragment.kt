package com.projeto2.electriccarapp.ui

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Base64InputStream
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.loader.content.AsyncTaskLoader
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.projeto2.electriccarapp.R
import com.projeto2.electriccarapp.data.CarFactory
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
            fabCalcular = findViewById(R.id.fab_calcular)
            listaCarros = findViewById(R.id.rv_lista_carros)
        }
    }

    fun setupLista(){
        val adapter = CarAdapter(CarFactory.list)
        listaCarros.adapter = adapter

    }

    fun setupListeners(){
        fabCalcular.setOnClickListener{
            MyTask().execute("https://igorbag.github.io/cars-api/cars.json")
            //calcular()
            //startActivity(Intent(context,CalcularAutonomiaActivity::class.java))

        }

    }

    inner class MyTask : AsyncTask<String, String, String>(){

        override fun onPreExecute() {
            super.onPreExecute()
            Log.d("MyTask","Iniciando...")
        }
        override fun doInBackground(vararg url: String?): String {

            var urlConnection: HttpURLConnection? = null

            try {
                val urlBase = URL(url[0])

                urlConnection = urlBase.openConnection() as HttpURLConnection
                urlConnection.connectTimeout = 60000
                urlConnection.readTimeout = 60000

                var response = urlConnection.inputStream.bufferedReader().use{it.readText()}
                publishProgress(response)

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

                    val url = jsonArray.getJSONObject(i).getString("urlPhoto")
                    Log.d("URL ->", url)

                }

            }catch (ex: Exception){

            }
        }

        fun streamToString(inputStream: InputStream) : String{

            val bufferReader=BufferedReader(InputStreamReader(inputStream))
            var line: String
            var result = ""

            try {
                do{
                    line = bufferReader.readLine()
                    line?.let {
                        result += line
                    }
                } while (line != null)
            } catch (ex: Exception){
                Log.e("Erro", "Erro ao parcelar Stream")
            }
            return result
        }
    }

}