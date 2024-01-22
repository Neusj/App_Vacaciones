package com.example.vacaciones.Json

import kotlinx.coroutines.runBlocking

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class IndicadorData(
    val dolar: IndicadorItem
)

data class IndicadorItem(
    val valor: Double
)


fun getMiIndicadorData(): IndicadorData? = runBlocking {
    val servicio = RetrofitClient.getRetrofit().create(ApiService::class.java)

    try {
        val response = servicio.getMiIndicadorData()
        if (response.isSuccessful) {
            response.body()
        } else {
            println("Error en la respuesta: ${response.code()}")
            null
        }
    } catch (e: Exception) {
        println("Error: ${e.message}")
        null
    }
}

object RetrofitClient {
    private const val BASE_URL = "https://mindicador.cl/api/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getRetrofit(): Retrofit {
        return retrofit
    }
}


