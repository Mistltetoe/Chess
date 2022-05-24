package view;


import model.*;
import controller.ClickController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个类表示面板上的棋盘组件对象
 */
public class Chessboard extends JComponent {
    /**
     * CHESSBOARD_SIZE： 棋盘是8 * 8的
     * <br>
     * BACKGROUND_COLORS: 棋盘的两种背景颜色
     * <br>
     * chessListener：棋盘监听棋子的行动
     * <br>
     * chessboard: 表示8 * 8的棋盘
     * <br>
     * currentColor: 当前行棋方
     */

    private ChessComponent[][] chessComponents = new ChessComponent[8][8];
    private ChessColor currentColor = ChessColor.WHITE;
    //all chessComponents in this chessboard are shared only one model controller
    private final ClickController clickController = new ClickController(this);
    private final int CHESS_SIZE;
    private List<ChessComponent[][]> allChessComponents = new ArrayList<>();
    private boolean whiteWin;
    private boolean blackWin;
    private boolean drawChess;
    private boolean gameOver;

    public boolean isWhiteWin() {
        return whiteWin;
    }

    public void setWhiteWin(boolean whiteWin) {
        this.whiteWin = whiteWin;
    }

    public boolean isBlackWin() {
        return blackWin;
    }

    public void setBlackWin(boolean blackWin) {
        this.blackWin = blackWin;
    }

    public boolean isDrawChess() {
        return drawChess;
    }

