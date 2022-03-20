package absyn;

public class VarDeclList extends Absyn {
	public VarDecl head;
	public VarDeclList tail;
	public VarDeclList(VarDecl head, VarDeclList tail) {
		this.head = head;
		this.tail = tail;
	}
	public void accept(AbsynVisitor visitor, int level) {
		visitor.visit(this, level);
	}
}