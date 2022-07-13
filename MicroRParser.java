import java.io.*;

public class MicroRParser {
	private int n = -1;

	private static final int MAX_TOKENS = 1000;

	private Token [] token = new Token [MAX_TOKENS];

	private MicroRLexer lexer;

	public MicroRParser (java.io.Reader readin) {
		this . lexer = new MicroRLexer (readin);
	}

	public boolean start () throws IOException {
		if(!this . program ())
			return false;

		return true;
	}

	private void getToken () throws IOException {
		if(n < MAX_TOKENS)
			token [++n] = lexer . nextToken ();
		else {
			ErrorMessage . print ("Maximum number of tokens exceeded");
			System . exit(1);
		}
	}

	private boolean program () throws IOException {
		Node funcHead; //setting up the tree head for printing
		String funcName; //this is to save the id of the function's name

		this . getToken ();
		if (token [n] . symbol () != Symbol . SOURCE)
			return false;

		this . getToken ();
		if (token [n] . symbol () != Symbol . LPARA)
			return false;

		this . getToken ();
		if (token [n] . symbol () != Symbol . LISTR)
			return false;

		this . getToken ();
		if (token [n] . symbol () != Symbol . RPARA)
			return false;

		this . getToken();
		while (token [n] . symbol () == Symbol . ID) {
			funcName = token [n] . lexeme (); //setting the functions name for later use
			funcHead = this . function_def (); //setting the head to the outputted tree of the function
			if(funcHead == null) //checking the tree head for a tree
				return false;
			else {
				System . out . println ("Abstract Syntax Tree for" + funcName + "\n");
				funcHead . print ();
			}

			this . getToken ();
		}

		//System . out . println (token [n] . lexeme ());
		funcName = token [n] . lexeme (); // same thing as with functions, but for main
		funcHead = this . main_def ();

		if(funcHead == null)
			return false;
		else {
			//System . out . println (funcHead.getClass().getName());
			System . out . println ("\nAbstract Syntax Tree for " + funcName);
			funcHead . print ();
		}

		return true;
	}

	private Node function_def () throws IOException {
		Node funcHead = null, tempNode; //assigning the head node to a new node to make children
		Node curr;

		//this is the function's normal parsing functionality
		{
		this . getToken ();
		if (token [n] . symbol () != Symbol . ASSIGN)
			return null;

		this . getToken ();
		if (token [n] . symbol () != Symbol . FUNC)
			return null;

		this . getToken ();
		if (token [n] . symbol () != Symbol . LPARA)
			return null;

		this . getToken ();
		if (token [n] . symbol () == Symbol . ID) {
			this . getToken ();

			while (token [n] . symbol () == Symbol . COMMA) {
				this . getToken ();
				if (token [n] . symbol () != Symbol . ID)
					return null;

				this . getToken ();
			}
		}

		if (token [n] . symbol () != Symbol . RPARA)
			return null;

		this . getToken ();
		if (token [n] . symbol () != Symbol . LBRACE)
			return null;
		}

		//modified code for parse tree
		this . getToken ();
		while (token [n] . symbol () == Symbol . IF || token [n] . symbol () == Symbol . WHILE ||
				token [n] . symbol () == Symbol . ID || token [n] . symbol () == Symbol . PRINT) {
			curr = this . statement ();
			if (curr == null)
				return null;
			/*
			System . out . println ("\n");
			curr . print();
			System . out . println ("\n");
			*/
			if (funcHead == null)
				funcHead = curr;
			else {
				tempNode = new Node();
				tempNode . l = funcHead;
				tempNode . r = curr;
				funcHead = tempNode;
			}
		}

		tempNode = new Node();
		tempNode . l = funcHead;
		funcHead = tempNode;

		if (token [n] . symbol () != Symbol . RETURN)
			return null;

		this . getToken ();
		if (token [n] . symbol () != Symbol . LPARA)
			return null;

		this. getToken ();
		if(funcHead == null) {
			funcHead = new ReturnNode();
			funcHead . r = this . expr();
			if(funcHead . r == null)
				return null;
		} else {
			tempNode =  new ReturnNode();
			tempNode . r = this . expr();
			funcHead . r = tempNode;
			/*
			System . out . println ("\n");
			tempNode . print();
			System . out . println ("\n");

			System . out . println ("\n");
			funcHead . r . print();
			System . out . println ("\n");

			System . out . println ("\n");
			funcHead . print ();
			System . out . println ("\n");
			*/
			if(tempNode . r == null)
				return null;
		}

		//this . getToken ();
		if (token [n] . symbol () != Symbol . RPARA)
			return null;

		this . getToken ();
		if (token [n] . symbol () != Symbol . SEMICOLON)
			return null;

		this . getToken ();
		if (token [n] . symbol () != Symbol . RBRACE)
			return null;

		return funcHead;
	}

