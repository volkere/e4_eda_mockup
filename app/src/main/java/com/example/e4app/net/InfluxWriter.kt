package com.example.e4app.net

import com.example.e4app.data.EdaSample
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

class InfluxWriter(
    baseUrl: String,
    private val org: String,
    private val bucket: String,
    private val token: String,
    private val measurement: String = "eda",
    client: OkHttpClient? = null
) {
    private val writeUrl: String
    private val http: OkHttpClient = client ?: OkHttpClient.Builder()
        .callTimeout(10, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    init {
        val normalized = baseUrl.trimEnd('/')
        val qOrg = URLEncoder.encode(org, "UTF-8")
        val qBucket = URLEncoder.encode(bucket, "UTF-8")
        writeUrl = "$normalized/api/v2/write?org=$qOrg&bucket=$qBucket&precision=ns"
    }

    fun writeSample(sample: EdaSample, sourceTag: String = "app"): Boolean {
        val timestampNs = sample.timestampEpochMs * 1_000_000L
        val line = "$measurement,source=$sourceTag value=${sample.microsiemens} $timestampNs\n"
        val body = line.toRequestBody("text/plain; charset=utf-8".toMediaType())
        val request = Request.Builder()
            .url(writeUrl)
            .addHeader("Authorization", "Token $token")
            .post(body)
            .build()
        return try {
            http.newCall(request).execute().use { resp ->
                resp.isSuccessful
            }
        } catch (_: Exception) {
            false
        }
    }
}


