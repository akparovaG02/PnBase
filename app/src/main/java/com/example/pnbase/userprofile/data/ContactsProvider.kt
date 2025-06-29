package com.example.pnbase.userprofile.data

import android.content.ContentResolver
import android.provider.ContactsContract

data class Contact(
    val name: String,
    val phoneNumber: String
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

            while (it.moveToNext()) {
                val name = it.getString(nameIndex) ?: ""
                val phone = it.getString(phoneIndex) ?: ""
                contactList.add(Contact(name, phone))
            }
        }
        return contactList
    }
}