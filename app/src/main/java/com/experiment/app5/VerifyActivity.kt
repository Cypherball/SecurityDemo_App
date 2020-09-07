package com.experiment.app5

import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.experiment.app5.databinding.ActivityVerifyBinding
import com.google.android.material.snackbar.Snackbar
import java.security.KeyFactory
import java.security.PublicKey
import java.security.Signature
import java.security.spec.InvalidKeySpecException
import java.security.spec.X509EncodedKeySpec
import kotlin.text.Charsets.UTF_8

class VerifyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerifyBinding
    private val TAG = "VerifyActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bVerify.setOnClickListener{
            val message: String = binding.messageET.text.toString()
            val publicKey_str: String = binding.publicKeyET.text.toString()
            val signature: String = binding.signatureET.text.toString()

            if (message.isEmpty() || publicKey_str.isEmpty() || signature.isEmpty()){
                Snackbar.make(
                    binding.parentLayout,
                    "Please fill all fields!",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else{
                val kf: KeyFactory = KeyFactory.getInstance("RSA")
                try {
                    //Generate Public Key from String
                    val publicKey: PublicKey = kf.generatePublic(
                        X509EncodedKeySpec(
                            Base64.decode(
                                publicKey_str,
                                Base64.DEFAULT
                            )
                        )
                    );
                    if(verify(message, publicKey, signature)){
                        binding.verificationOutput.text = "VALID"
                        binding.verificationOutput.setTextColor(Color.parseColor("#5cb85c"))
                    } else {
                        binding.verificationOutput.text = "INVALID"
                        binding.verificationOutput.setTextColor(Color.parseColor("#d9534f"))
                    }
                } catch (e: InvalidKeySpecException) {  //Catch exceptions while parsing Key or verifying signature
                    Log.d(TAG, "onCreate: $e")
                    Snackbar.make(
                        binding.parentLayout,
                        "Error parsing public key!",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    binding.verificationOutput.text = "ERROR"
                    binding.verificationOutput.setTextColor(Color.parseColor("#d9534f"))
                }  catch (e: IllegalArgumentException) {
                    Log.d(TAG, "onCreate: $e")
                    Snackbar.make(
                        binding.parentLayout,
                        "Error! Bad Input.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    binding.verificationOutput.text = "ERROR"
                    binding.verificationOutput.setTextColor(Color.parseColor("#d9534f"))
                }  catch (e: java.lang.Exception) {
                    Log.d(TAG, "onCreate: $e")
                    Snackbar.make(
                        binding.parentLayout,
                        "Error!",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    binding.verificationOutput.text = "ERROR"
                    binding.verificationOutput.setTextColor(Color.parseColor("#d9534f"))
                }
            }
        }
    }

    @Throws(Exception::class)
    private fun verify(message: String, publicKey: PublicKey, signature: String):Boolean {
        val _signature:Signature = Signature.getInstance("SHA256withRSA")
        _signature.initVerify(publicKey)
        _signature.update(message.toByteArray(UTF_8))
        val signatureBytes:ByteArray = Base64.decode(signature, Base64.DEFAULT)
        return _signature.verify(signatureBytes)
    }
}