package com.example.android.recordme.recordandplay

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.recordme.R
import com.example.android.recordme.adapters.MyAdapter
import com.example.android.recordme.databinding.RecordAndPlayFragmentBinding

const val PERMISSIONS_REQUEST_CODE = 123

class RecordAndPlayFragment : Fragment() {

    private var _binding: RecordAndPlayFragmentBinding? = null
    private val binding
        get() = _binding!!
    private lateinit var viewModel: RecordAndPlayViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RecordAndPlayFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(RecordAndPlayViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.recordings.observe(viewLifecycleOwner, {
            it?.let {
            }
        })

        // checking if there is need of checking permissions
        viewModel.checkPermissions.observe(viewLifecycleOwner, {
            if (it) {
                checkPermissions()
                viewModel.permissionsChecked()
            }
        })

        // recyclerview
        viewManager = LinearLayoutManager(requireContext())
        viewAdapter = MyAdapter(viewModel.listOfRecordings)

        recyclerView = binding.recordingsList.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

    }

    /**
     * Show Alert Dialog with explanation why User should allow permissions.
     * After click "ok" button request permissions will show
     */
    private fun showRationaleDialog(title: String, message: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Ok") { _, _ ->
                requestPermissions()
            }
        builder.create().show()
    }

    /**
     * Check permissions and if it is granted start recording and if it is not ask for permissions
     * Also checking app should show rationale dialog
     */
    private fun checkPermissions() {
        when {
            // check if user has permissions
            (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO))
                    + (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )) == PackageManager.PERMISSION_GRANTED -> {
                // User has permissions
                viewModel.permissionsGranted()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO) ||
                    shouldShowRequestPermissionRationale(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) -> {
                // User has denied permissions once but he didn't click "Never Show Again" checkbox
                showRationaleDialog(
                    getString(R.string.rationale_permissions_dialog_title),
                    getString(R.string.rationale_permissions_dialog_message)
                )

                // TODO 03 Add some message when user clicked "Never Show Again Button":
                //  - Add some Toast with info how to change this denied permissions
                //  - Test this case on device
            }

            else -> {
                // User has never seen a permission Dialog
                requestPermissions()
            }
        }
    }

    private fun requestPermissions() {
        requestPermissions(
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), PERMISSIONS_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] + grantResults[1] ==
            PackageManager.PERMISSION_GRANTED
        ) {
            // Permissions is now granted
            Toast.makeText(requireActivity(), "Permissions Granted", Toast.LENGTH_SHORT).show()
        } else {
            // Permissions are not granted by User
            Toast.makeText(requireActivity(), "Permissions Denied", Toast.LENGTH_SHORT).show()
        }
    }
}