package controller;


import model.*;
import view.Chessboard;

import javax.swing.*;
import java.util.ArrayList;

public class ClickController {
    private final Chessboard chessboard;
    private ChessComponent first;
    public ArrayList<String> step = new ArrayList<>();

    public ClickController(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    public void onClick(ChessComponent chessComponent) {
        int testWhite = 0;
        int testBlack = 0;
        if (first == null) {
            if (handleFirst(chessComponent)) {
                chessComponent.setSelected(true);
                first = chessComponent;
                first.repaint();
            }
        }
        else {
            if (first == chessComponent) { // 再次点击取消选取
                chessComponent.setSelected(false);
                ChessComponent recordFirst = first;
                first = null;
                recordFirst.repaint();
            }
            else {
                if (first instanceof PawnChessComponent){
                    if (!handleSecond(chessComponent)){
                        if (judgePassPawn(first,chessComponent)){
                            saveStep(first,chessComponent);
                            chessboard.passPawn(first,chessComponent);
                            chessboard.swapColor();
                            first.setSelected(false);
                            first = null;
                        }
//                        else if (judgePassPawn(first,chessComponent)){
//                            saveStep(first,chessComponent);
//                            chessboard.passPawn(first,chessComponent);
//                            chessboard.swapColor();
//                            first.setSelected(false);
//                            first = null;
//                        }
                    }
                    else {
                        //repaint in swap chess method.
                        if (chessboard.getCurrentColor()==ChessColor.WHITE){
                            if (chessComponent instanceof KingChessComponent && chessComponent.getChessColor()==ChessColor.BLACK){
                                testWhite++;
                            }
                        }
                        else {
                            if (chessComponent instanceof KingChessComponent && chessComponent.getChessColor()==ChessColor.WHITE){
                                testBlack++;
                            }
                        }
                        saveStep(first,chessComponent);
                        chessboard.swapChessComponents(first, chessComponent);
                        chessboard.swapColor();
                        first.setSelected(false);
                        first = null;
                    }
                }
                else if (handleSecond(chessComponent)){
                    if (chessboard.getCurrentColor()==ChessColor.WHITE){
                        if (chessComponent instanceof KingChessComponent && chessComponent.getChessColor()==ChessColor.BLACK){
                            testWhite++;
                        }
                    }
                    else {
                        if (chessComponent instanceof KingChessComponent && chessComponent.getChessColor()==ChessColor.WHITE){
                            testBlack++;
                        }
                    }
                    saveStep(first,chessComponent);
                    chessboard.swapChessComponents(first, chessComponent);
                    chessboard.swapColor();
                    first.setSelected(false);
                    first = null;
                }
            }
        }

        if (testWhite==1){
            JOptionPane.showMessageDialog(null,"White Player Win");
        }
        if (testBlack==1){
            JOptionPane.showMessageDialog(null,"Black Player Win");
        }
    }

    /**
     * @param chessComponent 目标选取的棋子
     * @return 目标选取的棋子是否与棋盘记录的当前行棋方颜色相同
     */

    private boolean handleFirst(ChessComponent chessComponent) {
        return chessComponent.getChessColor() == chessboard.getCurrentColor();
    }

    /**
     * @param chessComponent first棋子目标移动到的棋子second
     * @return first棋子是否能够移动到second棋子位置
     */

    private boolean handleSecond(ChessComponent chessComponent) {
        return chessComponent.getChessColor() != chessboard.getCurrentColor() &&
                first.canMoveTo(chessboard.getChessComponents(), chessComponent.getChessboardPoint());
    }

    public void saveStep(ChessComponent first,ChessComponent destination){
        StringBuilder a = new StringBuilder();
        if (first instanceof KingChessComponent && first.getChessColor()==ChessColor.WHITE){
            a.append("k");
            a.append(first.getChessboardPoint().getX());
            a.append(first.getChessboardPoint().getY());
        }
        if (first instanceof KingChessComponent && first.getChessColor()==ChessColor.BLACK){
            a.append("K");
            a.append(first.getChessboardPoint().getX());
            a.append(first.getChessboardPoint().getY());
        }
        if (first instanceof QueenChessComponent && first.getChessColor()==ChessColor.WHITE){
            a.append("q");
            a.append(first.getChessboardPoint().getX());
            a.append(first.getChessboardPoint().getY());
        }
        if (first instanceof QueenChessComponent && first.getChessColor()==ChessColor.BLACK){
            a.append("Q");
            a.append(first.getChessboardPoint().getX());
            a.append(first.getChessboardPoint().getY());
        }
        if (first instanceof RookChessComponent && first.getChessColor()==ChessColor.WHITE){
            a.append("r");
            a.append(first.getChessboardPoint().getX());
            a.append(first.getChessboardPoint().getY());
        }
        if (first instanceof RookChessComponent && first.getChessColor()==ChessColor.BLACK){
            a.append("R");
            a.append(first.getChessboardPoint().getX());
            a.append(first.getChessboardPoint().getY());
        }
        if (first instanceof KnightChessComponent && first.getChessColor()==ChessColor.WHITE){
            a.append("k");
            a.append(first.getChessboardPoint().getX());
            a.append(first.getChessboardPoint().getY());
        }
        if (first instanceof KnightChessComponent && first.getChessColor()==ChessColor.BLACK){
            a.append("K");
            a.append(first.getChessboardPoint().getX());
            a.append(first.getChessboardPoint().getY());
        }
        if (first instanceof PawnChessComponent && first.getChessColor()==ChessColor.WHITE){
            a.append("p");
            a.append(first.getChessboardPoint().getX());
            a.append(first.getChessboardPoint().getY());
        }
        if (first instanceof PawnChessComponent && first.getChessColor()==ChessColor.BLACK){
            a.append("P");
            a.append(first.getChessboardPoint().getX());
            a.append(first.getChessboardPoint().getY());
        }
        if (first instanceof BishopChessComponent && first.getChessColor()==ChessColor.WHITE){
            a.append("b");
            a.append(first.getChessboardPoint().getX());
            a.append(first.getChessboardPoint().getY());
        }
        if (first instanceof BishopChessComponent && first.getChessColor()==ChessColor.BLACK){
            a.append("B");
            a.append(first.getChessboardPoint().getX());
            a.append(first.getChessboardPoint().getY());
        }

        if (destination instanceof KingChessComponent && destination.getChessColor()==ChessColor.WHITE){
            a.append("k");
            a.append(destination.getChessboardPoint().getX());
            a.append(destination.getChessboardPoint().getY());
        }
        if (destination instanceof KingChessComponent && destination.getChessColor()==ChessColor.BLACK){
            a.append("K");
            a.append(destination.getChessboardPoint().getX());
            a.append(destination.getChessboardPoint().getY());
        }
        if (destination instanceof QueenChessComponent && destination.getChessColor()==ChessColor.WHITE){
            a.append("q");
            a.append(destination.getChessboardPoint().getX());
            a.append(destination.getChessboardPoint().getY());
        }
        if (destination instanceof QueenChessComponent && destination.getChessColor()==ChessColor.BLACK){
            a.append("Q");
            a.append(destination.getChessboardPoint().getX());
            a.append(destination.getChessboardPoint().getY());
        }
        if (destination instanceof RookChessComponent && destination.getChessColor()==ChessColor.WHITE){
            a.append("r");
            a.append(destination.getChessboardPoint().getX());
            a.append(destination.getChessboardPoint().getY());
        }
        if (destination instanceof RookChessComponent && destination.getChessColor()==ChessColor.BLACK){
            a.append("R");
            a.append(destination.getChessboardPoint().getX());
            a.append(destination.getChessboardPoint().getY());
        }
        if (destination instanceof KnightChessComponent && destination.getChessColor()==ChessColor.WHITE){
            a.append("k");
            a.append(destination.getChessboardPoint().getX());
            a.append(destination.getChessboardPoint().getY());
        }
        if (destination instanceof KnightChessComponent && destination.getChessColor()==ChessColor.BLACK){
            a.append("K");
            a.append(destination.getChessboardPoint().getX());
            a.append(destination.getChessboardPoint().getY());
        }
        if (destination instanceof PawnChessComponent && destination.getChessColor()==ChessColor.WHITE){
            a.append("p");
            a.append(destination.getChessboardPoint().getX());
            a.append(destination.getChessboardPoint().getY());
        }
        if (destination instanceof PawnChessComponent && destination.getChessColor()==ChessColor.BLACK){
            a.append("P");
            a.append(destination.getChessboardPoint().getX());
            a.append(destination.getChessboardPoint().getY());
        }
        if (destination instanceof BishopChessComponent && destination.getChessColor()==ChessColor.WHITE){
            a.append("b");
            a.append(destination.getChessboardPoint().getX());
            a.append(destination.getChessboardPoint().getY());
        }
        if (destination instanceof BishopChessComponent && destination.getChessColor()==ChessColor.BLACK){
            a.append("B");
            a.append(destination.getChessboardPoint().getX());
            a.append(destination.getChessboardPoint().getY());
        }
        if (destination instanceof EmptySlotComponent){
            a.append("-");
            a.append(destination.getChessboardPoint().getX());
            a.append(destination.getChessboardPoint().getY());
        }
        step.add(a.toString());
        System.out.println(a);
    }

    public String getLastStep(){
        return step.get(step.size()-1);
    }

    public boolean judgePassPawn(ChessComponent origin,ChessComponent destination){
        if (origin.getChessColor()==ChessColor.WHITE && destination instanceof EmptySlotComponent && origin.getChessboardPoint().getX()==3
        && destination.getChessboardPoint().getX()==2){
            if (getLastStep().charAt(1)=='1' && getLastStep().charAt(4)=='3' && getLastStep().charAt(0)=='P' &&
            destination.getChessboardPoint().getY()+48==getLastStep().charAt(5)){
                return true;
            }
        }
        else if (origin.getChessColor()==ChessColor.BLACK && destination instanceof EmptySlotComponent && origin.getChessboardPoint().getX()==4
        && destination.getChessboardPoint().getX()==5){
            if (getLastStep().charAt(1)=='6' && getLastStep().charAt(4)=='4' && getLastStep().charAt(0)=='p' &&
                    destination.getChessboardPoint().getY()+48==getLastStep().charAt(5)){
                return true;
            }
        }
        return false;
    }
}
