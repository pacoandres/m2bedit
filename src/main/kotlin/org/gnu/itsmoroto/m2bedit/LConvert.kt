package org.gnu.itsmoroto.m2bedit

import org.liblouis.DisplayTable.StandardDisplayTables
import org.liblouis.Louis
import org.liblouis.Translator
import org.liblouis.Typeform
import java.io.File

class NoLouisTableException: Exception ("No Louis table defined")

class LConvert (path: String? = null) {

    companion object {
        var mtables: ArrayList<LouisTables> = ArrayList<LouisTables>()
    }
    init {

        if (path != null) {
            val fp: File = File(path)
            Louis.setLibraryPath(fp)
        }
        getLanguages()
    }

    private var mtranslator: Translator? = null
    private var mtypefomrs: Set<Typeform>? = null
    fun setTable (table: String){
        mtranslator = Translator ("braille-patterns.cti,$table")
        if (mtranslator != null)
            mtypefomrs = mtranslator?.supportedTypeforms
    }
    fun getTypeformNames (): Array<String>{
        val ret = Array<String> (mtypefomrs!!.size) {""}
        var i = 0
        for (t in mtypefomrs){
            ret[i] = t.name
            i++
        }
        return ret
    }

    private fun getTypeform (type: TextPart.TextTypes): Typeform? {
        if (mtypefomrs != null) {
            for (t in mtypefomrs){
                if (t.name == type.louisname)
                    return t
            }
        }
        return null
    }

    val typestocheck: Set<TextPart.TextTypes> = setOf(TextPart.TextTypes.Italic,
        TextPart.TextTypes.Underline, TextPart.TextTypes.Bold)
    fun convert (text: String, format: Int): String{
        //braille-patterns must be explicit or doesn't work
        var tmp: Typeform?=null
        if (mtranslator == null){
            throw NoLouisTableException ()
            return ""
        }
        val stringsize = text.codePoints().toArray().size

        var typeformcc = Typeform.PLAIN_TEXT
        for (tc in typestocheck){
            if ((format and tc.id) != 0){
                tmp = getTypeform(tc)
                if (tmp != null)
                    typeformcc.add(tmp)
            }
        }

        val tforms: Array<Typeform> = Array<Typeform>(stringsize){
            typeformcc}


        if (text == "")
            return ""
        return mtranslator!!.translate(text, tforms, null, null, StandardDisplayTables.DEFAULT).braille
    }

    private fun getLanguages (){
        val tables = Louis.listTables()
        for (table in tables){
            val lan = table.info.get("language")
            if (lan.isEmpty())
                continue
            val uc = table.info.get("unicode-range")
            if (uc != null && uc == "ucs4")
                continue
            val gr = table.info.get("grade")
            if (gr != null && gr != "1")
                continue
            val con = table.info.get("contraction")
            if (con == null || con != "no") //Only grade 1, uncontracted and regular unicode.
                continue

            val reg = table.info.get("region")
            //print(lan)
            val desc = table.info.get("display-name")
            val dn = table.info.get("index-name")
            if (dn.isNotEmpty()) {
                //print("\t" + dn)
                mtables.add(LouisTables(lan, dn, table.identifier, reg, desc))
            }
            else
                mtables.add(LouisTables(lan, "", table.identifier, reg, desc))
            /*val tp = table.info.get("type")
            if (tp.isNotEmpty())
                print ("\t" + tp)

            if (gr != null)
                print ("\t" + gr)
            else
                print ("\t00")*/
            //println ("\t" + table.identifier)
        }
        mtables = mtables.sortedWith(compareBy { it.IndexName }).toList() as ArrayList<LouisTables>
        /*for (t in mtables){
            println (t.LocaleName + "\t" + t.TablePath)
        }*/
    }
}