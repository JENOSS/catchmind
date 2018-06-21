package client;

import server.Draw;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ConnectException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.util.StringTokenizer;


/**
 * 캐치마인드 게임의 클라이언트 동작을 실행하는 클래스
 *
 * 증명서를이용해 TrustStore를 만들고 실행한다.
 *
 * 1. 증명서를 이용하여 TrustStore를 만든다.
 * $ keytool -import -alias CatchMind -file CatchMind.cer -keystore trustedcerts
 *
 *
 * *************통신 프로토콜************
 *                                      *
 * // 일반 메시지로 전송                *
 * 100#[name]message#                   *
 *                                      *
 * // 정답 메시지로 전송                *
 * 200#answer#                          *
 *                                      *
 * // 서버 접속 알림                    *
 * 300#name#                            *
 *                                      *
 * // 그림을 그린다.                    *
 * 500#x1/y1/x2/y2#                     *
 *                                      *
 * // 메시지를 받으면 그림그릴 차례     *
 * 600#turn#                            *
 *                                      *
 * // 게임을 시작하면 모두에게 전송     *
 * .. 타이머가 작동한다                 *
 * 700#seconds#                         *
 *                                      *
 * // 펜의 색상을 선택한다              *
 * 800#R/G/B#                           *
 *                                      *
 * // 화면을 모두 지운다                *
 * 900#erase#                           *
 *                                      *
 * // 게임 종료 및 화면 초기화          *
 * 1000#end#                            *
 *                                      *
 * **************************************
 *
 * 프로그램 실행
 * $ java Client [password]
 *
 */
public class Client {

    // 현재 프로그램은 편의를 위해 하드코딩 함.
    private static String password = "123456";

    static Draw stub;

    // 그래픽 요소
    Views myGUI;

    // 출력
    PrintWriter out;

    // 유저네임
    String username;

    // 내 차례 인가?
    boolean turn = false;


    /**
     * args[0] 인자로 Keystore의 password 를 받아와야한다.
     *
     * @param args
     */
    public static void main(String[] args) {

        // program argument 로 password를 받는다.
        if(args.length == 1){
            password = args[0];
        }

        // System.setProperty("javax.net.ssl.trustStore", "trustedcerts");
        // System.setProperty("javax.net.ssl.trustStorePassword", password);
        //System.setProperty("javax.net.debug", "ssl");

        // Draw Interface를 실행하는 SSLRMIClient를 연다.
        try {
            RMIClientSocketFactory rmicsf = new SslRMIClientSocketFactory();

            Registry registry = LocateRegistry.getRegistry("localhost", 7776, rmicsf);

            stub = (Draw) registry.lookup("CatchMind");

        } catch (Exception e) {
            e.printStackTrace();
        }

        Client myClient = new Client();
    }

    public Client() {

        // GUI 생성
        myGUI = new Views(this);
    }

