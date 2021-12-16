package com.appschef.hospitalapp.ui

import android.Manifest
import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import com.appschef.hospitalapp.R
import com.appschef.hospitalapp.databinding.BottomSheetAddHospitalBinding
import com.google.android.gms.location.LocationRequest
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class BottomSheetAddHospital : BottomSheetDialogFragment() {

    private var _binding: BottomSheetAddHospitalBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddHospitalViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAddHospitalBinding.bind(
            inflater.inflate(R.layout.bottom_sheet_add_hospital, container, false)
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivDismiss.setOnClickListener {
            dismiss()
        }

        binding.btnSave.setOnClickListener {
            checkAddHospitalInput()
        }
    }

    private fun checkAddHospitalInput() {
        when {
            binding.edtHospitalName.text.toString().isEmpty() -> {
                binding.containerHospitalName.error = "Hospital name cant be empty"
                binding.edtHospitalName.doOnTextChanged { text, start, before, count ->
                    if (text.toString().isNotEmpty()) {
                        binding.containerHospitalName.error = null
                    }
                }
            }
            binding.edtAddress.text.toString().isEmpty() -> {
                binding.containerAddress.error = "Adress cant be empty"
                binding.edtHospitalName.doOnTextChanged { text, start, before, count ->
                    if (text.toString().isNotEmpty()) {
                        binding.containerAddress.error = null
                    }
                }
            }
            binding.edtLatitude.text.toString().isEmpty() -> {
                binding.containerLatitude.error = "latitude cant be empty"
                binding.edtLatitude.doOnTextChanged { text, start, before, count ->
                    if (text.toString().isNotEmpty()) {
                        binding.containerLatitude.error = null
                    }
                }
            }
            binding.edtLatitude.text.toString().contains(",") -> {
                binding.containerLatitude.error = "cant use ',' here"
                binding.edtLatitude.doOnTextChanged { text, start, before, count ->
                    if (text.toString().isNotEmpty()) {
                        binding.containerLatitude.error = null
                    }
                }
            }
            binding.edtLongitude.text.toString().isEmpty() -> {
                binding.containerLongitude.error = "longitude cant be empty"
                binding.edtLongitude.doOnTextChanged { text, start, before, count ->
                    if (text.toString().isNotEmpty()) {
                        binding.containerLongitude.error = null
                    }
                }
            }
            binding.edtLongitude.text.toString().contains(",") -> {
                binding.containerLongitude.error = "cant use ',' here"
                binding.edtLongitude.doOnTextChanged { text, start, before, count ->
                    if (text.toString().isNotEmpty()) {
                        binding.containerLongitude.error = null
                    }
                }
            }
            else -> handleAddHospital()
        }
    }

    private fun handleAddHospital() {
        viewModel.addNewHospital(
            binding.edtHospitalName.text.toString(),
            binding.edtLatitude.text.toString().toDouble(),
            binding.edtLongitude.text.toString().toDouble(),
            binding.edtAddress.text.toString()
        )
        viewModel.showProgress.observe(viewLifecycleOwner, { result ->
            when(result) {
                true -> {
                    homeViewModel.getHospitalList()
                    Toast.makeText(requireContext(), "success add hospital", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
                false -> {
                    Toast.makeText(requireContext(), "add hospital failed", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}