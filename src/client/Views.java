package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * 클라이언트의 GUI를 구현한 클래스
 * 인텔리제이의 GUI Designer를 사용하여 구현
 */

public class Views extends JFrame implements ActionListener, KeyListener {

    /* swing 요소들 */
    public JPanel mainPanel;
    public JTextField timeField;
    public JTextField quizField;
    public JTextArea chatTextArea;
    public JComboBox chatComboBox;
    public JTextField chatTextField;
    public JButton sendButton;
    public ButtonGroup buttonGroup;
    public JRadioButton radioButton1;
    public JRadioButton radioButton2;
    public JRadioButton radioButton3;
    public JRadioButton radioButton4;
    public JRadioButton radioButton5;
    public JRadioButton radioButton6;
    public JRadioButton radioButton7;
    public JRadioButton radioButton8;
    public JRadioButton radioButton9;
    public JRadioButton radioButton10;
    public JRadioButton radioButton11;
    public JRadioButton radioButton12;
    public JRadioButton radioButton13;
    public JButton eraseButton;
    public JPanel upperPanel;
    public JLabel timeLabel;
    public JLabel quizLabel;
    public JLabel hintLabel;
    public JPanel centerPanel;
    public JPanel rigntPanel;
    public JPanel rightBottomPanel;
    public JPanel leftPanel;
    public JPanel paintPanel;
    public JPanel colorPanel;
    public JPanel drawPanel;
    public JScrollPane scrollPane;

    Dimension screenSize;

    public JDialog loginDialog;
    public JPanel infoPanel;
    public JPanel ipportPanel;
    public JPanel namePanel;
    public JPanel buttonPanel;
    public JLabel ipLabel;
    public JLabel portLabel;
    public JLabel nameLabel;
    public JTextField ipField;
    public JTextField portField;
    public JTextField nameField;
    public JButton startButton;
    public JButton endButton;
    // 스윙 요소들

    // 그래픽 요소
    Color selectedColor;
    Graphics graphics;
    Graphics2D g;
    int thickness = 10;
    int startX;
    int startY;
    int endX;
    int endY;

    int x1, x2, y1, y2;

    Client client;

    public Views(Client client) {
        this.client = client;
        $$$setupUI$$$();
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();

        buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButton1);
        buttonGroup.add(radioButton2);
        buttonGroup.add(radioButton3);
        buttonGroup.add(radioButton4);
        buttonGroup.add(radioButton5);
        buttonGroup.add(radioButton6);
        buttonGroup.add(radioButton7);
        buttonGroup.add(radioButton8);
        buttonGroup.add(radioButton9);
        buttonGroup.add(radioButton10);
        buttonGroup.add(radioButton11);
        buttonGroup.add(radioButton12);
        buttonGroup.add(radioButton13);
        radioButton1.setSelected(true);

        chatComboBox.addItem("전체");
        chatComboBox.addItem("정답");


        // 화면에 중앙에 맞춘다.
        scrollPane.setAutoscrolls(true);
        Toolkit tk = Toolkit.getDefaultToolkit();
        screenSize = tk.getScreenSize();
        // 프레임 사이즈 조절
        Dimension frameSize = new Dimension(1200, 800);
        setLocation(screenSize.width / 2 - frameSize.width / 2,
                screenSize.height / 2 - frameSize.height / 2);
        setSize(frameSize);
        setTitle("CatchMind");
        setResizable(false);
        setVisible(false);