    /**
     * SSLClientSocketFactory를 이용하여 Socket을 열고
     * Thread를 할당한다.
     */
    public void start(String ip, int port, String name) {
        try {
            String serverIp = ip;
            // 소켓을 생성하여 연결을 요청한다.
            SSLSocketFactory sslFact =
                    (SSLSocketFactory) SSLSocketFactory.getDefault();

            SSLSocket socket = (SSLSocket) sslFact.createSocket(serverIp, port);
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            myGUI.loginDialog.setVisible(false);
            myGUI.setVisible(true);
            myGUI.chatTextArea.append("서버에 연결되었습니다.\n");

            username = name;
            out.println("300#" + name + "#");
            out.flush();

            Thread receiver = new Thread(new ChatClientReceiver(socket));

            receiver.start();
        } catch (ConnectException ce) {
            JDialog exitDialog = new JDialog(myGUI, "CatchMind");
            Dimension dialogSize = new Dimension(200, 200);
            exitDialog.setLocation(myGUI.screenSize.width / 2 - dialogSize.width / 2,
                    myGUI.screenSize.height / 2 - dialogSize.height / 2);
            exitDialog.setSize(dialogSize);
            exitDialog.setLayout(new GridLayout(2, 1));
            JButton exitButton = new JButton("종료하기");
            exitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == exitButton) {
                        System.exit(1);
                    }
                }
            });
            exitDialog.add(new JLabel("서버 연결에 실패했습니다."));
            exitDialog.add(exitButton);
            exitDialog.setVisible(true);
            ce.printStackTrace();
        } catch (Exception e) {
        }
    }

    /**
     * 사용자가 입력한 메시지를 서버로 전송한다.
     *
     * 프로토콜에 맞게 메시지를 전송.
     *
     * 100 : 일반 메시지
     * 200 : 정답 메시지
     *
     * @param msg
     * @throws IOException
     */
    public void sendMessage(String msg) throws IOException {
        int statusCode = 100;
        String str = (String) (myGUI.chatComboBox.getSelectedItem());
        if (str.equalsIgnoreCase("전체"))
            statusCode = 100;
        else if (turn) {
            return;
        } else
            statusCode = 200;
        out.println(statusCode + "#[" + username + "]" + msg + "#");
        out.flush();
    }

    /**
     * RMI Object 실행
     *
     * 그려질 선의 startX, startY, endX, endY를 인자로 받아 모든 클라이언트에게 전송한다.
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @throws IOException
     */
    public void sendPaint(int x1, int y1, int x2, int y2) throws IOException {
        try {
            stub.drawPen(x1, y1, x2, y2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * RMI Object 실행
     *
     * 화면에 그려질 graphic의 (rColor, gColor, bColor)를 인자로 받아 모든 클라이언트에 전송한다.
     *
     * @param color
     */
    public void sendColor(Color color) {
        int rColor = color.getRed();
        int gColor = color.getGreen();
        int bColor = color.getBlue();
        try {
            stub.changeColor(rColor, gColor, bColor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * RMI Object 실행
     *
     * 화면을 지우는 메소드이다.
     */
    public void sendErase() {
        try {
            stub.eraseScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 서버와 연결 되면 서버로부터 listen하는 쓰레드
     *
     */
    class ChatClientReceiver extends Thread {
        SSLSocket socket;
        BufferedReader in;

        ChatClientReceiver(SSLSocket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
            }
        } // 생성자

        /**
         * 프로토콜에 따라서
         * 메시지에 따른 이벤트를 처리한다.
         */
        @Override
        public void run() {
            while (in != null) {
                try {
                    String readSome = in.readLine();
                    StringTokenizer st = new StringTokenizer(readSome, "#");
                    int num = Integer.parseInt(st.nextToken());
                    String message = st.nextToken();
                    switch (num) {

                        // 일반 메시지 채팅창에 출력
                        case 100: {
                            myGUI.chatTextArea.append(message + "\n");
                            //스크롤 페인 맨 밑으로
                            myGUI.scrollPane.getVerticalScrollBar().setValue(myGUI.scrollPane.getVerticalScrollBar().getMaximum());
                            break;
                        }

                        // 정답이 나왔을 때 화면을 초기화해고 trun을 false로 변경
                        case 200: {
                            turn = false;
                            Color tmpColor = myGUI.g.getColor();
                            myGUI.g.setColor(Color.WHITE);
                            myGUI.g.fill(new Rectangle(37, 90, 760, 630));
                            myGUI.g.setColor(tmpColor);
                            myGUI.quizField.setText("");
                            myGUI.timeField.setText("");
                            break;
                        }

                        // 그림을 그리는 메시지
                        case 500: {
                            String lineString[] = message.split("/", 4);
                            int x1 = Integer.parseInt(lineString[0]);
                            int y1 = Integer.parseInt(lineString[1]);
                            int x2 = Integer.parseInt(lineString[2]);
                            int y2 = Integer.parseInt(lineString[3]);
                            myGUI.g.drawLine(x1, y1, x2, y2);
                            break;
                        }

                        // 내 턴 이어서 받는 메시지 ( 문제를 출제하게 된다.)
                        case 600: {
                            myGUI.quizField.setText(message);
                            turn = true;
                            break;
                        }
                        // 게임을 시작하면 받는 메시지 ( 시간 초가 가기 시작한다.)
                        case 700: {
                            myGUI.timeField.setText(message);
                            break;
                        }

                        // 화면을 그릴 펜의 색상을 정하는 메시지
                        case 800: {
                            String lineString[] = message.split("/", 3);
                            myGUI.g.setColor(new Color(Integer.parseInt(lineString[0]), Integer.parseInt(lineString[1]), Integer.parseInt(lineString[2])));
                            break;
                        }

                        // 화면을 지우는 메시지
                        case 900: {
                            Color tmpColor = myGUI.g.getColor();
                            myGUI.g.setColor(Color.WHITE);
                            myGUI.g.fill(new Rectangle(37, 90, 760, 630));
                            myGUI.g.setColor(tmpColor);
                            break;
                        }

                        // 비정상으로 게임이 종료되었을 때 화면을 초기화하는 메시지
                        case 1000: {
                            turn = false;
                            myGUI.timeField.setText("0");
                            myGUI.quizField.setText("");
                            Color tmpColor = myGUI.g.getColor();
                            myGUI.g.setColor(Color.WHITE);
                            myGUI.g.fill(new Rectangle(37, 90, 760, 630));
                            myGUI.g.setColor(tmpColor);
                            break;
                        }
                    }

                } catch (IOException e) {
                }
            }
        }
    }
}
