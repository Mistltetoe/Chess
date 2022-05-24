package view;

import controller.GameController;
import controller.MusicController;
import model.ChessColor;
import model.KingChessComponent;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

/**
 * 这个类表示游戏过程中的整个游戏界面，是一切的载体
 */
public class ChessGameFrame extends JFrame {
    //    public final Dimension FRAME_SIZE ;
    private final int WIDTH;
    private final int HEIGHT;
    public final int CHESSBOARD_SIZE;
    private GameController gameController;

    public ChessGameFrame(int width, int height) {
        setTitle("2022 CS102A Project Demo"); //设置标题
        this.WIDTH = width;
        this.HEIGHT = height;
        this.CHESSBOARD_SIZE = HEIGHT * 4 / 5;

        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);

        Chessboard chessboard = new Chessboard(CHESSBOARD_SIZE, CHESSBOARD_SIZE);
        gameController = new GameController(chessboard);
        chessboard.setLocation(HEIGHT / 10, HEIGHT / 10);
        add(chessboard);

        MusicController bgm = new MusicController("./music/BGM.wav");

        addBeginButton(chessboard);
        addLoadButton();
        addSaveButton();
        addWithdrawButton(chessboard);
        addReplayButton(chessboard);
        addMusicButton(bgm);
        addChangeChessboardColor(chessboard);
        addIsWin(chessboard);

        chessboard.getClickController().getStatusLabel().setLocation(HEIGHT, HEIGHT / 10);
        chessboard.getClickController().getStatusLabel().setSize(200, 60);
        chessboard.getClickController().getStatusLabel().setFont(new Font("Rockwell", Font.BOLD, 20));
        chessboard.getClickController().getStatusLabel().setText("White");
        add(chessboard.getClickController().getStatusLabel());

    }


    /**
     * 在游戏面板中添加棋盘
     */
    private void addChessboard() {
        Chessboard chessboard = new Chessboard(CHESSBOARD_SIZE, CHESSBOARD_SIZE);
        gameController = new GameController(chessboard);
        chessboard.setLocation(HEIGHT / 10, HEIGHT / 10);
        add(chessboard);
    }

    private void addBeginButton(Chessboard chessboard) {
        JButton button = new JButton("重新开始");
        button.setLocation(HEIGHT, HEIGHT / 10 + 120);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            int value = JOptionPane.showConfirmDialog(this, "确定重新开始吗？");
            if (value == JOptionPane.YES_OPTION) {
//                chessboard.initiateChessboard();
//                chessboard.setCurrentColor(ChessColor.WHITE);
//                chessboard.getClickController().getStatusLabel().setText("White");
//                int n = chessboard.getAllChessComponents().size();
//                for (int i = 0; i < n; i++) {
//                    chessboard.removeAllChessComponents();
//                }
                int n = chessboard.getAllChessComponents().size();
                for (int i = 0; i < n - 1; i++) {
                    chessboard.removeAllChessComponents();
                }
                chessboard.paintChessComponents(chessboard.getAllChessComponents().get(0));
                chessboard.setCurrentColor(ChessColor.WHITE);
                chessboard.getClickController().getStatusLabel().setText("White");
            }
        });
    }



    private void addSaveButton() {
        JButton button = new JButton("保存棋盘");
        button.setLocation(HEIGHT, HEIGHT / 10 + 180);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click save");
            gameController.saveFileFromGame();
        });
    }

    private void addLoadButton() {
        JButton button = new JButton("导入棋盘");
        button.setLocation(HEIGHT, HEIGHT / 10 + 240);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click load");
            gameController.loadGameFromFile();
        });
    }


    private void addWithdrawButton(Chessboard chessboard) {
        JButton button = new JButton("悔棋");
        button.setLocation(HEIGHT, HEIGHT / 10 + 300);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            if (chessboard.getAllChessComponents().size()>=3) {
                int value = JOptionPane.showConfirmDialog(this, "确定悔棋吗？");
                if (value == JOptionPane.YES_OPTION) {
                    gameController.withdraw();
                }
            } else {
                JOptionPane.showMessageDialog(this,"无法悔棋");
            }
        });
    }

    private void addReplayButton(Chessboard chessboard) {
        JButton button = new JButton("回放");
        button.setLocation(HEIGHT, HEIGHT / 10 + 360);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            int value = JOptionPane.showConfirmDialog(this, "确定回放吗？");
            if (value == JOptionPane.YES_OPTION) {
                addNextButton(chessboard);
            }
        });
    }

    private void addNextButton(Chessboard chessboard) {
        JButton button = new JButton("->");
        button.setLocation(HEIGHT-60, HEIGHT / 10 + 360);
        button.setSize(60, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        chessboard.replay();

        button.addActionListener(e -> {
            boolean con = chessboard.replay();
            if (!con) {
                remove(button);
            }
        });
    }

    private void addMusicButton(MusicController bgm) {
        JButton button = new JButton("音乐开关");
        button.setLocation(HEIGHT, HEIGHT / 10 + 420);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            if (bgm.getN() == 0 || bgm.getN() == 2) {
                bgm.musicMain(3);
            } else bgm.musicMain(2);
        });
    }

    private void addChangeChessboardColor(Chessboard chessboard) {
        JButton button = new JButton("更换棋盘");
        button.setLocation(HEIGHT, HEIGHT / 10 + 480);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            Color[] colors1 = {Color.WHITE, Color.BLACK};
            Color[] colors2 = {Color.PINK, Color.GRAY};
            if (chessboard.getChessComponents()[0][0].getBACKGROUND_COLORS()[0] == Color.WHITE) {
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        chessboard.getChessComponents()[i][j].setBACKGROUND_COLORS(colors2);
                        chessboard.getChessComponents()[i][j].repaint();
                    }
                }
            } else {
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        chessboard.getChessComponents()[i][j].setBACKGROUND_COLORS(colors1);
                        chessboard.getChessComponents()[i][j].repaint();
                    }
                }
            }

        });
    }

    private void addIsWin(Chessboard chessboard){
        int testWhite = 0;
        int testBlack = 0;
        StringBuilder a = new StringBuilder();
        JOptionPane.showMessageDialog(this,a);
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if (chessboard.getCurrentColor()==ChessColor.WHITE){
                    if (chessboard.getChessComponents()[i][j] instanceof
                            KingChessComponent && chessboard.getChessComponents()[i][j].getChessColor()==ChessColor.BLACK){
                        testWhite++;
                    }
                }
                else {
                    if (chessboard.getChessComponents()[i][j] instanceof
                            KingChessComponent && chessboard.getChessComponents()[i][j].getChessColor()==ChessColor.WHITE){
                        testBlack++;
                    }
                }
            }
        }

        if (testWhite==0){
            a.append("White Player Win");
        }
        else if (testBlack==0){
            a.append("Black Player Win");
        }
    }

}
