package tgound.example.test4

import android.app.Application
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel

class SpeechToTextViewModel(application: Application) : AndroidViewModel(application) {
    private val speechRecognizer = SpeechRecognizerManager(application)
    private val textToSpeech = TextToSpeechManager(application)

    var recognizedText by mutableStateOf("")
        private set

    var inputText by mutableStateOf("")
        private set

    init {
        speechRecognizer.setRecognitionListener(object : SpeechRecognizerManager.RecognitionCallback {
            override fun onResult(text: String) {
                recognizedText = text
            }
        })
    }

    fun startSpeechRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        }
        speechRecognizer.startListening(intent)
    }

    fun updateInputText(text: String) {
        inputText = text
    }

    fun speakText() {
        textToSpeech.speak(inputText)
    }

    override fun onCleared() {
        super.onCleared()
        speechRecognizer.destroy()
        textToSpeech.shutdown()
    }
}
