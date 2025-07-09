package com.example.pnbase.userprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pnbase.userprofile.data.Contact
import com.example.pnbase.userprofile.data.SmsData
import com.example.pnbase.userprofile.domain.GetContactsUseCase
import com.example.pnbase.userprofile.domain.GetSmsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ContactsViewModel(
    private val getContactsUseCase: GetContactsUseCase
) : ViewModel() {

    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts

    fun loadContacts() {
        viewModelScope.launch {
            _contacts.value = getContactsUseCase()
        }
    }
}

class SMSViewModel(
    private val getSmsUseCase: GetSmsUseCase
) : ViewModel() {

    private val _sms = MutableStateFlow<List<SmsData>>(emptyList())
    val sms: StateFlow<List<SmsData>> = _sms

    fun loadSms() {
        viewModelScope.launch {
            _sms.value = getSmsUseCase()
        }
    }
}