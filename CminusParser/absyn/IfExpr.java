package absyn;

public class IfExpr extends Expr {
	public int row, column;
	public Expr test;
	public Expr thenpart;
	public Expr elsepart;

	//NOTE: then/else were originally ExprList
	public IfExpr(int[] pos, Expr test, Expr thenpart, Expr elsepart) {
		this.row = pos[0];
		this.column = pos[1];
		this.test = test;
		this.thenpart = thenpart;
		this.elsepart = elsepart;
	}
	public void accept(AbsynVisitor visitor, int level) {
		visitor.visit(this, level);
	}
}

