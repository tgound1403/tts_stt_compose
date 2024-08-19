 package tgound.example.test4

 import android.content.Intent
 import android.speech.RecognizerIntent
 import android.speech.SpeechRecognizer
 import android.speech.tts.TextToSpeech
 import androidx.compose.foundation.layout.*
 import androidx.compose.material.*
 import androidx.compose.runtime.*
 import androidx.compose.ui.Alignment
 import androidx.compose.ui.Modifier
 import androidx.compose.ui.platform.LocalContext
 import androidx.compose.ui.unit.dp
 import androidx.core.app.ActivityCompat
 import androidx.core.content.ContextCompat
 import android.Manifest
 import android.content.pm.PackageManager
 import android.os.Bundle
 import android.speech.RecognitionListener
 import androidx.activity.ComponentActivity
 import androidx.activity.compose.rememberLauncherForActivityResult
 import androidx.activity.compose.setContent
 import androidx.activity.enableEdgeToEdge
 import androidx.activity.result.contract.ActivityResultContracts
 import androidx.compose.material3.Button
 import androidx.compose.material3.Scaffold
 import androidx.compose.material3.Text
 import androidx.compose.material3.TextField
 import androidx.compose.ui.tooling.preview.Preview
 import tgound.example.test4.ui.theme.Test4Theme
 import java.util.*

 class MainActivity : ComponentActivity() {
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         enableEdgeToEdge()
         setContent {
             Test4Theme {
                 Scaffold( modifier = Modifier.fillMaxSize() ) { innerPadding ->
                     SpeechToTextAndTextToSpeech(
                         modifier = Modifier.padding(innerPadding)
                     )
                 }
             }
         }
     }
 }

 @Composable
 fun SpeechToTextAndTextToSpeech(modifier: Modifier) {
     val context = LocalContext.current
     var recognizedText by remember { mutableStateOf("") }
     var inputText by remember { mutableStateOf("") }

     val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }

     val tts = remember { TextToSpeech(context) { } }

     var hasRecordAudioPermission by remember {
         mutableStateOf(
             ContextCompat.checkSelfPermission(
                 context,
                 Manifest.permission.RECORD_AUDIO
             ) == PackageManager.PERMISSION_GRANTED
         )
     }

     val permissionLauncher = rememberLauncherForActivityResult(
         ActivityResultContracts.RequestPermission()
     ) { isGranted: Boolean ->
         hasRecordAudioPermission = isGranted
     }

     LaunchedEffect(Unit) {
         if (!hasRecordAudioPermission) {
             permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
         }
     }

     Column(
         modifier = Modifier
             .fillMaxSize()
             .padding(16.dp),
         horizontalAlignment = Alignment.CenterHorizontally
     ) {
         Text("Recognized Text: $recognizedText")
         Spacer(modifier = Modifier.height(16.dp))
         Button(onClick = {
             if (hasRecordAudioPermission) {
                 val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                     putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                 }
                 speechRecognizer.startListening(intent)
             } else {
                 permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
             }
         }) {
             Text("Start Speech Recognition")
         }

         Spacer(modifier = Modifier.height(32.dp))

         TextField(
             value = inputText,
             onValueChange = { inputText = it },
             label = { Text("Enter text to speak") }
         )
         Spacer(modifier = Modifier.height(16.dp))
         Button(onClick = {
             tts.speak(inputText, TextToSpeech.QUEUE_FLUSH, null, "")
         }) {
             Text("Speak Text")
         }
     }

     DisposableEffect(Unit) {
         speechRecognizer.setRecognitionListener(object : RecognitionListener {
             override fun onResults(results: Bundle) {
                 val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                 if (!matches.isNullOrEmpty()) {
                     recognizedText = matches[0]
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

         tts.language = Locale.getDefault()

         onDispose {
             speechRecognizer.destroy()
             tts.stop()
             tts.shutdown()
         }
     }
 }
