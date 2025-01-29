import java.awt.*;

public class SandLab
{
    public static void main(String[] args)
    {
        SandLab lab = new SandLab(120, 80);
        lab.run();
    }

    //add constants for particle types here
    public static final int EMPTY = 0;
    public static final int METAL = 1;
    public static final int SAND = 2;
    public static final int WATER = 3;

    //do not add any more fields
    private int[][] grid;
    private SandDisplay display;

    public SandLab(int numRows, int numCols)
    {
        String[] names;
        names = new String[4];
        names[EMPTY] = "Empty";
        names[METAL] = "Metal";
        names[SAND] = "Sand";
        names[WATER] = "Water";
        display = new SandDisplay("Falling Sand", numRows, numCols, names);
        grid = new int[120][80];
    }

    //called when the user clicks on a location using the given tool
    private void locationClicked(int row, int col, int tool)
    {
        grid[row][col] = tool;
    }

    //copies each element of grid into the display
    public void updateDisplay() {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                int type = grid[row][col]; //use type to select color
                if (type == EMPTY) {
                    display.setColor(row, col, Color.BLACK);
                } else if (type == METAL) {
                    display.setColor(row, col, Color.GRAY);
                } else if (type == SAND) {
                    display.setColor(row, col, Color.YELLOW);
                } else if (type == WATER) {
                    display.setColor(row, col, Color.BLUE);
                }
            }
        }
    }

    public void step() {
        int row = (int) (Math.random() * 119);
        int col = (int) (Math.random() * 80);
        int type = grid[row][col];

        if (type == SAND && row < 119) {
            if (grid[row + 1][col] == EMPTY) { //check if there is an empty space below
                //swamp sand and empty space to move sand down
                grid[row][col] = EMPTY;
                grid[row + 1][col] = SAND;
            } else if (grid[row + 1][col] == WATER) { //check if there is water below
                //swap sand and water
                grid[row][col] = WATER;
                grid[row + 1][col] = SAND;
            }
        } else if (type == WATER) {
            //random directions: down, left, or right
            int direction = (int) (Math.random() * 3);
            int newRow = row;
            int newCol = col;

            if (direction == 1 && col > 0) {
                newCol--; //move left
            } else if (direction == 2 && col < 79) {
                newCol++; //move right
            } else {
                newRow++; //move down otherwise
            }

            if (newRow < 120 && grid[newRow][newCol] == EMPTY) { //check if the new position is valid/empty
                grid[row][col] = EMPTY;
                grid[newRow][newCol] = WATER;
            }
        }
    }


    //do not modify
    public void run()
    {
        while (true)
        {
            for (int i = 0; i < display.getSpeed(); i++)
                step();
            updateDisplay();
            display.repaint();
            display.pause(1);  //wait for redrawing and for mouse
            int[] mouseLoc = display.getMouseLocation();
            if (mouseLoc != null)  //test if mouse clicked
                locationClicked(mouseLoc[0], mouseLoc[1], display.getTool());
        }
    }
}