package absyn;

public class VarExpr extends Expr {
	public int row, column;
	public Var variable;

	public VarExpr(int[] pos, Var variable) {
		this.row = pos[0];
		this.column = pos[1];
		this.variable = variable;
	}
	public void accept(AbsynVisitor visitor, int level) {
		visitor.visit(this, level);
	}
}
