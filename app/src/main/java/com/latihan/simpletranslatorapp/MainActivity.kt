package com.latihan.simpletranslatorapp

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.latihan.simpletranslatorapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

   private lateinit var binding: ActivityMainBinding
   private lateinit var englishToIndoTranslator: Translator

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivityMainBinding.inflate(layoutInflater)
      setContentView(binding.root)
      ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
         val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
         v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
         insets
      }

      val options = TranslatorOptions.Builder()
         .setSourceLanguage(TranslateLanguage.ENGLISH)
         .setTargetLanguage(TranslateLanguage.JAPANESE)
         .build()

      englishToIndoTranslator = Translation.getClient(options)

      val conditions = DownloadConditions.Builder()
         .requireWifi()
         .build()

      englishToIndoTranslator.downloadModelIfNeeded(conditions)
         .addOnSuccessListener {
            binding.btnTranslate.setOnClickListener {
               val textToTranslate = binding.inputText.text.toString()
               translateText(textToTranslate, binding.tvTranslatedWord)
               Log.d("TranslatedText", "${binding.inputText.text}")
            }
         }
         .addOnFailureListener { exception ->
            binding.tvTranslatedWord.text = getString(R.string.model_failed)
         }
   }

   private fun translateText(inputText: String, outputText: TextView) {
      englishToIndoTranslator.translate(inputText)
         .addOnSuccessListener { translatedText ->
            outputText.text = translatedText
         }
         .addOnFailureListener { exception ->
            outputText.text = getString(R.string.translate_failed)
         }
   }

   override fun onDestroy() {
      super.onDestroy()
      englishToIndoTranslator.close()
   }
}