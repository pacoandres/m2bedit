# Introdution
The idea for developing this application comes through the difficulties that comes when 
you have to teach high school maths to a person that needs Braille written material. 

The purpose of this application is to help to write math exercises, notes and exams in Braille
quick and, somehow, easy. In Windows and Linux.  
This is not an application for professional or academical edition. There are applications for
doing that, like [Duxury DBT](https://www.duxburysystems.com/) (expensive) or 
[BrailleBlaster](https://brailleblaster.org/) (sloped learning curve). This is an application
for writing simple high school math Braille documents in a quick and easy way.


# Dependencies
  This application is fundamentally based on three open source libraries:

  - Liblouis: a library for translating text to Braille, and vice versa, with
      a lot of languages and functionality: [Sitio de Liblouis](https://liblouis.io/)

  - Snuggletex: a library for LaTex to Mathml translation:
      [Código de Snuggletex](https://github.com/davemckain/snuggletex)

  - MathCat: a library that translates Mathml into Braille, allowing to choose
between multiple codings: [Sitio de MathCat](https://nsoiffer.github.io/MathCAT/)

In addition to these libraries, many other types of software are used,
which will not be named due to the length they would require.

I want to say thanks to the people who work on all these developments: everything that
works in this application is thanks to them. Any operating errors that occur
are exclusively mine.

# Compiling
If you haven't developed ever the easy way is to download the application from the [releases](https://github.com/pacoandres/m2bedit/releases)
section, but if you want to start you only need to 
[install a Maven development environment](https://maven.apache.org/install.html) and get
into the adventure.

If you have development experience you will have no problem compiling it.

# Problems, improvements, requests, ...
If you have something to tell about the application you can write it at
[the issue page](https://github.com/pacoandres/m2bedit/issues)
