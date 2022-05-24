package model;

import view.ChessboardPoint;
import controller.ClickController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * 这个类是一个抽象类，主要表示8*8棋盘上每个格子的棋子情况，当前有两个子类继承它，分别是EmptySlotComponent(空棋子)和RookChessComponent(车)。
 */
public abstract class ChessComponent extends JComponent {

    /**
     * CHESSGRID_SIZE: 主要用于确定每个棋子在页面中显示的大小。
     * <br>
     * 在这个设计中，每个棋子的图案是用图片画出来的（由于国际象棋那个棋子手动画太难了）
     * <br>
     * 因此每个棋子占用的形状是一个正方形，大小是50*50
     */

//    private static final Dimension CHESSGRID_SIZE = new Dimension(1080 / 4 * 3 / 8, 1080 / 4 * 3 / 8);
    private static final Color[] BACKGROUND_COLORS = {Color.WHITE, Color.BLACK};
    /**
     * handle click event
     */
    private ClickController clickController;

    /**
     * chessboardPoint: 表示8*8棋盘中，当前棋子在棋格对应的位置，如(0, 0), (1, 0), (0, 7),(7, 7)等等
     * <br>
     * chessColor: 表示这个棋子的颜色，有白色，黑色，无色三种
     * <br>
     * selected: 表示这个棋子是否被选中
     */
    private ChessboardPoint chessboardPoint;
    protected final ChessColor chessColor;
    private boolean selected;

    protected ChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size) {
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        setLocation(location);
        setSize(size, size);
        this.chessboardPoint = chessboardPoint;
        this.chessColor = chessColor;
        this.selected = false;
        this.clickController = clickController;
    }

    public ChessboardPoint getChessboardPoint() {
        return chessboardPoint;
    }

    public void setChessboardPoint(ChessboardPoint chessboardPoint) {
        this.chessboardPoint = chessboardPoint;
    }

    public ChessColor getChessColor() {
        return chessColor;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * @param another 主要用于和另外一个棋子交换位置
     *                <br>
     *                调用时机是在移动棋子的时候，将操控的棋子和对应的空位置棋子(EmptySlotComponent)做交换
     */
    public void swapLocation(ChessComponent another) {
        ChessboardPoint chessboardPoint1 = getChessboardPoint(), chessboardPoint2 = another.getChessboardPoint();
        Point point1 = getLocation(), point2 = another.getLocation();
        setChessboardPoint(chessboardPoint2);
        setLocation(point2);
        another.setChessboardPoint(chessboardPoint1);
        another.setLocation(point1);
    }

    /**
     * @param e 响应鼠标监听事件
     *          <br>
     *          当接收到鼠标动作的时候，这个方法就会自动被调用，调用所有监听者的onClick方法，处理棋子的选中，移动等等行为。
     */
    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);

        if (e.getID() == MouseEvent.MOUSE_PRESSED) {
            System.out.printf("Click [%d,%d]\n", chessboardPoint.getX(), chessboardPoint.getY());
            clickController.onClick(this);
        }
    }

    /**
     * @param chessboard  棋盘
     * @param destination 目标位置，如(0, 0), (0, 7)等等
     * @return this棋子对象的移动规则和当前位置(chessboardPoint)能否到达目标位置
     * <br>
     * 这个方法主要是检查移动的合法性，如果合法就返回true，反之是false
     */
    public abstract boolean canMoveTo(ChessComponent[][] chessboard, ChessboardPoint destination);

    /**
     * 这个方法主要用于加载一些特定资源，如棋子图片等等。
     *
     * @throws IOException 如果一些资源找不到(如棋子图片路径错误)，就会抛出异常
     */
    public abstract void loadResource() throws IOException;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponents(g);
        System.out.printf("repaint chess [%d,%d]\n", chessboardPoint.getX(), chessboardPoint.getY());
        Color squareColor = BACKGROUND_COLORS[(chessboardPoint.getX() + chessboardPoint.getY()) % 2];
        g.setColor(squareColor);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

