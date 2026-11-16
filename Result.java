public record Result(int visited, int length) {

    static Result of(int[] parent, int goal, int visited) {
        if (parent[goal] == -1) return new Result(visited, -1);
        int length = 0;
        for (int v = goal; parent[v] != v; v = parent[v]) length++;
        return new Result(visited, length);
    }
}
