package absyn;

public class IndexVar extends Var {
	public int row, column;
	public String name;
	public Expr index;
	public IndexVar(int[] pos, String name, Expr index) {
		this.row = pos[0];
		this.column = pos[1];
		this.name = name;
		this.index = index;
	}
	public void accept(AbsynVisitor visitor, int level) {
		visitor.visit(this, level);
	}
}