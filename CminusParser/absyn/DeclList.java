package absyn;

public class DeclList extends Absyn { //extends ExprList?
	public Decl head;
	public DeclList tail;
	public DeclList(Decl head, DeclList tail) {
		this.head = head;
		this.tail = tail;
	}
	public void accept(AbsynVisitor visitor, int level) {
		visitor.visit(this, level);
	}
}