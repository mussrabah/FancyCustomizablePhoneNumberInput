package com.muss_coding.fancycustomizablephonenumberinput.domain

interface CountryClient {
    suspend fun getCountries(): List<SimpleCountry>
}