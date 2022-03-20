import absyn.*;

public class ShowTreeVisitor implements AbsynVisitor {

	final static int SPACES = 4;
	public static StringBuilder builder;
	public ShowTreeVisitor(StringBuilder lol) {
		this.builder = lol; //such a hack but i dont care. also public cuz everything else in this project is lmao
	}
	private void indent(int level) {
		for(int i = 0; i < level * SPACES; i++) builder.append(" ");
	}

	public void visit(ExprList expr, int level) {
		while(expr != null) {
			if (expr.head != null) {
				expr.head.accept(this, level);
			}
			expr = expr.tail;
		}
	}

	public void visit (VarDeclList expr, int level) {
		while(expr != null) {
			if (expr.head != null) {
				expr.head.accept(this, level);
			}
			expr = expr.tail;
		}
	}

	public void visit (DeclList expr, int level) {
		while(expr != null) {
			if (expr.head != null) {
				expr.head.accept(this, level);
			}
			expr = expr.tail;
		}
	}

	public void visit(FunctionDecl expr, int level) {
		indent(level);
		if (expr.type == null) {
			builder.append("FunctionDecl: " + expr.name + " - void" + "\n"); 
		}
		else 
			builder.append("FunctionDecl: " + expr.name + " - " + expr.type.getTypeName() + "\n"); 
		level++;
		if (expr.params != null) {
			expr.params.accept(this, level);
		}
		if (expr.body != null){
			expr.body.accept(this, level);
		}
	}

	public void visit(AssignExpr expr, int level) {
		indent(level);
		builder.append("AssignExpr:" + "\n");
		level++;
		if (expr.lhs != null) expr.lhs.accept(this, level);
		if (expr.rhs != null) expr.rhs.accept(this, level);
	}

	public void visit(IfExpr expr, int level) {
		indent(level);
		builder.append("IfExpr:" + "\n");
		level++;
		expr.test.accept(this, level);
		expr.thenpart.accept(this, level);
		if (expr.elsepart != null) {
			indent(level);
			level++;
			builder.append("Else:" + "\n");
			expr.elsepart.accept(this, level);
		} //go deeper with the else to make it obvious which if it belongs to
	}

	public void visit(IntExpr expr, int level) {
		indent(level);
		builder.append( "IntExpr: " + expr.value + "\n"); 
	}

	public void visit(OpExpr expr, int level) {
		indent(level);
		builder.append("OpExpr: "); 
		switch(expr.op) {
			case OpExpr.PLUS:
				builder.append(" + ");
				break;
			case OpExpr.MINUS:
				builder.append(" - ");
				break;
			case OpExpr.TIMES:
				builder.append(" * ");
				break;
			case OpExpr.OVER:
				builder.append(" / ");
				break;
			case OpExpr.EQ:
				builder.append(" == ");
				break;
			case OpExpr.NEQ:
				builder.append(" != ");
				break;
			case OpExpr.GEQ:
				builder.append(" >= ");
				break;
			case OpExpr.LEQ:
				builder.append(" <= ");
				break;
			case OpExpr.LT:
				builder.append(" < ");
				break;
			case OpExpr.GT:
				builder.append(" > ");
				break;
			default:
				builder.append("Unrecognized operator at line " + expr.row + " and column " + expr.col);
		}
		builder.append("\n");
		level++;
		expr.left.accept(this, level);
		expr.right.accept(this, level);
	}

	public void visit(VarExpr expr, int level) {
		/*indent(level);
		builder.append("VarExpr: ");
		level++;
		NOTE: i have decided i dont want to print/indent on varexpr because its ugly n i hate it*/

		if (expr.variable != null) {
			expr.variable.accept(this, level);
		}
	}


	public void visit(CallExpr expr, int level) {
		indent(level);
		builder.append("CallExpr: " + expr.func + "\n");
		level++;
		if (expr.args != null) {
			expr.args.accept(this, level);
		}
	}

	public void visit(BlockExpr expr, int level) {
		indent(level);
		builder.append("BlockExpr: " + "\n");
		if (expr.decls != null || expr.exprs != null) {
			level++;
		}

		if (expr.decls != null) {
			expr.decls.accept(this, level);
		}
		if (expr.exprs != null) {
			expr.exprs.accept(this, level);
		}
	}

	public void visit(ReturnExpr expr, int level) {
		indent(level);
		builder.append("ReturnExpr: " + "\n");
		level++;
		if (expr.expr != null) {
			expr.expr.accept(this, level);
		}
	}

	public void visit(WhileExpr expr, int level) {
		indent(level);
		builder.append("WhileExpr: " + "\n");
		level++;
		if (expr.test != null) {
			expr.test.accept(this, level);
		}
		if (expr.body != null) {
			expr.body.accept(this, level);
		}
	}

	public void visit(NilExpr expr, int level) {
		indent(level);
		builder.append("NilExpr:" + "\n");
	}

	public void visit(Type expr, int level) {
		indent(level);
		builder.append("Type: " + expr.getTypeName() + "\n");
	}

	public void visit(SimpleVar expr, int level) {
		indent(level);
		builder.append(expr.getClass().getSimpleName() + ": " + expr.name + "\n");
		level++;
	}

	public void visit(IndexVar expr, int level) {
		if (expr instanceof IndexVar) {
			expr.index.accept(this, level);
		}
	} //sdamn its the same method. if onmly java was made for this sor tof thing

	public void visit(SimpleDecl expr, int level) {
		indent(level);
		builder.append(expr.getClass().getSimpleName() + ": " + expr.name + " - " + expr.type.getTypeName() + "\n"); 
		level++;
	}
	
	public void visit(ArrayDecl expr, int level) {
		indent(level);

		if (expr.size != null) {
			builder.append("ArrayDecl: " + expr.name + "[" + expr.size.value + "]" + " - " + expr.type.getTypeName() + "\n");
		}
		else {
			builder.append("ArrayDecl: " + expr.name + "[]" + " - " + expr.type.getTypeName() + "\n");
		}
	}
} //i hate the way this program is structured jsyk
