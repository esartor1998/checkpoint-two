/*
  Created by: Eric S & THeodore O
  File Name: Main.java
  To Build: 
  After the scanner, cminus.flex, and the parser, cminus.cup, have been created.
	javac Main.java
  
  To Run: 
	java -cp /usr/share/java/cup.jar:. Main [args] [1-5].cm

  where 1 through 5 .cm are test files for the C minus language.
*/
   
import java.io.*;
import absyn.*;

class CM {
	public static boolean SHOW_TREE = false;
	static public void main(String argv[]) {    
		/* Start the parser */
		for (String arg : argv) {
			if (arg.equals("-a")) {
				SHOW_TREE = true;
			}
		} //get arg(s)

		try {
			parser p = new parser(new Lexer(new FileReader(argv[argv.length - 1])));
			Absyn result = (Absyn)(p.parse().value);     
			if (SHOW_TREE && result != null) {
				StringBuilder hack = new StringBuilder();
				ShowTreeVisitor visitor = new ShowTreeVisitor(hack);
				result.accept(visitor, 0);
				FileWriter publicAwareStaticNotStaticReallyStringFactoryProducingHumanReadableOutput = new FileWriter(new File(argv[argv.length - 1].substring(0,argv[argv.length - 1].length() - 3) + ".abs")); //did this on purpose
				publicAwareStaticNotStaticReallyStringFactoryProducingHumanReadableOutput.write(hack.toString()); //LOOOOL
				publicAwareStaticNotStaticReallyStringFactoryProducingHumanReadableOutput.close();
			} //i hate "Java"
		} catch (Exception e) {
			/* do cleanup here -- possibly rethrow e */
			System.err.println("Fuck");
			e.printStackTrace();
		}
	}
}