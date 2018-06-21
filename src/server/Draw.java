package server;


import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 게임의 화면을 통제하는 메소드를 정의해놓은 interface
 * Server 에서 Remote Object를 구현한다.
 */
public interface Draw extends Remote {
    void drawPen(int x1, int y1, int x2, int y2) throws RemoteException;
    void changeColor(int rColor, int gColor, int bColor) throws RemoteException;
    void eraseScreen() throws RemoteException;
}