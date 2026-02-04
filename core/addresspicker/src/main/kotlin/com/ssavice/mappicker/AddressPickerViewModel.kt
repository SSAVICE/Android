package com.ssavice.mappicker

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.mappicker.datasource.ConvertCoordinateDatasource
import com.ssavice.mappicker.model.AddressPickResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressPickerViewModel @Inject constructor(
    private val convertCoordinateDatasource: ConvertCoordinateDatasource
): ViewModel(){
    private val _uiState = MutableStateFlow<AddressPickerState>(AddressPickerState.Idle)
    val uiState = _uiState.asStateFlow()
    private val _coordinateConvertFinishedEvent = Channel<AddressPickResult>()
    val coordinateConvertFinishedEvent = _coordinateConvertFinishedEvent.receiveAsFlow()



    fun convertCoordinate(address: AddressPickResult) {
        _uiState.update {
            AddressPickerState.Loading
        }
        viewModelScope.launch(Dispatchers.IO) {
            convertCoordinateDatasource.convertCoordinate(
                address.address
            ).onSuccess { result ->
                _uiState.update {
                    AddressPickerState.Idle
                }
                _coordinateConvertFinishedEvent.send(address.copy(
                    latitude = result.y,
                    longitude = result.x
                ))
            }.onFailure {
                Log.e(
                    "AddressPickerViewModel",
                    "convertCoordinate: ${it.message}",
                    it
                )
                _uiState.update {
                    AddressPickerState.Idle
                }
            }
        }
    }
}
