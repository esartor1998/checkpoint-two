/*
  Created By: Eric S & Theodore O
  File Name: cminus.flex
  To Build: jflex cminus.flex

  and then after the parser is created
	 javac Lexer.java
*/
	
/* --------------------------Usercode Section------------------------ */
	
import java_cup.runtime.*;
		
%%
	
/* -----------------Options and Declarations Section----------------- */
	
/* 
	The name of the class JFlex will create will be Lexer.
	Will write the code to the file Lexer.java. 
*/
%class Lexer

%eofval{
  return null;
%eofval};

/*
  The current line number can be accessed with the variable yyline
  and the current column number with the variable yycolumn.
*/
%line
%column
	 
/* 
	Will switch to a CUP compatibility mode to interface with a CUP
	generated parser.
*/
%cup
	
/*
  Declarations
	
  Code between %{ and %}, both of which must be at the beginning of a
  line, will be copied letter to letter into the lexer class source.
  Here you declare member variables and functions that are used inside
  scanner actions.  
*/
%{   
	/* To create a new java_cup.runtime.Symbol with information about
	the current token, the token will have no value in this
	case. */
	private Symbol symbol(int type) {
		return new Symbol(type, yyline, yycolumn);
	}
	 
	/* Also creates a new java_cup.runtime.Symbol with information
	about the current token, but this object has a value. */
	private Symbol symbol(int type, Object value) {
		return new Symbol(type, yyline, yycolumn, value);
	}
%}
	
/* A identifier integer is a word beginning a letter between A and
Z, a and z, or an underscore followed by zero or more letters
between A and Z, a and z, zero and nine, or an underscore. */

//defined in the spec as such. not very robust...
num = [0-9]+
//id defined as follows, except without the shorthand
id = [_a-zA-Z][\w]*
comment = \/\*[^\*]*[^\/]*\*\/
%state COMMENT
%%
/* ------------------------Lexical Rules Section---------------------- */
	
/*This section contains regular expressions and actions, i.e. Java
code, that will be executed when the scanner matches the associated
regular expression. */
<YYINITIAL> {
	[\s\n\r]+            { /* skip whitespace */ }   
	"if"                 { return symbol(sym.IF); }
	"else"               { return symbol(sym.ELSE); }
	"while"              { return symbol(sym.WHILE); }
	"return"             { return symbol(sym.RETURN); }
	"int"                { return symbol(sym.INT); }
	"void"               { return symbol(sym.VOID); }
	">="                 { return symbol(sym.GEQ); }
	"<="                 { return symbol(sym.LEQ); }
	"!="                 { return symbol(sym.NEQ); }
	"=="                 { return symbol(sym.EQ); }
	"="                  { return symbol(sym.ASSIGN); }
	"<"                  { return symbol(sym.LT); }
	">"                  { return symbol(sym.GT); }
	"+"                  { return symbol(sym.PLUS); }
	"-"                  { return symbol(sym.MINUS); }
	"*"                  { return symbol(sym.TIMES); }
	"/"                  { return symbol(sym.OVER); }
	","                  { return symbol(sym.COMMA); }
	"("                  { return symbol(sym.LPAREN); }
	")"                  { return symbol(sym.RPAREN); }
	"["                  { return symbol(sym.LBRACK); }
	"]"                  { return symbol(sym.RBRACK); }
	"{"                  { return symbol(sym.LBRACE); }
	"}"                  { return symbol(sym.RBRACE); }
	";"                  { return symbol(sym.SEMI); }
	{num}                { return symbol(sym.NUM, yytext()); }
	{id}                 { return symbol(sym.ID, yytext()); }
	"/*"                 { yybegin(COMMENT); }
	.                    { System.err.println("ERR: unrecognized/unexpected symbol \"" + yytext() + "\" on line " + yyline + " column " + yycolumn);
						return symbol(sym.ERROR); }
}

<COMMENT> {
	"*/" { yybegin(YYINITIAL); }
	[^]  { }
}
