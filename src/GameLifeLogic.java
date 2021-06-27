import java.util.Random;

public class GameLifeLogic {
    private int row;
    private int column;

    public GameLifeLogic(int rows, int columns)
    {
        row = rows;
        column = columns;
    }

    public void Evolve(int x, int y, int currentGeneration[][], int nextGeneration[][]) {
        int activeSurroundingCell=0;

        if(isValidCell(x - 1,y - 1) && (currentGeneration[x - 1][y - 1] == 1))
            activeSurroundingCell++;
        if(isValidCell(x,y - 1) && (currentGeneration[x][y - 1] == 1))
            activeSurroundingCell++;
        if(isValidCell(x + 1,y - 1) && (currentGeneration[x + 1][y - 1] == 1))
            activeSurroundingCell++;
        if(isValidCell(x + 1,y) && (currentGeneration[x + 1][y] == 1))
            activeSurroundingCell++;
        if(isValidCell(x + 1,y + 1) && (currentGeneration[x + 1][y + 1] == 1))
            activeSurroundingCell++;
        if(isValidCell(x,y + 1) && (currentGeneration[x][y + 1] == 1))
            activeSurroundingCell++;
        if(isValidCell(x - 1,y + 1)&&(currentGeneration[x - 1][y + 1] == 1))
            activeSurroundingCell++;
        if(isValidCell(x - 1,y) && (currentGeneration[x - 1][y] == 1))
            activeSurroundingCell++;
        if(activeSurroundingCell == 3)
        {
            nextGeneration[x][y] = 1;
        }
        else if(activeSurroundingCell == 2)
        {
            nextGeneration[x][y]=currentGeneration[x][y];
        }
        else
        {
            nextGeneration[x][y] = 0;
        }
    }

    //判断是否出界
    private boolean isValidCell(int x,int y)
    {
        if((x >= 0)&&(x < row)&&(y >= 0)&&(y < column)) return true;
        return false;
    }

    public void SetRandom(int [][] currentGeneration) {
        Random a=new Random();
        for(int i = 0; i < row; i++)
            for(int j = 0; j < column; j++)
                currentGeneration[i][j] = Math.abs(a.nextInt(2));
    }
}
