package org.gnu.itsmoroto.m2bedit


class UnclosedException (w: Int, l: TextPart.TextTypes):
    Exception (M2BApp.mbrstrings.getString("unclosedlabel")){
        val where = w
        val label = l
    }

class TextPart (start: Int, type: Int){
    enum class TextTypes  (val id: Int, val start: String, val end: String, val louisname: String){
        //First 4 Matching liblouis typeforms, value and name.
        Plain (0, "", "\n", "plain"),
        Italic (1, "\\textit{", "}", "italic"),
        Underline (2, "\\underline{", "}", "underline"),
        Bold (4, "\\textbf{", "}", "bold"),
        Tex1 (8, "$", "$", ""),
        Tex2 (16, "\\[", "\\]", ""),
        FullLine(32, "", "\n", "")
    }

    enum class OutFormat (val extension: String) {
        UTF ("txt"),
        BRF ("brf"),
        BAN ("ban")
    }

    var mstart: Int
    //private var mtext: String = text
    private val mchilds: ArrayList<TextPart> = ArrayList<TextPart> () //Not sure
    //fun render (): String
    var mend: Int
    private var moffset: Int



    companion object {
        @OptIn(ExperimentalUnsignedTypes::class)
        private var brfcode: UByteArray = ubyteArrayOf(
            32u, 65u, 49u, 66u, 39u, 75u, 50u, 76u, 64u, 67u, 73u, 70u, 47u, 77u, 83u, 80u, 34u,
            69u, 51u, 72u, 57u, 79u, 54u, 82u, 94u, 68u, 74u, 71u, 62u, 78u, 84u, 81u, 44u, 42u,
            53u, 60u, 45u, 85u, 56u, 86u, 46u, 37u, 91u, 36u, 43u, 88u, 33u, 38u, 59u, 58u, 52u,
            92u, 48u, 90u, 55u, 40u, 95u, 63u, 87u, 93u, 35u, 89u, 41u, 61u
        )

        @OptIn(ExperimentalUnsignedTypes::class)
        private var oncecode: UByteArray = ubyteArrayOf(
            32u, 97u, 44u, 98u, 46u, 107u, 59u, 108u, 39u, 99u, 105u, 102u, 237u, 109u,
            115u, 112u, 64u, 101u, 58u, 104u, 125u, 111u, 43u, 114u, 94u, 100u, 106u, 103u, 204u, 110u,
            116u, 113u, 95u, 49u, 63u, 50u, 45u, 117u, 60u, 118u, 123u, 51u, 57u, 54u, 243u, 120u, 233u, 38u,
            34u, 53u, 42u, 56u, 62u, 122u, 61u, 225u, 37u, 52u, 119u, 55u, 35u, 121u, 250u, 92u
        )


        private const val escape = '\\'
        private const val newline = '\n'
        private const val ecstart = '$'
        val htmlstring: StringBuilder = StringBuilder ("")
        infix fun from(id: Int): TextTypes? = TextTypes.entries.firstOrNull { it.id == id }
    }
    init {
        mend = -1
        mstart = start
        moffset = 0
        val text = MainView.editText.text
        var tmp: TextTypes = TextTypes.Plain
        for (t in TextTypes.entries){
            /*if (t == TextTypes.FullLine)
                continue*/
            if ((type and t.id) != 0){
                tmp = t
                moffset = t.start.length
                mend = text.indexOf(t.end, start+moffset)
                break
            }
        }
        if (mend == -1 && type > TextTypes.Plain.id && type < TextTypes.FullLine.id){
            throw UnclosedException (start, tmp)
        }
        else if (mend == -1){
            mend = text.length
        }
        else
            mend += from (type)!!.end.length - 1
        mchilds.clear()
    }
    var mtype: Int = type
    private val needles = listOf<Char>(escape, newline, ecstart)
    fun findChilds (){
        var position = mstart+moffset
        val text = MainView.editText.text
        val mytype = TextTypes.entries.first { it.id == mtype }
        if (/*mend == text.length || */(mtype and TextTypes.Tex1.id) != 0
            || (mtype and TextTypes.Tex2.id) != 0)
            return //No possible children

        val poslist: ArrayList<Int> = ArrayList<Int> ()
        while (position < mend - 1){
            poslist.clear()
            var positem = text.substring(position).indexOfFirst { it in needles }
            if (positem == -1)
                break

            positem += position
            if (positem >= mend)
                break

            if (text[positem] == ecstart){
                val nt = TextPart (positem, TextTypes.Tex1.id)
                position = nt.mend + 1
                nt.findChilds()
                mchilds.add(nt)
                continue
            }
            else if (text[positem] == newline && positem <text.length - 1){
                val nt = TextPart (position, TextTypes.FullLine.id)
                //This has no children
                mchilds.add(nt)
                position = positem + 1
                continue
            }
            else {
                val nt: TextPart
                if (text[positem + 1] == ecstart){
                    position = positem + 1
                    continue //it's a escaped $
                }
                else if (text.startsWith(TextTypes.Underline.start, positem))
                    nt = TextPart (positem, TextTypes.Underline.id)
                else if (text.startsWith(TextTypes.Bold.start, positem))
                    nt = TextPart (positem, TextTypes.Bold.id)
                else if (text.startsWith(TextTypes.Italic.start, positem))
                    nt = TextPart (positem, TextTypes.Italic.id)
                else if (text.startsWith(TextTypes.Tex2.start, positem))
                    nt = TextPart (positem, TextTypes.Tex2.id)
                else
                    continue
                nt.findChilds()
                position = nt.mend + from(nt.mtype)!!.end.length
                mend = if (position < text.length && position >= mend)
                    text.indexOf(TextTypes.valueOf(mytype.name).end, position)
                else
                    text.length
                mchilds.add(nt)
            }
        }
        M2BApp.mnodescount += mchilds.size
    }

