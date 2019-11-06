

public class Lemin {

	public static void	main(String[] args) {
		Graph graph = Parser.parse();

		if (graph.isValid())
			graph.display();
		else
			System.err.println("lem-in: " + graph.getError());
	}
}
