package com.example.suitmediatesmagang.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.suitmediatesmagang.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding
    private var userName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userName = intent.getStringExtra("name") ?: ""
        binding.tvUserName.text = userName

        binding.btnChooseUser.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_USER_SELECTION)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_USER_SELECTION && resultCode == RESULT_OK) {
            val selectedUser = data?.getStringExtra("selected_user")
            if (selectedUser != null) {
                binding.tvSelectedUser.text = selectedUser
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_USER_SELECTION = 1001
    }
}