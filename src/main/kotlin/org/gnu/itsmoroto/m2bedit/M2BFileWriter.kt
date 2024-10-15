package org.gnu.itsmoroto.m2bedit

import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter

class M2BFileWriter (f: File, fmt: TextPart.OutFormat){
    var fw: PrintWriter? = null
    var sw: FileOutputStream? = null

    init {
        if (fmt == TextPart.OutFormat.UTF)
            fw = f.printWriter()
        else
            sw = f.outputStream()
    }

    fun writeBytes (b: ByteArray){
        if (sw != null)
            sw!!.write(b)
    }

    fun writeString (s: String){
        if (fw != null)
            fw!!.write(s)
    }

    fun close (){
        if (fw != null)
            fw!!.close()
        if (sw != null)
            sw!!.close()
        fw = null
        sw = null
    }

}