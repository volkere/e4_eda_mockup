package com.example.e4app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.e4app.ui.EdaScreen
import com.example.e4app.ui.EdaViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: EdaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    EdaScreen(viewModel = viewModel)
                }
            }
        }
    }
}


