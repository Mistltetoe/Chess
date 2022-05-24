package view;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class LoginFrame extends JFrame implements ActionListener {
    JButton jb1, jb2;
    JTextField jtf1;
    JPasswordField jpf1;
    public LoginFrame() {
        setTitle("Login");// 窗口标题
        setSize(300, 180);// 窗口大小
        setLocationRelativeTo(null);// 窗口居中
        setDefaultCloseOperation(EXIT_ON_CLOSE);// 关闭窗口则退出虚拟机
        setLayout(new FlowLayout());// 设置布局流式布局
        JPanel jp = new JPanel(new GridLayout(4, 1));// 设置面板为表格布局4行1列
        // 第一行
        JPanel jp1 = new JPanel();
        JLabel jl1 = new JLabel("Account ");
        jtf1 = new JTextField(12);
        jp1.add(jl1);
        jp1.add(jtf1);
        jp.add(jp1);
        // 第二行
        JPanel jp2 = new JPanel();
        JLabel jl2 = new JLabel("Password ");
        jpf1 = new JPasswordField(12);
        jp2.add(jl2);
        jp2.add(jpf1);
        jp.add(jp2);
        // 第三行
        JPanel jp3 = new JPanel();
        jb1 = new JButton("Login");
        jb1.addActionListener(this);// 添加动作响应器
        jb2 = new JButton("reset");
        jb2.addActionListener(this);// 添加动作响应器
        jp3.add(jb1);
        jp3.add(jb2);
        jp.add(jp3);
        // 第四行
        JPanel jp4 = new JPanel();
        JLabel jl3 = new JLabel("Account: admin Password: 123");
        jl3.setForeground(Color.DARK_GRAY);
        jp4.add(jl3);
        jp.add(jp4);
        add(jp);
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();// 根据动作命令,来进行分别处理
        if (cmd.equals("Login")) {
            String id = jtf1.getText();// 取得用户名
            String key = new String(jpf1.getPassword());// 取得密码
            if (id.equals("admin") && key.equals("123")) {
                setVisible(false);
                new ChessGameFrame(1000,760).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "INPUT ERROR", "Notification", JOptionPane.ERROR_MESSAGE);
                clearText();
            }
        } else if (cmd.equals("reset")) {
            clearText();
        }
    }

    private void clearText() {
        jtf1.setText("");
        jpf1.setText("");
    }
}
