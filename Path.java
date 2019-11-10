/**
 * The path class, representing each path to be taken by the ants.
 *
 * Each path is simply represented by its first room after the starting node,
 * and its length.
 * The full path can be retrieved by following the Node's next pointer,
 * which points to the next Node in the path.
 */
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
}
