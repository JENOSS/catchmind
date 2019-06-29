package server;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;
import java.io.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * 실질적인 캐치마인드 게임의 서버 동작을 담당한다.
 *
 * 서버를 실행하기 전에 KeyStore를 만들어야한다.
 *
 * 1. 서버에서 사용하는 KeySotre를 만든다.
 * $ keytool -genkeypair -alias CatchMind -keyalg RSA -validity 7 -keystore CatchMindKey
 * 2. KeyStore에서 증명서를 export한다.
 * $ keytool -export -alias CatchMind -keystore CatchMindKey -rfc -file CatchMind.cer
 * 3. 클라이언트에서 증명서를 이용해서 trustStore를 만들어 접속한다.
 *
 *  * *************통신 프로토콜************
 *  *                                      *
 *  * // 일반 메시지로 전송                *
 *  * 100#[name]message#                   *
 *  *                                      *
 *  * // 정답 메시지로 전송                *
 *  * 200#answer#                          *
 *  *                                      *
 *  * // 서버 접속 알림                    *
 *  * 300#name#                            *
 *  *                                      *
 *   * // 그림을 그린다.                    *
 *  * 500#x1/y1/x2/y2#                     *
 *  *                                      *
 *  * // 메시지를 받으면 그림그릴 차례     *
 *  * 600#turn#                            *
 *  *                                      *
 *  * // 게임을 시작하면 모두에게 전송     *
 *  * .. 타이머가 작동한다                 *
 *  * 700#seconds#                         *
 *  *                                      *
 *  * // 펜의 색상을 선택한다              *
 *  * 800#R/G/B#                           *
 *  *                                      *
 *  * // 화면을 모두 지운다                *
 *  * 900#erase#                           *
 *  *                                      *
 *  * // 게임 종료 및 화면 초기화          *
 *  * 1000#end#                            *
 *  *                                      *
 *  * **************************************
 *
 * 프로그램 실행
 * $ java Server [password]
 **/


/**
 *
 * Server Class
 *
 * - RMI 기능을 구현하기 위해 UnicastRemoteObject를 확장하고, Draw 를 구현함.
 *
 */
public class Server extends UnicastRemoteObject implements Draw {
    private static final long serialVersionUID = 1L;


    // 현재 프로그램은 편의를 위해 하드코딩 함.
    private static String password = "123456";

    final static int port = 7777;

    Views myGUI;
    Questions question;
    UserInfoMap userInfoMap;
    Timer timer;


    // 게임이 시작했는지
    boolean gameSet = false;

    String answer = null;

    SSLServerSocket serverSocket = null;
    SSLSocket socket = null;

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

        System.setProperty("javax.net.ssl.keyStore", "CatchMindKey");
        System.setProperty("javax.net.ssl.keyStorePassword", password);
        System.setProperty("javax.net.debug", "ssl");

