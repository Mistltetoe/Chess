package model;

import view.ChessboardPoint;
import controller.ClickController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个类表示国际象棋里面的后
 */
public class QueenChessComponent extends ChessComponent {
    private static Image QUEEN_WHITE;
    private static Image QUEEN_BLACK;

    private Image queenImage;

    /**
     * 读取加载棋子的图片
     *
     * @throws IOException
     */
    public void loadResource() throws IOException {
        if (QUEEN_WHITE == null) {
            QUEEN_WHITE = ImageIO.read(new File("./images/queen-white.png"));
        }

        if (QUEEN_BLACK == null) {
            QUEEN_BLACK = ImageIO.read(new File("./images/queen-black.png"));
        }
    }


    /**
     * 在构造棋子对象的时候，调用此方法以根据颜色确定xxImage的图片是哪一种
     *
     * @param color 棋子颜色
     */

    private void initiateQueenImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                queenImage = QUEEN_WHITE;
            } else if (color == ChessColor.BLACK) {
                queenImage = QUEEN_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public QueenChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size) {
        super(chessboardPoint, location, color, listener, size);
        initiateQueenImage(color);
    }

    /**
     * 棋子的移动规则
     *
     * @param chessComponents 棋盘
     * @param destination     目标位置
     * @return 棋子移动的合法性
     */

    @Override
    public boolean canMoveTo(ChessComponent[][] chessComponents, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();
        if (source.getX() == destination.getX()) {
            int row = source.getX();
            for (int col = Math.min(source.getY(), destination.getY()) + 1;
                 col < Math.max(source.getY(), destination.getY()); col++) {
                if (!(chessComponents[row][col] instanceof EmptySlotComponent)) {
                    return false;
                }
            }
        }
        else if (source.getY() == destination.getY()) {
            int col = source.getY();
            for (int row = Math.min(source.getX(), destination.getX()) + 1;
                 row < Math.max(source.getX(), destination.getX()); row++) {
                if (!(chessComponents[row][col] instanceof EmptySlotComponent)) {
                    return false;
                }
            }
        }
        else if (Math.abs(source.getX()-destination.getX())==Math.abs(source.getY()-destination.getY())){
            int deltaX = source.getX()-destination.getX();
            int deltaY = source.getY()-destination.getY();
            if (deltaX==deltaY){
                for (int i=Math.min(source.getX(), destination.getX())+1; i<Math.abs(source.getX()-destination.getX());i++){
                    if (!(chessComponents[i][Math.abs(source.getY()-destination.getY())]
                            instanceof EmptySlotComponent)) {
                        return false;
                    }
                }
            }
            else if (deltaX+deltaY==0){
                for (int i=Math.min(source.getX(), destination.getX())+1;i<Math.max(source.getX(), destination.getX());i++){
                    if (!(chessComponents[Math.min(source.getX(), destination.getX())+i][source.getX()+source.getY()-(Math.min(source.getX(), destination.getX())+i)]
                            instanceof EmptySlotComponent)) {
                        return false;
                    }
                }
            }
            else {
                return false;
            }
        }
        else { // Not on the same row or the same column.
            return false;
        }
        return true;
    }

    /**
     * 注意这个方法，每当窗体受到了形状的变化，或者是通知要进行绘图的时候，就会调用这个方法进行画图。
     *
     * @param g 可以类比于画笔
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(queenImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 1, getWidth() , getHeight());
        }
    }

    public List<ChessboardPoint> canMoveList(ChessComponent[][] chessComponents){
        ArrayList<ChessboardPoint> canMoveList = new ArrayList<>();
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if (canMoveTo(chessComponents, new ChessboardPoint(i, j))){
                    canMoveList.add(new ChessboardPoint(i,j));
                }
            }
        }
        return canMoveList;
    }
}