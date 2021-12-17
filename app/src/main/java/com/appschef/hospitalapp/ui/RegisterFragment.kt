package com.appschef.hospitalapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.appschef.hospitalapp.R
import com.appschef.hospitalapp.databinding.RegisterFragmentBinding
import com.appschef.hospitalapp.util.isNotValidEmail
import com.appschef.hospitalapp.util.isOnline

class RegisterFragment : Fragment(R.layout.register_fragment) {

    private var _binding: RegisterFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = RegisterFragmentBinding.bind(view)

        binding.btnRegister.setOnClickListener {
            checkRegisterInput()
        }

        binding.btnToSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun checkRegisterInput() {
        when {
            binding.edtPassword.text.toString() != binding.edtConfirmPassword.text.toString() -> {
                binding.containerPasswordRegister.error = "Password not match"
                binding.containerConfirmPassword.error = "Password not match"
            }
            binding.edtRegisterEmail.text.toString().isEmpty() -> {
                binding.containerEmailRegister.error = "Email cant be empty"
                binding.edtRegisterEmail.doOnTextChanged { text, start, before, count ->
                    if (text.toString().isNotEmpty()) {
                        binding.containerEmailRegister.error = null
                    }
                }
            }
            binding.edtPassword.text.toString().isEmpty() -> {
                binding.containerPasswordRegister.error = "password cant be empty"
                binding.edtPassword.doOnTextChanged { text, start, before, count ->
                    if (text.toString().isNotEmpty()) {
                        binding.containerPasswordRegister.error = null
                    }
                }
            }
            binding.edtConfirmPassword.text.toString().isEmpty() -> {
                binding.containerConfirmPassword.error = "verify your password"
                binding.edtConfirmPassword.doOnTextChanged { text, start, before, count ->
                    if (text.toString().isNotEmpty()) {
                        binding.containerConfirmPassword.error = null
                    }
                }
            }
            binding.edtRegisterEmail.text.isNotValidEmail() -> {
                binding.containerEmailRegister.error = "please use correct email format"
                binding.edtRegisterEmail.doOnTextChanged { text, start, before, count ->
                    if (text.toString().isNotEmpty()) {
                        binding.containerEmailRegister.error = null
                    }
                }
            }
            else -> handleRegister()
        }
    }

    private fun handleRegister() {
        if (isOnline(requireContext())) {
            viewModel.handleRegisterInput(
                binding.edtRegisterEmail.text.toString(),
                binding.edtPassword.text.toString()
            )
            viewModel.showProgress.observe(viewLifecycleOwner, { result ->
                when (result) {
                    true -> {
                        binding.progressBarRegister.isVisible = true
                        binding.btnRegister.isVisible = false
                    }
                    false -> {
                        binding.progressBarRegister.isVisible = false
                        binding.btnRegister.isVisible = true
                    }
                }
            })
            viewModel.showError.observe(viewLifecycleOwner, { result ->
                if (result == true) {
                    Toast.makeText(requireContext(), "register failed", Toast.LENGTH_SHORT).show()
                } else {
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
            })
        } else {
            Toast.makeText(
                requireContext(),
                "register failed \n please check your internet connection",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}