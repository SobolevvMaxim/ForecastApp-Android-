package com.example.homeworksandroid

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.navigation.Navigator
import androidx.room.Room
import com.example.homeworksandroid.database.AppDatabase
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@HiltAndroidApp
class App : Application()

private const val APP_DATABASE = "APP_DATABASE"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            APP_DATABASE
        ).build()

    @Singleton
    @Provides
    fun providesNewsRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl("https://api.openweathermap.org")
            .client(okHttpClient)
            .build()

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @Provides
    fun provideGetCityTag(@ApplicationContext context: Context): String {
        return context.getString(R.string.get_city_extra)
    }

    @Singleton
    @Provides
    fun provideDateFormat() = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    @MainCoroutineDispatcher
    @Provides
    fun mainCoroutineDispatcherProvider(): CoroutineDispatcher = Dispatchers.Main

    @IOCoroutineDispatcher
    @Provides
    fun ioCoroutineDispatcherProvider(): CoroutineDispatcher = Dispatchers.IO
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainCoroutineDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IOCoroutineDispatcher

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun checkNetwork(context: Context?): Boolean {
    val manager: ConnectivityManager =
        context?.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val builder = NetworkRequest.Builder()
    builder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
    var result = false

    val networkRequest = builder.build()
    manager.registerNetworkCallback(networkRequest,
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Log.i("Test", "Network Available")
                result = true
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                Log.i("Test", "Connection lost")
            }
        })
    return result
}