package com.example.dgs.model

import com.example.dgs.model.signup.SignUpData

data class LoginResponse(var StatusCode: String,
                    var StatusMessage: String,
                    var data: SignUpData) {
}