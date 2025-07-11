package com.example.pnbase.auth.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pnbase.utils.LogFileWriter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        if (auth.currentUser == null) {
            LogFileWriter.writeLog("APPLOG", "Пользователь не авторизован")
            _authState.value = AuthState.Unauthenticated
        } else {
            LogFileWriter.writeLog("APPLOG", "Пользователь уже авторизован: ${auth.currentUser?.email}")
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(email: String, password: String, name: String) {
        if (email.isEmpty() || password.isEmpty()) {
            LogFileWriter.writeLog("APPLOG", "Пустой email или пароль")
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }

        _authState.value = AuthState.Loading
        LogFileWriter.writeLog("APPLOG", "Попытка входа: $email")

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    LogFileWriter.writeLog("APPLOG", "Успешный вход: ${user?.email}")
                    if (user != null) getOrCreateUserInDatabase(user, name)
                    _authState.value = AuthState.Authenticated
                } else {
                    LogFileWriter.writeLog("APPLOG", "Ошибка входа: ${task.exception?.message}")
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    fun signup(email: String, password: String, name: String) {
        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            LogFileWriter.writeLog("APPLOG", "Пустые поля регистрации")
            _authState.value = AuthState.Error("Email, password or name can't be empty")
            return
        }

        _authState.value = AuthState.Loading
        LogFileWriter.writeLog("APPLOG", "Регистрация пользователя: $email")

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    LogFileWriter.writeLog("APPLOG", "Регистрация прошла успешно: ${user?.email}")
                    if (user != null) getOrCreateUserInDatabase(user, name)
                    _authState.value = AuthState.Authenticated
                } else {
                    LogFileWriter.writeLog("APPLOG", "Ошибка регистрации: ${task.exception?.message}")
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    fun signout() {
        auth.signOut()
        LogFileWriter.writeLog("APPLOG", "Пользователь вышел из системы")
        _authState.value = AuthState.Unauthenticated
    }

    private fun getOrCreateUserInDatabase(user: FirebaseUser, name: String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("users")
        val userRef = dbRef.child(user.uid)

        LogFileWriter.writeLog("APPLOG", "Проверка существования пользователя в базе данных: ${user.uid}")

        dbRef.get().addOnSuccessListener { snapshot ->
            val isEmpty = !snapshot.exists()
            LogFileWriter.writeLog("APPLOG", "База данных ${if (isEmpty) "пустая (создаётся админ)" else "не пустая"}")

            userRef.get().addOnSuccessListener { userSnapshot ->
                if (!userSnapshot.exists()) {
                    val userData = mapOf(
                        "name" to name,
                        "email" to user.email,
                        "role" to if (isEmpty) "admin" else "user"
                    )
                    userRef.setValue(userData)
                        .addOnSuccessListener {
                            LogFileWriter.writeLog("APPLOG", "Пользователь успешно добавлен в базу: $userData")
                        }
                        .addOnFailureListener {
                            LogFileWriter.writeLog("APPLOG", "Ошибка при добавлении пользователя в базу: ${it.message}")
                        }
                } else {
                    LogFileWriter.writeLog("APPLOG", "Пользователь уже существует в базе данных")
                }
            }
        }.addOnFailureListener {
            LogFileWriter.writeLog("APPLOG", "Ошибка при доступе к базе данных: ${it.message}")
        }
    }
}
