package absyn;

public interface AbsynVisitor {
	public void visit(ArrayDecl expr, int level);
	public void visit(AssignExpr expr, int level); //im going to kms i hate the "visitor pattern"
	public void visit(BlockExpr expr, int level);
	public void visit(CallExpr expr, int level);
	public void visit(DeclList expr, int level);
	public void visit(ExprList expr, int level);
	public void visit(FunctionDecl expr, int level);
	public void visit(IfExpr expr, int level);
	public void visit(IndexVar expr, int level);
	public void visit(IntExpr expr, int level);
	public void visit(NilExpr expr, int level);
	public void visit(OpExpr expr, int level);
	public void visit(ReturnExpr expr, int level);
	public void visit(SimpleDecl expr, int level);
	public void visit(SimpleVar expr, int level);
	public void visit(Type expr, int level);
	public void visit(VarDeclList expr, int level);
	public void visit(VarExpr expr, int level);
	public void visit(WhileExpr expr, int level);
}
