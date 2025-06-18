package com.muss_coding.fancycustomizablephonenumberinput.di

/**
 * Configuration object for the Phone Input Library
 * This allows library consumers to customize the GraphQL endpoint and other settings
 */
object PhoneInputLibraryConfig {

    /**
     * Custom GraphQL endpoint URL
     * If null, the default endpoint will be used
     */
    var graphqlEndpoint: String? = null
        private set

    /**
     * Enable/disable caching
     */
    var enableCaching: Boolean = true
        private set

    /**
     * Cache duration in milliseconds
     */
    var cacheDurationMs: Long = 24 * 60 * 60 * 1000L // 24 hours
        private set

    /**
     * Customised second screen
     */
    var wantCustomizedConfirmationScreen: Boolean = false // use library's default confirmations screen
        private set

    /**
     * Configure the library with custom settings
     */
    fun configure(
        customGraphqlEndpoint: String? = null,
        enableCaching: Boolean = true,
        cacheDurationMs: Long = 24 * 60 * 60 * 1000L,
        wantCustomizedConfirmationScreen: Boolean = false
    ) {
        this.graphqlEndpoint = customGraphqlEndpoint
        this.enableCaching = enableCaching
        this.cacheDurationMs = cacheDurationMs
        this.wantCustomizedConfirmationScreen = wantCustomizedConfirmationScreen
    }

    /**
     * Reset to default configuration
     */
    fun reset() {
        graphqlEndpoint = null
        enableCaching = true
        cacheDurationMs = 24 * 60 * 60 * 1000L
    }
}