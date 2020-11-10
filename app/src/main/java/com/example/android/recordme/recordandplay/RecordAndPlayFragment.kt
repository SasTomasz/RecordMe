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
import androidx.lifecycle.ViewModelProvider
import com.example.android.recordme.R
import com.example.android.recordme.adapters.MyAdapter
import com.example.android.recordme.adapters.RecordClickListener
import com.example.android.recordme.databinding.RecordAndPlayFragmentBinding

const val PERMISSIONS_REQUEST_CODE = 123

class RecordAndPlayFragment : Fragment() {

    private var _binding: RecordAndPlayFragmentBinding? = null
    private val binding
        get() = _binding!!
    private lateinit var viewModel: RecordAndPlayViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RecordAndPlayFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(RecordAndPlayViewModel::class.java)
        binding.viewModel = viewModel

        // checking if there is need of checking permissions
        viewModel.checkPermissions.observe(viewLifecycleOwner, {
            if (it) {
                checkPermissions()
                viewModel.permissionsChecked()
            }
        })

        // checking if there is need of showing error message
        viewModel.errorMessage.observe(viewLifecycleOwner, { message ->
            message?.let {
                Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
                viewModel.errorMessageShowed()
            }
        })

        // recyclerview
        val adapter = MyAdapter(RecordClickListener { record ->
            Toast.makeText(context, "${record.recordId}", Toast.LENGTH_SHORT).show()
            viewModel.play(record)
        })
        binding.recordingsList.adapter = adapter
        viewModel.recordings.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })

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
        } else if (!shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO) ||
            !shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ) {
            // User permanently denied permissions
            Toast.makeText(
                requireActivity(), "Permissions Denied! Change device settings!",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            // Permissions are not granted by User
            Toast.makeText(requireActivity(), "Permissions Denied", Toast.LENGTH_SHORT).show()
        }
    }


}

// TODO 13 My response to this article
//  (https://medium.com/androiddevelopers/viewmodels-and-livedata-patterns-antipatterns-21efaef74a54)
//  - Try to move from viewModels android* imports
//  - Try to move from this fragment permission check logic from another place
//  - Make sure the code in views is ONLY responsible for SHOWING DATA in layout and INFORM
//      VIEWMODELS ABOUT USER INTERACTIONS
//  - Expose information about the state of your data using a wrapper or another LiveData
//      (in viewmodel)

// TODO 14 Try add some motionLayout to your app