    public void setDrawChess(boolean drawChess) {
        this.drawChess = drawChess;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public List<ChessComponent[][]> getAllChessComponents() {
        return allChessComponents;
    }

    public void addAllChessComponents(ChessComponent[][] newChessComponents) {
        this.allChessComponents.add(newChessComponents);
    }

    public void removeAllChessComponents() {
        this.allChessComponents.remove(this.allChessComponents.size()-1);
    }

    public void setChessComponents(ChessComponent[][] chessComponents) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.getChessComponents()[i][j] = chessComponents[i][j];
                this.getChessComponents()[i][j].repaint();
            }
        }
    }

    public Chessboard(int width, int height) {
        setLayout(null); // Use absolute layout.
        setSize(width, height);
        CHESS_SIZE = width / 8;
        System.out.printf("chessboard size = %d, chess size = %d\n", width, CHESS_SIZE);

        initiateChessboard();
    }

    public ClickController getClickController() {
        return clickController;
    }

    public int getCHESS_SIZE() {
        return CHESS_SIZE;
    }

    public ChessComponent[][] getChessComponents() {
        return chessComponents;
    }

    public void setCurrentColor(ChessColor currentColor) {
        this.currentColor = currentColor;
    }

    public ChessColor getCurrentColor() {
        return currentColor;
    }

    public void putChessOnBoard(ChessComponent chessComponent) {
        int row = chessComponent.getChessboardPoint().getX(), col = chessComponent.getChessboardPoint().getY();

        if (chessComponents[row][col] != null) {
            remove(chessComponents[row][col]);
        }
        add(chessComponents[row][col] = chessComponent);
    }

    public void passPawn(ChessComponent chess1, ChessComponent chess2){
        remove(chessComponents[chess1.getChessboardPoint().getX()][chess2.getChessboardPoint().getY()]);
        add(chessComponents[chess1.getChessboardPoint().getX()][chess2.getChessboardPoint().getY()] =
                new EmptySlotComponent(chessComponents[chess1.getChessboardPoint().getX()][chess2.getChessboardPoint().getY()].getChessboardPoint(),
                        chessComponents[chess1.getChessboardPoint().getX()][chess2.getChessboardPoint().getY()].getLocation(),clickController,CHESS_SIZE));

        chess1.swapLocation(chess2);
        int row1 = chess1.getChessboardPoint().getX(), col1 = chess1.getChessboardPoint().getY();
        chessComponents[row1][col1] = chess1;
        int row2 = chess2.getChessboardPoint().getX(), col2 = chess2.getChessboardPoint().getY();
        chessComponents[row2][col2] = chess2;

        chess1.repaint();
        chess2.repaint();
    }

    public void swapChessComponents(ChessComponent chess1, ChessComponent chess2) {
        // Note that chess1 has higher priority, 'destroys' chess2 if exists.
        if (!(chess2 instanceof EmptySlotComponent)) {
            remove(chess2);
            add(chess2 = new EmptySlotComponent(chess2.getChessboardPoint(), chess2.getLocation(), clickController, CHESS_SIZE));
        }
        chess1.swapLocation(chess2);
        int row1 = chess1.getChessboardPoint().getX(), col1 = chess1.getChessboardPoint().getY();
        chessComponents[row1][col1] = chess1;
        int row2 = chess2.getChessboardPoint().getX(), col2 = chess2.getChessboardPoint().getY();
        chessComponents[row2][col2] = chess2;

        chess1.repaint();
        chess2.repaint();
    }


    public void initiateChessboard() {
        initiateEmptyChessboard();

        // FIXME: Initialize chessboard for testing only.
        initRookOnBoard(0, 0, ChessColor.BLACK);
        initRookOnBoard(0, 8 - 1, ChessColor.BLACK);
        initRookOnBoard(8 - 1, 0, ChessColor.WHITE);
        initRookOnBoard(8 - 1, 8 - 1, ChessColor.WHITE);

        initKnightOnBoard(0, 1, ChessColor.BLACK);
        initKnightOnBoard(0, 8 - 2, ChessColor.BLACK);
        initKnightOnBoard(8 - 1, 1, ChessColor.WHITE);
        initKnightOnBoard(8 - 1, 8 - 2, ChessColor.WHITE);

        initBishopOnBoard(0, 2, ChessColor.BLACK);
        initBishopOnBoard(0, 8 - 3, ChessColor.BLACK);
        initBishopOnBoard(8 - 1, 2, ChessColor.WHITE);
        initBishopOnBoard(8 - 1, 8 - 3, ChessColor.WHITE);

        initQueenOnBoard(0, 3, ChessColor.BLACK);
        initQueenOnBoard(8 - 1, 3, ChessColor.WHITE);

        initKingOnBoard(0, 4, ChessColor.BLACK);
        initKingOnBoard(8 - 1, 4, ChessColor.WHITE);

        initPawnOnBoard(1, 0, ChessColor.BLACK);
        initPawnOnBoard(1, 8 - 1, ChessColor.BLACK);
        initPawnOnBoard(1, 1, ChessColor.BLACK);
        initPawnOnBoard(1, 8 - 2, ChessColor.BLACK);
        initPawnOnBoard(1, 2, ChessColor.BLACK);
        initPawnOnBoard(1, 8 - 3, ChessColor.BLACK);
        initPawnOnBoard(1, 3, ChessColor.BLACK);
        initPawnOnBoard(1, 8 - 4, ChessColor.BLACK);
        initPawnOnBoard(8 - 2, 0, ChessColor.WHITE);
        initPawnOnBoard(8 - 2, 8 - 1, ChessColor.WHITE);
        initPawnOnBoard(8 - 2, 1, ChessColor.WHITE);
        initPawnOnBoard(8 - 2, 8 - 2, ChessColor.WHITE);
        initPawnOnBoard(8 - 2, 2, ChessColor.WHITE);
        initPawnOnBoard(8 - 2, 8 - 3, ChessColor.WHITE);
        initPawnOnBoard(8 - 2, 3, ChessColor.WHITE);
        initPawnOnBoard(8 - 2, 8 - 4, ChessColor.WHITE);

        ChessComponent[][] ccp = new ChessComponent[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ccp[i][j] = chessComponents[i][j];
                chessComponents[i][j].repaint();
            }
        }
        addAllChessComponents(ccp);
    }

    public void initiateEmptyChessboard() {
        for (int i = 0; i < chessComponents.length; i++) {
            for (int j = 0; j < chessComponents[i].length; j++) {
                putChessOnBoard(new EmptySlotComponent(new ChessboardPoint(i, j), calculatePoint(i, j), clickController, CHESS_SIZE));

            }
        }
    }

    public void swapColor() {
        currentColor = currentColor == ChessColor.BLACK ? ChessColor.WHITE : ChessColor.BLACK;
    }

    private void initRookOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new RookChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        putChessOnBoard(chessComponent);
        chessComponent.setVisible(true);
    }

    private void initKnightOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new KnightChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        putChessOnBoard(chessComponent);
        chessComponent.setVisible(true);
    }

    private void initBishopOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new BishopChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        putChessOnBoard(chessComponent);
        chessComponent.setVisible(true);
    }

    private void initQueenOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new QueenChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        putChessOnBoard(chessComponent);
        chessComponent.setVisible(true);
    }

    private void initKingOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new KingChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        putChessOnBoard(chessComponent);
        chessComponent.setVisible(true);
    }

    private void initPawnOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new PawnChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        putChessOnBoard(chessComponent);
        chessComponent.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }


    private Point calculatePoint(int row, int col) {
        return new Point(col * CHESS_SIZE, row * CHESS_SIZE);
    }

    public void loadGame(List<String> chessData) {
        initiateEmptyChessboard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                char chess = chessData.get(i).charAt(j);
                if (chess == 'R') {
                    initRookOnBoard(i, j, ChessColor.BLACK);
                }
                if (chess == 'r') {
                    initRookOnBoard(i, j, ChessColor.WHITE);
                }
                if (chess == 'N') {
                    initKnightOnBoard(i, j, ChessColor.BLACK);
                }
                if (chess == 'n') {
                    initKnightOnBoard(i, j, ChessColor.WHITE);
                }
                if (chess == 'B') {
                    initBishopOnBoard(i, j, ChessColor.BLACK);
                }
                if (chess == 'b') {
                    initBishopOnBoard(i, j, ChessColor.WHITE);
                }
                if (chess == 'Q') {
                    initQueenOnBoard(i, j, ChessColor.BLACK);
                }
                if (chess == 'q') {
                    initQueenOnBoard(i, j, ChessColor.WHITE);
                }
                if (chess == 'K') {
                    initKingOnBoard(i, j, ChessColor.BLACK);
                }
                if (chess == 'k') {
                    initKingOnBoard(i, j, ChessColor.WHITE);
                }
                if (chess == 'P') {
                    initPawnOnBoard(i, j, ChessColor.BLACK);
                }
                if (chess == 'p') {
                    initPawnOnBoard(i, j, ChessColor.WHITE);
                }
                chessComponents[i][j].repaint();
            }
        }
        if ("w".equals(chessData.get(8))) {
            currentColor = ChessColor.WHITE;
            clickController.getStatusLabel().setText("White");
        } else {
            currentColor = ChessColor.BLACK;
            clickController.getStatusLabel().setText("Black");
        }
    }

    public List<String> saveGame() {
        List<String> re = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            String rei = "";
            for (int j = 0; j < 8; j++) {
                ChessComponent cc = chessComponents[i][j];
                if (cc instanceof RookChessComponent) {
                    if (cc.getChessColor() == ChessColor.WHITE) {
                        rei += "r";
                    } else rei += "R";
                }
                if (cc instanceof KnightChessComponent) {
                    if (cc.getChessColor() == ChessColor.WHITE) {
                        rei += "n";
                    } else rei += "N";
                }
                if (cc instanceof BishopChessComponent) {
                    if (cc.getChessColor() == ChessColor.WHITE) {
                        rei += "b";
                    } else rei += "B";
                }
                if (cc instanceof QueenChessComponent) {
                    if (cc.getChessColor() == ChessColor.WHITE) {
                        rei += "q";
                    } else rei += "Q";
                }
                if (cc instanceof KingChessComponent) {
                    if (cc.getChessColor() == ChessColor.WHITE) {
                        rei += "k";
                    } else rei += "K";
                }
                if (cc instanceof PawnChessComponent) {
                    if (cc.getChessColor() == ChessColor.WHITE) {
                        rei += "p";
                    } else rei += "P";
                }
                if (cc instanceof EmptySlotComponent) {
                    rei += "_";
                }
            }
            re.add(rei);
        }
        if (currentColor == ChessColor.WHITE) re.add("w");
        else re.add("b");
        return re;
    }



    public void paintChessComponents(ChessComponent[][] ccp) {
        initiateEmptyChessboard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessComponent cc = ccp[i][j];
                if (cc instanceof RookChessComponent) {
                    if (cc.getChessColor() == ChessColor.WHITE) {
                        initRookOnBoard(i, j, ChessColor.WHITE);
                    } else initRookOnBoard(i, j, ChessColor.BLACK);
                }
                if (cc instanceof KnightChessComponent) {
                    if (cc.getChessColor() == ChessColor.WHITE) {
                        initKnightOnBoard(i, j, ChessColor.WHITE);
                    } else initKnightOnBoard(i, j, ChessColor.BLACK);
                }
                if (cc instanceof BishopChessComponent) {
                    if (cc.getChessColor() == ChessColor.WHITE) {
                        initBishopOnBoard(i, j, ChessColor.WHITE);
                    } else initBishopOnBoard(i, j, ChessColor.BLACK);
                }
                if (cc instanceof QueenChessComponent) {
                    if (cc.getChessColor() == ChessColor.WHITE) {
                        initQueenOnBoard(i, j, ChessColor.WHITE);
                    } else initQueenOnBoard(i, j, ChessColor.BLACK);
                }
                if (cc instanceof KingChessComponent) {
                    if (cc.getChessColor() == ChessColor.WHITE) {
                        initKingOnBoard(i, j, ChessColor.WHITE);
                    } else initKingOnBoard(i, j, ChessColor.BLACK);
                }
                if (cc instanceof PawnChessComponent) {
                    if (cc.getChessColor() == ChessColor.WHITE) {
                        initPawnOnBoard(i, j, ChessColor.WHITE);
                    } else initPawnOnBoard(i, j, ChessColor.BLACK);
                }
                chessComponents[i][j].repaint();
            }
        }
    }

    int i = 0;
    public boolean replay() {
        if (allChessComponents.size()>i+1) {
            paintChessComponents(allChessComponents.get(i));
            i++;
            return true;
        } else {
            paintChessComponents(allChessComponents.get(i));
            i = 0;
            return false;
        }
    }
}
