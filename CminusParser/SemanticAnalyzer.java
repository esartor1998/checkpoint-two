import absyn.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator; //because .forEach was a worse mistake than the mars orbiter

public class SemanticAnalyzer implements AbsynVisitor {

	final static int SPACES = 4;
	final static int SIMPLE = 0;
	final static int ARRAY = 1;
	final static int FUNCTION = 2;

	final static int NOTFOUND = -1;
	final static int REDEFIND = 0; //on redefined
	final static int VERIFIED = 1;

	public boolean hasReturned = false;
	public Type functionType = null; //to figure out if we're in a function, and what return type it should have
	public HashMap<String, ArrayList<NodeType>> table;
	public static StringBuilder builder;

	public SemanticAnalyzer(StringBuilder lol) {
		this.table = new HashMap<String, ArrayList<NodeType>>(); //I WILL NEVER STRAY FROM THE PLAN OF FEI
		this.builder = lol; 								     //OOOHHHH IM VISITINGGGGG
		int[] startOfProg = {0, 0};
		NodeType inputNode = new NodeType("input", 
			new FunctionDecl(startOfProg, 
				new Type(startOfProg, Type.INT), "input",
					new VarDeclList(null, null),
				new BlockExpr(startOfProg, null, null)),
			0);
		NodeType outputNode = new NodeType("output", 
			new FunctionDecl(startOfProg, 
				new Type(startOfProg, Type.VOID), "output", 
					new VarDeclList(new SimpleDecl(startOfProg, new Type(startOfProg, Type.INT), "x"), null), 
				new BlockExpr(startOfProg, null, null)),
			0);
		ArrayList<NodeType> inputForTable = new ArrayList<NodeType>();
		ArrayList<NodeType> outputForTable = new ArrayList<NodeType>();
		inputForTable.add(inputNode);
		outputForTable.add(outputNode);
		this.table.put("input", inputForTable);
		this.table.put("output", outputForTable);
	}

	public HashMap<String, FunctionDecl> pain = new HashMap<String, FunctionDecl>();

	private void printerr(int row, int col, String msg) {
		System.err.println("Error on line " + Integer.toString(row) + ", col " + Integer.toString(col) + ": " + msg);
	}

	private int inclevel(int level) {
		//testindent(level, "increasing level from " + Integer.toString(level));
		return level+1;
	}
	private int declevel(int level) {
		//testindent(level, "decreasing level from " + Integer.toString(level));
		return level-1;
	}

	private void testindent(int level, String msg) {
		printindent(level);
		System.out.println(msg);
	}

	private void fileprint(int level, String msg) {
		indent(level);
		this.builder.append(msg + "\n");
	}

	private void printindent(int level) {
		for(int i = 0; i < level * SPACES; i++) System.out.print(" ");
	}

	private void indent(int level) {
		for(int i = 0; i < level * SPACES; i++) builder.append(" ");
	}

	private void push(NodeType toPush) { //if only the absyn stored relevant info! 🙂
		ArrayList holysht = new ArrayList<NodeType>(); 
		holysht.add(toPush);
		ArrayList scuffed = this.table.put(toPush.name, holysht);
		if (scuffed != null) { //if hashmap.put returns a value other than null, then that value already existed in the map.
			scuffed.add(toPush);
		} //thats why i named this variable "scuffed". thank u so much for this horrid plan
	}

	private void analyze(int currentLevel) { //is this what you wanted fei. is this what you wanted your students to go through?
		Iterator<ArrayList<NodeType>> hashMapIterator = this.table.values().iterator();
		while(hashMapIterator.hasNext()) {
			boolean toRemove = false;
			ArrayList<Integer> removalList = new ArrayList<Integer>();
			ArrayList<NodeType> currentID = hashMapIterator.next(); 
			Iterator<NodeType> nodeIterator = currentID.iterator();
			while(nodeIterator.hasNext()) {
				NodeType node = nodeIterator.next();
				//testindent(currentLevel, "node: " + node.name + ", nodelevel: " + Integer.toString(node.level) + " vs currentLevel: " + Integer.toString(currentLevel)); 
				if (node.level == currentLevel) {
					toRemove = true;
					if (node.def instanceof FunctionDecl) {
						FunctionDecl javasucks = (FunctionDecl)(node.def);
						pain.put(node.name, javasucks);
					}
					else if (node.def instanceof ArrayDecl) {
						ArrayDecl javabad = (ArrayDecl)(node.def);
						String sizeStr;
						if (javabad.size == null) {
							sizeStr = "";
						}
						else {
							sizeStr = Integer.toString(javabad.size.value);
						}
						testindent(currentLevel, node.name + "[" + sizeStr + "]: " + javabad.type.getTypeName() ); //FIXME: uh?
						//should only be an integer really, but hehehehehehehe
					}
					else if (node.def instanceof SimpleDecl) {
						SimpleDecl javajank = (SimpleDecl)(node.def); //still need to downcast because fuck me ig
						testindent(currentLevel, node.name + ": " + javajank.type.getTypeName());
					}
					else {
						System.err.println("The definition of node " + node.name + " is invalid or empty!");
						//then shit your pants ig
					} //SOOO UNNECESSARY
				}
			}
			//then, if it was found, remove the element. (this is called at the end of any scope)
			if (toRemove) {
				currentID.remove(currentID.size() - 1); //remove the most recently defined id
			}
		}
	}

