package com.asentris.exampleandroidfeatures.service

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import com.asentris.exampleandroidfeatures.R
import com.asentris.exampleandroidfeatures.core.presentation.BaseDialogFragment
import com.asentris.exampleandroidfeatures.databinding.LinearLayoutBinding

class DialogService : BaseDialogFragment() {

    private var _binding: LinearLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = LinearLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtons()
    }

    private fun setupButtons(): Unit = binding.run {
        ServiceList.values().forEach { item ->
            val switch: SwitchCompat = View
                .inflate(root.context, R.layout.item_switch_compat, null) as SwitchCompat
            switch.text = item.text
            layoutLinear.addView(switch, layoutLinear.childCount)
            switch.setOnCheckedChangeListener { _, isChecked ->
                when (item) {
                    ServiceList.SIMPLE_SERVICE_START_STOP -> startStopSimpleService(isChecked)
                    ServiceList.SIMPLE_SERVICE_SEND_DATA -> if (isChecked) sendDataSimpleService()
                }
            }
        }
    }

    private fun startStopSimpleService(start: Boolean) {
        if (start) Intent(requireActivity(), SimpleService::class.java).also { intent ->
            requireActivity().startService(intent)
            Toast
                .makeText(requireContext(), getString(R.string.Service_started), Toast.LENGTH_SHORT)
                .show()
        } else Intent(requireActivity(), SimpleService::class.java).also { intent ->
            requireActivity().stopService(intent)
            Toast
                .makeText(requireContext(), getString(R.string.Service_stopped), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun sendDataSimpleService() {
        // restarts service if stopped, but will use already created service if not
        Intent(requireActivity(), SimpleService::class.java).also { intent ->
            val random: Double = Math.random()
            intent.putExtra("EXTRA_DATA", "Sending data: $random")
            requireActivity().startService(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}