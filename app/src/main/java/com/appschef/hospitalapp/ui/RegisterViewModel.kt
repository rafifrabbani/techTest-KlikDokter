package com.appschef.hospitalapp.ui

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appschef.hospitalapp.data.remote.model.RegisterResponse
import com.appschef.hospitalapp.usecase.RegisterUseCase
import com.appschef.hospitalapp.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    val registerResponse = MutableLiveData<RegisterResponse>()
    val showProgress = MutableLiveData<Boolean>()
    val showError = MutableLiveData<Boolean>()

    private fun setLoadState() {
        showProgress.postValue(true)
        showError.postValue(false)
    }

    fun handleRegisterInput(
        email: String,
        password: String,
    ) {
        setLoadState()
        viewModelScope.launch {
            handleRegisterResult(registerUseCase(email, password))
        }
    }

    private fun handleRegisterResult(result: Result<RegisterResponse>) {
        when (result) {
            is Result.Success -> {
                setSuccessState(result.value)
            }
            is Result.Failure -> {
                showProgress.postValue(false)
                showError.postValue(true)
            }
        }
    }

    private fun setSuccessState(result: RegisterResponse) {
        registerResponse.postValue(result)
        showProgress.postValue(false)
    }
}