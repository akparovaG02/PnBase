package com.example.pnbase.userprofile.domain

import com.example.pnbase.userprofile.data.Contact
import com.example.pnbase.userprofile.data.ContactsProvider

class GetContactsUseCase(
    private val contactsProvider: ContactsProvider
) {
    operator fun invoke(): List<Contact> {
        return contactsProvider.getContacts()
    }
}