package com.example.suitmediatesmagang.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.suitmediatesmagang.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCheck.setOnClickListener {
            val sentence = binding.etPalindrome.text.toString().trim()
            if (sentence.isNotEmpty()) {
                val isPalindrome = isPalindrome(sentence)
                showDialog(if (isPalindrome) "isPalindrome" else "not palindrome")
            } else {
                showDialog("Please enter a sentence to check")
            }
        }

        binding.btnNext.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            if (name.isNotEmpty()) {
                val intent = Intent(this, SecondActivity::class.java)
                intent.putExtra("name", name)
                startActivity(intent)
            } else {
                showDialog("Please enter your name")
            }
        }
    }

    private fun isPalindrome(text: String): Boolean {
        val cleaned = text.replace("\\s".toRegex(), "").lowercase()
        return cleaned == cleaned.reversed()
    }

    private fun showDialog(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}