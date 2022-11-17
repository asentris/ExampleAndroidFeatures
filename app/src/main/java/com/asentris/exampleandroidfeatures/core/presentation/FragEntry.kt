package com.asentris.exampleandroidfeatures.core.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.asentris.exampleandroidfeatures.broadcastReceiver.DialogBroadcastReceiver
import com.asentris.exampleandroidfeatures.contentProvider.DialogContentProvider
import com.asentris.exampleandroidfeatures.core.extension.getArrayAdapter
import com.asentris.exampleandroidfeatures.core.extension.hideSoftKeyboard
import com.asentris.exampleandroidfeatures.databinding.FragEntryBinding
import com.asentris.exampleandroidfeatures.intent.DialogIntent

class FragEntry : Fragment() {

    private var _binding: FragEntryBinding? = null
    private val binding get() = _binding!!
    private val fragEntryVM = FragEntryVM()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragEntryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.varFeatureList.apply {
            setAdapter(fragEntryVM.features.getArrayAdapter(requireContext()))
            setOnClickListener { hideSoftKeyboard(binding.root) }
            setOnItemClickListener { parent, _, position, _ ->
                when (parent.getItemAtPosition(position) as FeatureList) {
                    FeatureList.BroadcastReceiver ->
                        DialogBroadcastReceiver().show(parentFragmentManager, "DBroadcastReceiver")
                    FeatureList.ContentProvider ->
                        DialogContentProvider().show(parentFragmentManager, "DContentProvider")
                    FeatureList.Intent ->
                        DialogIntent().show(parentFragmentManager, "DIntent")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}