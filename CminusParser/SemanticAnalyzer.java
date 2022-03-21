import absyn.*;

import java.util.HashMap;
import java.util.ArrayList;

public class SemanticAnalyzer implements AbsynVisitor {

	final static int SPACES = 4;

	public final static SIMPLE = 0;
	public final static ARRAY = 1;
	public final static FUNCTION = 2;

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
		level++;
		expr.left.accept(this, level);
		expr.right.accept(this, level);
		//TODO: error checking
	}

	public void visit(AssignExpr expr, int level) {
		level++;
		expr.left.accept(this, level);
		expr.right.accept(this, level);
		//TODO: error checking
	}

	public void visit(IfExpr expr, int level) {
		level++;
		expr.test.accept(this, level);
		expr.thenpart.accept(this, level); //TODO: needs error checking
	}

	public void visit(WhileExpr expr, int level) {
		//TODO: error checking
		level++;
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
		}

		if (expr.decls != null) {
			expr.decls.accept(this, level);
		}
		if (expr.exprs != null) {
			expr.exprs.accept(this, level);
		}
	}

	public void visit(ReturnExpr expr, int level) {
		//TODO: error checking (lots)
	}

	public void visit(CallExpr expr, int level) {
		//TODO: this. this is a biggie with many error cases
	}
	
	public void visit(NilExpr expr, int level) {
		//when the expr is Nil
	}

	public void visit(IntExpr expr, int level) {
		//when the expr is Int
	}

	public void visit(SimpleVar expr, int level) {
		//TODO: type checking and error checking
	}

	public void visit(IndexVar expr, int level) {
		expr.index.accept(this, level);
		//TODO: type echecking and error checking
	}

	public void visit(SimpleDecl expr, int level) {
		//TODO: type and error? checking
	}

	public void visit(ArrayDecl expr, int level) {
		//TODO: type and error checking. note the thing he says ab void in the guidelines
	}

	public void visit(FunctionDecl expr, int level) {
		//TODO: type and err checking. also note this prob denotes a scope change!
	}

	public void visit(Type expr, int level) {
		//when the superclass is abstract
	}
}