package com.example.pnbase.module

import com.example.pnbase.auth.presentation.AuthViewModel
import com.example.pnbase.userprofile.ContactsViewModel
import com.example.pnbase.userprofile.data.ContactsProvider
import com.example.pnbase.userprofile.domain.GetContactsUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module{
    viewModel { AuthViewModel() }
}

val contactModule = module {
    single { ContactsProvider(androidContext().contentResolver) }

    factory { GetContactsUseCase(get()) }

    viewModel { ContactsViewModel(get()) }
}