	public void visit(ExprList expr, int level) {
		while(expr != null) {
			if (expr.head != null) {
				expr.head.accept(this, level);
			}
			expr = expr.tail;
		}
	}

	public void visit(VarDeclList expr, int level) {
		while(expr != null) {
			if (expr.head != null) {
				expr.head.accept(this, level);
			}
			expr = expr.tail;
		}
	}

	public void visit(DeclList expr, int level) {
		testindent(level, "Entering the global scope");
		level = inclevel(level);
		while(expr != null) {
			if (expr.head != null) {
				expr.head.accept(this, level);
			}
			expr = expr.tail;
		}
		Iterator<FunctionDecl> lmfao = pain.values().iterator();
		while (lmfao.hasNext()) {
			FunctionDecl func = lmfao.next();
			StringBuilder paramMaker = new StringBuilder();
						VarDeclList p = func.params;
						VarDecl endingone = new SimpleDecl(new int[]{99969, 999420}, null, null); //i dont care any more
						while(p != null) {
							if (p.head != null) {
								endingone = p.head;
								if (p.head instanceof ArrayDecl) {
									ArrayDecl downcastingMeme = (ArrayDecl)(p.head);
									paramMaker.append(downcastingMeme.type.getTypeName() + "[], ");
								}
								else {
									SimpleDecl romaRomama = (SimpleDecl)(p.head);
									paramMaker.append(romaRomama.type.getTypeName() + ", ");
								}
							}
							p = p.tail;
						}
						String paramTypes = paramMaker.substring(0, Math.max(paramMaker.length() - ((endingone instanceof SimpleDecl) ? 2 : 4), 0)); //to remove the extra ", " but not provide substring with a bad arg
						
			testindent(level, func.name + ": (" + paramTypes + ") -> " + func.result.getTypeName());
		}
		level = declevel(level);
		testindent(level, "Exiting the global scope");
		//analyze(level);
	}

	public void visit(VarExpr expr, int level) {
		if (expr.variable != null) {
			expr.variable.accept(this, level);
		}
	}

	public void visit(OpExpr expr, int level) {
		expr.left.accept(this, level);
		expr.right.accept(this, level);
		//TODO: error checking
	}

	public void visit(AssignExpr expr, int level) {
		if (expr.lhs != null) expr.lhs.accept(this, level);
		if (!checkIndex(expr.rhs)) {
			printerr(expr.row, expr.column, "value to be assigned is not integer-resolving");
		}
		if (expr.rhs != null) expr.rhs.accept(this, level);
	}

	public void visit(IfExpr expr, int level) {
		expr.test.accept(this, level);
		expr.thenpart.accept(this, level);
		if (expr.elsepart != null) {
			expr.elsepart.accept(this, level);
		}
	}

	public void visit(WhileExpr expr, int level) {
		//TODO: error checking
		if (expr.test != null) {
			expr.test.accept(this, level);
		}
		if (expr.body != null) {
			expr.body.accept(this, level);
		}
	}

	public void visit(BlockExpr expr, int level) { //a nice toString override could REALLY simplify this code but I'm strictly following fei song's advice for the meme
		if (expr.exprs != null || expr.decls != null) {
			//testindent(level, "Entering a block");
			//level = inclevel(level);
			if (expr.decls != null)
				expr.decls.accept(this, level);
			if (expr.exprs != null)
				expr.exprs.accept(this, level);
			//level = declevel(level);
			//testindent(level, "Leaving a block");
			analyze(level);
		}
		//print all the relevant table info here because we have reached the end of the scope
	}

