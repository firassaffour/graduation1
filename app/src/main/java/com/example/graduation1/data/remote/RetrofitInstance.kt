package com.example.graduation1.data.remote

import android.content.Context
import okhttp3.Dns
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.Inet4Address
import java.net.InetAddress
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://graduation-project-backend-production-bc68.up.railway.app/"

object RetrofitInstance {

    private lateinit var context : Context

    fun initialize(context: Context){
        this.context = context.applicationContext
    }

    private val authInterceptor = AuthInterceptor{
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        prefs.getString("token", null)
    }

    val client = OkHttpClient.Builder()
        .dns(object : Dns {
            override fun lookup(hostname: String): List<InetAddress> {
                return Dns.SYSTEM.lookup(hostname)
                    .filterIsInstance<Inet4Address>()
            }
        })
        .addInterceptor(authInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    val api : ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}