import java.net.HttpURLConnection
import java.net.URI
import java.util.Base64
import javax.net.ssl.*
import java.security.SecureRandom
import java.security.cert.X509Certificate

fun sendBearer(url: String, token: String?): Pair<Int, String> {
    val connection = URI(url).toURL().openConnection() as HttpURLConnection
    connection.requestMethod = "GET"
    if (token != null) {
        connection.setRequestProperty("Authorization", "Bearer $token")
    }
    val code = connection.responseCode
    val body = if (code in 200..299)
        connection.inputStream.bufferedReader().readText()
    else
        connection.errorStream?.bufferedReader()?.readText() ?: "No body"
    connection.disconnect()
    return Pair(code, body)
}

fun main() {
    disableSslVerification()

    val encoder = Base64.getUrlEncoder().withoutPadding()
    val decoder = Base64.getUrlDecoder()

    // TODO 1: Собрать JWT
    println("=== Сборка JWT ===")
    val header = """{"alg":"HS256","typ":"JWT"}"""
    val payload = """{"sub":"1","name":"Ivan Petrov","role":"student","iat":1234567890}"""
    val fakeSignature = "dummysignature"

    val token = "${encoder.encodeToString(header.toByteArray())}" +
            ".${encoder.encodeToString(payload.toByteArray())}" +
            ".${encoder.encodeToString(fakeSignature.toByteArray())}"
    println("Token: $token")

    // TODO 2: Декодировать JWT
    println("\n=== Декодирование JWT ===")
    val parts = token.split(".")
    println("Header:  ${String(decoder.decode(parts[0]))}")
    println("Payload: ${String(decoder.decode(parts[1]))}")

    // TODO 3: GET /bearer с токеном
    println("\n=== GET /bearer (с токеном) ===")
    val (code3, body3) = sendBearer("https://httpbin.org/bearer", token)
    println("Статус: $code3\nТело: $body3")

    // TODO 4: GET /bearer без токена
    println("\n=== GET /bearer (без токена) ===")
    val (code4, body4) = sendBearer("https://httpbin.org/bearer", null)
    println("Статус: $code4\nТело: $body4")

    // TODO 5: Подмена payload
    println("\n=== Подмена payload ===")
    val fakePayload = """{"sub":"1","name":"Ivan Petrov","role":"admin","iat":1234567890}"""
    val tamperedToken = "${parts[0]}.${encoder.encodeToString(fakePayload.toByteArray())}.${parts[2]}"
    println("Подменённый токен: $tamperedToken")
    val (code5, body5) = sendBearer("https://httpbin.org/bearer", tamperedToken)
    println("Статус: $code5\nТело: $body5")
    println("Сервер отвергнет токен, т.к. подпись не совпадёт с изменённым payload")
}