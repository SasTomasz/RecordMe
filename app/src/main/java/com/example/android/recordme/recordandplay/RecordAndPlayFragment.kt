package com.example.android.recordme.recordandplay

import android.app.Application
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.recordme.R
import com.example.android.recordme.databinding.RecordAndPlayFragmentBinding

class RecordAndPlayFragment : Fragment() {

    companion object {
        fun newInstance() = RecordAndPlayFragment()
    }

    private var _binding: RecordAndPlayFragmentBinding? = null
    private val binding
        get() = _binding!!
    private lateinit var viewModelFactory: RecordAndPlayViewModelFactory
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
        viewModelFactory = RecordAndPlayViewModelFactory(requireNotNull(Application()))
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RecordAndPlayViewModel::class.java)
        binding.bRec.setOnClickListener { viewModel.onClickRecor() }
        binding.bPlay.setOnClickListener { viewModel.onClickStop() }
        // TODO: Use the ViewModel
    }

}