package absyn;

public class SimpleVar extends Var {
	public int row, column;
	public String name;
	public SimpleVar(int[] pos, String name) {
		this.row = pos[0];
		this.column = pos[1];
		this.name = name;
	}
	public void accept(AbsynVisitor visitor, int level) {
		visitor.visit(this, level);
	}
}