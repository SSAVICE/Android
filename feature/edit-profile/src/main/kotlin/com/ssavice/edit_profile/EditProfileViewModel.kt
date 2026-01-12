package com.ssavice.edit_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<EditProfileState>(
        EditProfileState(
            form = EditProfileForm(),
            profileImage = EditProfileImage.UrlImage("")
        )
    )

    val uiState = _uiState.asStateFlow()

    private fun checkNameValid(): Boolean {
        if (_uiState.value.form.name.isBlank()) {
            _uiState.update {
                it.copy(form = it.form.copy(nameErrorMessage = "이름을 입력해주세요"))
            }
            return false
        } else {
            _uiState.update { it.copy(form = it.form.copy(nameErrorMessage = null)) }
            return true
        }
    }

    private fun checkEmailValid(): Boolean {
        val email = _uiState.value.form.email
        if (email.isBlank()) {
            _uiState.update {
                it.copy(form = it.form.copy(emailErrorMessage = "이메일을 입력해주세요"))
            }
            return false
        }

        val emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
        if (!email.matches(emailPattern)) {
            _uiState.update {
                it.copy(form = it.form.copy(emailErrorMessage = "올바르지 않은 이메일 형식입니다"))
            }
            return false
        }

        _uiState.update { it.copy(form = it.form.copy(emailErrorMessage = null)) }
        return true
    }

    private fun checkPhoneNumberValid(): Boolean {
        if (_uiState.value.form.phoneNumber.length < 10) {
            _uiState.update {
                it.copy(form = it.form.copy(phoneNumberErrorMessage = "올바른 전화번호를 입력해주세요"))
            }
            return false
        } else {
            _uiState.update { it.copy(form = it.form.copy(phoneNumberErrorMessage = null)) }
            return true
        }

    }

    fun onUpdateButtonClick() {
        if (_uiState.value.profileUpdateState is ProfileUpdateState.Updating) return

        var valid = checkNameValid()
        valid = checkEmailValid() && valid
        valid = checkPhoneNumberValid() && valid
        if (valid) {
            updateProfile(
                _uiState.value.form.name,
                _uiState.value.form.email,
                _uiState.value.form.phoneNumber
            )
        }
    }

    private fun updateProfile(name: String, email: String, phoneNumber: String) {
        _uiState.update {
            it.copy(profileUpdateState = ProfileUpdateState.Updating)
        }

        viewModelScope.launch(Dispatchers.IO) {

        }
    }

    fun onNameChange(name: String) {
        _uiState.update {
            it.copy(form = it.form.copy(name = name))
        }
    }

    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(form = it.form.copy(email = email))
        }
    }

    fun onPhoneNumberChange(phoneNumber: String) {
        _uiState.update {
            it.copy(form = it.form.copy(phoneNumber = phoneNumber))
        }
    }

}
