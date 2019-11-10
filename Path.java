
public class	Path {

	private	Node	startRoom;
	private	int		length;

	public			Path(Node room) {
		startRoom = room;
	}

	public void		setLength(int len) {
		length = len;
	}

	public int		getLength() {
		return (length);
	}

	public Node		getStart() {
		return (startRoom);
	}

	public Node[]	toArray() {
		Node[]	array = new Node[length];
		Node	tmp = startRoom;

		for (int i = 0; i < length; ++i) {
			array[i] = tmp;
			tmp = tmp.getNext();
		}
		return (array);
	}
}
