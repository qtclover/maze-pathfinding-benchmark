import java.io.IOException;
import java.io.PrintWriter;

public class Benchmark {
    static final int[] SIZES = {10, 50, 100, 250, 500, 1000, 2000};
    static final String[] TYPES = {"perfekt", "schleifen", "offen"};
    static final double[] REMOVE = {0.0, 0.10, 0.40};
    static final String[] ALGOS = {"BFS", "DFS", "Dijkstra", "AStar"};

    public static void main(String[] args) throws IOException {
        int runs = args.length > 0 ? Integer.parseInt(args[0]) : 100;

        for (int seed = 0; seed < 300; seed++) {
            Maze m = new Maze(100, 100, REMOVE[seed % 3], seed);
            for (int a = 0; a < ALGOS.length; a++) run(a, m);
        }

        try (PrintWriter out = new PrintWriter("ergebnisse.csv")) {
            out.println("art,groesse,seed,algorithmus,laufzeit_ns,besucht,weglaenge");
            for (int t = 0; t < TYPES.length; t++) {
                for (int size : SIZES) {
                    for (int seed = 0; seed < runs; seed++) {
                        Maze m = new Maze(size, size, REMOVE[t], seed);
                        Result[] r = new Result[ALGOS.length];
                        for (int a = 0; a < ALGOS.length; a++) {
                            long t0 = System.nanoTime();
                            r[a] = run(a, m);
                            long ns = System.nanoTime() - t0;
                            out.println(TYPES[t] + "," + size + "," + seed + "," + ALGOS[a] + ","
                                    + ns + "," + r[a].visited() + "," + r[a].length());
                        }
                        check(r, t == 0, TYPES[t] + " " + size + " seed " + seed);
                    }
                    System.out.println(TYPES[t] + " " + size + "x" + size + " fertig");
                }
            }
        }
    }

    static Result run(int algo, Maze m) {
        int start = 0, goal = m.size() - 1;
        return switch (algo) {
            case 0 -> BFS.search(m, start, goal);
            case 1 -> DFS.search(m, start, goal);
            case 2 -> Dijkstra.search(m, start, goal);
            default -> AStar.search(m, start, goal);
        };
    }

    static void check(Result[] r, boolean perfect, String where) {
        int bfs = r[0].length();
        boolean ok = bfs > 0 && r[2].length() == bfs && r[3].length() == bfs
                && (!perfect || r[1].length() == bfs);
        if (!ok) throw new IllegalStateException("Weglängen passen nicht bei " + where + ": BFS " + bfs
                + ", DFS " + r[1].length() + ", Dijkstra " + r[2].length() + ", A* " + r[3].length());
    }
}
