package absyn;

public class NilExpr extends Expr {
	public int row, column;
	public NilExpr(int[] pos) {
		this.row = pos[0];
		this.column = pos[1];
	}
	public void accept(AbsynVisitor visitor, int level) {
		visitor.visit(this, level);
	}
}