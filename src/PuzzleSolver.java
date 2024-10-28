/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/PuzzleSolver.java to edit this template
 */


/*
 *
 * @author KimYen
 */

import java.util.*;

public class PuzzleSolver {
    static final int N = 3;
    static int[][] initialState;
    static int[][] goalState;

    public static void setInitialState(int[][] initialState) {
        PuzzleSolver.initialState = initialState;
    }

    public static void setGoalState(int[][] goalState) {
        PuzzleSolver.goalState = goalState;
    }

    public void start() {
        // Dùng 0 để biểu diễn ô trống
        Scanner sc = new Scanner(System.in);
        int[][] initialStateInput = new int[N][N];
        int[][] goallStateInput = new int[N][N];
        System.out.println("Enter the initial state:");
        for(int i =0; i< N; i++){ //nhập initalState
            for(int j = 0; j < N; j++){
                initialStateInput[i][j] = sc.nextInt();
            }
        }
        System.out.println("Enter the goal state:");
        for(int i =0; i< N; i++){ //nhập goalState
            for(int j = 0; j < N; j++){
                goallStateInput[i][j] = sc.nextInt();
            }
        }
        setInitialState(initialStateInput);
        setGoalState(goallStateInput);
        solvePuzzle();
    }

    static class PuzzleState implements Comparable<PuzzleState> {
        int[][] board;
        int g;
        int h; // heuristic function
        int f;

        public PuzzleState(int[][] board, int g) {
            this.board = new int[N][N];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    this.board[i][j] = board[i][j];
                }
            }
            this.g = g;
            this.h = calculateHeuristic();
            this.f = this.g + this.h;
        }

        private int calculateHeuristic() {
            int heuristic = 0;
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (this.board[i][j] != 0 && this.board[i][j] != goalState[i][j]) {
                        heuristic++;
                    }
                }
            }
            return heuristic;
        }

        public boolean isGoal() {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (this.board[i][j] != goalState[i][j]) {
                        return false;
                    }
                }
            }
            return true;
        }

        public List<PuzzleState> getSuccessors() {
            List<PuzzleState> successors = new ArrayList<>();
            int row = -1, col = -1;
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (this.board[i][j] == 0) {
                        row = i;
                        col = j;
                        break;
                    }
                }
            }

            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // trên dưới trái phải
            for (int[] d : directions) {
                int newRow = row + d[0], newCol = col + d[1];
                if (newRow >= 0 && newRow < N && newCol >= 0 && newCol < N) {
                    int[][] newBoard = new int[N][N];
                    for (int i = 0; i < N; i++) {
                        System.arraycopy(this.board[i], 0, newBoard[i], 0, N);
                    }

                    newBoard[row][col] = newBoard[newRow][newCol];
                    newBoard[newRow][newCol] = 0;
                    successors.add(new PuzzleState(newBoard, this.g + 1));
                }
            }
            return successors;
        }

        @Override
        public int compareTo(PuzzleState other) {
            return this.f - other.f;
        }

        public void printState() {
            System.out.println("g = " + this.g + ", h = " + this.h + ", f = " + this.f);

            // In viền trên
            System.out.print("┌");
            for (int j = 0; j < N; j++) {
                System.out.print("───");
                if (j < N - 1) System.out.print("┬"); // Giữa các ô
            }
            System.out.println("┐");

            // In các hàng của bảng với viền trái và phải
            for (int i = 0; i < N; i++) {
                System.out.print("│ "); // Viền trái
                for (int j = 0; j < N; j++) {
                    System.out.print(this.board[i][j] + " ");
                    if (j < N - 1) System.out.print("│ "); // Viền giữa các ô
                }
                System.out.println("│"); // Viền phải

                // In viền giữa các hàng
                if (i < N - 1) {
                    System.out.print("├");
                    for (int j = 0; j < N; j++) {
                        System.out.print("───");
                        if (j < N - 1) System.out.print("┼");
                    }
                    System.out.println("┤");
                }
            }

            // In viền dưới
            System.out.print("└");
            for (int j = 0; j < N; j++) {
                System.out.print("───");
                if (j < N - 1) System.out.print("┴");
            }
            System.out.println("┘\n");
        }
    }

    public static void solvePuzzle() {
        PriorityQueue<PuzzleState> openList = new PriorityQueue<>();
        Set<String> visited = new HashSet<>();

        PuzzleState startState = new PuzzleState(initialState, 0);
        openList.add(startState);

        while (!openList.isEmpty()) {
            PuzzleState currentState = openList.poll();
            currentState.printState();

            if (currentState.isGoal()) {
                System.out.println("Goal state reached!\n");
                return;
            }

            visited.add(Arrays.deepToString(currentState.board));

            for (PuzzleState nextState : currentState.getSuccessors()) {
                if (!visited.contains(Arrays.deepToString(nextState.board))) {
                    openList.add(nextState);
                }
            }
        }
        System.out.println("Solution not found.");
    }
}

