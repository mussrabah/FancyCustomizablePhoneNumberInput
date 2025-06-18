package com.muss_coding.fancycustomizablephonenumberinput.data

import com.muss_coding.fancycustomizablephonenumberinput.CountriesQuery
import com.muss_coding.fancycustomizablephonenumberinput.CountryQuery
import com.muss_coding.fancycustomizablephonenumberinput.domain.SimpleCountry


fun CountriesQuery.Country.toSimpleCountry(): SimpleCountry {
    return SimpleCountry(
        code = code,
        name = name,
        emoji = emoji,
        phone = phone
    )
}