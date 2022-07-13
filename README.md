# mirco-R-parser
This project is a parse tree generator for programs that use a toy R BNF grammar. The project exemplifies my skills at creating a parser from scratch. The lexical analyzer was created using JFlex.

This project was completed for my programming languages course on October 10, 2021, and I created a compiler using JFlex and CUP in the following year for my compilation methods course.

## Structure
This parser was created using JFlex as a lexical analyzer. Then I parse using the BNF grammar defined by the professor. <br>
The parser then generates a parse tree in prefix notation. <br>
The goal was to eventually create a Mirco R interpreter by converting the parse tree to Lisp.
