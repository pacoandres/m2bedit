package org.gnu.itsmoroto.m2bedit

import java.io.File

class FileManager {
    companion object {
        const val EXTENSION = "m2b"
        val MagicNumber: ByteArray = byteArrayOf(13, 2, 2, 6, 9, 12, 5)


        fun readFile(f: File): Boolean {
            val textbytes = f.readBytes()
            if (textbytes.size < MagicNumber.size)
                return false
            for (i in 0..MagicNumber.size - 1) {
                if (textbytes[i] != MagicNumber[i])
                    return false
            }
            MainView.editText.clear()
            val r = IntRange (MagicNumber.size, textbytes.size - 1)
            MainView.editText.text = String(
                textbytes.slice(r).toByteArray(),
                Charsets.UTF_8)
            return true
        }

        fun saveFile(f: File) {
            val out = f.outputStream()
            out.write(MagicNumber)
            val textbytes = MainView.editText.text.toByteArray(Charsets.UTF_8)
            out.write(textbytes)
            out.close()
        }
    }
}