	private Node main_def () throws IOException {
		Node mainHead;

		if (token [n] . symbol () != Symbol . MAIN)
			return null;

		this . getToken ();
		if (token [n] . symbol () != Symbol . ASSIGN)
			return null;

		this . getToken ();
		if (token [n] . symbol () != Symbol . FUNC)
			return null;

		this . getToken ();
		if (token [n] . symbol () != Symbol . LPARA)
			return null;

		this . getToken ();
		if (token [n] . symbol () != Symbol . RPARA)
			return null;

		this . getToken ();
		if (token [n] . symbol () != Symbol . LBRACE)
			return null;

		this . getToken ();
		mainHead = this . statement_list ();
		if (mainHead == null)
			return null;

		if (token [n] . symbol () != Symbol . RBRACE)
			return null;

		return mainHead;
	}

	private Node statement_list () throws IOException {
		Node stmtNode, tempNode;
		Node curr;

		stmtNode = this . statement ();
		if (stmtNode == null)
			return null;

		while (token [n] . symbol () == Symbol . IF || token [n] . symbol () == Symbol . WHILE ||
				token [n] . symbol () == Symbol . ID || token [n] . symbol () == Symbol . PRINT) {
			curr = this . statement ();
			//System . out . println (stmtNode.getClass().getName());
			if (curr == null)
				return null;

			tempNode = new Node();
			tempNode . l = stmtNode;
			tempNode . r = curr;
			stmtNode = tempNode;
			//System . out . println (stmtNode.getClass().getName());
		}

		return stmtNode;
	}

	private Node statement () throws IOException {
		Node stmtNode;

		if (token [n] . symbol () == Symbol . IF) {
			stmtNode = new IfNode();

			this . getToken ();
			if (token [n] . symbol () != Symbol . LPARA)
				return null;

			this . getToken ();
			stmtNode . l = this . cond ();
			if (stmtNode . l == null)
				return null;

			if (token [n] . symbol () != Symbol . RPARA)
				return null;

			this . getToken ();
			if (token [n] . symbol () != Symbol . LBRACE)
				return null;

			this . getToken ();
			stmtNode . r = this . statement_list ();
			if (stmtNode . r == null)
				return null;

			if (token [n] . symbol () != Symbol . RBRACE)
				return null;

			this . getToken ();
			if  (token [n] . symbol () == Symbol . ELSE) {
				this . getToken ();
				if (token [n] . symbol () != Symbol . LBRACE)
					return null;

				this . getToken ();
				stmtNode . e = this . statement_list ();
				if (stmtNode . e == null)
					return null;

				if (token [n] . symbol () != Symbol . RBRACE)
					return null;

				this . getToken();
			}
		} else if (token [n] . symbol () == Symbol . WHILE) {
			stmtNode = new WhileNode();

			this . getToken ();
			if (token [n] . symbol () != Symbol . LPARA)
				return null;

			this . getToken ();
			stmtNode . l = this . cond ();
			if(stmtNode . l == null)
				return null;

			if (token [n] . symbol () !=  Symbol . RPARA)
				return null;

			this . getToken ();
			if (token [n] . symbol () != Symbol . LBRACE)
				return null;

			this . getToken ();
			stmtNode . r = this . statement_list ();
			if (stmtNode . r == null)
				return null;

			if (token [n] . symbol () != Symbol . RBRACE)
				return null;

			this . getToken ();
		} else if (token [n] . symbol () == Symbol . ID) {
			stmtNode = new AssignNode();
			Node idNode = new IdNode();
			//System . out . println (token [n] . lexeme() + "\n");
			idNode . token  = token [n];
			//System . out . println (idNode . token . lexeme() + "\n");
			stmtNode . l = idNode;

			this . getToken ();
			if (token [n] . symbol () != Symbol . ASSIGN)
				return null;

			this . getToken ();
			stmtNode . r = this . expr ();
			if (stmtNode . r == null)
				return null;

			if(token [n] . symbol () != Symbol . SEMICOLON)
 				return null;

			this . getToken ();
		} else if (token [n] . symbol () == Symbol . PRINT) {
			stmtNode = new PrintNode();

			this . getToken();
			if (token [n] . symbol () != Symbol . LPARA)
				return null;

			this . getToken ();
			stmtNode . l = this . expr ();
			if (stmtNode . l == null)
				return null;

			if (token [n] . symbol () != Symbol . RPARA)
				return null;

			this . getToken ();
			if (token [n] . symbol () != Symbol . SEMICOLON)
				return null;

			this . getToken ();
		} else
			return null;

		return stmtNode;
	}

