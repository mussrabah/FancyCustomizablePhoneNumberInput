package com.muss_coding.fancycustomizablephonenumberinput.presentation.phone_number_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muss_coding.fancycustomizablephonenumberinput.domain.GetCountriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class Country(
    val code: String,
    val dialCode: String,
    val emoji: String,
    val name: String
)

@HiltViewModel
class PhoneNumberViewModel @Inject constructor(
    private val getCountriesUseCase: GetCountriesUseCase,
): ViewModel() {

    private val _state = MutableStateFlow(PhoneNumberState())
    val state = _state.asStateFlow()

//    private val countries = listOf(
//        Country("US", "+1", "🇺🇸", "United States"),
//        Country("GB", "+44", "🇬🇧", "United Kingdom"),
//        Country("CA", "+1", "🇨🇦", "Canada"),
//        Country("DE", "+49", "🇩🇪", "Germany"),
//        Country("FR", "+33", "🇫🇷", "France"),
//        Country("JP", "+81", "🇯🇵", "Japan"),
//        Country("AU", "+61", "🇦🇺", "Australia"),
//        Country("BR", "+55", "🇧🇷", "Brazil"),
//        Country("IN", "+91", "🇮🇳", "India"),
//        Country("CN", "+86", "🇨🇳", "China"),
//        Country("KR", "+82", "🇰🇷", "South Korea"),
//        Country("IT", "+39", "🇮🇹", "Italy"),
//        Country("ES", "+34", "🇪🇸", "Spain"),
//        Country("MX", "+52", "🇲🇽", "Mexico"),
//        Country("RU", "+7", "🇷🇺", "Russia"),
//        Country("PL", "+48", "🇵🇱", "Poland"),
//        Country("NL", "+31", "🇳🇱", "Netherlands"),
//        Country("SE", "+46", "🇸🇪", "Sweden"),
//        Country("NO", "+47", "🇳🇴", "Norway"),
//        Country("DK", "+45", "🇩🇰", "Denmark")
//    )
//private val countries = listOf<SimpleCountry>()
//    init {
//        loadCountries()
//        _state.update { it.copy(
//            countries = countries,
//            selectedCountry = countries.first { it.code == "US" }
//        ) }
//    }

    init {
        viewModelScope.launch {
            _state.update { it.copy(
                isLoading = true
            ) }
            val countries = getCountriesUseCase.execute()
            println(countries)
            _state.update { it.copy(
                countries = countries,
                isLoading = false,
                selectedCountry = countries.first { it.code == "US" }
            ) }
        }
    }


    private fun loadCountries() {
        viewModelScope.launch {
            _state.update { it.copy(
                isLoading = true
            ) }

        }
    }

    fun onEvent(event: PhoneNumberEvent) {
        when (event) {
            is PhoneNumberEvent.PhoneNumberChanged -> {
                _state.update { it.copy(
                    phoneNumber = event.phoneNumber,
                    isPhoneNumberValid = event.phoneNumber.isNotBlank()
                ) }
            }
            is PhoneNumberEvent.CountrySelected -> {
                _state.update { it.copy(
                    selectedCountry = event.country,
                    showCountryDialog = false
                ) }
            }
            PhoneNumberEvent.ShowCountryDialog -> {
                _state.update { it.copy(
                    showCountryDialog = true
                ) }
            }
            PhoneNumberEvent.DismissCountryDialog -> {
                _state.update { it.copy(
                    showCountryDialog = false
                ) }
            }
            PhoneNumberEvent.SubmitPhoneNumber -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }

                    // Simulate API call
                    delay(1000)

                    _state.update { it.copy(
                        isLoading = false,
                        isSubmitted = true
                    ) }
                }
            }
            PhoneNumberEvent.ResetSubmission -> {
                _state.update { it.copy(
                    isSubmitted = false,
                    phoneNumber = "",
                    isPhoneNumberValid = false
                ) }
            }
        }
    }
}