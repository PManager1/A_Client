package com.example.birdy.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

// MARK: - PromoCode Model (Matches backend / iOS PromoCode)
data class PromoCode(
    val id: String? = null,
    val code: String,
    val commissionFee: Double,
    val createdBy: String? = null,
    val expiresAt: Date,
    val usedBy: String? = null,
    val usedAt: Date? = null,
    val lastValidatedAt: Date? = null,
    val validationCount: Int = 0,
    val status: String,
    val createdAt: Date,
    val updatedAt: Date
) {
    val isActive: Boolean
        get() = status == "active" && Date().before(expiresAt)

    val isExpired: Boolean
        get() = Date().after(expiresAt)

    val isUsed: Boolean
        get() = status == "used"
}

// MARK: - PromoCode Validation Response
data class PromoCodeValidationResponse(
    val success: Boolean,
    val status: String,
    val code: String,
    val valid: Boolean? = null,
    val message: String,
    val commissionFee: Double? = null,
    val expiresAt: Date? = null
)

// MARK: - PromoCode Apply Response
data class PromoCodeApplyResponse(
    val success: Boolean,
    val message: String,
    val commissionFee: Double? = null,
    val code: PromoCode? = null
)

// MARK: - Analytics Models
data class CodeAnalytics(
    val code: String,
    val commissionFee: Double,
    val validationCount: Int? = null,
    val status: String? = null,
    val lastValidatedAt: Date? = null,
    val usedAt: Date? = null
)

data class PromoCodeAnalytics(
    val totalGenerated: Int,
    val totalUsed: Int,
    val totalExpired: Int,
    val totalValidated: Int,
    val totalValidationAttempts: Int,
    val conversionRate: Double,
    val mostValidatedCodes: List<CodeAnalytics>,
    val recentlyUsedCodes: List<CodeAnalytics>
)

// MARK: - PromoCode Error
sealed class PromoCodeError(message: String) : Exception(message) {
    class InvalidURL : PromoCodeError("Invalid URL")
    class NoData : PromoCodeError("No data received")
}

// MARK: - ReferralCodeService (Singleton, matches iOS ReferralCodeService)
object ReferralCodeService {

    private val baseURL = Config.API_BASE_URL + "/api/promo"

    private val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    private fun parseDate(str: String?): Date? {
        if (str == null) return null
        return try { isoFormat.parse(str) } catch (e: Exception) { null }
    }

    private fun getToken(): String {
        return AuthManager.getToken() ?: ""
    }

    // MARK: - Generate Promo Code
    suspend fun generatePromoCode(commissionFee: Double, expirationHours: Int): PromoCode {
        return withContext(Dispatchers.IO) {
            val url = URL("$baseURL/generate")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Authorization", "Bearer ${getToken()}")
            conn.connectTimeout = 15000
            conn.readTimeout = 15000

            val body = JSONObject().apply {
                put("commissionFee", commissionFee)
                put("expirationHours", expirationHours)
            }
            conn.outputStream.write(body.toString().toByteArray())

            println("🌐 ReferralCodeService: POST $url")
            println("📦 ReferralCodeService: Request body - $body")

            val code = conn.responseCode
            println("📡 ReferralCodeService: Response status: $code")

            if (code !in 200..299) {
                val errorBody = conn.errorStream?.bufferedReader()?.readText() ?: ""
                println("❌ ReferralCodeService: Server error - $errorBody")
                throw Exception("Server returned status $code")
            }

            val response = conn.inputStream.bufferedReader().readText()
            val json = JSONObject(response)
            val codeDict = json.getJSONObject("code")
            parsePromoCode(codeDict)
        }
    }

    // MARK: - Validate Promo Code
    suspend fun validatePromoCode(codeStr: String): PromoCodeValidationResponse {
        return withContext(Dispatchers.IO) {
            val url = URL("$baseURL/validate/$codeStr")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.setRequestProperty("Authorization", "Bearer ${getToken()}")
            conn.connectTimeout = 15000
            conn.readTimeout = 15000

            println("🌐 ReferralCodeService: GET $url")

            val code = conn.responseCode
            println("📡 ReferralCodeService: Response status: $code")

            val response = conn.inputStream.bufferedReader().readText()
            val json = JSONObject(response)

            PromoCodeValidationResponse(
                success = json.optBoolean("success"),
                status = json.optString("status"),
                code = json.optString("code"),
                valid = json.optBoolean("valid"),
                message = json.optString("message"),
                commissionFee = json.optDouble("commissionFee", 0.0).takeIf { json.has("commissionFee") },
                expiresAt = parseDate(json.optString("expiresAt"))
            )
        }
    }