        /* 로그인 다이얼로그 */
        loginDialog = new JDialog(this, "CatchMind");
        infoPanel = new JPanel();
        ipportPanel = new JPanel();
        namePanel = new JPanel();
        buttonPanel = new JPanel();
        ipLabel = new JLabel("아이피: ");
        ipField = new JTextField("127.0.0.1");
        portLabel = new JLabel("포트: ");
        portField = new JTextField("7777");
        nameLabel = new JLabel("닉네임: ");
        nameField = new JTextField(10);
        startButton = new JButton("게임시작");
        endButton = new JButton("게임종료");
        loginDialog.setLayout(new BorderLayout());
        loginDialog.add(infoPanel, BorderLayout.NORTH);
        loginDialog.add(buttonPanel, BorderLayout.CENTER);
        infoPanel.setLayout(new GridLayout(2, 1));
        infoPanel.add(ipportPanel);
        infoPanel.add(namePanel);
        ipportPanel.setLayout(new FlowLayout());
        ipportPanel.add(ipLabel);
        ipportPanel.add(ipField);
        ipportPanel.add(portLabel);
        ipportPanel.add(portField);
        namePanel.setLayout(new FlowLayout());
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        buttonPanel.setLayout(new GridLayout(2, 1));
        buttonPanel.add(startButton);
        buttonPanel.add(endButton);
        Dimension dialogSize = new Dimension(400, 600);
        loginDialog.setLocation(screenSize.width / 2 - dialogSize.width / 2,
                screenSize.height / 2 - dialogSize.height / 2);
        loginDialog.setSize(dialogSize);


