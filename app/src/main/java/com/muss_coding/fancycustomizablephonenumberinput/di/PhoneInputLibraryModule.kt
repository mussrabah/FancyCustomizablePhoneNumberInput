package com.muss_coding.fancycustomizablephonenumberinput.di

import com.apollographql.apollo3.ApolloClient
import com.muss_coding.fancycustomizablephonenumberinput.data.ApolloCountryClient
import com.muss_coding.fancycustomizablephonenumberinput.domain.CountryClient
import com.muss_coding.fancycustomizablephonenumberinput.domain.GetCountriesUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultGraphQLEndpoint

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CustomGraphQLEndpoint

@Module
@InstallIn(SingletonComponent::class)
object PhoneInputLibraryModule {

    @Provides
    @Singleton
    @DefaultGraphQLEndpoint
    fun provideDefaultGraphQLEndpoint(): String {
        return "https://countries.trevorblades.com/graphql"
    }

    @Provides
    @Singleton
    fun provideApolloClient(
        @DefaultGraphQLEndpoint defaultEndpoint: String
    ): ApolloClient {
        // Try to get custom endpoint from library configuration
        val endpoint = PhoneInputLibraryConfig.graphqlEndpoint ?: defaultEndpoint

        return ApolloClient.Builder()
            .serverUrl(endpoint)
            .build()
    }

    @Provides
    @Singleton
    fun provideCountryClient(apolloClient: ApolloClient): CountryClient {
        return ApolloCountryClient(apolloClient)
    }

    @Provides
    @Singleton
    fun provideGetCountriesUseCase(countryClient: CountryClient): GetCountriesUseCase {
        return GetCountriesUseCase(countryClient)
    }
}

//@Module
//@InstallIn(SingletonComponent::class)
//abstract class PhoneInputRepositoryModule {
//
//    @Binds
//    abstract fun bindCountryRepository(
//        countryRepositoryImpl: CountryRepositoryImpl
//    ): CountryRepository
//}