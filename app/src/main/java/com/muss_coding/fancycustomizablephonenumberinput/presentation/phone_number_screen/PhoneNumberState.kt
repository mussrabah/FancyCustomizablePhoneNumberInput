package com.muss_coding.fancycustomizablephonenumberinput.presentation.phone_number_screen

import com.muss_coding.fancycustomizablephonenumberinput.domain.SimpleCountry

data class PhoneNumberState(
    val countries: List<SimpleCountry> = emptyList(),
    val selectedCountry: SimpleCountry? = null,
    val phoneNumber: String = "",
    val isPhoneNumberValid: Boolean = false,
    val showCountryDialog: Boolean = false,
    val isLoading: Boolean = false,
    val isSubmitted: Boolean = false
)
