package com.example.android.recordme.recordandplay

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.android.recordme.R
import com.example.android.recordme.databinding.RecordAndPlayFragmentBinding

const val PERM_GRANTED = 123

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

        viewModel = ViewModelProviders.of(this).get(RecordAndPlayViewModel::class.java)

        binding.bRec.setOnClickListener { checkPermissions() }
        binding.bPlay.setOnClickListener { viewModel.onClickStop() }
        // TODO improve data binding


    }

    /**
     * Check permissions and if it is granted start recording and if it is not ask for permissions
     * Also checking app should show rationale dialog
     */
    private fun checkPermissions() {
        if ((ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO))
            + (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.onClickRecord(requireContext())
            // TODO add some behavior with permanently denied permissions
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.RECORD_AUDIO
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                showRationaleDialog(
                    getString(R.string.rationale_permissions_dialog_title),
                    getString(R.string.rationale_permissions_dialog_message)
                )
            } else {
                requestPermissions()
            }


        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), PERM_GRANTED
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERM_GRANTED -> {
                if ((grantResults.isNotEmpty() && grantResults[0] + grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED)
                ) {
                    viewModel.onClickRecord(requireContext())
                }
            }
        }
    }

    private fun showRationaleDialog(title: String, message: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Ok") { _, _ ->
                requestPermissions()
            }
        builder.create().show()
    }
}