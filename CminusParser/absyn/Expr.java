package absyn;

abstract public class Expr extends Absyn {
	public int row, column;
	public Decl declared_type; //as per Fei Song's Master Plan. Added in Checkpoint 2.
	abstract public void accept(AbsynVisitor visitor, int level);
}
