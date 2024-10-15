package org.gnu.itsmoroto.m2bedit
import uk.ac.ed.ph.snuggletex.SnuggleSession
import uk.ac.ed.ph.snuggletex.SnuggleEngine
import uk.ac.ed.ph.snuggletex.SnuggleInput
import uk.ac.ed.ph.snuggletex.XMLStringOutputOptions

class LatexError (node: TextPart): Exception (M2BApp.mbrstrings.getString("latexerror")){
    val mnode = node
}

class MMLConvert {
    val engine: SnuggleEngine = SnuggleEngine()
    val session: SnuggleSession = engine.createSession()

    fun convert (node: TextPart, s: String, withlabel: Boolean = false): String { //Format $....$
        if (s == "")
            return ""
        val input = SnuggleInput(s)
        session.reset()
        //It looks snuggletex doesn't throw errors on bad latex
        try {
            if (!session.parseInput(input))
                return ""
            val opt = XMLStringOutputOptions()
            opt.isIncludingXMLDeclaration = false
            if (session.errors.isNotEmpty())
                throw LatexError (node)

            val out = session.buildXMLString(opt)
            return if (withlabel)
                out
            else
                out.replace("xmlns=\"http://www.w3.org/1998/Math/MathML\"", "")
        }
        catch (e: Exception){
            throw e
        }
    }
}