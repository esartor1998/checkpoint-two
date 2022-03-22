import absyn.*;

import java.util.HashMap;
import java.util.ArrayList;

/*
indent(level);
builder.append(expr.name + ": " + node.def.type.getTypeName() + "\n");*/

public class SemanticAnalyzer implements AbsynVisitor {

	final static int SPACES = 4;

	final static int SIMPLE = 0;
	final static int ARRAY = 1;
	final static int FUNCTION = 2;
	public HashMap<String, ArrayList<NodeType>> table;
	public static StringBuilder builder;

	public SemanticAnalyzer(StringBuilder lol) {
		this.table = new HashMap<String, ArrayList<NodeType>>(); //I WILL NEVER STRAY FROM THE PLAN OF FEI
		this.builder = lol; 								     //OOOHHHH IM VISITINGGGGG
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
		while(expr != null) {
			if (expr.head != null) {
				expr.head.accept(this, level);
			}
			expr = expr.tail;
		}
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

	public void visit(BlockExpr expr, int level) {
		if (expr.decls != null || expr.exprs != null) {
			level++;
		} //we scopin. 
		if (expr.decls != null) {
			expr.decls.accept(this, level);
		}
		if (expr.exprs != null) {
			expr.exprs.accept(this, level);
		}

		//print all the relevant table info here because we have reached the end of the scope?
		final int currentLevel = level;
		this.table.forEach((k, v) -> {
			v.forEach((node) -> {
				if (node.level == currentLevel) {
					if (node.def instanceof FunctionDecl) {
						FunctionDecl javasucks = (FunctionDecl)(node.def);
						indent(currentLevel);
						StringBuilder paramMaker = new StringBuilder();
						VarDeclList p = javasucks.params;
						while(p != null) {
							if (p.head != null) {
								paramMaker.append(p.head.type.getTypeName() + ", ");
							}
							p = p.tail;
						}
						String paramTypes = paramMaker.substring(0, Math.max(paramMaker.length() - 2, 0)); //to remove the extra ", " but not provide substring with a bad arg
						builder.append(node.name + ": (" + paramTypes + ") ->" + javasucks.result.getTypeName() + "\n");
					}
					else if (node.def instanceof ArrayDecl) {
						ArrayDecl javabad = (ArrayDecl)(node.def);
						indent(currentLevel);
						String sizeStr;
						if (javabad.size == null) {
							sizeStr = "";
						}
						else {
							sizeStr = Integer.toString(javabad.size.value);
						}
						builder.append(node.name + "[" + sizeStr + "]: " + javabad.type.getTypeName() + "\n"); //FIXME: uh?
						//should only be an integer really, but hehehehehehehe
					}
					else if (node.def instanceof SimpleDecl) {
						indent(currentLevel);
						SimpleDecl javajank = (SimpleDecl)(node.def); //still need to downcast because fuck me ig
						System.out.println(Integer.toString(javajank.row) + ", " + Integer.toString(javajank.column));
						builder.append(node.name + ": " + javajank.type.getTypeName() + "\n");
						//but really, should only be an integer, so...
					}
					else {
						System.err.println("The definition of node " + node.name + " is invalid or empty!");
						//then shit your pants ig
					}
				}
			});
		});
		level--;
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
		//level++? here? no... cuz a function has a blockscope, right?
		//anyways too bad the inheritance is fucked so i cant do a nice get from table
		ArrayList lawl = this.table.get(expr.func);
		if (lawl == null) {
			//TODO: handle undef, param mismatch, etc
		} else {
			NodeType jank = (NodeType)lawl.get(0); //since function redefintions arent allowed, just take the first one.
			//check assign etc
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
		if (expr.params != null) {
			expr.params.accept(this, level);
		}
		if (expr.body != null){
			expr.body.accept(this, level);
		}
		//TODO: type and err checking.
	}


	public void visit(SimpleVar expr, int level) {
		//TODO: type checking and error checking. check if it is in the lookup table
	}

	public void visit(IndexVar expr, int level) { //printing the size of the array will be a fuckfest
		expr.index.accept(this, level);
		//TODO: type echecking and error checking
	}

	public void visit(Type expr, int level) {
		//when the superclass is abstract
	}
}