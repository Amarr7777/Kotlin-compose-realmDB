package com.example.p10

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val realm = MyApp.realm

    fun createUser(name: String) {
        viewModelScope.launch {
            realm.write {
                copyToRealm(User().apply {
                    this.name = name
                })
            }
        }
    }

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    fun fetchAllUsers() {
        viewModelScope.launch {
            _users.value = realm.query<User>().find()
        }
    }

    fun deleteUser(name: String) {
        viewModelScope.launch {
            realm.write {
                val user = query<User>("name == $0", name).first().find()
                user?.let { delete(it) }
            }
        }
    }

    fun updateUser(name: String, newName: String?) {
        viewModelScope.launch {
            realm.write {
                val user = query<User>("name == $0", name).first().find()
                user?.apply {
                    this.name = newName ?: this.name
                }
            }
        }
    }



}
