package com.khadar3344.myshop.ui.home.screens.profile_screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import com.khadar3344.myshop.R
import com.khadar3344.myshop.components.CustomAppBar
import com.khadar3344.myshop.components.CustomDefaultBtn
import com.khadar3344.myshop.model.User
import com.khadar3344.myshop.telephony.TelephonyManager
import com.khadar3344.myshop.ui.home.component.Error
import com.khadar3344.myshop.ui.home.component.Loading
import com.khadar3344.myshop.util.Dimensions
import com.khadar3344.myshop.util.Resource

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    telephonyManager: TelephonyManager,
    logout: () -> Unit,
    onBackBtnClick: () -> Unit,
    onMapClick: () -> Unit
) {
    val profileState by viewModel.userInfo.collectAsState()
    val context = LocalContext.current
    ProfileScreenContent(
        profileState = profileState,
        telephonyManager = telephonyManager,
        logout = {
            viewModel.logout()
            logout()
        },
        updateData = { user ->
            viewModel.updateUserInfoFirebase(user = user)
            Toast.makeText(context, "Update user data successfully", Toast.LENGTH_SHORT).show()
        },
        onBackBtnClick = onBackBtnClick,
        onMapClick = onMapClick
    )
}

@Composable
fun ProfileScreenContent(
    profileState: Resource<User>?,
    telephonyManager: TelephonyManager,
    logout: () -> Unit,
    updateData: (User) -> Unit,
    onBackBtnClick: () -> Unit,
    onMapClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when (profileState) {
            is Resource.Success -> {
                SuccessScreen(
                    profileState = profileState.data,
                    telephonyManager = telephonyManager,
                    logout = logout,
                    updateData = updateData,
                    onBackBtnClick = onBackBtnClick,
                    onMapClick = onMapClick
                )
            }
            is Resource.Failure<*> -> {
                Error(message = profileState.exception.toString())
            }
            is Resource.Loading -> {
                Loading()
            }
            else -> {}
        }
    }
}

@Composable
fun SuccessScreen(
    profileState: User,
    telephonyManager: TelephonyManager,
    logout: () -> Unit,
    updateData: (User) -> Unit,
    onBackBtnClick: () -> Unit,
    onMapClick: () -> Unit
) {
    var name: String by remember { mutableStateOf(profileState.name) }
    var phone: String by remember { mutableStateOf(profileState.phone) }
    var address: String by remember { mutableStateOf(profileState.address) }
    val nameErrorState = remember { mutableStateOf(false) }
    val phoneNumberErrorState = remember { mutableStateOf(false) }
    val addressErrorState = remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimensions.spacing_medium)
            .verticalScroll(scrollState)
    ) {
        CustomAppBar(
            onBackBtnClick = onBackBtnClick,
            appBarTitle = "Profile"
        )
        
        Spacer(modifier = Modifier.height(Dimensions.spacing_large))

        Text(
            text = profileState.email,
            modifier = Modifier.padding(Dimensions.spacing_small),
            fontSize = Dimensions.text_large,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(Dimensions.spacing_medium))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Name", fontSize = Dimensions.text_medium) },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = "Name"
                )
            },
            singleLine = true,
            visualTransformation = VisualTransformation.None,
            keyboardOptions = KeyboardType.Text.toKeyboardOptions(),
            isError = nameErrorState.value,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Dimensions.spacing_small))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text(text = "Phone Number", fontSize = Dimensions.text_medium) },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.phone),
                    contentDescription = "Phone",
                    modifier = Modifier.clickable {
                        if (phone.isNotEmpty()) {
                            telephonyManager.makePhoneCall(phone)
                        }
                    }
                )
            },
            singleLine = true,
            visualTransformation = VisualTransformation.None,
            keyboardOptions = KeyboardType.Phone.toKeyboardOptions(),
            isError = phoneNumberErrorState.value,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Dimensions.spacing_small))

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text(text = "Address", fontSize = Dimensions.text_medium) },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.location_point),
                    contentDescription = "Address",
                    modifier = Modifier.clickable { onMapClick() }
                )
            },
            singleLine = true,
            visualTransformation = VisualTransformation.None,
            keyboardOptions = KeyboardType.Text.toKeyboardOptions(),
            isError = addressErrorState.value,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Dimensions.spacing_large))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacing_medium)
        ) {
            CustomDefaultBtn(shapeSize = 50f, btnText = "Update") {
                val isNameValid = name.isEmpty() || name.length < 3
                val isPhoneValid = phone.isEmpty() || phone.length < 4
                val isAddressValid = address.isEmpty() || address.length < 5
                nameErrorState.value = isNameValid
                phoneNumberErrorState.value = isPhoneValid
                addressErrorState.value = isAddressValid
                if (!isNameValid && !isPhoneValid && !isAddressValid) {
                    updateData(
                        User(
                            name = name,
                            phone = phone,
                            address = address,
                            email = profileState.email
                        )
                    )
                }
            }

            CustomDefaultBtn(shapeSize = 50f, btnText = "View on Map") {
                onMapClick()
            }

            CustomDefaultBtn(shapeSize = 50f, btnText = "Logout") {
                logout()
            }
        }

        Spacer(modifier = Modifier.height(Dimensions.spacing_large))
    }
}
