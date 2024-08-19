package tgound.example.test4

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.compose.ui.platform.LocalContext

@Composable
fun SpeechToTextAndTextToSpeechScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel: SpeechToTextViewModel = SpeechToTextViewModel(application)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Recognized Text: ${viewModel.recognizedText}")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.startSpeechRecognition() }) {
            Text("Start Speech Recognition")
        }

        Spacer(modifier = Modifier.height(32.dp))

        TextField(
            value = viewModel.inputText,
            onValueChange = { viewModel.updateInputText(it) },
            label = { Text("Enter text to speak") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.speakText() }) {
            Text("Speak Text")
        }
    }
}
