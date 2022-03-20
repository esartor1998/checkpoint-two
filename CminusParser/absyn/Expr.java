package absyn;

abstract public class Expr extends Absyn {
	public int row, column;
	abstract public void accept(AbsynVisitor visitor, int level);
}
