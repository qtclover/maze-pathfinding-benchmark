# maze-pathfinding-benchmark

Four pathfinding algorithms raced through randomly generated mazes, in plain
Java with no dependencies: breadth-first search, depth-first search, Dijkstra
and A\*.

The question it answers: how much of a maze does each algorithm have to look at
before it finds the exit, and does that change with the kind of maze?

## Run it

Needs Java 17 or newer.

```sh
javac *.java
java Benchmark        # 100 mazes per size and type
java Benchmark 10     # or pick the number of mazes yourself
```

This writes `ergebnisse.csv` in the current directory, replacing the one in
this repository. The repository's copy is a full 100-maze run: 8,400 rows.

## How it works

**Mazes** ([`Maze.java`](Maze.java)) are grids from 10×10 up to 2000×2000. A
randomised depth-first search carves a *perfect* maze, one with exactly one
route between any two cells. Then a share of the remaining walls is knocked out
at random, which creates loops and alternative routes. Every maze is built from
a seed, so a run is reproducible.

| Type (`art`) | Walls removed | What it is like                      |
| ------------ | ------------- | ------------------------------------ |
| `perfekt`    | 0%            | One route only, long winding corridors |
| `schleifen`  | 10%           | Some loops and shortcuts             |
| `offen`      | 40%           | Mostly open, many routes             |

**Searches** all go from the top-left cell to the bottom-right one and report
how many cells they visited and how long the route they found is.

- [`BFS.java`](BFS.java): breadth-first, with a queue.
- [`DFS.java`](DFS.java): depth-first, with a stack.
- [`Dijkstra.java`](Dijkstra.java): priority queue ordered by distance so far.
- [`AStar.java`](AStar.java): priority queue ordered by distance so far plus
  the Manhattan distance to the goal.

**The benchmark** ([`Benchmark.java`](Benchmark.java)) warms up the JVM on 300
small mazes, then times every algorithm on every maze. After each maze it
checks the answers against each other: BFS, Dijkstra and A\* must agree on the
route length, and in a perfect maze DFS must too, since there is only one route.
A mismatch stops the run.

## Results

From the `ergebnisse.csv` in this repository, on 1000×1000 mazes (one million
cells), averaged over 100 mazes. "Visited" is the share of all cells the
algorithm looked at.

| Maze        | Algorithm | Visited | Route length | Median time |
| ----------- | --------- | ------- | ------------ | ----------- |
| `perfekt`   | BFS       | 54.6%   | 137,089      | 12.1 ms     |
|             | DFS       | 25.2%   | 137,089      | 3.7 ms      |
|             | Dijkstra  | 54.6%   | 137,089      | 25.0 ms     |
|             | A\*       | 54.2%   | 137,089      | 41.3 ms     |
| `schleifen` | BFS       | 100.0%  | 2,563        | 26.7 ms     |
|             | DFS       | 6.3%    | 9,894        | 0.9 ms      |
|             | Dijkstra  | 100.0%  | 2,563        | 128.9 ms    |
|             | A\*       | 88.5%   | 2,563        | 265.7 ms    |
| `offen`     | BFS       | 100.0%  | 1,998        | 23.2 ms     |
|             | DFS       | 2.2%    | 2,894        | 0.4 ms      |
|             | Dijkstra  | 100.0%  | 1,998        | 129.6 ms    |
|             | A\*       | 7.0%    | 1,998        | 24.8 ms     |

What that shows:

- **A\* only helps when the maze is open.** In a perfect maze the straight-line
  estimate is nearly useless, because the one route wanders everywhere: A\*
  visits 54.2% of the cells against 54.6% for BFS. With 40% of the walls gone
  it visits 7% where BFS visits everything.
- **DFS is fast but its route is not the shortest.** It visits the fewest cells
  in every kind of maze, but once there is more than one route it takes a
  detour: 9,894 steps instead of 2,563 in the `schleifen` mazes.
- **Dijkstra finds what BFS finds, more slowly.** Every step costs the same
  here, so the two visit almost the same number of cells and always return the
  same route length. The
  priority queue is pure overhead, about 2 to 6 times slower.
- **Visiting fewer cells is not the same as being faster.** In the `schleifen`
  mazes A\* visits fewer cells than BFS and still takes ten times as long,
  again because of the priority queue.

The visited counts and route lengths depend only on the seed and are the same
on any machine. The times are from one machine and will differ on yours.

## Reading the CSV

The column names and labels are German.

| Column        | Meaning                                          |
| ------------- | ------------------------------------------------ |
| `art`         | Maze type: `perfekt`, `schleifen` (loops), `offen` (open) |
| `groesse`     | Side length of the square maze, in cells         |
| `seed`        | Random seed the maze was built from              |
| `algorithmus` | `BFS`, `DFS`, `Dijkstra` or `AStar`              |
| `laufzeit_ns` | Running time in nanoseconds                      |
| `besucht`     | Cells visited                                    |
| `weglaenge`   | Length of the route found, in steps              |

## License

MIT
