package com.example.e4app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EdaScreen(viewModel: EdaViewModel) {
    val isStreaming by viewModel.isStreaming.collectAsState()
    val sample by viewModel.latest.collectAsState()
    val enabled by viewModel.influxEnabled.collectAsState()
    val url by viewModel.influxUrl.collectAsState()
    val org by viewModel.influxOrg.collectAsState()
    val bucket by viewModel.influxBucket.collectAsState()
    val token by viewModel.influxToken.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "EDA (µS): ${sample?.microsiemens?.let { String.format("%.3f", it) } ?: "--"}",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Time: ${sample?.timestampEpochMs ?: "--"}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = enabled, onCheckedChange = { viewModel.influxEnabled.value = it })
            Text("Write to InfluxDB", modifier = Modifier.padding(start = 8.dp))
        }
        OutlinedTextField(
            value = url,
            onValueChange = { viewModel.influxUrl.value = it },
            label = { Text("Base URL (http://host:8086)") },
            singleLine = true,
            modifier = Modifier.padding(top = 8.dp)
        )
        OutlinedTextField(
            value = org,
            onValueChange = { viewModel.influxOrg.value = it },
            label = { Text("Org") },
            singleLine = true,
            modifier = Modifier.padding(top = 8.dp)
        )
        OutlinedTextField(
            value = bucket,
            onValueChange = { viewModel.influxBucket.value = it },
            label = { Text("Bucket") },
            singleLine = true,
            modifier = Modifier.padding(top = 8.dp)
        )
        OutlinedTextField(
            value = token,
            onValueChange = { viewModel.influxToken.value = it },
            label = { Text("Token") },
            singleLine = true,
            modifier = Modifier.padding(top = 8.dp)
        )
        Button(
            onClick = { viewModel.toggleStreaming() },
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Text(if (isStreaming) "Stop" else "Start")
        }
    }
}


