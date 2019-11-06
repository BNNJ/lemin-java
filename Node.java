public class Node {
	
	public Node(String nodeName, int nodeId) {
		this.name = nodeName;
		this.id = nodeId;
	}

	public int	getId() {
		return (id);
	}

	public String getName() {
		return (name);
	}
	
	private String	name;
	private int		id;
}