    // MARK: - Apply Promo Code
    suspend fun applyPromoCode(codeStr: String): PromoCodeApplyResponse {
        return withContext(Dispatchers.IO) {
            val url = URL("$baseURL/apply")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Authorization", "Bearer ${getToken()}")
            conn.connectTimeout = 15000
            conn.readTimeout = 15000

            val body = JSONObject().apply { put("code", codeStr) }
            conn.outputStream.write(body.toString().toByteArray())

            println("🌐 ReferralCodeService: POST $url")

            val code = conn.responseCode
            println("📡 ReferralCodeService: Response status: $code")

            if (code !in 200..299) {
                val errorBody = conn.errorStream?.bufferedReader()?.readText() ?: ""
                println("❌ ReferralCodeService: Server error - $errorBody")
                throw Exception("Server returned status $code")
            }

            val response = conn.inputStream.bufferedReader().readText()
            val json = JSONObject(response)

            PromoCodeApplyResponse(
                success = json.optBoolean("success"),
                message = json.optString("message"),
                commissionFee = json.optDouble("commissionFee", 0.0).takeIf { json.has("commissionFee") },
                code = if (json.has("code")) parsePromoCode(json.getJSONObject("code")) else null
            )
        }
    }

    // MARK: - Get My Promo Codes
    suspend fun getMyPromoCodes(): List<PromoCode> {
        return withContext(Dispatchers.IO) {
            val url = URL("$baseURL/my-codes")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.setRequestProperty("Authorization", "Bearer ${getToken()}")
            conn.connectTimeout = 15000
            conn.readTimeout = 15000

            println("🌐 ReferralCodeService: GET $url")

            val code = conn.responseCode
            println("📡 ReferralCodeService: Response status: $code")

            if (code !in 200..299) {
                val errorBody = conn.errorStream?.bufferedReader()?.readText() ?: ""
                println("❌ ReferralCodeService: Server error - $errorBody")
                throw Exception("Server returned status $code")
            }

            val response = conn.inputStream.bufferedReader().readText()
            val json = JSONObject(response)
            val codesArray = json.getJSONArray("codes")

            val codes = mutableListOf<PromoCode>()
            for (i in 0 until codesArray.length()) {
                codes.add(parsePromoCode(codesArray.getJSONObject(i)))
            }
            println("✅ ReferralCodeService: Successfully decoded ${codes.size} promo codes")
            codes
        }
    }

    // MARK: - Get Analytics
    suspend fun getAnalytics(): PromoCodeAnalytics {
        return withContext(Dispatchers.IO) {
            val url = URL("$baseURL/analytics")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.setRequestProperty("Authorization", "Bearer ${getToken()}")
            conn.connectTimeout = 15000
            conn.readTimeout = 15000

            println("🌐 ReferralCodeService: GET $url")

            val code = conn.responseCode
            println("📡 ReferralCodeService: Response status: $code")

            if (code !in 200..299) {
                val errorBody = conn.errorStream?.bufferedReader()?.readText() ?: ""
                println("❌ ReferralCodeService: Server error - $errorBody")
                throw Exception("Server returned status $code")
            }

            val response = conn.inputStream.bufferedReader().readText()
            val json = JSONObject(response)
            val analytics = json.getJSONObject("analytics")

            parseAnalytics(analytics)
        }
    }

    // MARK: - JSON Parsing Helpers

    private fun parsePromoCode(json: JSONObject): PromoCode {
        return PromoCode(
            id = json.optString("id", null),
            code = json.optString("code", ""),
            commissionFee = json.optDouble("commissionFee", 0.0),
            createdBy = json.optString("createdBy", null),
            expiresAt = parseDate(json.optString("expiresAt")) ?: Date(),
            usedBy = json.optString("usedBy", null),
            usedAt = parseDate(json.optString("usedAt")),
            lastValidatedAt = parseDate(json.optString("lastValidatedAt")),
            validationCount = json.optInt("validationCount", 0),
            status = json.optString("status", "active"),
            createdAt = parseDate(json.optString("createdAt")) ?: Date(),
            updatedAt = parseDate(json.optString("updatedAt")) ?: Date()
        )
    }

    private fun parseCodeAnalytics(json: JSONObject): CodeAnalytics {
        return CodeAnalytics(
            code = json.optString("code", ""),
            commissionFee = json.optDouble("commissionFee", 0.0),
            validationCount = json.optInt("validationCount", 0),
            status = json.optString("status", null),
            lastValidatedAt = parseDate(json.optString("lastValidatedAt")),
            usedAt = parseDate(json.optString("usedAt"))
        )
    }

    private fun parseAnalytics(json: JSONObject): PromoCodeAnalytics {
        val mostValidated = mutableListOf<CodeAnalytics>()
        val mvArray = json.optJSONArray("mostValidatedCodes") ?: JSONArray()
        for (i in 0 until mvArray.length()) {
            mostValidated.add(parseCodeAnalytics(mvArray.getJSONObject(i)))
        }

        val recentlyUsed = mutableListOf<CodeAnalytics>()
        val ruArray = json.optJSONArray("recentlyUsedCodes") ?: JSONArray()
        for (i in 0 until ruArray.length()) {
            recentlyUsed.add(parseCodeAnalytics(ruArray.getJSONObject(i)))
        }

        return PromoCodeAnalytics(
            totalGenerated = json.optInt("totalGenerated", 0),
            totalUsed = json.optInt("totalUsed", 0),
            totalExpired = json.optInt("totalExpired", 0),
            totalValidated = json.optInt("totalValidated", 0),
            totalValidationAttempts = json.optInt("totalValidationAttempts", 0),
            conversionRate = json.optDouble("conversionRate", 0.0),
            mostValidatedCodes = mostValidated,
            recentlyUsedCodes = recentlyUsed
        )
    }
}