package tgound.example.test4

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer

class SpeechRecognizerManager(context: Context) {
    private val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    private var callback: RecognitionCallback? = null

    fun setRecognitionListener(callback: RecognitionCallback) {
        this.callback = callback
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onResults(results: Bundle) {
                val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    callback.onResult(matches[0])
                }
            }

            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) {}
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }

    fun startListening(intent: Intent) {
        speechRecognizer.startListening(intent)
    }

    fun destroy() {
        speechRecognizer.destroy()
    }

    interface RecognitionCallback {
        fun onResult(text: String)
    }
}
