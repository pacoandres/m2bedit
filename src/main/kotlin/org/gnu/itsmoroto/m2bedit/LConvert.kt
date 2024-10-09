package org.gnu.itsmoroto.m2bedit

import org.liblouis.DisplayTable.StandardDisplayTables
import org.liblouis.Louis
import org.liblouis.Translator
import org.liblouis.Typeform
import java.io.File



public class LConvert (path: String? = null) {

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
    public fun convert (text: String, format: Int, table: String): String{
        //braille-patterns must be explicit or doesn't work
        val t: Translator = Translator ("braille-patterns.cti,"+table)
        val stringsize = text.codePoints().toArray().size

        var typeformcc = Typeform.PLAIN_TEXT
        if ((format and TextPart.TextTypes.Italic.id) != 0)
            typeformcc.add(Typeform.ITALIC_TEXT)
        if ((format and TextPart.TextTypes.Bold.id) != 0)
            typeformcc.add(Typeform.BOLD_TEXT)
        if ((format and TextPart.TextTypes.Underline.id) != 0)
            typeformcc.add(Typeform.UNDERLINED_TEXT)

        val tforms: Array<Typeform> = Array<Typeform>(stringsize){
            typeformcc}


        if (text == "")
            return ""
        return t.translate(text, tforms, null, null, StandardDisplayTables.DEFAULT).braille
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

            //print(lan)
            val dn = table.info.get("index-name")
            if (dn.isNotEmpty()) {
                //print("\t" + dn)
                mtables.add(LouisTables(lan, dn, table.identifier))
            }
            else
                mtables.add(LouisTables(lan, "", table.identifier))
            /*val tp = table.info.get("type")
            if (tp.isNotEmpty())
                print ("\t" + tp)

            if (gr != null)
                print ("\t" + gr)
            else
                print ("\t00")*/
            //println ("\t" + table.identifier)
        }
        mtables = mtables.sortedWith(compareBy { it.LocaleName }).toList() as ArrayList<LouisTables>
        /*for (t in mtables){
            println (t.LocaleName + "\t" + t.TablePath)
        }*/
    }
}