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
 * 这个类表示国际象棋里面的象
 */
public class BishopChessComponent extends ChessComponent {
    private static Image BISHOP_WHITE;
    private static Image BISHOP_BLACK;

    private Image bishopImage;

    /**
     * 读取加载棋子的图片
     *
     * @throws IOException
     */
    public void loadResource() throws IOException {
        if (BISHOP_WHITE == null) {
            BISHOP_WHITE = ImageIO.read(new File("./images/bishop-white.png"));
        }

        if (BISHOP_BLACK == null) {
            BISHOP_BLACK = ImageIO.read(new File("./images/bishop-black.png"));
        }
    }


    /**
     * 在构造棋子对象的时候，调用此方法以根据颜色确定xxImage的图片是哪一种
     *
     * @param color 棋子颜色
     */

    private void initiateBishopImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                bishopImage = BISHOP_WHITE;
            } else if (color == ChessColor.BLACK) {
                bishopImage = BISHOP_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BishopChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size) {
        super(chessboardPoint, location, color, listener, size);
        initiateBishopImage(color);
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
        if (Math.abs(source.getX()-destination.getX())==Math.abs(source.getY()-destination.getY())){
            int deltaX = source.getX()-destination.getX();
            int deltaY = source.getY()-destination.getY();
            if (deltaX==deltaY){
                for (int i=Math.min(source.getX(), destination.getX()) + 1;
                     i<Math.abs(source.getX()-destination.getX());i++){
                    if (!(chessComponents[Math.min(source.getX(), destination.getX())+i][Math.min(source.getX(), destination.getX())+i]
                            instanceof EmptySlotComponent)) {
                        return false;
                    }
                }
            }
            else if (deltaX+deltaY==0){
                for (int i=Math.min(source.getX(), destination.getX())+1;i<Math.max(source.getX(), destination.getX());i++){
                    if (!(chessComponents[Math.min(source.getX(), destination.getX())+i][Math.min(source.getX(), destination.getX())+i]instanceof EmptySlotComponent)) {
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
        g.drawImage(bishopImage, 0, 0, getWidth() , getHeight(), this);
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