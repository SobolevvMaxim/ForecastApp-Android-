package com.example.forecast.feature_forecast.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import timber.log.Timber

abstract class BaseFragment<T : BaseViewModel>(
    @LayoutRes res: Int,
) : Fragment(res) {
    protected abstract val viewModel: T

    protected fun onError(error: Throwable?) {
        Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
        Timber.e(error)
    }

    protected open fun onLoading() {}

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = ""
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}