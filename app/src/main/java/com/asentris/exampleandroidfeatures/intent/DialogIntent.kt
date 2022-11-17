package com.asentris.exampleandroidfeatures.intent

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import com.asentris.exampleandroidfeatures.R
import com.asentris.exampleandroidfeatures.core.presentation.BaseDialogFragment
import com.asentris.exampleandroidfeatures.databinding.LinearLayoutBinding

class DialogIntent : BaseDialogFragment() {

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
        IntentList.values().forEach { item ->
            val switch: SwitchCompat = View
                .inflate(root.context, R.layout.item_switch_compat, null) as SwitchCompat
            switch.text = item.text
            layoutLinear.addView(switch, layoutLinear.childCount)
            switch.setOnCheckedChangeListener { _, isChecked ->
                when (item) {
                    IntentList.SEND_EMAIL -> if (isChecked) sendEmail()
                }
            }
        }
    }

    private fun sendEmail() {
        val sendEmail = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") //use only email apps
            // recipients
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.blank_email)))
            putExtra(Intent.EXTRA_SUBJECT, "This is the email subject")
            putExtra(
                Intent.EXTRA_TEXT, """Hello,
                            |
                            |This is the email contents.
                            |
                            |
                            |V/R,
                            |Sender""".trimMargin()
            )
        }
        if (sendEmail.resolveActivity(requireActivity().packageManager) != null)
            startActivity(sendEmail)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}