package absyn;

public class SimpleDecl extends VarDecl {
	public int row, column;
	public Type type;
	public String name;
	public SimpleDecl(int[] pos, Type type, String name) {
		this.row = pos[0];
		this.column = pos[1];
		this.type = type;
		this.name = name;
	}
	public void accept(AbsynVisitor visitor, int level) {
		visitor.visit(this, level);
	}
}