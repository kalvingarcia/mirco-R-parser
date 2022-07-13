public class Token {
	private Symbol symbol; //current token
	private String lexeme;
	
	public Token () { }
	
	public Token (Symbol symbol) { this (symbol, null); }
	
	public Token (Symbol symbol, String lexeme) {
		this . symbol = symbol;
		this . lexeme = lexeme;
	} 
	
	public Symbol symbol () { return symbol; }
	
	public String lexeme () { return lexeme; }
	
	public String toString () {
		switch (symbol) {
			case SEMICOLON: return "(punctuation, ;) ";
			case COMMA: return "(punctuation, ,) "; 
			case LBRACE: return "(punctuation, {) ";
			case RBRACE: return "(punctuation, }) ";
			case ASSIGN: return "(operator, <-) ";
			case EQ: return "(operator, ==) ";
			case NE: return "(operator, !=) ";
			case LT: return "(operator, <) ";
			case GT: return "(operator, >) ";
			case LE: return "(operator, <=) ";
			case GE: return "(operator, >=) ";
			case ADD: return "(operator, +) ";
			case SUB: return "(operator, -) ";
			case MULT: return "(operator, *) ";
			case DIV: return "(operator, /) ";
			case COR: return "(operator, ||) ";
			case CAND: return "(operator, &&) ";
			case CNOT: return "(operator, !) ";
			case LPARA: return "(operator, () ";
			case RPARA: return "(operator, )) ";
			case SOURCE: return "(keyword, source) ";
			case LISTR: return "(string, \"List.R\") ";
			case FUNC: return "(keyword, function) ";
			case RETURN: return "(keyword, return) ";
			case MAIN: return "(keyword, main) ";
			case IF: return "(keyword, if) ";
			case ELSE: return "(keyword, else) ";
			case WHILE: return "(keyword, while) ";
			case ASINT: return "(keyword, as.integer) ";
			case READ: return "(keyword, readline) ";
			case PRINT: return "(keyword, print) ";
			case CON: return "(keyword, cons) ";
			case HEAD: return "(keyword, head) ";
			case TAIL: return "(keyword, tail) ";
			case NULL: return "(keyword, null) ";
			case ID: return "(identifier, " + lexeme + ") ";
			case INT: return "(integer, " + lexeme + ") ";
			default:
				ErrorMessage . print ("Unrecognized token");
				return null;
		}
	}
}