        sendButton.addActionListener(this);
        startButton.addActionListener(this);
        endButton.addActionListener(this);
        chatTextField.addKeyListener(this);
        nameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String ip = ipField.getText().trim();
                    int port = Integer.parseInt(portField.getText().trim());
                    String name = nameField.getText().trim();
                    client.start(ip, port, name);
                }
            }
        });

        radioButton1.addActionListener(this);
        radioButton2.addActionListener(this);
        radioButton3.addActionListener(this);
        radioButton4.addActionListener(this);
        radioButton5.addActionListener(this);
        radioButton6.addActionListener(this);
        radioButton7.addActionListener(this);
        radioButton8.addActionListener(this);
        radioButton9.addActionListener(this);
        radioButton10.addActionListener(this);
        radioButton11.addActionListener(this);
        radioButton12.addActionListener(this);
        radioButton13.addActionListener(this);
        eraseButton.addActionListener(this);


        loginDialog.setVisible(true);

        // 그래픽 요소
        graphics = getGraphics();
        g = (Graphics2D) graphics;
        selectedColor = Color.BLACK;
        g.setColor(selectedColor);
        drawPanel.addMouseListener(new MouseListener() {
            // paint_panel에서의 MouseListener 이벤트 처리
            public void mousePressed(MouseEvent e) {
                if (e.getX() >= 5 && e.getX() <= 750 && e.getY() >= 7 && e.getY() <= 625) {

                    if (client.turn) {
                        // paint_panel에 마우스 눌림의 액션이 있을떄 밑 메소드 실행
                        x1 = e.getX(); // 마우스가 눌렸을때 그때의 X좌표값으로 초기화
                        y1 = e.getY(); // 마우스가 눌렸을때 그때의 Y좌표값으로 초기화
                        x2 = x1;
                        y2 = y1;
                    }
                }
            }

            public void mouseClicked(MouseEvent e) {
            } // 클릭이벤트 처리

            public void mouseEntered(MouseEvent e) {
            } // paint_panel범위 내에 진입시 이벤트 처리

            public void mouseExited(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
                if (client.turn) {
                    if (e.getX() >= 5 && e.getX() <= 750 && e.getY() >= 7 && e.getY() <= 625) {
//                        g.drawLine(x1 + 40, y1 + 90, x2 + 40, y2 + 90);
                        try {
                            client.sendPaint(x1, y1, x2, y2);
                        } catch (IOException ie) {
                            ie.printStackTrace();
                        }
                    }
                }
            }

        });
        drawPanel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (e.getX() >= 5 && e.getX() <= 750 && e.getY() >= 7 && e.getY() <= 625) {
                    if (client.turn) {
                        x2 = e.getX();
                        y2 = e.getY();
//                        g.drawLine(x1 + 40, y1 + 90, x2 + 40, y2 + 90);
                        try {
                            client.sendPaint(x1, y1, x2, y2);
                        } catch (IOException ie) {
                            ie.printStackTrace();
                        }
                        x1 = x2;
                        y1 = y2;
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });
        // paint_panel에 마우스 모션리스너 추가

        g.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, 0)); //선굵기

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == startButton) {
            String ip = ipField.getText().trim();
            int port = Integer.parseInt(portField.getText().trim());
            String name = nameField.getText().trim();
            client.start(ip, port, name);
        } else if (obj == endButton) {
            System.exit(1);
        } else if (obj == sendButton) {
            try {
                client.sendMessage(chatTextField.getText());
                chatTextField.setText("");
            } catch (IOException ie) {
            }
        } else if (obj == eraseButton && client.turn) {
            Color tmpColor = g.getColor();
            g.setColor(Color.WHITE);
            g.fill(new Rectangle(37, 90, 760, 630));
            g.setColor(tmpColor);
            client.sendErase();
        }
        if (client.turn) {
            if (radioButton1.isSelected()) {
                g.setColor(radioButton1.getBackground());
            } else if (radioButton2.isSelected()) {
                g.setColor(radioButton2.getBackground());
            } else if (radioButton3.isSelected()) {
                g.setColor(radioButton3.getBackground());
            } else if (radioButton4.isSelected()) {
                g.setColor(radioButton4.getBackground());
            } else if (radioButton5.isSelected()) {
                g.setColor(radioButton5.getBackground());
            } else if (radioButton6.isSelected()) {
                g.setColor(radioButton6.getBackground());
            } else if (radioButton7.isSelected()) {
                g.setColor(radioButton7.getBackground());
            } else if (radioButton8.isSelected()) {
                g.setColor(radioButton8.getBackground());
            } else if (radioButton9.isSelected()) {
                g.setColor(radioButton9.getBackground());
            } else if (radioButton10.isSelected()) {
                g.setColor(radioButton10.getBackground());
            } else if (radioButton11.isSelected()) {
                g.setColor(radioButton11.getBackground());
            } else if (radioButton12.isSelected()) {
                g.setColor(radioButton12.getBackground());
            } else if (radioButton13.isSelected()) {
                g.setColor(radioButton13.getBackground());
            }
        }
        client.sendColor(g.getColor());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            // 엔터 눌렀을 때 채팅 되게 구현
            try {
                client.sendMessage(chatTextField.getText());
                chatTextField.setText("");
            } catch (IOException ie) {
            }

            // 전체
        } else if (e.getKeyCode() == KeyEvent.VK_F1) {
            chatComboBox.setSelectedIndex(0);

            // 정답
        } else if (e.getKeyCode() == KeyEvent.VK_F2) {
            chatComboBox.setSelectedIndex(1);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        paintPanel = new JPanel();
        drawPanel = new JPanel();
        timeLabel = new JLabel();
        paintPanel.setLayout(null);
        paintPanel.add(drawPanel);
        drawPanel.setBounds(30, 30, 760, 630);
        drawPanel.setBackground(Color.WHITE);

    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        rigntPanel = new JPanel();
        rigntPanel.setLayout(new BorderLayout(0, 0));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        centerPanel.add(rigntPanel, gbc);
        rightBottomPanel = new JPanel();
        rightBottomPanel.setLayout(new GridBagLayout());
        rigntPanel.add(rightBottomPanel, BorderLayout.SOUTH);
        chatComboBox = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        rightBottomPanel.add(chatComboBox, gbc);
        chatTextField = new JTextField();
        chatTextField.setColumns(10);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 2.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        rightBottomPanel.add(chatTextField, gbc);
        sendButton = new JButton();
        sendButton.setText("전송");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        rightBottomPanel.add(sendButton, gbc);
        scrollPane = new JScrollPane();
        rigntPanel.add(scrollPane, BorderLayout.CENTER);
        chatTextArea = new JTextArea();
        chatTextArea.setEditable(false);
        chatTextArea.setEnabled(true);
        scrollPane.setViewportView(chatTextArea);
        leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout(0, 0));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 5.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        centerPanel.add(leftPanel, gbc);
        paintPanel.setBackground(new Color(-16721665));
        leftPanel.add(paintPanel, BorderLayout.CENTER);
        colorPanel = new JPanel();
        colorPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 7, new Insets(0, 0, 0, 0), 0, 0));
        leftPanel.add(colorPanel, BorderLayout.SOUTH);
        radioButton1 = new JRadioButton();
        radioButton1.setBackground(new Color(-16777216));
        radioButton1.setForeground(new Color(-16777216));
        radioButton1.setHideActionText(false);
        radioButton1.setHorizontalAlignment(2);
        radioButton1.setText("");
        colorPanel.add(radioButton1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        radioButton2 = new JRadioButton();
        radioButton2.setBackground(new Color(-1));
        radioButton2.setHideActionText(false);
        radioButton2.setHorizontalAlignment(2);
        radioButton2.setText("");
        colorPanel.add(radioButton2, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        radioButton3 = new JRadioButton();
        radioButton3.setBackground(new Color(-65536));
        radioButton3.setForeground(new Color(-65536));
        radioButton3.setText("");
        colorPanel.add(radioButton3, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        radioButton4 = new JRadioButton();
        radioButton4.setBackground(new Color(-41472));
        radioButton4.setText("");
        colorPanel.add(radioButton4, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        radioButton5 = new JRadioButton();
        radioButton5.setBackground(new Color(-17664));
        radioButton5.setText("");
        colorPanel.add(radioButton5, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        radioButton6 = new JRadioButton();
        radioButton6.setBackground(new Color(-7168));
        radioButton6.setText("");
        colorPanel.add(radioButton6, new com.intellij.uiDesigner.core.GridConstraints(0, 5, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        radioButton7 = new JRadioButton();
        radioButton7.setBackground(new Color(-5508608));
        radioButton7.setEnabled(true);
        radioButton7.setText("");
        colorPanel.add(radioButton7, new com.intellij.uiDesigner.core.GridConstraints(0, 6, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        radioButton8 = new JRadioButton();
        radioButton8.setBackground(new Color(-14820586));
        radioButton8.setText("");
        colorPanel.add(radioButton8, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        radioButton9 = new JRadioButton();
        radioButton9.setBackground(new Color(-16721665));
        radioButton9.setText("");
        colorPanel.add(radioButton9, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        radioButton10 = new JRadioButton();
        radioButton10.setBackground(new Color(-16755457));
        radioButton10.setText("");
        colorPanel.add(radioButton10, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        radioButton11 = new JRadioButton();
        radioButton11.setBackground(new Color(-16711425));
        radioButton11.setText("");
        colorPanel.add(radioButton11, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        radioButton12 = new JRadioButton();
        radioButton12.setBackground(new Color(-10551041));
        radioButton12.setText("");
        colorPanel.add(radioButton12, new com.intellij.uiDesigner.core.GridConstraints(1, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        radioButton13 = new JRadioButton();
        radioButton13.setBackground(new Color(-65315));
        radioButton13.setText("");
        colorPanel.add(radioButton13, new com.intellij.uiDesigner.core.GridConstraints(1, 5, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        eraseButton = new JButton();
        eraseButton.setBackground(new Color(-655873));
        eraseButton.setHideActionText(false);
        eraseButton.setText("지우기");
        colorPanel.add(eraseButton, new com.intellij.uiDesigner.core.GridConstraints(1, 6, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        upperPanel = new JPanel();
        upperPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        upperPanel.setBackground(new Color(-16711425));
        mainPanel.add(upperPanel, BorderLayout.NORTH);
        timeLabel = new JLabel();
        timeLabel.setForeground(new Color(-1));
        timeLabel.setText("남은 시간(초): ");
        upperPanel.add(timeLabel);
        timeField = new JTextField();
        timeField.setBackground(new Color(-1));
        timeField.setColumns(3);
        timeField.setEditable(false);
        timeField.setForeground(new Color(-16777216));
        upperPanel.add(timeField);
        quizLabel = new JLabel();
        quizLabel.setForeground(new Color(-1));
        quizLabel.setText("제시어: ");
        upperPanel.add(quizLabel);
        quizField = new JTextField();
        quizField.setBackground(new Color(-1));
        quizField.setColumns(10);
        quizField.setEditable(false);
        quizField.setForeground(new Color(-16777216));
        upperPanel.add(quizField);
        hintLabel = new JLabel();
        hintLabel.setForeground(new Color(-1));
        hintLabel.setText("(전체 말하기: F1, 정답 말하기:F2)");
        upperPanel.add(hintLabel);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
