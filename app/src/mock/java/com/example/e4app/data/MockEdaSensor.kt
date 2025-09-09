package com.example.e4app.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin

class MockEdaSensor : EdaSensorSource {
    private var job: Job? = null
    override val isStreaming: Boolean
        get() = job?.isActive == true

    override suspend fun startStreaming(onSample: (EdaSample) -> Unit) {
        if (isStreaming) return
        job = CoroutineScope(Dispatchers.Default).launch {
            var t = 0
            while (isActive) {
                val base = 5.0 + 0.5 * sin(2 * PI * t / 200.0)
                val noise = (Math.random() - 0.5) * 0.2
                val value = base + noise
                onSample(EdaSample(System.currentTimeMillis(), value))
                t++
                delay(62) // ~16 Hz
            }
        }
    }

    override suspend fun stopStreaming() {
        job?.cancel()
        job = null
    }
}


