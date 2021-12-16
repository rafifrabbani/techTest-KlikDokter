package com.appschef.hospitalapp.ui

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appschef.hospitalapp.data.remote.model.LoginResponse
import com.appschef.hospitalapp.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.appschef.hospitalapp.util.Result
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    val loginResponse = MutableLiveData<LoginResponse>()
    val showProgress = MutableLiveData<Boolean>()
    val showError = MutableLiveData<Boolean>()
    val token = MutableLiveData<String>()

    private fun setLoadState() {
        showProgress.postValue(true)
        showError.postValue(false)
    }
    fun login(email: String, password: String) {
        setLoadState()
        viewModelScope.launch {
            handleLoginResult(loginUseCase(email, password))
        }
    }

    private fun handleLoginResult(result: Result<LoginResponse>) {
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

    private fun setSuccessState(result: LoginResponse) {
        token.postValue(result.token)
        loginResponse.postValue(result)
        showProgress.postValue(false)
    }
}