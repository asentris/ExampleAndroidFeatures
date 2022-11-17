package com.asentris.exampleandroidfeatures.broadcastReceiver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import com.asentris.exampleandroidfeatures.R
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
        BroadcastReceiverList.values().forEach { item ->
            val switch: SwitchCompat = View
                .inflate(root.context, R.layout.item_switch_compat, null) as SwitchCompat
            switch.text = item.text
            layoutLinear.addView(switch, layoutLinear.childCount)
            switch.setOnCheckedChangeListener { _, isChecked ->
                when (item) {
                    BroadcastReceiverList.AIRPLANE_MODE_CHANGED -> {
                        if (isChecked) brReAirplaneModeChanged.register(context)
                        else brReAirplaneModeChanged.unregister(context)
                    }
                    BroadcastReceiverList.BATTERY_CHANGE -> {
                        if (isChecked) brReBatteryChanged.register(context)
                        else brReBatteryChanged.unregister(context)
                    }
                }
            }
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