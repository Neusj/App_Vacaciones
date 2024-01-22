package com.example.vacaciones.Json

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("https://mindicador.cl/api")
    suspend fun getMiIndicadorData(): Response<IndicadorData>
}
