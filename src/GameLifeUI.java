import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class GameLifeUI extends JFrame implements MouseMotionListener {
    private final World world;
    private GameLifeControl gameLifeControl;

    public static void main(String[]args) {
        GameLifeUI gameLifeUI = new GameLifeUI(40, 50);
    }

    public GameLifeUI(int rows,int columns)
    {
        //添加world(JPanel类)
        gameLifeControl = new GameLifeControl(rows, columns);
        world = new World(rows, columns, gameLifeControl);
        world.setBackground(Color.LIGHT_GRAY);
        new Thread(world).start();
        add(world);

        this.addMouseMotionListener(this);             //添加鼠标监听器
        JMenuBar menu=new JMenuBar();
        this.setJMenuBar(menu);                   //设置菜单栏
        //添加菜单
        JMenu options = new JMenu("Options");
        menu.add(options);
        JMenu changeSpeed = new JMenu("ChangeSpeed");
        menu.add(changeSpeed);
        JMenu other = new JMenu("Other");
        menu.add(other);

        //在options菜单下面添加JMenuItem
        JMenuItem start = options.add("Start");
        start.addActionListener(this.new StartActionListener());
        JMenuItem random=options.add("Random");
        random.addActionListener(this.new RandomActionListener());
        JMenuItem stop=options.add("Stop");
        stop.addActionListener(this.new StopActionListener());
        JMenuItem pause=options.add("Pause");
        pause.addActionListener(this.new PauseActionListener());
        JMenuItem doityourself=options.add("Add");
        doityourself.addActionListener(this.new DIYActionListener());
        JMenuItem clean=options.add("Kill");
        clean.addActionListener(this.new CleanActionListener());

        //在changeSpeed菜单下面添加JMenuItem
        JMenuItem slow=changeSpeed.add("Slow");
        slow.addActionListener(this.new SlowActionListener());
        JMenuItem fast=changeSpeed.add("Fast");
        fast.addActionListener(this.new FastActionListener());
        JMenuItem hyper=changeSpeed.add("Hyper");
        hyper.addActionListener(this.new HyperActionListener());

        //在other菜单下面添加JMenuItem
        JMenuItem help=other.add("Help");
        help.addActionListener(this.new HelpActionListener());
        JMenuItem about=other.add("About");
        about.addActionListener(this.new AboutActionListener());

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1007,859);
        this.setTitle("Game of Life");
        this.setVisible(true);
        this.setResizable(false);
    }
    class RandomActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            world.setBackground(Color.LIGHT_GRAY);
            gameLifeControl.SetRandom();
            world.repaint();
            world.updateNumber();
        }
    }
    class StartActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if(gameLifeControl.isInitiated() == false) return;
            world.setBackground(Color.LIGHT_GRAY);
            synchronized (world){
                gameLifeControl.Start();
                world.notifyAll();
            }
        }
    }
    class StopActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            world.setBackground(Color.LIGHT_GRAY);
            gameLifeControl.SetStop();
            world.repaint();
        }
    }
    class PauseActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            world.setBackground(Color.LIGHT_GRAY);
            gameLifeControl.SetPause();
        }
    }
    class SlowActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            gameLifeControl.SetSpeed(8);
        }
    }
    class FastActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            gameLifeControl.SetSpeed(4);
        }
    }
    class HyperActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            gameLifeControl.SetSpeed(2);
        }
    }
    class HelpActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            JOptionPane.showMessageDialog(null, "这是生命游戏!!!\n生命游戏是英国数学家约翰·何顿·康威在1970年发明的细胞自动机\n "+"1．如果一个细胞周围有3个细胞为生，则该细胞为生;\n"
                    +"2． 如果一个细胞周围有2个细胞为生，则该细胞的生死状态保持不变;\n"
                    +"3． 在其它情况下，该细胞为死。");
        }
    }
    class AboutActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            JOptionPane.showMessageDialog(null, "陈冠希");
        }
    }
    class CleanActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            gameLifeControl.SetPause();
            gameLifeControl.Clean();
            world.setBackground(Color.orange);
        }
    }
    class DIYActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            gameLifeControl.SetPause();
            gameLifeControl.Add();
            world.setBackground(Color.cyan);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
        if(gameLifeControl.GetDiyStatus() == true){
            int x=e.getX();
            int y=e.getY();
            gameLifeControl.UpdateGenerationElem((y - 50) / 20, x / 20, 1);
            world.repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
  //       TODO Auto-generated method stub
        if(gameLifeControl.GetCleanStatus() == true){
            int x=e.getX();
            int y=e.getY();
            gameLifeControl.UpdateGenerationElem((y - 50) / 20, x / 20, 0);
            world.repaint();
        }
    }

    public void RepaintMap(){
        world.repaint();
    }
}

class World extends JPanel implements Runnable
{
    private GameLifeControl gameLifeControl;
    private JLabel record;
    private int row;
    private int column;

    public World(int rows, int columns, GameLifeControl gameLifeCtl)
    {
        gameLifeControl = gameLifeCtl;
        record = new JLabel();
        row = rows;
        column = columns;
        add(record);
    }

    @Override
    public void paintComponent(Graphics g)             //画框图
    {
        int [][]currentGeneration = gameLifeControl.GetCurrentGeneration();
        int liveNum = 0;
        super.paintComponent(g);
        for(int i = 0; i < row; i++) {
            for(int j = 0; j < column; j++) {
                if(currentGeneration[i][j] == 1) {
                    g.fillRect(j * 20, i * 20, 20, 20);
                    liveNum++;
                } else {
                    g.drawRect(j * 20, i * 20, 20, 20);
                }
            }
        }
        gameLifeControl.UpdateLiveNum(liveNum);
        updateNumber();
    }

    @Override
    public void run() {
        while(true) {
            synchronized(this) {
                while(gameLifeControl.GetChangingStatus() == true) {
                    try {
                        this.wait();
                    }catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                sleep(gameLifeControl.GetSpeed());
                gameLifeControl.Evolve();           //进行下一个回合，更新细胞
                gameLifeControl.ExchangeBothGenerations();       //把当前数据和更新后的数据交换
                gameLifeControl.ClearNextGeneration();          //把nextGeneration清空
                repaint();
                updateNumber();
            }
        }
    }

    private void sleep(int x)
    {
        try {
            Thread.sleep(800 * x);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 更新活细胞数目
    public void updateNumber()
    {
        String s = " 存活数量： " + gameLifeControl.getLiveNum();
        record.setText(s);
    }
}