package absyn;

public class ArrayDecl extends VarDecl {
	public int row, column;
	public Type type;
	public String name;
	public IntExpr size;
	public ArrayDecl(int[] pos, Type type, String name, IntExpr size) {
		this.row = pos[0];
		this.column = pos[1];
		this.type = type;
		this.name = name;
		this.size = size;
	}
	public void accept(AbsynVisitor visitor, int level) {
		visitor.visit(this, level);
	}
}