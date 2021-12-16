package com.appschef.hospitalapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.appschef.hospitalapp.HospitalItemAdapter
import com.appschef.hospitalapp.R
import com.appschef.hospitalapp.data.remote.model.HospitalListItem
import com.appschef.hospitalapp.databinding.HomeFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.home_fragment) {

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModels()
    private val loginViewModel: LoginViewModel by activityViewModels()
    lateinit var hospitalItemAdapter: HospitalItemAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = HomeFragmentBinding.bind(view)

        hospitalItemAdapter = HospitalItemAdapter(
            { item ->
                directUserToGoogleMaps(item)
            },
            { hospitalItem ->
                promptUserItemDeletion(hospitalItem)
            }
        )

        setupRecyclerView()
        loggedInUser()
        loadData()
        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_bottomSheetAddHospital)
        }
    }

    private fun loggedInUser() {
        loginViewModel.token.observe(viewLifecycleOwner, { token ->
            if (token.isNotEmpty()) {
                binding.btnAdd.isVisible = true
            }
        })
    }

    private fun setupRecyclerView() {
        binding.rvHospital.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = hospitalItemAdapter
        }
    }

    private fun loadData() {
        viewModel.hospitalList.observe(viewLifecycleOwner, { result ->
            hospitalItemAdapter.submitList(result)
        })

        viewModel.showContent.observe(viewLifecycleOwner, { result ->
            when (result) {
                true -> {
                    binding.rvHospital.isVisible = true
                    binding.progressBar.isVisible = false
                }
                false -> {
                    binding.rvHospital.isVisible = false
                    binding.progressBar.isVisible = true
                }
            }
        })
    }

    private fun directUserToGoogleMaps(item: HospitalListItem) {
        val uri =
            "http://maps.google.com/maps?q=loc:${item.latitude},${item.longitude}"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }

    private fun promptUserItemDeletion(item: HospitalListItem) {
        loginViewModel.token.observe(viewLifecycleOwner, { token ->
            if (token.isNotEmpty()) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Delete Hospital ?")
                    .setMessage("this hospital will be deleted permanently")
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton("Accept") { dialog, _ ->
                        viewModel.deleteHospital(item.id.toInt())
                        viewModel.deleteItem.observe(viewLifecycleOwner, { result ->

                            when (result) {
                                true -> {
                                    dialog.dismiss()
                                    Toast.makeText(
                                        requireContext(),
                                        "Successfully delete item",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    viewModel.getHospitalList()
                                }
                                false -> {
                                    dialog.dismiss()
                                    Toast.makeText(
                                        requireContext(),
                                        "delete item failure",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        })
                    }
                    .show()
            } else {
                MaterialAlertDialogBuilder(requireContext())
                    .setMessage("You must logged in to delete this item")
                    .setPositiveButton("Accept") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}