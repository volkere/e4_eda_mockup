package com.example.e4app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e4app.data.EdaSample
import com.example.e4app.data.EdaSensorSource
import com.example.e4app.net.InfluxWriter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EdaViewModel : ViewModel() {
    // Swap implementation via flavors: MockEdaSensor in mock, E4EdaSensor in e4
    private val sensor: EdaSensorSource by lazy {
        createSensor()
    }

    private fun createSensor(): EdaSensorSource {
        return try {
            // In mock flavor, MockEdaSensor is available; in e4, E4EdaSensor is available.
            @Suppress("UNCHECKED_CAST")
            Class.forName("com.example.e4app.data.MockEdaSensor").newInstance() as EdaSensorSource
        } catch (_: Throwable) {
            @Suppress("UNCHECKED_CAST")
            Class.forName("com.example.e4app.data.E4EdaSensor").newInstance() as EdaSensorSource
        }
    }

    private val _latest = MutableStateFlow<EdaSample?>(null)
    val latest: StateFlow<EdaSample?> = _latest.asStateFlow()

    private val _isStreaming = MutableStateFlow(false)
    val isStreaming: StateFlow<Boolean> = _isStreaming.asStateFlow()

    // Influx settings/state
    val influxEnabled = MutableStateFlow(false)
    val influxUrl = MutableStateFlow("")
    val influxOrg = MutableStateFlow("")
    val influxBucket = MutableStateFlow("")
    val influxToken = MutableStateFlow("")
    private var influxWriter: InfluxWriter? = null

    fun toggleStreaming() {
        if (_isStreaming.value) {
            viewModelScope.launch {
                sensor.stopStreaming()
                _isStreaming.value = false
            }
        } else {
            viewModelScope.launch {
                sensor.startStreaming { sample ->
                    _latest.value = sample
                    if (influxEnabled.value) ensureWriter().writeSample(sample, sourceTag = "${if (sensor.isStreaming) "live" else "mock"}")
                }
                _isStreaming.value = true
            }
        }
    }

    private fun ensureWriter(): InfluxWriter {
        val existing = influxWriter
        if (existing != null) return existing
        val writer = InfluxWriter(
            baseUrl = influxUrl.value,
            org = influxOrg.value,
            bucket = influxBucket.value,
            token = influxToken.value,
            measurement = "eda"
        )
        influxWriter = writer
        return writer
    }
}


