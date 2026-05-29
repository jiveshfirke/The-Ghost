package com.example.the_ghost

import android.os.Bundle
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import com.example.ghost_vault.crypto.AESManager
import com.example.ghost_vault.fragment.FragmentManager
import java.io.File

class MainActivity : FlutterActivity() {
    private val CHANNEL = "vault/native"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {

        super.configureFlutterEngine(flutterEngine)

        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            CHANNEL
        ).setMethodCallHandler { call, result ->

            when (call.method) {

                "processFile" -> {

                    val path = call.argument<String>("path")!!

                    val response = processFile(path)

                    result.success(response)
                }

                else -> result.notImplemented()
            }
        }
    }

    private fun processFile(path: String): String {

        val originalFile = File(path)

        val vaultDir = File(filesDir, "vault")

        if (!vaultDir.exists()) {
            vaultDir.mkdirs()
        }

        val encryptedFile = File(vaultDir, "encrypted.bin")

        AESManager.encryptFile(originalFile, encryptedFile)

        val fragmentDir = File(vaultDir, "fragments")

        if (!fragmentDir.exists()) {
            fragmentDir.mkdirs()
        }

        FragmentManager.splitFile(
            encryptedFile,
            fragmentDir
        )

        encryptedFile.delete()

        return "File fragmented successfully"
    }
}
