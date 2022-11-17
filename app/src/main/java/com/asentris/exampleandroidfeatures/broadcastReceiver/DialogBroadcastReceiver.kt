package com.asentris.exampleandroidfeatures.broadcastReceiver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.asentris.exampleandroidfeatures.core.presentation.BaseDialogFragment
import com.asentris.exampleandroidfeatures.databinding.DialogBroadcastReceiverBinding

class DialogBroadcastReceiver : BaseDialogFragment() {

    private var _binding: DialogBroadcastReceiverBinding? = null
    private val binding get() = _binding!!
    private lateinit var brReAirplaneModeChanged: BrReAirplaneModeChanged
    private lateinit var brReBatteryChanged: BrReBatteryChanged

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = DialogBroadcastReceiverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        brReAirplaneModeChanged = BrReAirplaneModeChanged()
        brReBatteryChanged = BrReBatteryChanged()
        setupButtons()
    }

    private fun setupButtons(): Unit = binding.run {
        buttonAirplaneModeChange.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) brReAirplaneModeChanged.register(context)
            else brReAirplaneModeChanged.unregister(context)
        }
        buttonBatteryChange.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) brReBatteryChanged.register(context)
            else brReBatteryChanged.unregister(context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        brReAirplaneModeChanged.unregister(context) // prevent context memory leak
        brReBatteryChanged.unregister(context)
    }
}

// https://developer.android.com/reference/kotlin/android/content/BroadcastReceiver
// https://developer.android.com/about/versions/12/reference/broadcast-intents-31?hl=en