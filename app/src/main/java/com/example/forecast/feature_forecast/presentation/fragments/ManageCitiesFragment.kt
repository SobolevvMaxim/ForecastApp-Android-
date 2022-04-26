package com.example.forecast.feature_forecast.presentation.fragments

import androidx.fragment.app.viewModels
import com.example.forecast.R
import com.example.forecast.feature_forecast.presentation.base.BaseDialogFragment
import com.example.forecast.feature_forecast.presentation.viewmodels.CitiesViewModel

class ManageCitiesFragment :
    BaseDialogFragment<CitiesViewModel>(res = R.layout.fragment_manage_cities) {
    override val viewModel: CitiesViewModel by viewModels()


}