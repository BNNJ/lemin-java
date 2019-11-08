
public class	Path {

	private	int	startRoom;
	private	int	length;

	public		Path(int room) {
		startRoom = room;
	}

	public void	print(Graph g) {
		Node	n = g.nodeAt(startRoom);

		while (n.getNext() != -1) {
			System.out.println(n.getName());
			n = g.nodeAt(n.getNext());
		}
	}

	public void	setLength(int len) {
		length = len;
	}

	public int	getLength() {
		return (length);
	}

	public int	getStart() {
		return (startRoom);
	}
}