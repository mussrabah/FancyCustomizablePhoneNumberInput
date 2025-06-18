package com.muss_coding.fancycustomizablephonenumberinput.presentation.phone_number_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.muss_coding.fancycustomizablephonenumberinput.di.PhoneInputLibraryConfig.wantCustomizedConfirmationScreen
import com.muss_coding.fancycustomizablephonenumberinput.domain.SimpleCountry
import kotlin.Nothing

data class PhoneNumberColors(
    val background: Color = Color(0xFFF8F9FA),
    val cardBackground: Color = Color.White,
    val primaryAccent: Color = Color(0xFF6366F1),
    val successAccent: Color = Color(0xFF10B981),
    val textPrimary: Color = Color(0xFF1F2937),
    val textSecondary: Color = Color(0xFF6B7280),
    val textTertiary: Color = Color(0xFF374151),
    val placeholder: Color = Color(0xFF9CA3AF),
    val inputBackground: Color = Color(0xFFF8F9FA),
    val inputBorder: Color = Color(0xFFE5E7EB),
    val inputBorderFocused: Color = Color(0xFF6366F1),
    val buttonText: Color = Color.White,
    val loadingIndicator: Color = Color.White
)

@Composable
fun PhoneNumberScreen(
    colors: PhoneNumberColors = PhoneNumberThemes.Default,
    confirmationScreen: @Composable (state: PhoneNumberState) -> Unit = {}
) {
    val phoneNumberViewModel = hiltViewModel<PhoneNumberViewModel>()
    val phoneNumberState by phoneNumberViewModel.state.collectAsState()
    PhoneNumberInput(
        state = phoneNumberState,
        onEvent = phoneNumberViewModel::onEvent,
        colors = colors,
        confirmationScreen = confirmationScreen
    )
}

