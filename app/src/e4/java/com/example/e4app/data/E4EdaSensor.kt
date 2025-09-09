package com.example.e4app.data

// Placeholder for Empatica E4 SDK integration. Replace with real SDK usage.
class E4EdaSensor : EdaSensorSource {
    override val isStreaming: Boolean
        get() = false

    override suspend fun startStreaming(onSample: (EdaSample) -> Unit) {
        // TODO: integrate Empatica E4 SDK streaming and map samples to EdaSample
        throw NotImplementedError("E4 SDK integration pending")
    }

    override suspend fun stopStreaming() {
        // TODO
    }
}


