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
 * 这个类表示国际象棋里面的兵
 */
public class PawnChessComponent extends ChessComponent {
    private static Image PAWN_WHITE;
    private static Image PAWN_BLACK;

    private Image pawnImage;

    /**
     * 读取加载棋子的图片
     *
     * @throws IOException
     */
    public void loadResource() throws IOException {
        if (PAWN_WHITE == null) {
            PAWN_WHITE = ImageIO.read(new File("./images/pawn-white.png"));
        }

        if (PAWN_BLACK == null) {
            PAWN_BLACK = ImageIO.read(new File("./images/pawn-black.png"));
        }
    }

    /**
     * 在构造棋子对象的时候，调用此方法以根据颜色确定xxImage的图片是哪一种
     *
     * @param color 棋子颜色
     */

    private void initiateKnightImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                pawnImage = PAWN_WHITE;
            } else if (color == ChessColor.BLACK) {
                pawnImage = PAWN_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PawnChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size) {
        super(chessboardPoint, location, color, listener, size);
        initiateKnightImage(color);
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
        if (getChessColor() == ChessColor.WHITE) {
            if(source.getX() == 6) {
                if (destination.getX() > source.getX()) {
                    return false;
                }
                else if (alongSlop(source, destination)) {
                    if (Math.abs(source.getX()-destination.getX())>1) {
                        return false;
                    }
                    else return !(chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent);
                }
                else if (source.getY() == destination.getY()) {
                    if (Math.abs(source.getX()-destination.getX())>2) {
                        return false;
                    }
                    else if ((Math.abs(source.getX()-destination.getX())==2)) {
                        for (int i=1;i<3;i++){
                            if (!(chessComponents[source.getX()-i][source.getY()]instanceof EmptySlotComponent)){
                                return false;
                            }
                        }
                    }
                    else return chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent;
                }
                else {
                    return false;
                }
            }
            else {
                if (destination.getX() > source.getX()) {
                    return false;
                }
                else if (alongSlop(source, destination)) {
                    if (Math.abs(source.getX()-destination.getX())> 1) {
                        return false;
                    }
                    else return !(chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent);
                }
                else if (source.getY() == destination.getY()) {
                    if (Math.abs(source.getX()-destination.getX()) > 1) {
                        return false;
                    }
                    else return chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent;
                }
                else {
                    return false;
                }
            }
        }
        else {
            if(source.getX() == 1) {
                if (destination.getX() < source.getX()) {
                    return false;
                }
                else if (alongSlop(source, destination)) {
                    if (Math.abs(source.getX()-destination.getX())> 1) {
                        return false;
                    }
                    else return !(chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent);
                }
                else if (source.getY() == destination.getY()) {
                    if (Math.abs(source.getX()-destination.getX())> 2) {
                        return false;
                    } else if (Math.abs(source.getX()-destination.getX()) == 2) {
                        for (int i=1;i<3;i++){
                            if (!(chessComponents[source.getX()+i][destination.getY()]instanceof EmptySlotComponent)){
                                return false;
                            }
                        }

                    } else return chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent;
                }
                else {
                    return false;
                }
            }
            else{
                if (destination.getX() < source.getX()) {
                    return false;
                }
                else if (alongSlop(source, destination)) {
                    if (Math.abs(source.getX()-destination.getX())>1) {
                        return false;
                    }
                    else return !(chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent);
                }
                else if (source.getY() == destination.getY()) {
                    if (destination.getX()-source.getX() > 1) {
                        return false;
                    }
                    else return chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent;
                }
                else {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean alongSlop (ChessboardPoint p1 , ChessboardPoint p2){
        int r = Math.abs(p1.getX() - p2.getX());
        int c = Math.abs(p1.getY() - p2.getY());
        return r==c;
    }

    /**
     * 注意这个方法，每当窗体受到了形状的变化，或者是通知要进行绘图的时候，就会调用这个方法进行画图。
     *
     * @param g 可以类比于画笔
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(pawnImage, 0, 0, getWidth() , getHeight(), this);
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