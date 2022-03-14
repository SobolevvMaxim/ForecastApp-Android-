package com.example.forecast.base

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.forecast.R

abstract class BaseFragment<T : BaseViewModel> : Fragment() {

    protected abstract val viewModel: T

    protected fun onError(error: Throwable?) {
        Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
        Log.d(getString(R.string.main_log), "Error: $error")
    }

    protected abstract fun onLoading()
}