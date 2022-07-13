import java.io.*;

public class MicroRTree {
	public static void main (String [] args) throws IOException {
		MicroRParser parser = new MicroRParser (new InputStreamReader (System . in));


		System . out . println ("Source Program");
		System . out . println ("--------------");
		System . out . println ();

		/*
		n = -1;
		do {
			if (n < MAX_TOKENS)
				token [++n] = lexer . nextToken ();
			else
				ErrorMessage .	print ("Maximum number of tokens exceeded");
		} while (token [n] . symbol () != Symbol.EOF);
		*/

		if (!parser . start())
			System . out . println ("\nParse Fail!");
		else
			System . out . println ("\nParse Successful!");
	}
}
