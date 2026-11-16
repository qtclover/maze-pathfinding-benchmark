import java.util.ArrayDeque;
import java.util.Arrays;

public class DFS {
    static Result search(Maze m, int start, int goal) {
        int[] parent = new int[m.size()];
        Arrays.fill(parent, -1);
        boolean[] done = new boolean[m.size()];
        ArrayDeque<Integer> stack = new ArrayDeque<>();
        int[] nb = new int[4];
        int visited = 0;

        parent[start] = start;
        stack.push(start);
        while (!stack.isEmpty()) {
            int v = stack.pop();
            if (done[v]) continue;
            done[v] = true;
            visited++;
            if (v == goal) break;
            int c = m.neighbors(v, nb);
            for (int i = c - 1; i >= 0; i--) {
                int u = nb[i];
                if (!done[u]) {
                    parent[u] = v;
                    stack.push(u);
                }
            }
        }
        return Result.of(parent, goal, visited);
    }
}
