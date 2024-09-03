import java.util.Scanner;

public class MazeRunner {
    private static Maze myMap = new Maze();
    private static int userSteps = 0;

    public static void main(String[] args) {
        intro();
        while (!myMap.didIWin()) {
            String userDirection = userMove();
            navigatePit(userDirection);
        }
        System.out.println("Congratulations, you made it out alive in " + userSteps + " moves!");
    }

    public static void intro() {
        System.out.println("Welcome to Maze Runner!");
        myMap.printMap();
    }

    public static String userMove() {
        Scanner input = new Scanner(System.in);
        String direction;
        do {
            System.out.print("Move (R, L, U, D): ");
            direction = input.next().toUpperCase();
            if ("RLUD".contains(direction)) {
                userSteps++;
                if (canMove(direction)) {
                    moveCharacter(direction);
                    myMap.printMap();
                } else {
                    System.out.println("Can't move that way.");
                }
                break;
            }
        } while (true);
        return direction;
    }

    public static boolean canMove(String direction) {
        if (direction.equals("R")) return myMap.canIMoveRight();
        if (direction.equals("L")) return myMap.canIMoveLeft();
        if (direction.equals("U")) return myMap.canIMoveUp();
        if (direction.equals("D")) return myMap.canIMoveDown();
        return false;
    }

    public static void moveCharacter(String direction) {
        if (direction.equals("R")) myMap.moveRight();
        if (direction.equals("L")) myMap.moveLeft();
        if (direction.equals("U")) myMap.moveUp();
        if (direction.equals("D")) myMap.moveDown();
    }

    public static void navigatePit(String userDirection) {
        if (myMap.isThereAPit(userDirection)) {
            System.out.print("Jump over the pit? (yes/no): ");
            Scanner input = new Scanner(System.in);
            String jump = input.next();
            if (jump.equalsIgnoreCase("yes")) {
                myMap.jumpOverPit(userDirection);
            } else {
                System.out.println("You didn't jump. Game over!");
                System.exit(0);
            }
        }
    }
}

class Maze {
    private char[][] myMap;
    private int row;
    private int col;

    public Maze() {
        row = 1;
        col = 0;
        myMap = new char[5][5];
        fillMap();
    }

    private void fillMap() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                myMap[i][j] = '.';
            }
        }
        myMap[row][col] = 'x'; // Starting position
        myMap[2][1] = '0'; // Pit
        myMap[1][3] = '#'; // Wall
        myMap[3][4] = '#'; // Wall
    }

    public void printMap() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(myMap[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public boolean isThereAPit(String dir) {
        int newRow = row, newCol = col;
        if (dir.equals("R")) newCol++;
        if (dir.equals("L")) newCol--;
        if (dir.equals("U")) newRow--;
        if (dir.equals("D")) newRow++;
        return isInBounds(newRow, newCol) && myMap[newRow][newCol] == '0';
    }

    public void jumpOverPit(String dir) {
        int newRow = row, newCol = col;
        if (dir.equals("R")) newCol += 2;
        if (dir.equals("L")) newCol -= 2;
        if (dir.equals("U")) newRow -= 2;
        if (dir.equals("D")) newRow += 2;
        if (isInBounds(newRow, newCol)) {
            move(newRow - row, newCol - col);
        }
    }

    public boolean canIMoveRight() { return isInBounds(row, col + 1) && myMap[row][col + 1] != '#'; }
    public boolean canIMoveLeft() { return isInBounds(row, col - 1) && myMap[row][col - 1] != '#'; }
    public boolean canIMoveUp() { return isInBounds(row - 1, col) && myMap[row - 1][col] != '#'; }
    public boolean canIMoveDown() { return isInBounds(row + 1, col) && myMap[row + 1][col] != '#'; }

    private boolean isInBounds(int r, int c) {
        return r >= 0 && r < myMap.length && c >= 0 && c < myMap[0].length;
    }

    public void moveRight() { move(0, 1); }
    public void moveLeft() { move(0, -1); }
    public void moveUp() { move(-1, 0); }
    public void moveDown() { move(1, 0); }

    private void move(int rowChange, int colChange) {
        myMap[row][col] = '*'; // Mark as visited
        row += rowChange;
        col += colChange;
        myMap[row][col] = 'x'; // Update position
    }

    public boolean didIWin() {
        return row == 4 && col == 4; // Winning condition
    }
}

