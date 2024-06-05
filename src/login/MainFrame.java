package login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame implements ActionListener {
    private JMenuBar jMenuBar1;
    private JMenu jMenu1;
    private JMenu jMenu2;
    private JMenuItem jMenuItem1;
    private JMenuItem jMenuItem2;
    private JMenuItem jMenuItem3;
    private JMenuItem jMenuItem4;
    private JMenuItem jMenuItem5;
    private JLabel JLabel1;

    public MainFrame() {
        initComponents();
    }

    private void initComponents() {
        JLabel1 = new JLabel(" ");//用于显示登录成功
        JLabel1.setBounds(20, 100, 300, 30);
        add(JLabel1);

        jMenuBar1 = new JMenuBar();
        jMenu1 = new JMenu("系统设置");
        jMenuItem1 = new JMenuItem("退出");
        jMenu2 = new JMenu();
        jMenuItem2 = new JMenuItem();
        jMenuItem3 = new JMenuItem();
        jMenuItem4 = new JMenuItem();
        jMenuItem5 = new JMenuItem();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    jMenu1.setText("系统设置");
    jMenuItem1.setText("退出");

        jMenu1.add(jMenuItem1);
        jMenuItem1.addActionListener(this);
        jMenuBar1.add(jMenu1);
        jMenu2.setText("用户管理");
        jMenuItem2.setText("添加用户");
        jMenu2.add(jMenuItem2);
        jMenuItem2.addActionListener(this);
        jMenuItem3.setText("浏览用户");
        jMenu2.add(jMenuItem3);
        jMenuItem3.addActionListener(this);
        jMenuItem4.setText("修改用户");
        jMenu2.add(jMenuItem4);
        jMenuItem4.addActionListener(this);
        jMenuItem5.setText("删除用户");
        jMenu2.add(jMenuItem5);
        jMenuItem5.addActionListener(this);
        jMenuBar1.add(jMenu2);
        setJMenuBar(jMenuBar1);
        setBounds(200, 200, 300, 300);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jMenuItem1) {
            System.exit(0);
        } else {
            JLabel1.setText("您的选项是" + "\"" + e.getActionCommand() + "\"");
            this.setVisible(true);
        }
    }
}
