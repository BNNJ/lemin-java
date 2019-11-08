
public class Lemin {

	private static String	errorMessage;
	private static int		nbAnts;
	private static int		start = -1;
	private static int		end = -1;

	public static void		main(String[] args) {
		Graph	g = Parser.parse();

		if (dataIsValid(g))
			Solver.solve(g, start, end, nbAnts);
		else
			logError();
	}

	public static boolean	dataIsValid(Graph g) {
		if (nbAnts <= 0) {
			setError("Invalid number of ants: " + nbAnts);
			return (false);
		}
		if (start < 0) {
			setError("Undefined starting room");
			return (false);
		}
		if (end < 0) {
			setError("Undefined end room");
			return (false);
		}
		if (g.getNbNodes() < 2) {
			setError("not enough nodes");
			return (false);
		}
		if (start == end) {
			setError("start and end are the same room");
			return (false);
		}
		return (true);
	}

	public static void		setAnts(int n) {
		nbAnts = n;
	}

	public static void		setStart(int n) {
		start = n;
	}

	public static void		setEnd(int n) {
		end = n;
	}

	public	static void		setError(String errMsg) {
		errorMessage = errMsg;
	}

	public static void		logError() {
		System.err.println("lemin: error: " + errorMessage);
	}

}
