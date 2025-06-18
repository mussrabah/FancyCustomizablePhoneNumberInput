package com.muss_coding.fancycustomizablephonenumberinput

import android.content.Context
import com.muss_coding.fancycustomizablephonenumberinput.di.PhoneInputLibraryConfig

/**
 * Main entry point for the Phone Input Library
 * Provides initialization and configuration methods
 */
object PhoneInputLibrary {

    private var isInitialized = false

    /**
     * Initialize the library with default configuration
     */
    fun initialize(context: Context) {
        if (isInitialized) return

        // Perform any necessary initialization
        isInitialized = true
    }

    /**
     * Initialize the library with custom configuration
     */
    fun initialize(
        context: Context,
        customGraphqlEndpoint: String? = null,
        enableCaching: Boolean = true,
        cacheDurationMs: Long = 24 * 60 * 60 * 1000L,
        wantCustomizedConfirmationScreen: Boolean = false
    ) {
        if (isInitialized) return

        PhoneInputLibraryConfig.configure(
            customGraphqlEndpoint = customGraphqlEndpoint,
            enableCaching = enableCaching,
            cacheDurationMs = cacheDurationMs,
            wantCustomizedConfirmationScreen = wantCustomizedConfirmationScreen
        )

        initialize(context)
    }

    /**
     * Check if library is initialized
     */
    fun isInitialized(): Boolean = isInitialized
}