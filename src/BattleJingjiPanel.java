import constants.GameConstants;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

public class BattleJingjiPanel extends JPanel {
    private MyPanelCard myPanelCard;
    private CardLayout cardLayout;

    private JButton gobackBtn, exitBtn;
    //闪出来的长度
    protected final static int jiangeheng = 0, jiangeshu = 0;
    //时间监听毫秒
    private final int DELAY = 15;
    //时间老人
    Timer timer;
    //地图
    static Map map;
    //人物登场
    static P1 p1;
    static P2 p2;

    protected static int cishu = 0;

    //构造出来 初始化****************************************************************************************
    public BattleJingjiPanel(MyPanelCard myPanelCard, CardLayout cardLayout) {
        this.myPanelCard = myPanelCard;
        this.cardLayout = cardLayout;

        gobackBtn = new JButton(new ImageIcon("replay1.png"));
        gobackBtn.setBounds(10, 10, 128, 50);
        add(gobackBtn);

        exitBtn = new JButton(new ImageIcon("exit1.png"));
        exitBtn.setBounds(510, 10, 130, 50);
        add(exitBtn);
        //音乐准备
        Music.Musicload();

        map = new Map();
        p1 = new P1();
        p2 = new P2();
        timer = new Timer(DELAY, new Mytime());
        //addKeyListener(Listener);
        setLayout(null);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        timer.start();

    }

    //画图啦**********************************************************************************************
    public void paintComponent(Graphics page) {
        super.paintComponent(page);
        map.biwumen().paintIcon(this, page, 0, 0);
        map.getBack().paintIcon(this, page, 0, 200);
        //不断刷新的
        p1.move();
        p2.move();
        p1.toDie(p1);
        p2.toDie(p2);
        p1.eatdaoju();
        p2.eatdaoju();
        p1.beExp();
        p2.beExp();
        p1.beBack();
        p2.beBack();

        if (cishu == 0) {
            if (p1.outlooking == 7) {
                Music.stop();
                Music.music[8].loop();
                cishu = 1;
                cardLayout.show(myPanelCard, "fighterWinPanel");
            }
            if (p2.outlooking == 7) {
                Music.stop();
                cishu = 1;
                Music.music[8].loop();
                cardLayout.show(myPanelCard, "baoziWinPanel");
            }
        }
        //绘图采用一行一行扫的形式              墙   人  糖浆   糖泡 道具
        for (int j = 0; j < GameConstants.SHU; j++)
            for (int i = 0; i < GameConstants.HENG; i++) {
                if (map.boommap[i][j] != null) {
                    map.boommap[i][j].getBallIcon().paintIcon(this, page, i * 50, j * 50 + 200);
                    map.boommap[i][j].addTime();
                }
                if (map.wallmap[i][j] != null && (!map.wallmap[i][j].isRuin()))
                    map.wallmap[i][j].getImage().paintIcon(this, page, i * 50, j * 50 - 12 + 200);
                if (map.daojumap[i][j] != null) {
                    map.daojumap[i][j].getNow().paintIcon(this, page, i * 50, j * 50 + 200);
                    map.daojumap[i][j].beExo();
                }
                if (map.expmap[i][j] != null) {
                    map.expmap[i][j].getImage().paintIcon(this, page, i * 50, j * 50 + 200);
                    map.expmap[i][j].addTime();
                }
                if (p1.getHeng() == i && p1.getShu() == j && p2.getShu() != j)
                    p1.now.paintIcon(this, page, p1.getx(), p1.gety() + 200);
                if (p2.getHeng() == i && p2.getShu() == j && p1.getShu() != j)
                    p2.now.paintIcon(this, page, p2.getx(), p2.gety() + 200);
                if (p2.X == i && p2.Y == j)
                    p2.now.paintIcon(this, page, p2.getx(), p2.gety() + 200);
                if (p1.getShu() == j && p2.getShu() == j && p1.Y > p2.Y) {
                    p2.now.paintIcon(this, page, p2.getx(), p2.gety() + 200);
                    p1.now.paintIcon(this, page, p1.getx(), p1.gety() + 200);
                }
                if (p1.getShu() == j && p2.getShu() == j && p1.Y < p2.Y) {
                    p1.now.paintIcon(this, page, p1.getx(), p1.gety() + 200);
                    p2.now.paintIcon(this, page, p2.getx(), p2.gety() + 200);
                }
            }
    }

    //时间监听*******************************************************************************************
    public class Mytime implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            repaint();
        }
    }

    public JButton getGobackBtn() {
        return gobackBtn;
    }

    public JButton getExitBtn() {
        return exitBtn;
    }
}