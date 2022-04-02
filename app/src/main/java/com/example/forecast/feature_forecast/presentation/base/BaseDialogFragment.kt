package com.example.forecast.feature_forecast.presentation.base

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.features.DialogFragmentSetup.alignToLeft
import com.example.forecast.R

abstract class BaseDialogFragment<T : BaseViewModel> : DialogFragment() {
    protected abstract val viewModel: T

    override fun onResume() {
        super.onResume()
        alignToLeft()
    }

    protected fun onError(error: Throwable?) {
        Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
        Log.d(getString(R.string.main_log), "Error: $error")
    }

    protected open fun onLoading() {}
}