        try {
            RMIServerSocketFactory rmissf = new SslRMIServerSocketFactory();
            RMIClientSocketFactory rmicsf = new SslRMIClientSocketFactory();

            Registry registry = LocateRegistry.createRegistry(7776, rmicsf, rmissf);

            Server server = new Server(7776, rmicsf, rmissf);

            registry.rebind("CatchMind", server);

        } catch (Exception e) {
            System.err.println("Server Excepton: " + e.toString());
            e.printStackTrace();
        }
    }

    public Server() throws RemoteException {
        super();
    }

    public Server(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);

        myGUI = new Views(this);
        userInfoMap = new UserInfoMap();

        question = new Questions();

    }

    /**
     * RMI Object 구현
     *
     * 그려질 선의 startX, startY, endX, endY를 인자로 받아 모든 클라이언트에게 전송한다.
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    @Override
    public void drawPen(int x1, int y1, int x2, int y2) {
        String xyString = (x1 + 40) + "/" + (y1 + 90) + "/" + (x2 + 40) + "/" + (y2 + 90);
        sendToAll("500#" + xyString + "#");
    }

    /**
     * RMI Object 구현
     *
     * 화면에 그려질 graphic의 rColor gColor bColor를 인자로 받아 모든 클라이언트에 전송한다.
     *
     * @param rColor
     * @param gColor
     * @param bColor
     */
    @Override
    public void changeColor(int rColor, int gColor, int bColor) {
        String colorString = rColor + "/" + gColor + "/" + bColor;
        sendToAll("800#" + colorString + "#");
    }

    /**
     * RMI Object 구현
     *
     * 화면을 지우는 메소드이다.
     */
    @Override
    public void eraseScreen() {
        sendToAll("900#" + "화면지움" + "#");
    }

    /**
     * SSLServerSocketFactory를 이용하여 ServerSocket을 열고
     * Thread를 할당한다.
     */
    public void start() {
        try {
            SSLServerSocketFactory sslSrvFact =
                    (SSLServerSocketFactory)
                            SSLServerSocketFactory.getDefault();
            serverSocket = (SSLServerSocket) sslSrvFact.createServerSocket(7777);
            myGUI.serverLog.append("서버가 시작되었습니다.\n");
            myGUI.exitButton.setText("서버 종료");
            Thread thread = new serverThread();
            thread.start();


        } catch (Exception e) {
            e.printStackTrace();
        }
    } // start()

    /**
     * 모든 클라이언트에 메시지를 전송한다.
     *
     * @param msg
     */
    void sendToAll(String msg) {
        Iterator it = userInfoMap.get().keySet().iterator();

        while (it.hasNext()) {
            try {
                PrintWriter out = (PrintWriter) userInfoMap.get().get(it.next()).pw;
                out.println(msg);
                out.flush();
            } catch (Exception e) {
            }
        } // while
    } // sendToAll()

    /**
     * UserInfo.host == true 인 클라이언트에 메시지를 전송한다.
     *
     *
     * @param msg
     */
    void sendToHost(String msg) {
        Iterator it = userInfoMap.get().keySet().iterator();

        while (it.hasNext()) {
            try {
                UserInfo user = userInfoMap.get().get(it.next());
                if (user.host) {
                    PrintWriter out = user.pw;
                    out.println(msg);
                    out.flush();
                }
            } catch (Exception e) {
            }
        }
    } // sendToOne()

    /**
     * UserInfo.turn == true 인 클라이언트에 메시지를 전송한다.
     *
     *
     * @param msg
     */
    void sendToTurn(String msg) {
        Iterator it = userInfoMap.get().keySet().iterator();

        while (it.hasNext()) {
            try {
                UserInfo user = userInfoMap.get().get(it.next());
                if (user.turn) {
                    PrintWriter out = user.pw;
                    out.println(msg);
                    out.flush();
                }
            } catch (Exception e) {
            }
        }
    } // sendToOne()

    /**
     * user.turn = true로 설정한다.
     *
     * @param user
     */
    void setTurn(UserInfo user) {
        Iterator it = userInfoMap.get().keySet().iterator();
        while (it.hasNext()) {
            try {
                UserInfo tmp = (UserInfo) it.next();
                if (tmp == user) {
                    user.turn = true;
                } else {
                    user.turn = false;
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * user.turn = true로 설정하고
     * 게임을 시작한다.
     *
     * @param user
     */
    void gameStart(UserInfo user) {
        sendToAll("100#" + "게임을 시작합니다#");
        userInfoMap.setTurn(user);
        sendToTurn("100#" + "당신의 차례입니다. 그림을 그리세요.#");
        answer = question.setQuestion();
        sendToTurn("600#" + answer + "#");

        if (timer != null) {
            timer.interrupt();
        }
        timer = new Timer();
        timer.start();
    }

    /**
     * 게임 시작 버튼을 누르면 시작되는 쓰레드
     * 대기하며 접속한 클라이언트를 연결하는 SeverReceiver 쓰레드를 실행한다.
     *
     */
    class serverThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    socket = (SSLSocket) serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                myGUI.serverLog.append("[" + socket.getInetAddress() + ":" + socket.getPort() + "]" + "에서접속하였습니다.\n");
                ServerReceiver thread = new ServerReceiver(socket);
                thread.start();
            }

        }
    }

    /**
     * 클라이언트가 접속하면 클라이언트와 연결되어 listen하는 쓰레드
     */
    class ServerReceiver extends Thread {
        SSLSocket socket;/*
        DataInputStream in;
        DataOutputStream out;*/
        private BufferedReader in = null;
        private PrintWriter out = null;

        ServerReceiver(SSLSocket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            } catch (IOException e) {
            }
        }

        /**
         * 프로토콜에 따라서
         * 메시지에 따른 이벤트를 처리한다.
         * 사용자가 접속을 종료하면 그에 따라 발생하는 이벤트를 처리한다.
         */
        @Override
        public void run() {
            String message = "";
            String line = null;
            StringTokenizer st = null;


            // 방장의 닉네임
            String name = "";

            int num = 0;
            try {
                // 클라이언트에서 전송하는 메시지를 받는다.
                while ((line = in.readLine()) != null) {


                    // 메시지를 num과 message로 파싱한다.
                    st = new StringTokenizer(line, "#");

                    num = Integer.parseInt(st.nextToken());
                    message = st.nextToken();

                    switch (num) {
                        // 일반채팅
                        case 100: {
                            sendToAll("100#" + message + "#");
                            break;
                        }
                        // 정답 채팅
                        case 200: {
                            if (isAnswer(message)) {
                                sendToAll("200#정답 나옴#");
                                sendToAll("100#" + "정답입니다.! 정답은 [ " + answer + " ] (이)였습니다." + "#");
                                sendToAll("100#" + getUser(message) + " 님이 맞추었습니다.!" + "#");
                                sendToAll("100#" + "3초 후 게임이 시작됩니다." + "#");
                                this.sleep(3000);
                                gameSet = true;
                                gameStart(userInfoMap.getUser(getUser(message)));
                            } else {

                            }

                            break;
                        }
                        // 게임접속
                        case 300: {
                            sendToAll("100#" + "#" + message + " 님이 들어오셨습니다.#");
                            userInfoMap.add(message, out);
                            name = message;
                            myGUI.serverLog.append("현재 서버접속자 수는 " + userInfoMap.size() + "입니다.\n");
                            if (userInfoMap.size() == 1) {
                                userInfoMap.get().get(message).host = true;
                            }
                            if (!gameSet) {
                                if (userInfoMap.size() >= 2) {
                                    gameSet = true;
                                    gameStart(userInfoMap.getHost());
                                }
                            }
                            break;
                        }
                    }
                }
            } catch (IOException e) {
            } catch (InterruptedException ie) {

            }
            // 클라이언트에서 접속을 종료한 경우
            finally {
                sendToAll("100#" + name + "님이 나가셨습니다.");
                UserInfo exitUser = userInfoMap.getUser(name);
                userInfoMap.remove(name);

                // 방장이 나갈경우 나머지 사람 중 한명이 방장이 된다.
                if (exitUser.host) {
                    if (userInfoMap.size() >= 1) {
                        userInfoMap.getRandomUser().host = true;
                    }
                }
                myGUI.serverLog.append("[" + socket.getInetAddress() + ":" + socket.getPort() + "]" + "에서 접속을 종료하였습니다.\n");
                myGUI.serverLog.append("현재 서버접속자 수는 " + userInfoMap.size() + "입니다.\n");
                myGUI.scrollPane.getVerticalScrollBar().setValue(myGUI.scrollPane.getVerticalScrollBar().getMaximum());

                // 사람이 혼자 남아서 게임이 종료되는 메소드
                if (gameSet) {
                    if (userInfoMap.size() < 2) {
                        sendToAll("100#" + "인원 수가 부족하여 게임을 종료합니다." + "#");
                        gameSet = false;
                        sendToAll("1000#게임 종료#");
                        timer.interrupt();
                    } else if (exitUser.turn) {
                        sendToAll("100#현재 차례인 유저가 게임을 종료하였습니다.#");
                        sendToAll("100#3초후 게임을 다시 시작합니다.#");
                        sendToAll("1000#턴이 나감#");
                        timer.interrupt();
                        try {
                            this.sleep(3000);
                        } catch (InterruptedException ie) {

                        }
                        userInfoMap.getRandomUser().turn = true;
                        gameStart(userInfoMap.getTurn());
                    }
                }

            }
        } // run

        /**
         * 사용자가 전송한 msg를 파싱하고, 정답이라면 true, 틀렸다면 false 리턴
         * @param msg
         * @return
         */
        public boolean isAnswer(String msg) {
            String[] submit = msg.split("]", 2);
            return (submit[1].equalsIgnoreCase(answer));
        }

        /**
         * msg를 파싱하여 username을 리턴
         * @param msg
         * @return
         */
        public String getUser(String msg) {
            return msg.substring(1, msg.indexOf(']'));
        }


    } // ReceiverThread

    /**
     * 게임이 시작되면 Timer Thread를 실행한다.
     * 시간초가 종료되면 이벤트를 처리한다.
     */
    class Timer extends Thread {
        @Override
        public void run() {
            try {
                for (int i = 100; i >= 0; i--) {
                    sendToAll("700#" + i + "#");
                    this.sleep(1000);
                }
                sendToAll("200#정답 안나옴#");
                sendToAll("100#" + "TIME OVER! 정답은 [ " + answer + " ] (이)였습니다." + "#");
                sendToAll("100#" + "5초 후 게임이 시작됩니다." + "#");
                this.sleep(5000);
                userInfoMap.getTurn().turn = false;
                userInfoMap.getRandomUser().turn = true;
                gameSet = true;
                gameStart(userInfoMap.getRandomUser());
            } catch (InterruptedException ie) {
            }

        }
    }

} // class