//        if (isCanBeMoved()) {
//            g.setColor(Color.BLUE);
//            g.fillRect(0, 0, getWidth() , getHeight());
//        }

    }
    //判断胜负
    public boolean isWin(ChessComponent[][] chessComponent,RookChessComponent rookChessComponent,
                         BishopChessComponent bishopChessComponent,
                         KingChessComponent kingChessComponent,
                         KnightChessComponent knightChessComponent,
                         PawnChessComponent pawnChessComponent,
                         QueenChessComponent queenChessComponent){
        //己方棋子可以落子的范围
        ArrayList<ChessboardPoint> currentPointList = collectTargetPoints(chessComponent,rookChessComponent,bishopChessComponent,kingChessComponent,knightChessComponent,pawnChessComponent,queenChessComponent,getChessColor());
        //对方棋子可以落子的范围
        ArrayList<ChessboardPoint> counterPointList = new ArrayList<>();
        if (getChessColor()==ChessColor.WHITE){
            counterPointList = collectTargetPoints(chessComponent,rookChessComponent,bishopChessComponent,kingChessComponent,knightChessComponent,pawnChessComponent,queenChessComponent,ChessColor.BLACK);
        }
        else {
            counterPointList = collectTargetPoints(chessComponent,rookChessComponent,bishopChessComponent,kingChessComponent,knightChessComponent,pawnChessComponent,queenChessComponent,ChessColor.WHITE);
        }
        //王及王可以行走的范围
        ArrayList<ChessboardPoint> kingPointList = new ArrayList<>();

        kingPointList.add(new ChessboardPoint(kingChessComponent.getX(),kingChessComponent.getY()+1));
        kingPointList.add(new ChessboardPoint(kingChessComponent.getX(),kingChessComponent.getY()-1));
        kingPointList.add(new ChessboardPoint(kingChessComponent.getX()+1,kingChessComponent.getY()));
        kingPointList.add(new ChessboardPoint(kingChessComponent.getX()-1,kingChessComponent.getY()));
        kingPointList.add(new ChessboardPoint(kingChessComponent.getX()+1,kingChessComponent.getY()+1));
        kingPointList.add(new ChessboardPoint(kingChessComponent.getX()-1,kingChessComponent.getY()+1));
        kingPointList.add(new ChessboardPoint(kingChessComponent.getX()+1,kingChessComponent.getY()-1));
        kingPointList.add(new ChessboardPoint(kingChessComponent.getX()-1,kingChessComponent.getY()-1));


        //去除王落子范围内含有己方棋子的点
        kingPointList.removeIf(a -> chessComponent[a.getX()][a.getY()].getChessColor() == getChessColor());

        ArrayList<ChessboardPoint> counterChessLocation = new ArrayList<>();
        if (getChessColor()==ChessColor.WHITE){
            counterChessLocation = getChessLocation(chessComponent,rookChessComponent,bishopChessComponent,kingChessComponent,knightChessComponent,pawnChessComponent,queenChessComponent,ChessColor.BLACK);
        }
        else {
            counterChessLocation = getChessLocation(chessComponent,rookChessComponent,bishopChessComponent,kingChessComponent,knightChessComponent,pawnChessComponent,queenChessComponent,ChessColor.WHITE);
        }

        //加上王本身的点
        kingPointList.add(new ChessboardPoint(kingChessComponent.getX(),kingChessComponent.getY()));

        //去除对方被我方威胁的棋子的落子范围
        for (int i=0;i<counterChessLocation.size();i++){
            for (int j=0;j<currentPointList.size();j++){
                if (counterChessLocation.get(i)==currentPointList.get(j)){
                    if (chessComponent[counterChessLocation.get(i).getX()][counterChessLocation.get(i).getY()]instanceof RookChessComponent){
                        counterPointList.removeAll(rookChessComponent.canMoveList(chessComponent));
                    }
                    else if (chessComponent[counterChessLocation.get(i).getX()][counterChessLocation.get(i).getY()]instanceof BishopChessComponent){
                        counterPointList.removeAll(bishopChessComponent.canMoveList(chessComponent));
                    }
                    else if (chessComponent[counterChessLocation.get(i).getX()][counterChessLocation.get(i).getY()]instanceof KnightChessComponent){
                        counterPointList.removeAll(knightChessComponent.canMoveList(chessComponent));
                    }
                    else if (chessComponent[counterChessLocation.get(i).getX()][counterChessLocation.get(i).getY()]instanceof PawnChessComponent){
                        counterPointList.removeAll(pawnChessComponent.canMoveList(chessComponent));
                    }
                    else if (chessComponent[counterChessLocation.get(i).getX()][counterChessLocation.get(i).getY()]instanceof QueenChessComponent){
                        counterPointList.removeAll(queenChessComponent.canMoveList(chessComponent));
                    }
                    else if (chessComponent[counterChessLocation.get(i).getX()][counterChessLocation.get(i).getY()]instanceof KingChessComponent){
                        counterPointList.removeAll(kingChessComponent.canMoveList(chessComponent));
                    }
                }
            }
        }

        int a = 0;
        for (int i=0;i<counterChessLocation.size();i++){
            for (int j=0;j<kingPointList.size();j++){
                if (counterPointList.get(i)==kingPointList.get(j)){
                    a++;
                }
            }
        }

        if (a==kingPointList.size()){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isCheckmate(ChessComponent[][] chessComponent,RookChessComponent rookChessComponent,
                         BishopChessComponent bishopChessComponent,
                         KingChessComponent kingChessComponent,
                         KnightChessComponent knightChessComponent,
                         PawnChessComponent pawnChessComponent,
                         QueenChessComponent queenChessComponent){
        ArrayList<ChessboardPoint> temp = collectTargetPoints(chessComponent,rookChessComponent,bishopChessComponent,kingChessComponent,knightChessComponent,pawnChessComponent,queenChessComponent,getChessColor());
        int test = 0;
        for (int i=0;i<temp.size();i++){
            if (Objects.equals(temp.get(i), new ChessboardPoint(kingChessComponent.getX(), kingChessComponent.getY()))){
                test++;
                break;
            }
        }
        return test > 0;
    }

    public ArrayList<ChessboardPoint> collectTargetPoints(ChessComponent[][] chessComponent,RookChessComponent rookChessComponent,
                                                    BishopChessComponent bishopChessComponent,
                                                    KingChessComponent kingChessComponent,
                                                    KnightChessComponent knightChessComponent,
                                                    PawnChessComponent pawnChessComponent,
                                                    QueenChessComponent queenChessComponent,
                                                    ChessColor chessColor){
        ArrayList<ChessboardPoint> temp = new ArrayList<>();
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if (chessColor==ChessColor.BLACK){
                    if (chessComponent[i][j]instanceof RookChessComponent && rookChessComponent.getChessColor()==ChessColor.BLACK){
                        temp.addAll(rookChessComponent.canMoveList(chessComponent));
                    }
                    else if (chessComponent[i][j]instanceof BishopChessComponent && bishopChessComponent.getChessColor()==ChessColor.BLACK){
                        temp.addAll(bishopChessComponent.canMoveList(chessComponent));
                    }
                    else if (chessComponent[i][j]instanceof KnightChessComponent && knightChessComponent.getChessColor()==ChessColor.BLACK){
                        temp.addAll(knightChessComponent.canMoveList(chessComponent));
                    }
                    else if (chessComponent[i][j]instanceof PawnChessComponent && pawnChessComponent.getChessColor()==ChessColor.BLACK){
                        temp.addAll(pawnChessComponent.canMoveList(chessComponent));
                    }
                    else if (chessComponent[i][j]instanceof QueenChessComponent && queenChessComponent.getChessColor()==ChessColor.BLACK){
                        temp.addAll(queenChessComponent.canMoveList(chessComponent));
                    }
                    else if (chessComponent[i][j]instanceof KingChessComponent && kingChessComponent.getChessColor()==ChessColor.BLACK){
                        temp.addAll(kingChessComponent.canMoveList(chessComponent));
                    }
                }
                else if (chessColor==ChessColor.WHITE){
                    if (chessComponent[i][j]instanceof RookChessComponent && rookChessComponent.getChessColor()==ChessColor.WHITE){
                        temp.addAll(rookChessComponent.canMoveList(chessComponent));
                    }
                    else if (chessComponent[i][j]instanceof BishopChessComponent && bishopChessComponent.getChessColor()==ChessColor.WHITE){
                        temp.addAll(bishopChessComponent.canMoveList(chessComponent));
                    }
                    else if (chessComponent[i][j]instanceof KnightChessComponent && knightChessComponent.getChessColor()==ChessColor.WHITE){
                        temp.addAll(knightChessComponent.canMoveList(chessComponent));
                    }
                    else if (chessComponent[i][j]instanceof PawnChessComponent && pawnChessComponent.getChessColor()==ChessColor.WHITE){
                        temp.addAll(pawnChessComponent.canMoveList(chessComponent));
                    }
                    else if (chessComponent[i][j]instanceof QueenChessComponent && queenChessComponent.getChessColor()==ChessColor.WHITE){
                        temp.addAll(queenChessComponent.canMoveList(chessComponent));
                    }
                    else if (chessComponent[i][j]instanceof KingChessComponent && kingChessComponent.getChessColor()==ChessColor.WHITE){
                        temp.addAll(kingChessComponent.canMoveList(chessComponent));
                    }
                }
            }
        }
        return temp;
    }

    public ArrayList<ChessboardPoint> getChessLocation(ChessComponent[][] chessComponent,RookChessComponent rookChessComponent,
                                                    BishopChessComponent bishopChessComponent,
                                                    KingChessComponent kingChessComponent,
                                                    KnightChessComponent knightChessComponent,
                                                    PawnChessComponent pawnChessComponent,
                                                    QueenChessComponent queenChessComponent,
                                                    ChessColor chessColor){
        ArrayList<ChessboardPoint> temp = new ArrayList<>();
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if (chessColor==ChessColor.BLACK){
                    if (chessComponent[i][j]instanceof RookChessComponent && rookChessComponent.getChessColor()==ChessColor.BLACK){
                        temp.add(new ChessboardPoint(i,j));
                    }
                    else if (chessComponent[i][j]instanceof BishopChessComponent && bishopChessComponent.getChessColor()==ChessColor.BLACK){
                        temp.add(new ChessboardPoint(i,j));
                    }
                    else if (chessComponent[i][j]instanceof KnightChessComponent && knightChessComponent.getChessColor()==ChessColor.BLACK){
                        temp.add(new ChessboardPoint(i,j));
                    }
                    else if (chessComponent[i][j]instanceof PawnChessComponent && pawnChessComponent.getChessColor()==ChessColor.BLACK){
                        temp.add(new ChessboardPoint(i,j));
                    }
                    else if (chessComponent[i][j]instanceof QueenChessComponent && queenChessComponent.getChessColor()==ChessColor.BLACK){
                        temp.add(new ChessboardPoint(i,j));
                    }
                    else if (chessComponent[i][j]instanceof KingChessComponent && kingChessComponent.getChessColor()==ChessColor.BLACK){
                        temp.add(new ChessboardPoint(i,j));
                    }
                }
                else if (chessColor==ChessColor.WHITE){
                    if (chessComponent[i][j]instanceof RookChessComponent && rookChessComponent.getChessColor()==ChessColor.WHITE){
                        temp.add(new ChessboardPoint(i,j));
                    }
                    else if (chessComponent[i][j]instanceof BishopChessComponent && bishopChessComponent.getChessColor()==ChessColor.WHITE){
                        temp.add(new ChessboardPoint(i,j));
                    }
                    else if (chessComponent[i][j]instanceof KnightChessComponent && knightChessComponent.getChessColor()==ChessColor.WHITE){
                        temp.add(new ChessboardPoint(i,j));
                    }
                    else if (chessComponent[i][j]instanceof PawnChessComponent && pawnChessComponent.getChessColor()==ChessColor.WHITE){
                        temp.add(new ChessboardPoint(i,j));
                    }
                    else if (chessComponent[i][j]instanceof QueenChessComponent && queenChessComponent.getChessColor()==ChessColor.WHITE){
                        temp.add(new ChessboardPoint(i,j));
                    }
                    else if (chessComponent[i][j]instanceof KingChessComponent && kingChessComponent.getChessColor()==ChessColor.WHITE){
                        temp.add(new ChessboardPoint(i,j));
                    }
                }
            }
        }
        return temp;
    }
}
