package com.asentris.exampleandroidfeatures.contentProvider

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Email
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.asentris.exampleandroidfeatures.R
import com.asentris.exampleandroidfeatures.core.presentation.BaseDialogFragment
import com.asentris.exampleandroidfeatures.databinding.LinearLayoutBinding
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DialogContentProvider : BaseDialogFragment() {

    private var _binding: LinearLayoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                when (isGranted) {
                    true -> showRandomContact()
                    false -> Unit
                }
            }
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
        ContentProviderList.values().forEach { item ->
            val switch: SwitchCompat = View
                .inflate(root.context, R.layout.item_switch_compat, null) as SwitchCompat
            switch.text = item.text
            layoutLinear.addView(switch, layoutLinear.childCount)
            switch.setOnCheckedChangeListener { _, isChecked ->
                when (item) {
                    ContentProviderList.CONTACTS -> if (isChecked) readContacts()
                }
            }
        }
    }

    // manifest defines contacts read permission, but must request from user
    private fun readContacts() {
        when (checkIfReadContactsIsGranted()) {
            PackageManager.PERMISSION_GRANTED -> showRandomContact()
            PackageManager.PERMISSION_DENIED ->
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    private fun checkIfReadContactsIsGranted(): Int =
        ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)

    private fun getContacts(): List<SampleContact?>? {
        val contResv = requireContext().contentResolver
        val contractUri: Uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val contractId: String = ContactsContract.Contacts._ID
        val contractIdRef: String = ContactsContract.CommonDataKinds.Phone.CONTACT_ID
        val contractName: String = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        val contractNumber: String = ContactsContract.CommonDataKinds.Phone.NUMBER
        val contractHasNumber: String = ContactsContract.Contacts.HAS_PHONE_NUMBER

        var allContacts: List<SampleContact?>? = null
        val csr: Cursor? =
            contResv.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        if (csr?.moveToFirst() == true) {
            fun getStringOrEmpty(cursor: Cursor, string: String): String {
                var column: Int? = cursor.getColumnIndex(string)
                column = if (column == -1) null else column
                return if (column == null) "" else cursor.getString(column)
            }
            allContacts = emptyList()
            do {
                val id = getStringOrEmpty(csr, contractId)
                val name = getStringOrEmpty(csr, contractName)
                val emails: Cursor? = contResv.query(
                    Email.CONTENT_URI, null, "${Email.CONTACT_ID} = $id", null, null
                )
                var email = ""
                while (emails?.moveToNext() == true) {
                    email = getStringOrEmpty(emails, Email.DATA)
                    break
                }
                emails?.close()
                var phoneNumber = ""
                if (getStringOrEmpty(csr, contractHasNumber).isNotBlank()) {
                    val pCur: Cursor? =
                        contResv.query(contractUri, null, "$contractIdRef = ?", arrayOf(id), null)
                    phoneNumber = ""
                    while (pCur?.moveToNext() == true) {
                        phoneNumber = getStringOrEmpty(pCur, contractNumber)
                        break
                    }
                    pCur?.close()
                }
                val sampleContact = SampleContact(id, name, phoneNumber, email)
                allContacts = allContacts?.plus(sampleContact)
            } while (csr.moveToNext())
        }
        csr?.close()
        return allContacts
    }

    private fun showRandomContact() {
        lifecycleScope.launch {
            isCancelable = false
            val deferred: Deferred<SampleContact?> = async(Dispatchers.Default) {
                val contacts = getContacts()
                val randomNumber: Int = (Math.random() * (contacts?.size ?: 1) - 1).toInt()
                getContacts()?.get(randomNumber)
            }
            val contact: SampleContact? = deferred.await()
            val name = contact?.name
            val number = contact?.phoneNumber
            Toast.makeText(context, "$name / $number", Toast.LENGTH_SHORT).show()
            isCancelable = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        requestPermissionLauncher.unregister()
    }
}

// https://developer.android.com/guide/topics/providers/contacts-provider