@Composable
private fun PhoneNumberInput(
    modifier: Modifier = Modifier,
    state: PhoneNumberState,
    onEvent: (PhoneNumberEvent) -> Unit,
    colors: PhoneNumberColors = PhoneNumberThemes.DarkYellow,
    confirmationScreen: @Composable (state: PhoneNumberState) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        if (state.isSubmitted) {
            if (!wantCustomizedConfirmationScreen) {
                SuccessCard(
                    phoneNumber = "+${state.selectedCountry?.phone} ${state.phoneNumber}",
                    onReset = { onEvent(PhoneNumberEvent.ResetSubmission) },
                    colors = colors
                )
            } else {
                confirmationScreen(state)
            }
        } else {
            PhoneInputCard(
                state = state,
                onEvent = onEvent,
                colors = colors
            )
        }

        // Country Selection Dialog
        if (state.showCountryDialog) {
            CountrySelectionDialog(
                countries = state.countries,
                onCountrySelected = { country ->
                    onEvent(PhoneNumberEvent.CountrySelected(country))
                },
                onDismiss = { onEvent(PhoneNumberEvent.DismissCountryDialog) },
                colors = colors
            )
        }
    }
}
@Composable
private fun PhoneInputCard(
    state: PhoneNumberState,
    onEvent: (PhoneNumberEvent) -> Unit,
    colors: PhoneNumberColors
) {
    Card(
        modifier = Modifier
            .wrapContentSize()
            .widthIn(min = 320.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                //.background(colors.cardBackground)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = "phone",
                modifier = Modifier.size(48.dp),
                tint = colors.primaryAccent
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Enter Your Phone Number",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "We'll send you a verification code",
                fontSize = 16.sp,
                color = colors.textSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Phone Input Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Country Code Selector
                state.selectedCountry?.let { country ->
                    Card(
                        modifier = Modifier
                            .clickable { onEvent(PhoneNumberEvent.ShowCountryDialog) }
                            .wrapContentWidth(),
                        elevation = CardDefaults.cardElevation(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .background(colors.inputBackground)
                                .padding(horizontal = 16.dp, vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = country.emoji,
                                fontSize = 24.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "+${country.phone}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colors.textTertiary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Select Country",
                                tint = colors.textSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Phone Number Input
                OutlinedTextField(
                    value = state.phoneNumber,
                    onValueChange = { onEvent(PhoneNumberEvent.PhoneNumberChanged(it)) },
                    modifier = Modifier.weight(1f),
                    placeholder = {
                        Text(
                            text = "Phone number",
                            color = colors.placeholder
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone
                    ),
                    shape = RoundedCornerShape(12.dp),
//                        colors = TextFieldDefaults.outlinedTextFieldColors(
//                            backgroundColor = colors.inputBackground,
//                            focusedBorderColor = colors.inputBorderFocused,
//                            unfocusedBorderColor = colors.inputBorder,
//                            textColor = colors.textTertiary
//                        ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colors.inputBorderFocused,
                        unfocusedBorderColor = colors.inputBorder,
                        focusedTextColor = colors.textTertiary,
                        focusedContainerColor = colors.inputBackground,
                        unfocusedContainerColor = colors.inputBackground,
                    ),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Submit Button
            Button(
                onClick = { onEvent(PhoneNumberEvent.SubmitPhoneNumber) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        backgroundColor = colors.primaryAccent,
//                        contentColor = colors.buttonText
//                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primaryAccent,
                    contentColor = colors.buttonText
                ),
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                ),
                enabled = state.isPhoneNumberValid && !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = colors.loadingIndicator,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Continue",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun SuccessCard(
    phoneNumber: String,
    onReset: () -> Unit,
    colors: PhoneNumberColors
) {
    Card(
        modifier = Modifier
            .wrapContentSize()
            .widthIn(min = 320.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .background(colors.cardBackground)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Success",
                modifier = Modifier.size(48.dp),
                tint = colors.successAccent
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Phone Number Submitted!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "We'll send a verification code to:",
                fontSize = 16.sp,
                color = colors.textSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = phoneNumber,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = colors.textTertiary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onReset,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primaryAccent,
                    contentColor = colors.buttonText
                )
            ) {
                Text(
                    text = "Enter Another Number",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun CountrySelectionDialog(
    countries: List<SimpleCountry>,
    onCountrySelected: (SimpleCountry) -> Unit,
    onDismiss: () -> Unit,
    colors: PhoneNumberColors
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 400.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(colors.cardBackground)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Select Country",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn {
                    items(countries) { country ->
                        CountryItem(
                            country = country,
                            onCountryClick = { onCountrySelected(country) },
                            colors = colors
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CountryItem(
    country: SimpleCountry,
    onCountryClick: () -> Unit,
    colors: PhoneNumberColors
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCountryClick() }
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = country.emoji,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = country.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = colors.textTertiary
            )
            Text(
                text = "+${country.phone}",
                fontSize = 14.sp,
                color = colors.textSecondary
            )
        }
    }
}

// Predefined color schemes for easy theming
object PhoneNumberThemes {
    val Default = PhoneNumberColors()

    val Dark = PhoneNumberColors(
        background = Color(0xFF1F2937),
        cardBackground = Color(0xFF374151),
        primaryAccent = Color(0xFF8B5CF6),
        successAccent = Color(0xFF10B981),
        textPrimary = Color.White,
        textSecondary = Color(0xFFD1D5DB),
        textTertiary = Color(0xFFF3F4F6),
        placeholder = Color(0xFF9CA3AF),
        inputBackground = Color(0xFF4B5563),
        inputBorder = Color(0xFF6B7280),
        inputBorderFocused = Color(0xFF8B5CF6),
        buttonText = Color.White,
        loadingIndicator = Color.White
    )

    val Blue = PhoneNumberColors(
        background = Color(0xFFEFF6FF),
        cardBackground = Color.White,
        primaryAccent = Color(0xFF2563EB),
        successAccent = Color(0xFF059669),
        textPrimary = Color(0xFF1E3A8A),
        textSecondary = Color(0xFF64748B),
        textTertiary = Color(0xFF334155),
        placeholder = Color(0xFF94A3B8),
        inputBackground = Color(0xFFF1F5F9),
        inputBorder = Color(0xFFCBD5E1),
        inputBorderFocused = Color(0xFF2563EB),
        buttonText = Color.White,
        loadingIndicator = Color.White
    )

    val Green = PhoneNumberColors(
        background = Color(0xFFF0FDF4),
        cardBackground = Color.White,
        primaryAccent = Color(0xFF16A34A),
        successAccent = Color(0xFF059669),
        textPrimary = Color(0xFF14532D),
        textSecondary = Color(0xFF64748B),
        textTertiary = Color(0xFF374151),
        placeholder = Color(0xFF94A3B8),
        inputBackground = Color(0xFFF7FEF0),
        inputBorder = Color(0xFFBBF7D0),
        inputBorderFocused = Color(0xFF16A34A),
        buttonText = Color.White,
        loadingIndicator = Color.White
    )

    val DarkYellow = PhoneNumberColors(
        background = Color(0xFFFFF9E6),         // light yellow background
        cardBackground = Color.White,           // keep card background white
        primaryAccent = Color(0xFFD97706),      // darker yellow / amber
        successAccent = Color(0xFFCA8A04),      // strong yellowish amber
        textPrimary = Color(0xFF92400E),        // dark brownish yellow for primary text
        textSecondary = Color(0xFF854D0E),      // slightly lighter for secondary text
        textTertiary = Color(0xFF78350F),       // even darker for tertiary
        placeholder = Color(0xFFE2B63D),        // soft yellow for placeholder
        inputBackground = Color(0xFFFFFBEB),    // very light yellow for inputs
        inputBorder = Color(0xFFFCD34D),        // light border
        inputBorderFocused = Color(0xFFD97706), // same as primary accent when focused
        buttonText = Color.White,               // white text on yellow buttons
        loadingIndicator = Color.White          // keep loading indicator white
    )
}


@Preview
@Composable
fun PhoneNumberScreenPreview(modifier: Modifier = Modifier) {
    PhoneNumberInput(
        state = PhoneNumberState(
            selectedCountry = SimpleCountry(
                name = "United States",
                code = "US",
                phone = "1",
                emoji = "🇺🇸"
            )
        ),
        onEvent = {}
    )
}