
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@Serializable
@JsonIgnoreUnknownKeys
data class AuthErrorResponse(
    @SerialName("status") val status: Int,
    @SerialName("properties") val errorProperties: ErrorProperties,
    @SerialName("detail") val detail: String,
)

@Serializable
@JsonIgnoreUnknownKeys
data class ErrorResponse(
    @SerialName("status") val status: Int,
    @SerialName("error_code") val errorCode: String,
    @SerialName("detail") val detail: String,
)

@Serializable
data class ErrorProperties(
    @SerialName("error_code")
    val errorCode: String,
)

enum class ErrorCode(
    val code: String,
    val status: Int,
) {
    INVALID_TOKEN("INVALID_TOKEN", 401),
    EXPIRED_TOKEN("EXPIRED_TOKEN", 401),
    UNSUPPORTED_TOKEN("UNSUPPORTED_TOKEN", 401),
    MISSING_TOKEN("MISSING_TOKEN", 401),
    UNKNOWN_TOKEN_ERROR("UNKNOWN_TOKEN_ERROR", 401),
    FORBIDDEN("FORBIDDEN", 403),
    USER_NOT_FOUND("USER_NOT_FOUND", 404),
    COMPANY_NOT_FOUND("COMPANY_NOT_FOUND", 404),
    COMPANY_ALREADY_EXISTS("COMPANY_ALREADY_EXISTS", 409),
    MEMBER_FULL("MEMBER_FULL", 409),
}

internal val errorCodeMap = ErrorCode.entries.associateBy { it.code }
