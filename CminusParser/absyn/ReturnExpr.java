package absyn;

public class ReturnExpr extends Expr {
	public int row, column;
	public Expr expr;
	public ReturnExpr(int[] pos, Expr expr) {
		this.row = pos[0];
		this.column = pos[1];
		this.expr = expr;
	}
	public void accept(AbsynVisitor visitor, int level) {
		visitor.visit(this, level);
	}
}