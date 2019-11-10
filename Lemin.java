
public class Lemin {

	private static String	errorMessage;

	private static int		nbAnts;
	private static int		overRideAnts;
	private static int		start = -1;
	private static int		end = -1;

	private static int		options;
	private static String	optChars = "hp";
	public static final int	HELP = 1;
	public static final int	PRINT = 2;
	public static final int	OVERRIDE_ANTS = 8;

	public static void		main(final String[] args) {
		if (!parseOptions(args)) {
			logError();
			displayUsage();
			return ;
		}
		if (getOption(HELP)) {
			displayUsage();
			return ;
		}

		Graph	g = Parser.parse();

		if (getOption(OVERRIDE_ANTS))
			nbAnts = overRideAnts;

		if (dataIsValid(g))
			Solver.solve(g, start, end, nbAnts);
		else
			logError();
	}

	public static void		displayUsage() {
		if (getOption(HELP))
			System.err.println("Lemin is a path finding program for ants !\n"
				+ "It finds the best combination of paths in order to "
				+ "send ants through a maze in the minimum number of moves\n"
				+ "Only one ant is allowed in each room at any given time\n"
				+ "The maze is read from the standard input. exemples:\n"
				+ " > java Lemin < 'map_file'\n"
				+ " > cat 'map_file' | java Lemin\n");
		System.err.println("usage:\tjava Lemin [options] [--ants X]\n"
			+ "\t-h --help\tdisplay help\n"
			+ "\t-p --print\techo the input to stdout");
	}

	private static boolean	parseOptions(final String[] args) {
		for (int i = 0; i < args.length; ++i) {
			String	arg = args[i];
			if (arg.startsWith("--")) {
				if (arg.equals("--ants")) {
					try {
						overRideAnts = Integer.parseInt(args[i + 1]);
						options |= OVERRIDE_ANTS;
					}
					catch (NumberFormatException e) {
						System.err.println("Warning: Invalid number of ants: "
							+ args[i + 1] + "\nIgnoring override");
					}
					++i;
				}
				else if (arg.equals("--help"))
					options |= HELP;
				else if (arg.equals("--print"))
					options |= PRINT;
				else {
					setError("invalid option: " + arg);
					return (false);
				}
			}
			else if (arg.startsWith("-")) {
				int	optCharIndex;
				for (int j = 1; j < arg.length(); ++j) {
					if ((optCharIndex = optChars.indexOf(arg.charAt(j))) >= 0)
						options |= (1 << optCharIndex);
					else {
						setError("Invalid option: -" + arg.charAt(j));
						return (false);
					}
				}
			}
		}
		return (true);
	}

	public static boolean	getOption(int opt) {
		return ((options & opt) != 0);
	}


	private static boolean	dataIsValid(Graph g) {
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
