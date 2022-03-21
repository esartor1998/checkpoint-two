import absyn.*;

public class NodeType {
	public String name;
	public Decl def;
	public int level;
	public NodeType(String name, Decl def, int level) {
		this.name = name;
		this.def = def;
		this.level = level;
	}
}