package absyn;

public class AssignExpr extends Expr {
	public int row, column;
	public Var lhs;
	public Expr rhs;

	public AssignExpr(int[] pos, Var lhs, Expr rhs) {
		this.row = pos[0];
		this.column = pos[1];
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public void accept(AbsynVisitor visitor, int level) {
		visitor.visit(this, level);
	}
}
