package com.example.android.recordme.recordandplay

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.recordme.R

class RecordAndPlayFragment : Fragment() {

    companion object {
        fun newInstance() = RecordAndPlayFragment()
    }

    private lateinit var viewModel: RecordAndPlayViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.record_and_play_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RecordAndPlayViewModel::class.java)
        // TODO: Use the ViewModel
    }

}