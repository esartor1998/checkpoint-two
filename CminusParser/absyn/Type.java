package absyn;

public class Type extends Absyn { //assuming these should be public and not package-priv
	public final static int VOID = 0;
	public final static int INT  = 1;
	public int row, column, type;
	public Type(int[] pos, int type) {
		this.row = pos[0];
		this.column = pos[1];
		this.type = type;
	}
	public String getTypeName() {
		if (this.type == 0) {
			return new String("void");
		}
		else return new String ("int");
	}
	public void accept(AbsynVisitor visitor, int level) {
		visitor.visit(this, level);
	}
}