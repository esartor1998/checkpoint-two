package absyn;

public class IntExpr extends Expr {
	public int row, column, value;
	public IntExpr(int[] pos, int value) {
		this.row = pos[0];
		this.column = pos[1];
		this.value = value;
	}
	public void accept(AbsynVisitor visitor, int level) {
		visitor.visit(this, level);
	}
}