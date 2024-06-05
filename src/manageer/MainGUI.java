package manageer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Map;
public class MainGUI {
    private JFrame frame;
    private Manager manager;
    private User user;

    public MainGUI(Manager manager, User user) {
        this.manager = manager;
        this.user = user;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("图书借阅管理系统");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(0, 1));

        JButton viewBooksButton = new JButton("查看所有书籍");
        JButton viewBorrowRecordsButton = new JButton("查看借阅记录");
        JButton findBookButton = new JButton("查询指定书目");
        JButton borrowBookButton = new JButton("借阅图书");
        JButton returnBookButton = new JButton("归还图书");


        menuPanel.add(viewBooksButton);
        menuPanel.add(viewBorrowRecordsButton);
        menuPanel.add(findBookButton);
        menuPanel.add(borrowBookButton);
        menuPanel.add(returnBookButton);


        if (user.isAdmin) {
            JButton modifyBookButton = new JButton("修改图书信息");

            menuPanel.add(modifyBookButton);

            modifyBookButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    modifyBookInfo();
                }
            });
        }

        frame.add(menuPanel, BorderLayout.WEST);

        JPanel mainPanel = new JPanel(new CardLayout());
        frame.add(mainPanel, BorderLayout.CENTER);

        JPanel viewBooksPanel = createViewBooksPanel();
        JPanel viewBorrowRecordsPanel = createViewBorrowRecordsPanel();
        JPanel findBookPanel = createFindBookPanel();
        JPanel borrowBookPanel = createBorrowBookPanel();
        JPanel returnBookPanel = createReturnBookPanel();

        mainPanel.add(viewBooksPanel, "viewBooks");
        mainPanel.add(viewBorrowRecordsPanel, "viewBorrowRecords");
        mainPanel.add(findBookPanel, "findBook");
        mainPanel.add(borrowBookPanel, "borrowBook");
        mainPanel.add(returnBookPanel, "returnBook");

        viewBooksButton.addActionListener(e -> switchPanel(mainPanel, "viewBooks"));
        viewBorrowRecordsButton.addActionListener(e -> switchPanel(mainPanel, "viewBorrowRecords"));
        findBookButton.addActionListener(e -> switchPanel(mainPanel, "findBook"));
        borrowBookButton.addActionListener(e -> switchPanel(mainPanel, "borrowBook"));
        returnBookButton.addActionListener(e -> switchPanel(mainPanel, "returnBook"));

        if (user.isAdmin) {
            JButton statisticsButton = new JButton("统计借阅情况");
            menuPanel.add(statisticsButton);
            JPanel statisticSPanel = createStatisticsPanel();
            mainPanel.add(statisticSPanel,"record");
            statisticsButton.addActionListener(e-> switchPanel(mainPanel,"record"));
        }
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                manager.saveBooks();
                manager.saveBorrowRecords();
            }
        });
    }

    private void switchPanel(JPanel mainPanel, String panelName) {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, panelName);
    }

    private JPanel createViewBooksPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = new JButton("刷新");
        refreshButton.addActionListener(e -> {
            textArea.setText("");
            for (Book book : manager.getAllBooks()) {
                textArea.append(String.format("书名: %s, ID: %s, 库存: %d, 价格: %.2f%n", book.name, book.id, book.nums, book.price));
            }
        });
        panel.add(refreshButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createViewBorrowRecordsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = new JButton("刷新");
        refreshButton.addActionListener(e -> {
            textArea.setText("");
            textArea.append(manager.viewBorrowRecords(user.username));
        });
        panel.add(refreshButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createFindBookPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextField textField = new JTextField();
        panel.add(textField, BorderLayout.NORTH);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton searchButton = new JButton("搜索");
        searchButton.addActionListener(e -> {
            String bookName = textField.getText();
            StringBuilder result = new StringBuilder();
            for (Book book : manager.getAllBooks()) {
                if (book.name.contains(bookName)) {
                    result.append(String.format("书名: %s, ID: %s, 库存: %d, 价格: %.2f%n", book.name, book.id, book.nums, book.price));
                }
            }
            if (result.length() > 0) {
                textArea.setText(result.toString());
            } else {
                textArea.setText("未找到书籍：" + bookName);
            }
        });
        panel.add(searchButton, BorderLayout.SOUTH);

        return panel;
    }


    private JPanel createBorrowBookPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextField textField = new JTextField();
        panel.add(textField, BorderLayout.NORTH);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton borrowButton = new JButton("借阅");
        borrowButton.addActionListener(e -> {
            String bookName = textField.getText();
            textArea.setText(manager.borrowBook(user.username, bookName));
        });
        panel.add(borrowButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createReturnBookPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextField textField = new JTextField();
        panel.add(textField, BorderLayout.NORTH);

        JButton returnButton = new JButton("归还");
        returnButton.addActionListener(e -> {
            String bookName = textField.getText();
            manager.returnBook(user.username, bookName);
        });
        panel.add(returnButton, BorderLayout.SOUTH);

        return panel;
    }

    private void modifyBookInfo() {
        JFrame modifyFrame = new JFrame("修改图书信息");
        modifyFrame.setSize(600, 400);
        modifyFrame.setLayout(new GridLayout(6, 2));

        JLabel nameLabel = new JLabel("书名:||(输入新增可增添图书)");
        JTextField nameField = new JTextField();
        JLabel newNameLabel = new JLabel("新书名:");
        JTextField newNameField = new JTextField();
        JLabel newIdLabel = new JLabel("新ID:");
        JTextField newIdField = new JTextField();
        JLabel newPriceLabel = new JLabel("新价格:");
        JTextField newPriceField = new JTextField();
        JLabel newNumLabel = new JLabel("新数量:");
        JTextField newNumField = new JTextField();
        JButton modifyButton = new JButton("修改");

        modifyFrame.add(nameLabel);
        modifyFrame.add(nameField);
        modifyFrame.add(newNameLabel);
        modifyFrame.add(newNameField);
        modifyFrame.add(newIdLabel);
        modifyFrame.add(newIdField);
        modifyFrame.add(newPriceLabel);
        modifyFrame.add(newPriceField);
        modifyFrame.add(newNumLabel);
        modifyFrame.add(newNumField);

        modifyFrame.add(new JLabel()); // 占位
        modifyFrame.add(modifyButton);

        modifyButton.addActionListener(e -> {
            String name = nameField.getText();
            String newName = newNameField.getText();
            String newId = newIdField.getText();
            double newPrice = Double.parseDouble(newPriceField.getText());
            int newNum = Integer.parseInt(newNumField.getText());
            manager.modifyBookInfo(name, newName, newId, newPrice,newNum);
            manager.saveBooks();
            modifyFrame.dispose();
        });


        modifyFrame.setVisible(true);

    }

    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = new JButton("刷新");
        refreshButton.addActionListener(e -> {
            textArea.setText("");
            StringBuilder statistics = new StringBuilder();
            for (Map.Entry<String, List<String>> entry : manager.viewBorrowRecords().entrySet()) {
                statistics.append(entry.getKey()).append(": ").append(String.join(", ", entry.getValue())).append("\n");
            }
            textArea.append(statistics.toString());
        });
        panel.add(refreshButton, BorderLayout.SOUTH);

        return panel;
    }

}