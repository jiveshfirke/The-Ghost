package com.example.the_ghost.fragment

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object FragmentManager {

    private const val CHUNK_SIZE = 1024 * 1024

    fun splitFile(file: File, outputDir: File): List<File> {

        val fragments = mutableListOf<File>()

        val buffer = ByteArray(CHUNK_SIZE)

        FileInputStream(file).use { input ->

            var part = 0
            var bytesRead: Int

            while (input.read(buffer).also { bytesRead = it } > 0) {

                val fragment = File(
                    outputDir,
                    "fragment_$part.dat"
                )

                FileOutputStream(fragment).use { output ->
                    output.write(buffer, 0, bytesRead)
                }

                fragments.add(fragment)

                part++
            }
        }

        return fragments
    }

    fun mergeFiles(parts: List<File>, outputFile: File) {

        FileOutputStream(outputFile).use { output ->

            parts.sortedBy { it.name }.forEach { part ->

                FileInputStream(part).use { input ->
                    input.copyTo(output)
                }
            }
        }
    }
}