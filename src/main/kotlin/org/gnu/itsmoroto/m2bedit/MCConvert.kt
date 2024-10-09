package org.gnu.itsmoroto.m2bedit
//import org.gnu.itsmoroto.cminterface.KtInterface

import com.sun.jna.StringArray
import com.sun.jna.Structure
import onl.mdw.mathcat4j.api.MathCat
import onl.mdw.mathcat4j.api.MathCatJni
import onl.mdw.mathcat4j.core.mathCAT
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.name

class MCConvert (rulesdir: String) {
    //private val inter = KtInterface ()
    private val mrulesdir: String = rulesdir
    companion object {
        var mcodes: ArrayList<String> = ArrayList<String>()
        var mlanguages: ArrayList<String> = ArrayList<String>()
    }

    init {

        /*val directory = javaClass.getResource(rulesdir)
        mrulesdir = Paths.get(directory!!.toURI()).toFile().canonicalPath*/
        mathCAT { (MathCat::setRulesDir) (rulesdir)}
        mathCAT { (MathCat::setPreference) ("Language", "es")}
        mathCAT { (MathCat::setPreference) ("TTS", "none")}
        mathCAT { (MathCat::setPreference) ("BrailleCode", "CMU")}
        //inter.setRulesDir(rulesdir)
        //inter.setPreference ("Language", "es")
        //inter.setPreference ("TTS", "none")
        //inter.setPreference ("BrailleCode", "CMU")
        getCodes()
    }

    fun convert (s: String, mclang: String, mccode: String): String{ //Format <math>..</math>
        if (s == "")
            return ""
        //inter.setMathml(s)
        mathCAT { (MathCat::setPreference) ("Language", mclang)}
        mathCAT { (MathCat::setPreference) ("BrailleCode", mccode)}
        mathCAT { (MathCat::setMathml) (s)}
        //return inter.getBraille("")
        return mathCAT { (MathCat::getBraille) ("")}
    }
    private fun getCodes (){
        var files = Files.walk(Paths.get (mrulesdir, "Braille"), 1)
            .filter {it -> Files.isDirectory(it)}
        for (f in files){
            if (f.name.endsWith("Braille"))
                continue
            mcodes.add(f.name)
        }

        files = Files.walk(Paths.get (mrulesdir, "Languages"), 1)
            .filter {it -> Files.isDirectory(it)}
        for (f in files){
            if (f.name.endsWith("Languages"))
                continue
            mlanguages.add(f.name)
        }
        mcodes.sort()
        mlanguages.sort()
        /*for (c in mcodes){
            println(c)
        }*/
    }
}