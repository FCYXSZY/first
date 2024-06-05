package manageer;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        Scanner scanner = new Scanner(System.in);

        // 预添加一些书籍
        manager.addBook("Java Programming", "001", 10, 59.99);
        manager.addBook("Python Programming", "002", 5, 49.99);
        manager.addBook("C++ Programming", "003", 8, 69.99);

        // 模拟用户登录
        User currentUser = login(scanner);

        // 根据用户权限显示菜单选项
        if (currentUser != null) {
            if (currentUser.isAdmin) {
                showAdminMenu(scanner, manager);
            } else {
                showUserMenu(scanner, manager, currentUser);
            }
        } else {
            System.out.println("登录失败，退出系统。");
        }

        scanner.close();
    }

    private static User login(Scanner scanner) {
        System.out.println("请输入用户名：");
        String username = scanner.nextLine();
        System.out.println("请输入密码：");
        String password = scanner.nextLine();
        // 此处可以根据实际情况进行用户认证，这里简单模拟
        if ("admin".equals(username) && "admin123".equals(password)) {
            return new User(username, true); // 管理员用户
        } else {
            return new User(username, false); // 普通用户
        }
    }

    private static void showUserMenu(Scanner scanner, Manager manager, User currentUser) {
        // 用户菜单
        System.out.println("欢迎，" + currentUser.username);
        while (true) {
            System.out.println("请选择操作：");
            System.out.println("1. 查看所有书籍");
            System.out.println("2. 查看借阅记录");
            System.out.println("3. 查询指定书目");
            System.out.println("4. 借阅图书");
            System.out.println("5. 归还图书");
            System.out.println("6. 退出");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume the newline character

            switch (choice) {
                case 1:
                    manager.viewAllBooks();
                    break;
                case 2:
                    manager.viewBorrowRecords(currentUser.username);
                    break;
                case 3:
                    System.out.println("请输入书名：");
                    String bookName = scanner.nextLine();
                    manager.findBook(bookName);
                    break;
                case 4:
                    System.out.println("请输入要借阅的书名：");
                    bookName = scanner.nextLine();
                    manager.borrowBook(currentUser.username, bookName);
                    break;
                case 5:
                    System.out.println("请输入要归还的书名：");
                    bookName = scanner.nextLine();
                    manager.returnBook(currentUser.username, bookName);
                    break;
                case 6:
                    System.out.println("退出系统");
                    return;
                default:
                    System.out.println("无效选择，请重试。");
            }
            System.out.println("输入回车继续...");
            scanner.nextLine();
        }
    }
    private static void showAdminMenu(Scanner scanner, Manager manager) {
        // 管理员菜单
        System.out.println("欢迎，管理员");
        while (true) {
            System.out.println("请选择操作：");
            System.out.println("1. 查看所有书籍");
            System.out.println("2. 查看借阅记录");
            System.out.println("3. 查询指定书目");
            System.out.println("4. 借阅图书");
            System.out.println("5. 归还图书");
            System.out.println("6. 修改图书信息");
            System.out.println("7. 统计借阅情况");
            System.out.println("8. 退出");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume the newline character

            switch (choice) {
                case 1:
                    manager.viewAllBooks();
                    break;
                case 2:
                    System.out.println("请输入用户名：");
                    String username = scanner.nextLine();
                    manager.viewBorrowRecords(username);
                    break;
                case 3:
                    System.out.println("请输入书名：");
                    String bookName = scanner.nextLine();
                    manager.findBook(bookName);
                    break;
                case 4:
                    System.out.println("请输入要借阅的书名：");
                    bookName = scanner.nextLine();
                    manager.borrowBook("admin", bookName);
                    break;
                case 5:
                    System.out.println("请输入要归还的书名：");
                    bookName = scanner.nextLine();
                    manager.returnBook("admin", bookName);
                    break;
                case 6:
                    modifyBookInfo(scanner, manager);
                    break;
                case 7:
                    // 统计借阅情况
                    break;
                case 8:
                    System.out.println("退出系统");
                    return;
                default:
                    System.out.println("无效选择，请重试。");
            }
            System.out.println("输入回车继续...");
            scanner.nextLine();
        }
    }

    private static void modifyBookInfo(Scanner scanner, Manager manager) {
        System.out.println("请输入要修改的书名：");
        String name = scanner.nextLine();
        System.out.println("请输入修改内容\n1:书名\n2:id\n3:价格\n4:退出");

        int choice = scanner.nextInt();
        if(choice==4)return;

        Book currt = manager.adminFind(name);
        if(currt==null)return;
        String newName = currt.name;
        String newId = currt.id;
        double newPrice = currt.price;
        while(true) {
            switch (choice) {
                case 1:
                    System.out.println("请输入新的书名：");
                    newName = scanner.nextLine();
                    newName = scanner.nextLine();
                    break;
                case 2:
                    System.out.println("请输入新的ID: ");
                    newId = scanner.nextLine();
                    newId = scanner.nextLine();
                    break;
                case 3:
                    System.out.println("请输入新的价格：");
                    newPrice = scanner.nextDouble();
                    break;
                default:
                    break;
            }
            System.out.println("请输入修改内容\n1:书名\n2:id\n3:价格\n4:退出");
            if(choice==4)break;
            choice = scanner.nextInt();
        }
        System.out.println("请输入新的数量：");
        int newNum= scanner.nextInt();
        scanner.nextLine(); // consume the newline character
        manager.modifyBookInfo(name, newName, newId, newPrice,newNum);
    }
}
