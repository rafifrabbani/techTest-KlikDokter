package com.appschef.hospitalapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.appschef.hospitalapp.R
import com.appschef.hospitalapp.databinding.LoginFragmentBinding
import com.appschef.hospitalapp.util.isNotValidEmail

class LoginFragment : Fragment(R.layout.login_fragment) {

    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = LoginFragmentBinding.bind(view)

        binding.btnLogin.setOnClickListener {
            checkLoginInput()
        }

        binding.btnToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun checkLoginInput() {
        when {
            binding.edtEmailLogin.text.toString().isEmpty() -> {
                binding.containerLogin.error = "Email cant be empty"
                binding.edtEmailLogin.doOnTextChanged { text, start, before, count ->
                    if (text.toString().isNotEmpty()) {
                        binding.containerLogin.error = null
                    }
                }
            }
            binding.edtPassword.text.toString().isEmpty() -> {
                binding.containerPassword.error = "Password cant be empty"
                binding.edtPassword.doOnTextChanged { text, start, before, count ->
                    if (text.toString().isNotEmpty()) {
                        binding.containerPassword.error = null
                    }
                }
            }
            binding.edtEmailLogin.text.isNotValidEmail() -> {
                binding.containerLogin.error = "please use correct email format"
                binding.edtEmailLogin.doOnTextChanged { text, start, before, count ->
                    if (text.toString().isNotEmpty()) {
                        binding.containerLogin.error = null
                    }
                }
            }
            else -> handleLogin()
        }
    }

    private fun handleLogin() {
        viewModel.login(
            binding.edtEmailLogin.text.toString(),
            binding.edtPassword.text.toString()
        )
        viewModel.showProgress.observe(viewLifecycleOwner, { result ->
            when (result) {
                true -> {
                    binding.progressBarLogin.isVisible = true
                    binding.btnLogin.isVisible = false
                }
                false -> {
                    binding.progressBarLogin.isVisible = false
                    binding.btnLogin.isVisible = true
                }
            }
        })
        viewModel.showError.observe(viewLifecycleOwner, { result ->
            if (result == true) {
                Toast.makeText(requireContext(), "login failed", Toast.LENGTH_SHORT).show()
            } else {
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
        })

        viewModel.token.observe(viewLifecycleOwner, {
            Log.i("loginToken", "currentToken: $it")
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}