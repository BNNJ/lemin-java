
public class	Path {

	private	int	startRoom;
	private	int	length;

	public		Path(int room) {
		startRoom = room;
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