	private Node cond () throws IOException {
		Node condNode, tempNode;
		Node curr;

		condNode = this . and_expr ();
		if (condNode . l == null)
			return null;

		while (token [n] . symbol () == Symbol . COR) {
			this . getToken();
			curr = this . and_expr ();
			if (curr == null)
				return null;

			tempNode = new OrNode();
			tempNode . l = condNode;
			tempNode . r = curr;
			condNode = tempNode;
		}

		return condNode;
	}

	private Node and_expr () throws IOException {
		Node andNode, tempNode;
		Node curr;

		andNode = this . rel_expr ();
		if (andNode == null)
			return null;

		while (token [n] . symbol () == Symbol . CAND) {
			this . getToken ();
			curr = this . rel_expr ();
			if (curr == null)
				return null;

			tempNode = new AndNode();
			tempNode . l = andNode;
			tempNode . r = curr;
			andNode = tempNode;
		}

		return andNode;
	}

	private Node rel_expr () throws IOException {
		Node relNode = new OprNode();

		if (token [n] . symbol () == Symbol . CNOT) {
			relNode . token  = token [n];
			this . getToken ();
		}

		relNode . l = this . expr ();
		if (relNode == null)
			return null;

		if (token [n] . symbol () != Symbol . LT && token [n] . symbol () != Symbol . LE &&
				token [n] . symbol () != Symbol . GT && token [n] . symbol () != Symbol . GE &&
				token [n] . symbol () != Symbol . EQ && token [n] . symbol () != Symbol . NE)
			return null;
		else {
			if (relNode . token != null) {
				relNode . l = relNode;
				relNode . token = token [n];
			} else
				relNode . token = token [n];
		}

		this . getToken ();
		relNode . r = this . expr ();
		if (relNode . r == null)
			return null;

		return relNode;
	}

	private Node expr () throws IOException {
		Node exprNode, tempNode;
		Node curr;
		Token held_token;

		exprNode = this . mul_expr ();
		if (exprNode == null)
			return null;

		while (token [n] . symbol () == Symbol . ADD || token [n] . symbol () == Symbol . SUB) {
			held_token = token [n];

			this . getToken ();
			curr = this . mul_expr ();
			if (curr == null)
				return null;

			tempNode = new OprNode();
			tempNode . l = exprNode;
			tempNode . r = curr;
			tempNode . token = held_token;

			exprNode = tempNode;
		}

		return exprNode;
	}

	private Node mul_expr () throws IOException {
		Node exprNode, tempNode;
		Node curr;
		Token held_token;

		exprNode = this . prefix_expr ();
		if (exprNode == null)
			return null;

		while (token [n] . symbol () == Symbol . MULT || token [n] . symbol () == Symbol . DIV) {
			held_token = token [n];

			this . getToken ();
			curr = this . prefix_expr();
			if (curr == null)
				return null;

			tempNode = new OprNode();
			tempNode . l = exprNode;
			tempNode . r = curr;
			tempNode . token = held_token;

			exprNode = tempNode;
		}

		return exprNode;
	}

