package absyn;

public abstract class Var extends Absyn { //keeping this package-priv because the sig is explicitly stated in the slide
	public int row, column;
	public String name;
	abstract public void accept(AbsynVisitor visitor, int level);
}