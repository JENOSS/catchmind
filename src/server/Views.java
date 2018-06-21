package server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * 서버의 GUI
 */
public class Views extends JFrame {

    Server server;

    // 로그가 붙는 scrollPane
    JScrollPane scrollPane;

    // 서버의 로그가 남는 창
    JTextArea serverLog;

    // 서버를 종료하는 버튼
    JButton exitButton;

    Views(Server server) {
        this.server = server;
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        // 서버의 로그가 남는 부분
        serverLog = new JTextArea();
        scrollPane = new JScrollPane(serverLog);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        serverLog.setEditable(false);
        serverLog.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    server.start();
                }

            }
        });

        // 서버를 시작, 종료하는 버튼
        exitButton = new JButton("서버 시작");
        contentPane.add(exitButton, BorderLayout.SOUTH);
        exitButton.addActionListener(new MyActionListener());


        // 메인 프레임의 사이즈를 설정해준다.
        Dimension frameSize = new Dimension(1000, 800);
        this.setSize(frameSize);

        // 화면의 중앙에 맞춘다.
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(screenSize.width / 2 - frameSize.width / 2,
                screenSize.height / 2 - frameSize.height / 2);


        setTitle("CatchMind Server");
        setVisible(true);
        setResizable(false);
    }

    /**
     * 게임 시작, 게임 종료 버튼에 붙는 리스너
     */
    class MyActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton b = (JButton) e.getSource();
            if (b.getText().equals("서버 시작")) {
                server.start();
            } else {
                b.setText("서버 시작");
                serverLog.append("서버가 종료되었습니다.\n");
                System.exit(1);
            }
        }
    }

}
