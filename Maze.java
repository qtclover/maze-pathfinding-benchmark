import java.util.Random;

public class Maze {
    final int w, h;
    final boolean[] openRight, openDown;

    Maze(int w, int h, double removeShare, long seed) {
        this.w = w;
        this.h = h;
        openRight = new boolean[w * h];
        openDown = new boolean[w * h];
        Random rnd = new Random(seed);
        carve(rnd);
        removeWalls(removeShare, rnd);
    }

    int size() {
        return w * h;
    }

    private void carve(Random rnd) {
        boolean[] seen = new boolean[size()];
        int[] stack = new int[size()];
        int top = 0;
        int[] cand = new int[4];
        stack[top++] = 0;
        seen[0] = true;
        while (top > 0) {
            int v = stack[top - 1];
            int x = v % w, y = v / w, c = 0;
            if (x + 1 < w && !seen[v + 1]) cand[c++] = v + 1;
            if (x > 0 && !seen[v - 1]) cand[c++] = v - 1;
            if (y + 1 < h && !seen[v + w]) cand[c++] = v + w;
            if (y > 0 && !seen[v - w]) cand[c++] = v - w;
            if (c == 0) {
                top--;
                continue;
            }
            int u = cand[rnd.nextInt(c)];
            if (Math.abs(u - v) == 1) openRight[Math.min(u, v)] = true;
            else openDown[Math.min(u, v)] = true;
            seen[u] = true;
            stack[top++] = u;
        }
    }

    private void removeWalls(double share, Random rnd) {
        if (share <= 0) return;
        for (int v = 0; v < size(); v++) {
            if (v % w + 1 < w && !openRight[v] && rnd.nextDouble() < share) openRight[v] = true;
            if (v / w + 1 < h && !openDown[v] && rnd.nextDouble() < share) openDown[v] = true;
        }
    }

    int neighbors(int v, int[] out) {
        int c = 0;
        if (openRight[v]) out[c++] = v + 1;
        if (openDown[v]) out[c++] = v + w;
        if (v % w > 0 && openRight[v - 1]) out[c++] = v - 1;
        if (v >= w && openDown[v - w]) out[c++] = v - w;
        return c;
    }
}
