import java.util.ArrayDeque;
import java.util.Arrays;

public class BFS {
    static Result search(Maze m, int start, int goal) {
        int[] parent = new int[m.size()];
        Arrays.fill(parent, -1);
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        int[] nb = new int[4];
        int visited = 0;

        parent[start] = start;
        queue.add(start);
        while (!queue.isEmpty()) {
            int v = queue.poll();
            visited++;
            if (v == goal) break;
            int c = m.neighbors(v, nb);
            for (int i = 0; i < c; i++) {
                int u = nb[i];
                if (parent[u] == -1) {
                    parent[u] = v;
                    queue.add(u);
                }
            }
        }
        return Result.of(parent, goal, visited);
    }
}
