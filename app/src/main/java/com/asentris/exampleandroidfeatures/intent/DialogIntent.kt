package com.asentris.exampleandroidfeatures.intent

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SwitchCompat
import com.asentris.exampleandroidfeatures.R
import com.asentris.exampleandroidfeatures.core.presentation.BaseDialogFragment
import com.asentris.exampleandroidfeatures.core.presentation.second.SecondActivity
import com.asentris.exampleandroidfeatures.databinding.LinearLayoutBinding

class DialogIntent : BaseDialogFragment() {

    private var _binding: LinearLayoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var launchImagePicker: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launchImagePicker =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result -> showImagePopup(result) }
    }

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
                    IntentList.START_ACTIVITY -> if (isChecked) startSecondActivity()
                    IntentList.SELECT_AND_SHOW_IMAGE -> if (isChecked) selectAndShowImage()
                }
            }
        }
    }

    private fun sendEmail() {
        val sendEmail = Intent(Intent.ACTION_SENDTO).also { intent ->
            intent.data = Uri.parse("mailto:") //use only email apps
            // recipients
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.blank_email)))
            intent.putExtra(Intent.EXTRA_SUBJECT, "This is the email subject")
            intent.putExtra(
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

    private fun startSecondActivity() {
        // manifest defines second activity
        Intent(requireActivity(), SecondActivity::class.java).also { intent ->
            startActivity(intent)
        }
    }

    private fun selectAndShowImage() {
        Intent(Intent.ACTION_GET_CONTENT).also { intent ->
            intent.type = "image/*" // * can be replaced with jpg, png, etc.
            launchImagePicker.launch(intent)
        }
    }

    private fun showImagePopup(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            val uri: Uri? = result.data?.data
            val popupView = layoutInflater.inflate(R.layout.image_view, null)
            val wc = LinearLayout.LayoutParams.WRAP_CONTENT
            val focusable = true // allows tap outside to dismiss
            val popupWindow = PopupWindow(popupView, wc, wc, focusable)
            val imageView: ImageView = popupView.findViewById(R.id.view_image)
            imageView.setImageURI(uri)
            popupWindow.showAtLocation(binding.root, Gravity.CENTER, 0, 0)
            popupView.setOnClickListener { popupWindow.dismiss() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        launchImagePicker.unregister()
    }
}