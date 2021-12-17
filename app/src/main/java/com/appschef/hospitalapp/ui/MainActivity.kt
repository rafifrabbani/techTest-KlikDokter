package com.appschef.hospitalapp.ui

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.ui.*
import com.appschef.hospitalapp.HospitalItemAdapter
import com.appschef.hospitalapp.R
import com.appschef.hospitalapp.data.remote.model.HospitalListItem
import com.appschef.hospitalapp.databinding.ActivityMainBinding
import com.appschef.hospitalapp.util.isOnline
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: HomeViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    lateinit var hospitalItemAdapter: HospitalItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        hospitalItemAdapter = HospitalItemAdapter(
            {
                directUserToGoogleMaps(it)
            },
            {
                promptUserItemDeletion(it)
            }
        )

        setSupportActionBar(binding.toolbar)
        val navController = findNavController(R.id.navHostFragment)
        binding.toolbar.setupWithNavController(navController)
        setupActionBarWithNavController(navController)
    }

    private fun directUserToGoogleMaps(item: HospitalListItem) {
        val uri =
            "http://maps.google.com/maps?q=loc:${item.latitude},${item.longitude}"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }

    private fun promptUserItemDeletion(item: HospitalListItem) {
        loginViewModel.token.observe(this, { token ->
            if (token.isNotEmpty()) {
                MaterialAlertDialogBuilder(this)
                    .setTitle("Delete Hospital ?")
                    .setMessage("this hospital will be deleted permanently")
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton("Accept") { dialog, _ ->
                        handleItemDeletion(item, dialog)
                    }
                    .show()
            } else {
                MaterialAlertDialogBuilder(this)
                    .setMessage("You must logged in to delete this item")
                    .setPositiveButton("Accept") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        })
    }

    private fun handleItemDeletion(item: HospitalListItem, dialog: DialogInterface) {
        if (isOnline(this)) {
            viewModel.deleteHospital(item.id.toInt())
            viewModel.deleteItem.observe(this, { result ->

                when (result) {
                    true -> {
                        dialog.dismiss()
                        Toast.makeText(
                            this,
                            "Successfully delete item",
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.getHospitalList()
                    }
                    false -> {
                        dialog.dismiss()
                        Toast.makeText(
                            this,
                            "delete item failure",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        } else {
            Toast.makeText(
                this,
                "delete failed \n please check your internet connection",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val menuItem = menu.findItem(R.id.search)
        val searchView = menuItem.actionView as androidx.appcompat.widget.SearchView
        searchView.queryHint = "type id to search"
        searchView.inputType = InputType.TYPE_CLASS_NUMBER
        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query != null && query.isNotEmpty()) {
                    viewModel.searchHospitalById(query.toInt())
                    viewModel.hospitalList.observe(this@MainActivity, {
                        hospitalItemAdapter.submitList(it)
                    })
                } else {
                    viewModel.getHospitalList()
                    viewModel.hospitalList.observe(this@MainActivity, {
                        hospitalItemAdapter.submitList(it)
                    })
                }
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.search -> {
                true
            }
            R.id.loginFragment -> {
                val navController = findNavController(R.id.navHostFragment)
                item.onNavDestinationSelected(navController)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}