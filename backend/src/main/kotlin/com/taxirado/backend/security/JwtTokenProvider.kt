package com.taxirado.backend.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.security.Key
import java.util.*

/**
 * JWT Token Provider for generating and validating JWT tokens
 */
@Component
class JwtTokenProvider {

    @Value("\${jwt.secret:taxirado-secret-key-for-jwt-token-generation-minimum-256-bits}")
    private lateinit var jwtSecret: String

    @Value("\${jwt.expiration:86400000}") // 24 hours
    private var jwtExpiration: Long = 86400000

    private fun getSigningKey(): Key {
        val keyBytes = jwtSecret.toByteArray(StandardCharsets.UTF_8)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    fun generateToken(username: String, userId: Long): String {
        val now = Date()
        val expiryDate = Date(now.time + jwtExpiration)

        return Jwts.builder()
            .subject(username)
            .claim("userId", userId)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(getSigningKey())
            .compact()
    }

    fun getUsernameFromToken(token: String): String {
        val claims = getClaims(token)
        return claims.subject
    }

    fun getUserIdFromToken(token: String): Long {
        val claims = getClaims(token)
        return claims.get("userId", java.lang.Long::class.java).toLong()
    }

    fun validateToken(token: String): Boolean {
        return try {
            getClaims(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun getClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(jwtSecret.toByteArray(StandardCharsets.UTF_8)))
            .build()
            .parseSignedClaims(token)
            .payload
    }
}
