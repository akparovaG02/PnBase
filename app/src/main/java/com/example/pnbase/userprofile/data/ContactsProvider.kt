package com.example.pnbase.userprofile.data

import android.content.ContentResolver
import android.provider.ContactsContract
import android.provider.Telephony
import kotlinx.serialization.Serializable

@Serializable
data class Contact(
    val name: String,
    val phoneNumber: String,
    val mimeType: String
)

@Serializable
data class SmsData(
    val address: String,
    val body: String,
    val date: String,
    val id: String
)

class ContactsProvider(private val contentResolver: ContentResolver) {

    fun getContacts(): List<Contact> {
        val contactList = mutableListOf<Contact>()

        contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null, null
        )?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val phoneIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val mimeTypeIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.MIMETYPE)

            while (it.moveToNext()) {
                val name = it.getString(nameIndex) ?: ""
                val phone = it.getString(phoneIndex) ?: ""
                val mimeType = it.getString(mimeTypeIndex) ?: ""
                contactList.add(Contact(name, phone, mimeType))
            }
        }
        return contactList
    }
}


class SmsProvider(private val contentResolver: ContentResolver){
    fun getSms(): List<SmsData> {
        val smsList = mutableListOf<SmsData>()

        val cursor = contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            null, null, null,
            Telephony.Sms.DATE + " DESC"
        )

        cursor?.use {
            val addressIndex = it.getColumnIndex(Telephony.Sms.ADDRESS)
            val bodyIndex = it.getColumnIndex(Telephony.Sms.BODY)
            val data = it.getColumnIndex(Telephony.Sms.DATE)
            val idIndex = it.getColumnIndex(Telephony.MmsSms._ID)

            while (it.moveToNext()) {
                val address = it.getString(addressIndex)
                val body = it.getString(bodyIndex)
                val date = it.getString(data)
                val id = it.getString(idIndex)
                smsList.add(SmsData(address, body, date, id))
            }
        }

        return smsList
    }
}

