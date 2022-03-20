package absyn;

public class CallExpr extends Expr {
	public int row, column;
	public String func;
	public ExprList args;
	public CallExpr(int[] pos, String func, ExprList args) {
		this.row = pos[0];
		this.column = pos[1];
		this.func = func;
		this.args = args;
	}
	public void accept(AbsynVisitor visitor, int level) {
		visitor.visit(this, level);
	}
}