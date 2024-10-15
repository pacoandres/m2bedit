# Introducción
La idea de desarrollar esta aplicación surge de las complicaciones que se presentan cuando hay
que dar clase de matemáticas de secundaria a personas que necesitan material en braille.

El objeto de esta aplicación es proporcionar una manera (relativamente) sencilla y rápida de crear hojas de 
ejercicios, apuntes y exámenes y traducirlos a braille de manera automática tanto en 
Windows como en Linux.  
No es una aplicación para edición profesional o académica de textos matemáticos en braille. Para
eso ya existen aplicaciones como [Duxury DBT](https://www.duxburysystems.com/) (que es bastante
cara) o [Edico de la ONCE](https://educacion.once.es/recursos-documentacion/edico) (que tiene
una curva de aprendizaje importante y solo funciona en Windows). Esta es una aplicación para crear
rápidamente material sencillo de matemáticas en braille para personas que están en secundaria.

# Dependencias
Esta aplicación se basa fundamentalmente en tres bibliotecas de código abierto:

  - Liblouis: una biblioteca para traducir textos a braille, y viceversa, con gran cantidad de idiomas y
funcionalidades: [Sitio de Liblouis](https://liblouis.io/)

  - Snuggletex: una biblioteca para traducir LaTex a Mathml:
    [Código de Snuggletex](https://github.com/davemckain/snuggletex)

  - MathCat: una biblioteca que convierte Mathml en braille, con la posibilidad de elegir entre varias
codificaciones: [Sitio de MathCat](https://nsoiffer.github.io/MathCAT/)

Además de estas bibliotecas se hace uso de otras muchas, y otro tipo de software,
que no se van a nombrar por la extensión que requeriría.

Mi agradecimiento a las personas que trabajan en todos estos desarrollos: todo lo que
funciona en esta aplicación es gracias a ellas. Los errores de funcionamiento que se produzcan
son exclusivamente míos.

# Compilación
Si no has desarrollado nunca lo más sencillo es descargar la aplicación desde la sección de 
[releases](https://github.com/pacoandres/m2bedit/releases), pero si
te sientes con ganas de aventura solo necesitas [instalar Maven](https://maven.apache.org/install.html) 
para poder compilar la aplicación.

Si tienes experiencia en desarrollo no tendrás problema para compilarla, solo hace falta Maven.

# Problemas, mejoras, consejos, ...
Si tienes algo que contar acerca de la aplicación puedes hacerlo en la
[sección correspondiente](https://github.com/pacoandres/m2bedit/issues)
