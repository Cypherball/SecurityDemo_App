package com.experiment.app5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.experiment.app5.databinding.ActivitySelectionBinding

class SelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bGenerateSignatureView.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.bVerifySignatureView.setOnClickListener{
            val intent = Intent(this, VerifyActivity::class.java)
            startActivity(intent)
        }
    }
}