	private Node prefix_expr () throws IOException {
		Node exprNode;

		if (token [n] . symbol () == Symbol . ADD || token [n] . symbol () == Symbol . SUB) {
			exprNode = new OprNode();
			exprNode . token  = token [n];
			this . getToken ();

			exprNode . l = this . simple_expr ();
			if (exprNode == null)
				return null;
		} else {
			exprNode = this . simple_expr ();
			if (exprNode == null)
				return null;
		}

		return exprNode;
	}

	private Node simple_expr () throws IOException {
		Node exprNode = null, tempNode;

		if (token [n] . symbol () ==  Symbol . INT) {
			exprNode = new IntNode();
			exprNode . token = token [n];
			this . getToken ();
		} else if (token [n] . symbol () == Symbol . LPARA) {
			exprNode = new Node();

			this . getToken ();
			exprNode = this . expr ();
			if (exprNode == null)
				return null;

			if (token [n] . symbol () != Symbol . RPARA)
				return null;

			this . getToken ();
		} else if (token [n] . symbol () == Symbol . ASINT) {
			exprNode = new ReadNode();

			this . getToken ();
			if (token [n] . symbol () != Symbol . LPARA)
				return null;

			this . getToken ();
			if (token [n] . symbol () != Symbol . READ)
				return null;

			this . getToken ();
			if (token [n] . symbol () != Symbol . LPARA)
				return null;

			for (int i = 0; i < 2; i++) {
				this . getToken ();
				if (token [n] . symbol () != Symbol . RPARA)
					return null;
			}

			this . getToken ();
		} else if (token [n] . symbol () == Symbol . ID) {
			Token held_token = token [n];

			this . getToken ();
			if (token [n] . symbol () == Symbol . LPARA) {
				exprNode = new FuncNode();
				Node curr, paraNode;

				this . getToken ();
				paraNode = this . expr();
				if (paraNode == null)
					return null;

				while (token [n] . symbol () == Symbol . COMMA) {
					this . getToken ();
					curr = this . expr ();
					if (curr == null)
						return null;

					tempNode = new Node();
					tempNode . l = paraNode;
					tempNode . r = curr;
					paraNode = tempNode;
				}

				if (token [n] . symbol () != Symbol . RPARA)
					return null;

				exprNode . token = held_token;
				exprNode . r = paraNode;
				this . getToken ();
			} else {
				exprNode = new IdNode();
				//System . out . println (held_token . lexeme());
				exprNode . token = held_token;
			}
		} else if (token [n] . symbol () == Symbol . CON) {
			exprNode = new FuncNode();
			exprNode . token = token [n];

			this . getToken ();
			if (token [n] . symbol () != Symbol . LPARA)
				return null;

			this . getToken ();
			exprNode . l = this . expr ();
			if (exprNode . l == null)
				return null;

			if (token [n] . symbol () != Symbol . COMMA)
				return null;

			this . getToken ();
			exprNode . r = this . expr();
			if (exprNode . r == null)
				return null;

			if (token [n] . symbol () != Symbol . RPARA)
				return null;

			this . getToken ();
		} else if (token [n] . symbol () == Symbol . HEAD) {
			exprNode = new FuncNode();
			exprNode . token = token [n];

			this . getToken ();
			if (token [n] . symbol () != Symbol . LPARA)
				return null;

			this . getToken ();
			exprNode . r = this . expr ();
			if (exprNode . r == null)
				return null;

			if (token [n] . symbol () != Symbol . RPARA)
				return null;

			this . getToken ();
		} else if (token [n] . symbol () == Symbol . TAIL) {
			exprNode = new FuncNode();
			exprNode . token = token [n];

			this . getToken ();
			if (token [n] . symbol () != Symbol . LPARA)
				return null;

			this . getToken ();
			exprNode . r = this . expr ();
			if (exprNode . r == null)
				return null;

			if (token [n] . symbol () != Symbol . RPARA)
				return null;

			this . getToken ();
		}  else if (token [n] . symbol () == Symbol . NULL) {
			exprNode = new OprNode();
			exprNode . token = token [n];

    	this . getToken ();
      if (token [n] . symbol () != Symbol . LPARA)
              return null;

      this . getToken ();
      if (token [n] . symbol () != Symbol . RPARA)
              return null;

      this . getToken ();
		}

		return exprNode;
	}
}
