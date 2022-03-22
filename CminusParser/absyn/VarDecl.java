package absyn;

public abstract class VarDecl extends Decl {
	public int row, column;
	public Type type;
	public String name;
}