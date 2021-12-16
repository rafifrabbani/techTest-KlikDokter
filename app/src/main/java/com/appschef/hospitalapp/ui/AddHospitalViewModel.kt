package com.appschef.hospitalapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.location.LocationManagerCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appschef.hospitalapp.data.remote.model.HospitalListItem
import com.appschef.hospitalapp.usecase.AddHospitalUseCase
import com.appschef.hospitalapp.util.Result
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

@HiltViewModel
class AddHospitalViewModel @Inject constructor(
    private val addHospitalUseCase: AddHospitalUseCase
) : ViewModel() {
    val addItem = MutableLiveData<HospitalListItem>()
    val showError = MutableLiveData<Boolean>()
    val showProgress = MutableLiveData<Boolean>()

    fun addNewHospital(
        hospitalName: String,
        latitude: Double,
        longitude: Double,
        hospitalAddress: String
    ) {
        viewModelScope.launch {
            setLoadState()
            handleAddHospitalResult(
                addHospitalUseCase(
                    hospitalName,
                    latitude,
                    longitude,
                    hospitalAddress
                )
            )
        }
    }

    private fun setLoadState() {
        showProgress.postValue(true)
        showError.postValue(false)
    }

    private fun handleAddHospitalResult(result: Result<HospitalListItem>) {
        when (result) {
            is Result.Success -> setSuccessState(result.value)
            is Result.Failure -> setErrorState()
        }
    }

    private fun setErrorState() {
        showProgress.postValue(false)
        showError.postValue(true)
    }

    private fun setSuccessState(addHospitalResult: HospitalListItem) {
        addItem.postValue(addHospitalResult)
        showProgress.postValue(false)
        showError.postValue(false)
    }
}