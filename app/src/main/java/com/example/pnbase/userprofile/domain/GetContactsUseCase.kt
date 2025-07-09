package com.example.pnbase.userprofile.domain

import com.example.pnbase.userprofile.data.Contact
import com.example.pnbase.userprofile.data.ContactsProvider
import com.example.pnbase.userprofile.data.SmsData
import com.example.pnbase.userprofile.data.SmsProvider

class GetContactsUseCase(
    private val contactsProvider: ContactsProvider
) {
    operator fun invoke(): List<Contact> {
        return contactsProvider.getContacts()
    }
}

class GetSmsUseCase(
    private val smsProvider: SmsProvider
) {
    operator fun invoke(): List<SmsData> {
        return smsProvider.getSms()
    }
}