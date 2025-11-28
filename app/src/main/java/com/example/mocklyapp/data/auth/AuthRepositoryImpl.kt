package com.example.mocklyapp.data.auth

import com.example.mocklyapp.data.auth.local.AuthLocalDataSource
import com.example.mocklyapp.data.auth.remote.AuthApi
import com.example.mocklyapp.data.auth.remote.LoginRequest
import com.example.mocklyapp.data.auth.remote.RefreshRequest
import com.example.mocklyapp.data.auth.remote.RegisterRequest
import com.example.mocklyapp.data.auth.remote.TokenResponseDto
import com.example.mocklyapp.domain.auth.AuthRepository
import com.example.mocklyapp.domain.auth.model.Tokens

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val local: AuthLocalDataSource
): AuthRepository
    {
        override suspend fun login(
            email: String,
            password: String
        ): Tokens {
            val resp = api.login(LoginRequest(email, password))
            val tokens = resp.toDomain()
            local.saveTokens(tokens)
            return tokens
        }

        override suspend fun register(
            email: String,
            password: String,
            name: String,
            surname: String,
            role: String
        ): Tokens {
            val resp = api.register(RegisterRequest(email, password, name, surname, role))
            val tokens = resp.toDomain()
            local.saveTokens(tokens)
            return tokens
        }

        override suspend fun refresh(refreshTokens: String): Tokens {
            val resp = api.refresh(RefreshRequest(refreshTokens))
            val tokens = resp.toDomain()
            local.saveTokens(tokens)
            return tokens
        }

        override suspend fun logout() {
            val tokens = local.getTokens() ?: return
            api.logout(RefreshRequest(tokens.refreshToken))
            local.clear()
        }
    }


private fun TokenResponseDto.toDomain() = Tokens(
    accessToken = accessToken,
    refreshToken = refreshToken,
    userId = userId
)