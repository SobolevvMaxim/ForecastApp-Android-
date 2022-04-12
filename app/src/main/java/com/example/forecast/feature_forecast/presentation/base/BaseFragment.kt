package com.example.forecast.feature_forecast.presentation.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.forecast.R

abstract class BaseFragment<T : BaseViewModel>(
    res: Int,
) : Fragment(res) {
    protected abstract val viewModel: T

    protected fun onError(error: Throwable?) {
        Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
        Log.d(getString(R.string.main_log), "Error: $error")
    }

    protected open fun onLoading() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = ""
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}