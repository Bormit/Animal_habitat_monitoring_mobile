package ru.mirea.animal_habitat_monitoring_mobile.model.repository

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import okhttp3.Interceptor
import okhttp3.Response

class ConnectivityInterceptor(private val context: Context) : Interceptor {
    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun intercept(chain: Interceptor.Chain): Response {
        val networkInfo = connectivityManager.activeNetworkInfo
        val isConnected = networkInfo != null && networkInfo.isConnected
        if (!isConnected) {
            // Выводим Toast с сообщением об ошибке подключения
            val errorMessage = "Отсутствует подключение к интернету"
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
        return chain.proceed(chain.request())
    }
}