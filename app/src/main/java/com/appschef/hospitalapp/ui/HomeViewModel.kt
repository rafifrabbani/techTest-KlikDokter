package com.appschef.hospitalapp.ui

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appschef.hospitalapp.data.remote.model.HospitalListItem
import com.appschef.hospitalapp.usecase.DeleteHospitalUseCase
import com.appschef.hospitalapp.usecase.GetHospitalByIdUseCase
import com.appschef.hospitalapp.usecase.GetHospitalListUseCase
import com.appschef.hospitalapp.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHospitalListUseCase: GetHospitalListUseCase,
    private val deleteHospitalUseCase: DeleteHospitalUseCase,
    private val getHospitalByIdUseCase: GetHospitalByIdUseCase,
) : ViewModel(), LifecycleObserver {

    val hospitalList = MutableLiveData<List<HospitalListItem>>()
    val deleteItem = MutableLiveData<Boolean>()
    val showProgress = MutableLiveData<Boolean>()
    val showError = MutableLiveData<Boolean>()
    val showContent = MutableLiveData<Boolean>()

    init {
        setLoadState()
        getHospitalList()
    }

    private fun setLoadState() {
        showProgress.postValue(true)
        showError.postValue(false)
        showContent.postValue(false)
    }

    fun getHospitalList() {
        viewModelScope.launch {
            handleHospitalListResult(getHospitalListUseCase())
        }
    }

    private fun handleHospitalListResult(result: Result<List<HospitalListItem>>) {
        try {
            when (result) {
                is Result.Success -> setContentState(result.value)
                is Result.Failure -> setErrorState()
            }
        } catch (exception: IOException) {
            setErrorState()
        }
    }

    fun deleteHospital(id: Int) {
        viewModelScope.launch {
            handleDeleteHospitalItemResult(deleteHospitalUseCase(id))
        }
    }

    fun searchHospitalById(id: Int) {
        viewModelScope.launch {
            handleSearchHospitalResult(getHospitalByIdUseCase(id))
        }
    }

    private fun handleSearchHospitalResult(result: Result<List<HospitalListItem>>) {
        when (result) {
            is Result.Success -> {
                setContentState(result.value)
            }
            is Result.Failure -> setErrorState()
        }
    }

    private fun handleDeleteHospitalItemResult(result: Result<HospitalListItem>) {
        when (result) {
            is Result.Success -> deleteItem.postValue(true)
            is Result.Failure -> deleteItem.postValue(false)
        }
    }

    private fun setContentState(hospitalListResult: List<HospitalListItem>) {
        showContent.postValue(true)
        hospitalList.postValue(hospitalListResult)
        showProgress.postValue(false)
        showError.postValue(false)
    }

    private fun setErrorState() {
        showError.postValue(true)
        showProgress.postValue(false)
        showContent.postValue(false)
    }
}