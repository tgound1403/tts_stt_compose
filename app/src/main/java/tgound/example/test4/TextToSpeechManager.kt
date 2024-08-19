package tgound.example.test4

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.*

class TextToSpeechManager(context: Context) {
    private val tts = TextToSpeech(context) { }

    init {
        tts.language = Locale.getDefault()
    }

    fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    fun shutdown() {
        tts.stop()
        tts.shutdown()
    }
}
