package manageer;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;
public class LoginGUI {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private Manager manager;
    private Map<String, String> users;
    public LoginGUI(Manager manager) {
        this.manager = manager;
        users = new HashMap<>();
        loadUsers();
        initialize();
    }
    private void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    users.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 保存用户信息
    private void saveUsers() {
        try (FileWriter writer = new FileWriter("users.txt")) {
            for (Map.Entry<String, String> entry : users.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initialize() {
        frame = new JFrame("登录");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setSize(500, 300);
        frame.setBounds(400, 400, 400, 300);
        frame.setLayout(new GridLayout(8, 1));
        JLabel welcomeLabel = new JLabel("欢迎使用图书管理系统", SwingConstants.CENTER);
        //frame.add(new JLabel());
        frame.add(new JLabel(),BorderLayout.CENTER);
        frame.add(new JLabel(),BorderLayout.CENTER);
        frame.add(welcomeLabel, BorderLayout.CENTER);


        JLabel usernameLabel = new JLabel("用户名:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("密码:");
        frame.add(new JLabel());
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("登录");
        JButton registerButton = new JButton("注册");
        frame.add(new JLabel());frame.add(new JLabel());
        frame.add(usernameLabel);//frame.add(new JLabel());
        frame.add(usernameField);//frame.add(new JLabel());
        //frame.add(new JLabel());
        frame.add(passwordLabel);//frame.add(new JLabel());
        frame.add(passwordField);//frame.add(new JLabel());
        frame.add(new JLabel()); // 占位
        frame.add(new JLabel());
        frame.add(new JLabel());
        frame.add(new JLabel());
        frame.add(loginButton,BorderLayout.SOUTH);
        //frame.add(new JLabel()); // 占位
        frame.add(registerButton,BorderLayout.SOUTH);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (users.containsKey(username) && users.get(username).equals(password)){
                    // 假设管理员用户名是 "admin"，密码是 "admin123"
                    if ("admin".equals(username) && "admin123".equals(password)) {
                        frame.dispose();
                        new MainGUI(manager, new User(username, true));
                    } else {
                        frame.dispose();
                        new MainGUI(manager, new User(username, false));
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "用户名或密码错误！");
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (!username.isEmpty() && !password.isEmpty()) {
                    try {
                        FileWriter writer = new FileWriter("users.txt", true);
                        writer.write(username + ":" + password + "\n");
                        writer.close();
                        JOptionPane.showMessageDialog(frame, "注册成功！");
                        loadUsers();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "注册失败！");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "用户名和密码不能为空！");
                }
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                manager.saveBooks();
                manager.saveBorrowRecords();
            }
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Manager manager = new Manager();
//        manager.addBook("Java Programming", "001", 10, 59.99);
//        manager.addBook("Python Programming", "002", 5, 49.99);
//        manager.addBook("C++ Programming", "003", 8, 69.99);

        new LoginGUI(manager);
    }
}
