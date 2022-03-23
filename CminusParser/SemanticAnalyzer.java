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

	public ArrayList<String> pain = new ArrayList<String>();

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
						StringBuilder paramMaker = new StringBuilder();
						VarDeclList p = javasucks.params;
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
						pain.add(node.name + ": (" + paramTypes + ") -> " + javasucks.result.getTypeName());
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
		Iterator<String> lmfao = pain.iterator();
		while (lmfao.hasNext()) {
			String xdxd = lmfao.next();
			testindent(level, xdxd);
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
		if (expr.rhs != null) expr.rhs.accept(this, level);
		//TODO: error checking
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
		//TODO: error checking (lots)
	}

	public void visit(CallExpr expr, int level) {
		if (expr.args != null) {
			expr.args.accept(this, level);
		}
		//TODO: this. this is a biggie with many error cases
	}
	
	public void visit(NilExpr expr, int level) {
		//when the expr is Nil
	}

	public void visit(IntExpr expr, int level) {
		//when the expr is Int
	}

	public void visit(SimpleDecl expr, int level) {
		NodeType thisNode = new NodeType(expr.name, expr, level);
		push(thisNode);
		//TODO: type and error? checking
	}

	public void visit(ArrayDecl expr, int level) {
		NodeType thisNode = new NodeType(expr.name, expr, level);
		push(thisNode);
		//TODO: type and error checking. note the thing he says ab void in the guidelines
	}

	public void visit(FunctionDecl expr, int level) { //this mfer got some params n shit ig
		NodeType thisNode = new NodeType(expr.name, expr, level);
		push(thisNode);
		if (expr.body != null){
			testindent(level, "Entering function scope (" + expr.name + ")");
			level = inclevel(level);
			if (expr.params != null) {
				expr.params.accept(this, level);
			}
			expr.body.accept(this, level);
			level = declevel(level);
			testindent(level, "Leaving function scope (" + expr.name + ")");
			analyze(level);
		}
		//TODO: type and err checking.
	}

	private static int ERRNOTFOUND(int row, int col, String offending) {
		System.err.println("Error on " + Integer.toString(row) + ", " + Integer.toString(col) + ": " + offending + " not found or inaccessible.");
		return NOTFOUND;
	}

	private static int ERRREDEFINED(int row, int col, String offending) {
		System.err.println("Error on " + Integer.toString(row) + ", " + Integer.toString(col) + ": " + offending + " redefined in an identical scope.");
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
		//TODO: type checking and error checking. check if it is in the lookup table
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
			String name = ((CallExpr)expr).func;
			ArrayList<NodeType> god = this.table.get(name);
			NodeType jankYe = null;
			if (god != null) jankYe = god.stream().filter(e -> e.name.equals(name)).findFirst().orElse(null);
			if (jankYe == null) { //NOTE: could probably have used determineexistence but it 5am idrc
				ERRNOTFOUND(((CallExpr)expr).row, ((CallExpr)expr).column, name);
				return false;
			}
			else {
				return jankYe.def.type.type == Type.INT;
			}
		}
		else if (expr instanceof OpExpr) {
			return checkIndex(((OpExpr)expr).left) && checkIndex(((OpExpr)expr).right);
		}
		return false;
	}

	public void visit(IndexVar expr, int level) { //printing the size of the array will be a fuckfest
		//TODO: type echecking and error checking
		int isntRedef = determineRedefinition(expr, level);
		if (expr.index != null) {
			checkIndex(expr.index);
			expr.index.accept(this, level);
		}
	}

	public void visit(Type expr, int level) {
		//when the superclass is abstract
	}
}