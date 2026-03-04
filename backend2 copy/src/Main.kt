import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.*
import java.security.SecureRandom
import java.security.cert.X509Certificate

// Задача 1. HTTP-запросы через HttpURLConnection

fun disableSslVerification() {
    val trustAll = arrayOf<TrustManager>(object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<X509Certificate>?, authType: String?) {}
        override fun checkServerTrusted(chain: Array<X509Certificate>?, authType: String?) {}
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    })
    val sc = SSLContext.getInstance("SSL")
    sc.init(null, trustAll, SecureRandom())
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
    HttpsURLConnection.setDefaultHostnameVerifier { _, _ -> true }
}

fun sendGet(url: String): Pair<Int, String> {
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.requestMethod = "GET"
    connection.setRequestProperty("Accept", "application/json")

    val code = connection.responseCode
    val body = if (code in 200..299)
        connection.inputStream.bufferedReader().readText()
    else
        connection.errorStream.bufferedReader().readText()

    connection.disconnect()
    return Pair(code, body)
}

fun sendPost(url: String, json: String): Pair<Int, String> {
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.requestMethod = "POST"
    connection.doOutput = true
    connection.setRequestProperty("Content-Type", "application/json")
    connection.setRequestProperty("Accept", "application/json")

    connection.outputStream.write(json.toByteArray())

    val code = connection.responseCode
    val body = if (code in 200..299)
        connection.inputStream.bufferedReader().readText()
    else
        connection.errorStream.bufferedReader().readText()

    connection.disconnect()
    return Pair(code, body)
}

fun main() {
    disableSslVerification()

    // TODO 1: GET /posts/1
    println("=== GET /posts/1 ===")
    val (code1, body1) = sendGet("https://jsonplaceholder.typicode.com/posts/1")
    println("Статус: $code1")
    println("Тело: $body1")

    // TODO 2: POST /posts
    println("\n=== POST /posts ===")
    val json = """{"title": "Hello", "body": "World", "userId": 1}"""
    val (code2, body2) = sendPost("https://jsonplaceholder.typicode.com/posts", json)
    println("Статус: $code2")
    println("Тело: $body2")

    // TODO 3: GET /posts/9999 (несуществующий ресурс)
    println("\n=== GET /posts/9999 ===")
    val (code3, body3) = sendGet("https://jsonplaceholder.typicode.com/posts/9999")
    if (code3 in 200..299) {
        println("Статус: $code3")
        println("Тело: $body3")
    } else {
        println("Ошибка! Статус: $code3")
        println("Тело ошибки: $body3")
    }
}