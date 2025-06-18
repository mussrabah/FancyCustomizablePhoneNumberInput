package com.muss_coding.fancycustomizablephonenumberinput.presentation.phone_number_screen

import com.muss_coding.fancycustomizablephonenumberinput.domain.SimpleCountry


sealed class PhoneNumberEvent {
    data class PhoneNumberChanged(val phoneNumber: String) : PhoneNumberEvent()
    data class CountrySelected(val country: SimpleCountry) : PhoneNumberEvent()
    object ShowCountryDialog : PhoneNumberEvent()
    object DismissCountryDialog : PhoneNumberEvent()
    object SubmitPhoneNumber : PhoneNumberEvent()
    object ResetSubmission : PhoneNumberEvent()
}