	public void visit(ReturnExpr expr, int level) {
		if (expr.expr != null) {
			expr.expr.accept(this, level);
		}
		if (functionType == null) {
			printerr(expr.row, expr.column, "return statement outside function");
		}
		else if (functionType.type != Type.INT) {
			printerr(expr.row, expr.column, "return statement inside void function");
		} //and i guess we dont have to check if the return type matches cuz theres only two types 💀
		hasReturned = true;
	}

	public void visit(CallExpr expr, int level) {
		ArrayList<NodeType> fsong = this.table.get(expr.func);
		if (fsong != null) {
			NodeType def = fsong.stream().filter(e -> e.name.equals(expr.func)).findFirst().orElse(null);
			if (def == null && !pain.containsKey(expr.func)) {
				printerr(expr.row, expr.column, "function " + expr.func + " is not defined");
			} 
			else {
				int countedArgs = 0;
				ExprList arglist = expr.args;
				ArrayList<Type> types = new ArrayList<Type>(); //they all gotta be int but idc
				if (def != null || pain.containsKey(expr.func)) {
					FunctionDecl downcastingmemeStrikesAgain;
					if (def != null) downcastingmemeStrikesAgain = (FunctionDecl)(def.def);
					else downcastingmemeStrikesAgain = pain.get(expr.func);
					VarDeclList expectedParams = downcastingmemeStrikesAgain.params;
					while(expectedParams != null) {
						if (expectedParams.head != null) {
							types.add(expectedParams.head.type);
						}
						expectedParams = expectedParams.tail;
					}
					while(arglist != null) {
						if (arglist.head != null) {
							countedArgs += 1;
						}
						arglist = arglist.tail;
					} //TODO: check param count and function existence
					if (countedArgs != (types.size())) {
						printerr(expr.row, expr.column, "incorrect number of arguments provided to " + expr.func + " (expected " + Integer.toString(types.size()) + ", got " + Integer.toString(countedArgs));
					}
				}
				else {
					printerr(expr.row, expr.column, "function " + expr.func + " is not defined");
				}
			}
		}
		else {
			printerr(expr.row, expr.column, "function " + expr.func + " is not defined");
		}
		if (expr.args != null) {
			expr.args.accept(this, level);
		}
	}

	private FunctionDecl isFunctionDefined(String id) {
		ArrayList<NodeType> fug = this.table.get(id);
		NodeType def = null;
		if (fug != null) def = fug.stream().filter(e -> e.name.equals(id)).findFirst().orElse(null);
		if (fug != null && def != null) {
			return (FunctionDecl)(def.def);
		}
		else { //we can also check pain
			return pain.get(id);
		}
	}

	private boolean isFunctionDefinedAndIntReturning(String id) {
		FunctionDecl gg = isFunctionDefined(id);
		if (gg != null) {
			if (gg.result.type == Type.INT) {
				return true;
			}
			return false;
		}
		return false;
	}

	public void visit(SimpleDecl expr, int level) {
		if (expr.type.type == Type.VOID) {
			printerr(expr.row, expr.column, "void type variable declaration. Changing to int");
			expr.type.type = Type.INT;
		}
		NodeType thisNode = new NodeType(expr.name, expr, level);
		push(thisNode);
	}

	public void visit(ArrayDecl expr, int level) {
		if (expr.type.type == Type.VOID) {
			printerr(expr.row, expr.column, "void type array declaration. Changing to int");
			expr.type.type = Type.INT;
		}
		//if (checkIndex()); nvm on second thought no need to check index resolves to int cuz its forced to be by syntax
		if (expr.size.value <= 0) {
			printerr(expr.row, expr.column, "invalid size for array, changing to 1");
			expr.size.value = 1;
		}
		NodeType thisNode = new NodeType(expr.name, expr, level);
		push(thisNode);
	}

	public void visit(FunctionDecl expr, int level) { //this mfer got some params n shit ig
		NodeType thisNode = new NodeType(expr.name, expr, level);
		push(thisNode);
		if (expr.body != null){
			testindent(level, "Entering function scope (" + expr.name + ")");
			functionType = expr.result;
			hasReturned = false;
			level = inclevel(level);
			if (expr.params != null) {
				expr.params.accept(this, level);
			}
			expr.body.accept(this, level);
			functionType = null;
			if (!hasReturned && (expr.result.type == Type.INT)) {
				printerr(expr.row, expr.column, "integer-promising function missing a return value");
			}
			level = declevel(level);
			testindent(level, "Leaving function scope (" + expr.name + ")");
			analyze(level);
		}
		//TODO: type and err checking.
	}

