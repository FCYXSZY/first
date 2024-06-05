package login;

import manageer.MainGUI;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

public class LoginForm extends JFrame implements ActionListener {
    /* 主窗体 */
    private static final long serialVersionUID = 1L;
    private BackgroundPanel jp; // 使用自定义背景图片的面板
    private JLabel title = new JLabel();
    private JLabel[] jlArray = {new JLabel("用户名"), new JLabel("密码"), new JLabel("")};
    private JButton[] jbArray = {new JButton("登录"), new JButton("清空")};
    private JTextField jtxtName = new JTextField();
    private JPasswordField jtxtPassword = new JPasswordField();

    public LoginForm() {
        jp = new BackgroundPanel("D:\\javae\\myjava\\first\\src\\login\\background.jpg"); // 指定背景图片的路径
        Toolkit kit = Toolkit.getDefaultToolkit();
        Image image = kit.getImage("D:\\javae\\myjava\\first\\src\\login\\logo1.gif"); // 指定图标文件的路径
        this.setIconImage(image); // 设置窗体的图标
        this.setTitle("登录");
        this.setResizable(true);
        this.setBounds(100, 100, 400, 300); // 调整窗口大小以适应背景图片
        this.setResizable(false);
        init();
    }

    private void init() {
        jp.setLayout(null);
        title.setBounds(30, 10, 300, 30);
        title.setText("欢迎使用本系统");
        title.setFont(new Font("宋体", Font.BOLD, 30));
        jp.add(title);
        for (int i = 0; i < 2; i++) {
            jlArray[i].setBounds(30, 50 + i * 40, 80, 26);
            jbArray[i].setBounds(50 + i * 120, 150, 80, 26);
            jp.add(jlArray[i]);
            jp.add(jbArray[i]);
            jbArray[i].addActionListener(this);
        }
        jtxtName.setBounds(110, 50, 180, 30); // 调整以适应新的面板大小
        jp.add(jtxtName);
        jtxtName.addActionListener(this);
        jtxtPassword.setBounds(110, 90, 180, 30); // 调整以适应新的面板大小
        jp.add(jtxtPassword);
        jtxtPassword.setEchoChar('*');
        jtxtPassword.addActionListener(this);
        jlArray[2].setBounds(10, 180, 300, 30);
        jlArray[2].setFont(new Font("宋体", Font.BOLD, 25));
        jp.add(jlArray[2]);
        this.add(jp);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jtxtName) {
            jtxtPassword.requestFocus();
        } else if (e.getSource() == jbArray[1]) {
            jlArray[2].setText("");
            jtxtName.setText("");
            jtxtPassword.setText("");
            jtxtName.requestFocus();
        } else {
            if (jtxtName.getText().equals("admin") &&
                    String.valueOf(jtxtPassword.getPassword()).equals("123456")) {
                jlArray[2].setText("登录成功");
                //new MainFrame().setVisible(true);
                //SwingUtilities.invokeLater(MainGUI::new);
                this.dispose(); // 释放当前窗体
            } else {
                jlArray[2].setText("非法用户名和密码");
            }
        }
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }

    // 自定义背景面板类
    class BackgroundPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private Image backgroundImage;

        public BackgroundPanel(String filePath) {
            backgroundImage = new ImageIcon(filePath).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
