import javax.swing.*;
import javax.swing.plaf.SplitPaneUI;

public class GameLifeControl{
    GameLifeLogic gameLifeLogic;
    private boolean isChanging;
    private boolean diy;
    private boolean clean;
    private boolean initiated;
    private int [][] currentGeneration;
    private int [][] nextGeneration;
    private int speed;
    private int row;
    private int column;
    private int liveNum;

    GameLifeControl(int rows, int columns) {
        gameLifeLogic = new GameLifeLogic(rows, columns);
        isChanging = true;
        initiated = false;
        currentGeneration = new int[rows][columns];
        nextGeneration = new int[rows][columns];
        speed = 8;
        row = rows;
        column = columns;
        liveNum = 0;
    }

    public void Evolve(){
        for(int i = 0; i < row; i++)
            for(int j = 0; j < column; j++)
                gameLifeLogic.Evolve(i, j, currentGeneration, nextGeneration);           //进行下一个回合，更新细胞
    }

    public int getLiveNum(){
        return liveNum;
    }

    public void UpdateLiveNum(int l){
        liveNum = l;
    }

    public void ExchangeBothGenerations(){
        int[][] temp;
        temp = currentGeneration;
        currentGeneration = nextGeneration;
        nextGeneration = temp;
    }

    public void ClearNextGeneration(){
        for(int i = 0; i < row; i++)
            for(int j = 0; j < column; j++)
                nextGeneration[i][j] = 0;
    }

    public int[][] GetCurrentGeneration(){
        return currentGeneration;
    }

    public boolean isInitiated() {
        return initiated;
    }

    public void Start(){
        diy = false;
        clean = false;
        isChanging = false;
    }

    public void Clean(){
        initiated = true;
        clean = true;
        diy = false;
    }

    public void Add(){
        initiated = true;
        clean = false;
        diy = true;
    }

    public void SetStop() {
        initiated = false;
        isChanging = true;
        for(int i = 0; i < row; i++)
            for(int j = 0; j < column; j++)
                currentGeneration[i][j] = 0;
        ClearNextGeneration();
    }

    public void SetRandom(){
        initiated = true;
        isChanging = true;
        diy = false;
        clean = false;
        gameLifeLogic.SetRandom(currentGeneration);
        isChanging = true;                               // 防止run方法中自动开始游戏
    }

    public void SetSpeed(int s){
        speed = s;
    }

    public void SetPause(){
        isChanging = true;
    }

    public boolean GetChangingStatus() {
        return isChanging;
    }

    public boolean GetCleanStatus(){
        return clean;
    }

    public boolean GetDiyStatus(){
        return diy;
    }

    public int GetSpeed(){
        return speed;
    }

    public void UpdateGenerationElem(int y, int x, int info){
        try {
            currentGeneration[y][x] = info;
        }catch (Exception e){
            ;
        }
    }
}