	private int ERRNOTFOUND(int row, int col, String offending) {
		if (!pain.containsKey(offending))
			printerr(row, col, "var or function " + offending + " not found or inaccessible.");
		return NOTFOUND;
	}

	private int ERRREDEFINED(int row, int col, String offending) {
		if (!pain.containsKey(offending))
			printerr(row, col, "var or function " + offending + " redefined in an identical scope");
		return NOTFOUND;
	}

	private int determineExistence(Var id) {
		if (id instanceof SimpleVar) {
			SimpleVar fuck = (SimpleVar)id;
			ArrayList<NodeType> tmp = this.table.get(fuck.name);
			if (tmp == null) return ERRNOTFOUND(fuck.row, fuck.column, fuck.name);
			else { //this is so hilariously extra. god i hate this lang
				NodeType match = tmp.stream().filter(e -> e.name.equals(fuck.name)).findFirst().orElse(null);
				if (match == null) return ERRNOTFOUND(fuck.row, fuck.column, fuck.name);
				else return VERIFIED;
			}
		}
		else {
			IndexVar life = (IndexVar)id;
			ArrayList<NodeType> tmp = this.table.get(life.name);
			if (tmp == null) return ERRNOTFOUND(life.row, life.column, life.name);
			else { //this is so hilariously extra. god i hate this lang
				NodeType match = tmp.stream().filter(e -> e.name.equals(life.name)).findFirst().orElse(null);
				if (match == null) return ERRNOTFOUND(life.row, life.column, life.name);
				else return VERIFIED;
			}
		} //instanceofinstanceofinstanceofinstanceofinstanceofinstanceofinstanceofinstanceofinstanceof
	}

	private int determineRedefinition(Var id, int level) {
		int test = determineExistence(id);
		if (test != NOTFOUND) {
			ArrayList<NodeType> tmp = this.table.get(id.name);
			if (tmp != null) {
				ArrayList<Integer> scopes = new ArrayList<Integer>();
				Iterator<NodeType> iter = tmp.iterator();
				while(iter.hasNext()) {
					NodeType curr = iter.next();
					if (scopes.contains(curr.level)) return ERRREDEFINED(id.row, id.column, id.name);
				}
				return VERIFIED;
			}
			else return NOTFOUND;
		} else return test; //this is the worst code i've ever written in my life and i'm blaming fei song
	}

	public void visit(SimpleVar expr, int level) {
		if (expr != null) {
			determineRedefinition(expr, level);
		}
	}

	private boolean checkIndex(Expr expr) {
		if (expr instanceof IntExpr) {
			return true;
		}
		else if (expr instanceof VarExpr) {
			VarExpr haha = (VarExpr)expr;
			ArrayList<NodeType> god = this.table.get(haha.variable.name);
			NodeType doubleErrSkeetAroundUsingDetermineExistence = null;
			if (god != null) doubleErrSkeetAroundUsingDetermineExistence = god.stream().filter(e -> e.name.equals(haha.variable.name)).findFirst().orElse(null);
			if (doubleErrSkeetAroundUsingDetermineExistence != null) {
				return true;
			}
			else return false;
		}
		else if (expr instanceof CallExpr) {
			FunctionDecl called = isFunctionDefined(((CallExpr)(expr)).func);
			if (called != null) {
				return (called.result.type == Type.INT);
			}
			else {
				ERRNOTFOUND(expr.row, expr.column, ((CallExpr)(expr)).func);
				return false;
			}
		}
		else if (expr instanceof OpExpr) {
			return checkIndex(((OpExpr)expr).left) && checkIndex(((OpExpr)expr).right);
		}
		return false;
	}

	public void visit(IndexVar expr, int level) { //printing the size of the array will be a fuckfest
		int isntRedef = determineRedefinition(expr, level);
		if (expr.index != null) {
			if(!checkIndex(expr.index)) {
				printerr(expr.row, expr.column, "non-integer-resolving array dereference");
			}
			expr.index.accept(this, level);
		}
	}

	public void visit(Type expr, int level) {
		//when the superclass is abstract
	}
	
	public void visit(NilExpr expr, int level) {
		//when the expr is Nil
	}

	public void visit(IntExpr expr, int level) {
		//when the expr is Int
	}
}