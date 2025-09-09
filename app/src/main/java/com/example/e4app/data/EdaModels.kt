package com.example.e4app.data

data class EdaSample(
    val timestampEpochMs: Long,
    val microsiemens: Double
)

interface EdaSensorSource {
    suspend fun startStreaming(onSample: (EdaSample) -> Unit)
    suspend fun stopStreaming()
    val isStreaming: Boolean
}


