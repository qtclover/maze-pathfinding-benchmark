import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Dijkstra {
    static Result search(Maze m, int start, int goal) {
        int[] parent = new int[m.size()];
        Arrays.fill(parent, -1);
        int[] dist = new int[m.size()];
        Arrays.fill(dist, Integer.MAX_VALUE);
        boolean[] done = new boolean[m.size()];
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e[0]));
        int[] nb = new int[4];
        int visited = 0;

        parent[start] = start;
        dist[start] = 0;
        pq.add(new int[] {0, start});
        while (!pq.isEmpty()) {
            int v = pq.poll()[1];
            if (done[v]) continue;
            done[v] = true;
            visited++;
            if (v == goal) break;
            int c = m.neighbors(v, nb);
            for (int i = 0; i < c; i++) {
                int u = nb[i];
                int d = dist[v] + 1;
                if (d < dist[u]) {
                    dist[u] = d;
                    parent[u] = v;
                    pq.add(new int[] {d, u});
                }
            }
        }
        return Result.of(parent, goal, visited);
    }
}
