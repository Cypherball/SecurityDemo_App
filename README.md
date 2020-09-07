# SecurityDemo Android App
__Generate and Verify Digital Signatures.__

This app is developed in Kotlin. 

**Main Libraries:** Material Components, Java Security, ViewBinding

### Description
+ The app uses RSA to generate 2048 bit Private and Public Key pairs which are displayed as base64 encoded strings. SecureRandom is also used to provide unique pairs.
+ The message is signed using the private key using SHA256 with RSA algorithm. This is the digital signature.
+ The public key, message, and signature can be shared as message to any supporting app.
+ The signature can be verified using the public key, message, and signature itself. Any slight modification will result in an invalid signature.
+ In the verfication screen, the public key is parsed using X509 Encoded Key Spec. InvalidKeySpecException is handled.
+ IllegalArgumentException is handled for Signature verifcation method. All other exceptions generated are caught and logged.
