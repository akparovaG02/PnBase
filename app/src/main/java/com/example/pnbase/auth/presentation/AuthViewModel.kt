package com.example.pnbase.auth.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class AuthViewModel : ViewModel() {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus(){
        if(auth.currentUser==null){
            _authState.value = AuthState.Unauthenticated
        }else{
            _authState.value = AuthState.Authenticated
        }
    }

     fun login(email : String,password : String, name: String){
        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{task->
                if (task.isSuccessful){
                    val user = auth.currentUser
                    if (user != null) getOrCreateUserInDatabase(user, name)
                    _authState.value = AuthState.Authenticated
                }else{
                    _authState.value = AuthState.Error(task.exception?.message?:"Something went wrong")
                }
            }
    }

    fun signup(email: String, password: String, name: String) {
        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            _authState.value = AuthState.Error("Email, password or name can't be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) getOrCreateUserInDatabase(user, name)
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }


    fun signout(){
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }


    private fun getOrCreateUserInDatabase(user: FirebaseUser, name: String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("users")
        val userRef = dbRef.child(user.uid)

        dbRef.get().addOnSuccessListener { snapshot ->
            val isEmpty = !snapshot.exists()

            userRef.get().addOnSuccessListener { userSnapshot ->
                if (!userSnapshot.exists()) {
                    val userData = mapOf(
                        "name" to name,
                        "email" to user.email,
                        "role" to if (isEmpty) "admin" else "user"
                    )
                    userRef.setValue(userData)
                }
            }
        }
    }
}