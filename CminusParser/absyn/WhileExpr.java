package absyn;

public class WhileExpr extends Expr {
	public int row, column;
	public Expr test;
	public Expr body;
	public WhileExpr(int[] pos, Expr test, Expr body) {
		this.row = pos[0];
		this.column = pos[1];
		this.test = test;
		this.body = body;
	}
	public void accept(AbsynVisitor visitor, int level) {
		visitor.visit(this, level);
	}
}