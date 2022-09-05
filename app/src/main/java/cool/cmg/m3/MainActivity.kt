package cool.cmg.m3

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import cool.cmg.m3.ui.theme.M3Theme
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            M3Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        var jokeString by remember {
                            mutableStateOf("")
                        }
                        Greeting(jokeString, modifier = Modifier.weight(1f))
                        Button(onClick = {
                            lifecycleScope.launch {
                                jokeString = getJoke()
                            }
                        }) {
                            Text("下一个笑话")
                        }
                    }
                }
            }
        }
    }

    private suspend fun getJoke(): String = withContext(Dispatchers.IO) {
        HttpClient() {
            install(ContentNegotiation) {
                json()
            }
        }.use { client ->
            client.get("https://v2.jokeapi.dev") {
                url {
                    appendPathSegments("joke", "Any")
                    parameters.append("type", "single")
                }
            }.body<Joke>().joke
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessages ->
                val messages: List<NdefMessage> = rawMessages.map { it as NdefMessage }
                val allString = messages.joinToString { ndefMessage -> ndefMessage.records.joinToString { it.payload.toString() } }
                // Process the messages array.
//                messages.first().records.first().payload.toString()
                Toast.makeText(this, allString, Toast.LENGTH_SHORT).show()
            }
        }
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = name, modifier = modifier)
}