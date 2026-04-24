package com.example.crudapp.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.crudapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "App Android Final Álvaro Gay"

        setupButtons()
    }

    private fun setupButtons() {
        // Navigate to List
        binding.btnLista.setOnClickListener {
            startActivity(Intent(this, ListActivity::class.java))
        }

        // Add new item directly
        binding.btnAnnadir.setOnClickListener {
            startActivity(Intent(this, AddEditActivity::class.java))
        }

        // --- INTENTS EXTERNOS ---

        // Abrir navegador
        binding.btnNavegador.setOnClickListener {
            val url = "https://www.google.com"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        // Abrir Google Maps
        binding.btnMapa.setOnClickListener {
            val location = Uri.parse("geo:41.676788,-0.898274?q=Parque+Goya+Zaragoza")
            val intent = Intent(Intent.ACTION_VIEW, location)
            intent.setPackage("com.google.android.apps.maps")
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                // Fallback: abrir en navegador
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://maps.google.com/?q=Parque+Goya+Zaragoza")
                    )
                )
            }
        }

        // Llamar por teléfono
        binding.btnTlfn.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+34 722584054"))
            startActivity(intent)
        }

        // Enviar email
        binding.btnMail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.setType("message/rfc822")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>("24.damm.alvarogay@fundacionmontessori.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "Asunto del correo")
            intent.putExtra(Intent.EXTRA_TEXT, "Intent Gmail")
            try {
                startActivity(Intent.createChooser(intent, "Enviar correo..."))
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(this, "No hay clientes de correo instalados.", Toast.LENGTH_SHORT)
                    .show()
            }

            // Compartir texto
            binding.btnCompartir.setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, "Compartido desde la app de Álvaro")
                    putExtra(Intent.EXTRA_TEXT, "¡Chequea esta app de Álvaro!")
                }
                startActivity(Intent.createChooser(shareIntent, "Compartir con..."))
            }

            // Abrir WhatsApp (o app de mensajería)
            binding.btnSms.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:+34722584054"))
                intent.putExtra("sms_body", "Hola! Te escribo desde la app de Álvaro.")
                startActivity(intent)
            }
        }
    }
}
