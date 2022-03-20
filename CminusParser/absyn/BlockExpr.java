package absyn;

public class BlockExpr extends Expr {
	public int row, column;
	public VarDeclList decls;
	public ExprList exprs;
	public BlockExpr(int[] pos, VarDeclList decls, ExprList exprs) {
		this.row = pos[0];
		this.column = pos[1];
		this.decls = decls;
		this.exprs = exprs;
	}
	public void accept(AbsynVisitor visitor, int level) {
		visitor.visit(this, level);
	}
}