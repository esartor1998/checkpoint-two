package absyn;

public class ExprList extends Absyn {
	public Expr head;
	public ExprList tail;

	public ExprList(Expr head, ExprList tail) {
		this.head = head;
		this.tail = tail;
	}
	public void accept(AbsynVisitor visitor, int level) {
		visitor.visit(this, level);
	}
}
