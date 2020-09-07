package com.experiment.app5

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import com.experiment.app5.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import java.security.*
import kotlin.text.Charsets.UTF_8

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var privateKey: Key ?= null
    var publicKey: Key ?= null
    private var signature: ByteArray ?= null
    private var message: String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        setListeners()
    }

    private fun initViews() {
        binding.bSign.isEnabled = false
        binding.bSharePublicKey.isEnabled = false
        binding.bShareMessageSignature.isEnabled = false
    }

    private fun setListeners() {
        binding.bGenerateKeys.setOnClickListener{
            generateKeys()
            val privateKey_str: String = Base64.encodeToString(privateKey?.encoded, Base64.DEFAULT)
            val publicKey_str: String = Base64.encodeToString(publicKey?.encoded, Base64.DEFAULT)
            binding.apply {
                privateKey.setText(privateKey_str)
                publicKey.setText(publicKey_str)

                signatureET.text?.clear()
                bSign.isEnabled = true
                bSharePublicKey.isEnabled = true
            }
        }

        binding.bSign.setOnClickListener{
            val _message:String = binding.messageET.text.toString()
            if (_message.isEmpty()){
                Snackbar.make(binding.parentLayout,"Please enter a message!",Snackbar.LENGTH_SHORT).show()
            } else {
                sign(_message)
                val signature_str:String = Base64.encodeToString(signature, Base64.DEFAULT)
                binding.apply {
                    signatureET.setText(signature_str)
                    bShareMessageSignature.isEnabled = true
                }
                message = _message
            }
        }

        binding.bClear.setOnClickListener{
            binding.apply {
                bSign.isEnabled = false
                bShareMessageSignature.isEnabled = false
                bSharePublicKey.isEnabled = false
                publicKey.text?.clear()
                privateKey.text?.clear()
                messageET.text?.clear()
                signatureET.text?.clear()
            }
        }

        binding.bSharePublicKey.setOnClickListener{
            val message_body = "Public Key:\n" + Base64.encodeToString(publicKey?.encoded, Base64.DEFAULT) + "\n\nMessage sent using Implicit Intent from SecurityDemo App."
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message_body)
                putExtra(Intent.EXTRA_TITLE, "Public Key")
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(Intent.createChooser(intent, "Share using"))
            }
        }

        binding.bShareMessageSignature.setOnClickListener{
            val message_body = "Message:\n" + message  + "\n\nDigital Signature:\n" + Base64.encodeToString(signature, Base64.DEFAULT) + "\n\nMessage sent using Implicit Intent from SecurityDemo App."
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message_body)
                putExtra(Intent.EXTRA_TITLE, "Message and Digital Signature")
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(Intent.createChooser(intent, "Share using"))
            }
        }
    }

    private fun generateKeys(){
        //Init RSA keyPairGen instance
        val kpg: KeyPairGenerator = KeyPairGenerator.getInstance("RSA")
        kpg.initialize(2048, SecureRandom())
        val kp: KeyPair = kpg.genKeyPair()
        //get keys
        publicKey = kp.public
        privateKey = kp.private
    }

    private fun sign(message: String) {
        val _signature: Signature = Signature.getInstance("SHA256withRSA")
        _signature.initSign(privateKey as PrivateKey?)
        _signature.update(message.toByteArray(UTF_8))
        signature = _signature.sign()
    }
}