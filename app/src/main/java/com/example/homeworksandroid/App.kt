package com.example.homeworksandroid

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.example.homeworksandroid.database.AppDatabase
import com.example.homeworksandroid.database.CitiesDao
import com.example.homeworksandroid.services.CitiesService
import com.example.homeworksandroid.services.TemperatureService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        appDatabase = Room.databaseBuilder(this, AppDatabase::class.java, APP_DATABASE).build()
    }

    companion object {
        private const val APP_DATABASE = "APP_DATABASE"

        private lateinit var appDatabase: AppDatabase

        private val retrofit = Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl("https://api.openweathermap.org")
            .client(getHttpClientWithInterceptor())
            .build()

        val citiesService: CitiesService = retrofit.create(CitiesService::class.java)
        val forecastService: TemperatureService =
            retrofit.create(TemperatureService::class.java)

        private fun getHttpClientWithInterceptor(): OkHttpClient {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            return OkHttpClient.Builder().addInterceptor(interceptor).build()
        }

        fun getCityDao(): CitiesDao = appDatabase.citiesDao()

        @RequiresApi(Build.VERSION_CODES.M)
        fun checkNetwork(context: Context?): Boolean {
            val manager: ConnectivityManager =
                context?.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities =
                manager.getNetworkCapabilities(manager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                        return true
                    }
                }
            }
            return false
        }
    }
}