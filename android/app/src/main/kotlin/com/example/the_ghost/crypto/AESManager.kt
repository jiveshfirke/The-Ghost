package com.example.the_ghost.crypto

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object AESManager {

    private const val TRANSFORMATION = "AES/GCM/NoPadding"
    private const val AES_KEY_SIZE = 256
    private const val GCM_IV_LENGTH = 12
    private const val GCM_TAG_LENGTH = 128

    private val secretKey: SecretKey by lazy {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(AES_KEY_SIZE)
        keyGenerator.generateKey()
    }

    fun encryptFile(inputFile: File, outputFile: File) {

        val cipher = Cipher.getInstance(TRANSFORMATION)

        val iv = ByteArray(GCM_IV_LENGTH)
        SecureRandom().nextBytes(iv)

        val spec = GCMParameterSpec(GCM_TAG_LENGTH, iv)

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec)

        FileOutputStream(outputFile).use { fos ->

            fos.write(iv)

            FileInputStream(inputFile).use { fis ->

                val inputBytes = fis.readBytes()

                val encryptedBytes = cipher.doFinal(inputBytes)

                fos.write(encryptedBytes)
            }
        }
    }

    fun decryptFile(inputFile: File, outputFile: File) {

        val fileBytes = inputFile.readBytes()

        val iv = fileBytes.copyOfRange(0, GCM_IV_LENGTH)

        val encryptedBytes = fileBytes.copyOfRange(
            GCM_IV_LENGTH,
            fileBytes.size
        )

        val cipher = Cipher.getInstance(TRANSFORMATION)

        val spec = GCMParameterSpec(GCM_TAG_LENGTH, iv)

        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

        val decryptedBytes = cipher.doFinal(encryptedBytes)

        FileOutputStream(outputFile).use {
            it.write(decryptedBytes)
        }
    }
}