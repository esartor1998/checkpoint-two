package absyn;

public class FunctionDecl extends Decl {
	public int row, column;
	public Type result;
	public String name;
	public VarDeclList params;
	public BlockExpr body;
	public FunctionDecl(int[] pos, Type result, String func, VarDeclList params, BlockExpr body) {
		this.row = pos[0];
		this.column = pos[1];
		this.result = result;
		this.name = func;
		this.params = params;
		this.body = body;
	}
	public void accept(AbsynVisitor visitor, int level) {
		visitor.visit(this, level);
	}
}