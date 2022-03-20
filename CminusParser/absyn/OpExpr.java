package absyn;

public class OpExpr extends Expr {
	public final static int PLUS  = 0;
	public final static int MINUS = 1;
	public final static int TIMES = 2;
	public final static int OVER  = 3;
	public final static int EQ    = 4;
	public final static int LT    = 5;
	public final static int GT    = 6; //love that java doesnt have a real (sensible) enum
	public final static int LEQ   = 7;
	public final static int GEQ   = 8;
	public final static int NEQ   = 9;

	public int row, column;
	public Expr left;
	public int op;
	public Expr right;

	public OpExpr(int[] pos, Expr left, int op, Expr right) {
		this.row = pos[0];
		this.column = pos[1];
		this.left = left;
		this.op = op;
		this.right = right;
	}
	public void accept(AbsynVisitor visitor, int level) {
		visitor.visit(this, level);
	}
}
