package com.muss_coding.fancycustomizablephonenumberinput.data

import com.apollographql.apollo3.ApolloClient
import com.muss_coding.fancycustomizablephonenumberinput.CountriesQuery
import com.muss_coding.fancycustomizablephonenumberinput.CountryQuery
import com.muss_coding.fancycustomizablephonenumberinput.domain.CountryClient
import com.muss_coding.fancycustomizablephonenumberinput.domain.SimpleCountry

class ApolloCountryClient(
    private val apolloClient: ApolloClient
): CountryClient {

    override suspend fun getCountries(): List<SimpleCountry> {
        return apolloClient
            .query(CountriesQuery())
            .execute()
            .data
            ?.countries
            ?.map { it.toSimpleCountry() }
            ?: emptyList()
    }
}