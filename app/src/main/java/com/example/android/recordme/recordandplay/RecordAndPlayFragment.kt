package com.example.android.recordme.recordandplay

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
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
    }

    /**
     * Check permissions and if it is granted start recording and if it is not ask for permissions
     *
     * There is (in my opinion) possibility do this more convenient with new Activity 1.2.0-alpha02
     * see new feature for permissions https://www.youtube.com/watch?v=R3caBPj-6Sg (14:57)
     * TODO try ask permissions more easily with new feature from jetpack
     * TODO try move checking permissions to viewmodel
     */
    private fun checkPermissions() {
        if ((ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.RECORD_AUDIO))
            + (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.onClickRecord(requireContext())
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.RECORD_AUDIO
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                // TODO show some info about why user should
                //  allow to recording and accessing storage
                Toast.makeText(
                    activity,
                    "Record & Write permissions is require if you want to record audio",
                    Toast.LENGTH_SHORT
                ).show()
            }

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), PERM_GRANTED
            )
        }
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
}