    fun traceContent (){
        var position = mstart+moffset
        val text = MainView.editText.text
        for (child in mchilds){
            print (text.substring(position, child.mstart))
            if (position < child.mstart)
                println ("\tType ${TextTypes.entries.first { it.id == mtype }}")
            child.traceContent()
            position = child.mend + 1
        }
        if (position < mend){
            print (text.substring(position, mend))
            println ("\tType ${TextTypes.entries.first { it.id == mtype }}")
        }
    }
    val louistypes: Set<Int> = setOf(TextTypes.Plain.id, TextTypes.FullLine.id,
        TextTypes.Underline.id, TextTypes.Bold.id, TextTypes.Italic.id)

    private fun escapeHTML(s: String):String {
        val out = StringBuilder (16.coerceAtLeast(s.length))
        for (c in s) {

            if (c.code > 127 || c == '"' || c == '\'' || c == '<' || c == '>' || c == '&') {
                out.append("&#")
                out.append(c.code)
                out.append(';')
            } else {
                out.append(c)
            }
        }
        return out.toString()
    }

    fun stringTranslate (s: String,
                         toFile: Boolean = true): String{
        var translated:String = String ()
        if (mtype in louistypes){
            if (toFile) {
                translated += M2BApp.lConverter.convert(s, mtype)
                if (mtype == TextTypes.FullLine.id) {
                    translated += "\n"
                }
            }
            else {
                translated = escapeHTML(s)
                if (mtype == TextTypes.FullLine.id)
                    translated += "</p>"
            }
        }
        else{
            //Its latex
            val translate: String = if (s.endsWith('\\'))
                "$" + s.substring(0, s.length - 1) + "$"
            else
                "$$s$"

            var tmp = M2BApp.mmlConverter.convert(this, translate)
            if (toFile) {
                translated += if (mtype == TextTypes.Tex2.id)
                    "\n" +
                            M2BApp.mcConverter.convert(tmp) + "\n"
                else
                    M2BApp.mcConverter.convert(tmp.replace('*', '·'))
            }
            else {
                if (mtype == TextTypes.Tex2.id)
                    translated += "<p style=\"text-align: center;\">$tmp</p>"
                else
                    translated = tmp
            }
        }
        return translated
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    fun convertCodec (s: String, fmt: OutFormat): ByteArray {
        val ch = s.toCharArray()
        val outarray = UByteArray (ch.size)
        var i = 0
        for (c in ch){
            if (c == '\n'){
                outarray[i] = '\n'.code.toUByte()
            }
            else {
                val c2: Int = 0xFF and c.code //c.toInt() ¿Deprecated?
                if (fmt == OutFormat.BAN)
                    outarray[i] = oncecode[c2]
                else
                    outarray[i] = brfcode[c2]
            }
            i++
        }

        return outarray.toByteArray()
    }
    @OptIn(ExperimentalUnsignedTypes::class)
    fun <T> b2File (writer: (p: T)-> Unit, fmt: OutFormat){
        var position = mstart+moffset
        val text = MainView.editText.text
        var translate: String = ""
        var translated: String = ""
        for (child in mchilds){
            translate = text.substring(position, child.mstart)
            translated = stringTranslate(translate)
            if (fmt == OutFormat.UTF)
                writer(translated as T)
            else{
                writer(convertCodec(translated, fmt) as T)
            }

            child.b2File(writer, fmt)
            position = child.mend + 1
        }
        if (position < mend){
            translate = text.substring(position, mend)
            translated += stringTranslate(translate)
            if (fmt == OutFormat.UTF)
                writer(translated as T)
            else{
                writer(convertCodec(translated, fmt) as T)
            }
        }
    }

    fun toHtml (){
        var position = mstart+moffset
        val text = MainView.editText.text
        var translate: String = ""
        var translated: String = ""
        for (child in mchilds){
            translate = text.substring(position, child.mstart)
            translated = stringTranslate(translate,  false)
            if ((htmlstring.toString().isEmpty() ||
                htmlstring.endsWith("</p>")) && translated.isNotEmpty()
            )
                htmlstring.append("<p>")
            htmlstring.append(translated)

            child.toHtml()
            position = child.mend + 1
        }
        if (position < mend){
            translate = text.substring(position, mend)
            translated += stringTranslate(translate, false)
            if ((htmlstring.toString().isEmpty() ||
                htmlstring.endsWith("</p>")) && translated.isNotEmpty()
            )
                htmlstring.append("<p>")
            htmlstring.append(translated)
        }
    }
}