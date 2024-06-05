package manageer;
import java.util.*;
import java.io.*;
class Book {
    String name;
    String id;
    int nums;
    double price;

    public Book(String name, String id, double price, int nums) {
        this.name = name;
        this.id = id;
        this.price = price;
        this.nums = nums;
    }

    public void setNums(int num) {
        this.nums += num;
        System.out.println(String.format("当前%s的数量为%d", name, nums));
    }

    public String toString(){
        return this.name+","+this.id+","+this.nums+","+this.price;
    }
}

class User {
    String username;
    boolean isAdmin;

    public User(String username, boolean isAdmin) {
        this.username = username;
        this.isAdmin = isAdmin;
    }
}

public class Manager {
    private Map<String, Book> mp = new TreeMap<>();
    private Map<String, List<String>> borrowRecords = new HashMap<>(); // 记录借阅信息
    private double summoney = 0;
    public Manager() {
        loadBooks();
        loadBorrowRecords();
    }
    Book adminFind(String name){
        return mp.get(name);
    }
    void addBook(String name, String id, int nums, double price) {
        mp.put(name, new Book(name, id, price, nums));
    }

    private void loadBooks() {
        try (BufferedReader br = new BufferedReader(new FileReader("books.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String name = parts[0];
                    String id = parts[1];
                    int nums = Integer.parseInt(parts[2]);
                    double price = Double.parseDouble(parts[3]);
                    mp.put(name, new Book(name, id, price, nums));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading books: " + e.getMessage());
        }
    }

    private void loadBorrowRecords() {
        try (BufferedReader br = new BufferedReader(new FileReader("borrow_records.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String username = parts[0];
                    String[] books = parts[1].split(",");
                    borrowRecords.put(username, new ArrayList<>(Arrays.asList(books)));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading borrow records: " + e.getMessage());
        }
    }

    public void saveBooks() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("books.txt"))) {
            for (Book book : mp.values()) {
                bw.write(book.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    public void saveBorrowRecords() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("borrow_records.txt"))) {
            for (Map.Entry<String, List<String>> entry : borrowRecords.entrySet()) {
                bw.write(entry.getKey() + ":" + String.join(",", entry.getValue()));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving borrow records: " + e.getMessage());
        }
    }
    void bookIn(String... books) {
        for (var book : books) {
            if (mp.containsKey(book)) {
                mp.get(book).setNums(1);
            } else {
                System.out.println("书籍不存在：" + book);
            }
        }
    }

    void bookOut(String... books) {
        for (var book : books) {
            if (mp.containsKey(book) && mp.get(book).nums > 0) {
                mp.get(book).setNums(-1);
            } else {
                System.out.println("书籍库存不足：" + book);
            }
        }
    }

    void findBook(String book) {
        if (mp.containsKey(book)) {
            Book target = mp.get(book);
            System.out.println(String.format("书名: %s, ID: %s, 库存: %d, 价格: %.2f", target.name, target.id, target.nums, target.price));
        } else {
            System.out.println("未找到书籍：" + book);
        }
    }

    String borrowBook(String username, String... books) {
        for (var book : books) {

            if (mp.containsKey(book) && mp.get(book).nums > 0) {
                mp.get(book).setNums(-1);
                borrowRecords.computeIfAbsent(username, k -> new ArrayList<>()).add(book);
                System.out.println(username + " 借阅了 " + book);
                return username + " 借阅了 " + book;
            } else {
                System.out.println("书籍库存不足或不存在：" + book);
                return "书籍库存不足或不存在：" + book;
            }
        }
        return "服务结束，祝您阅读愉快";
    }

    void returnBook(String username, String... books) {
        for (var book : books) {
            if (borrowRecords.containsKey(username) && borrowRecords.get(username).contains(book)) {
                mp.get(book).setNums(1);
                borrowRecords.get(username).remove(book);
                System.out.println(username + " 归还了 " + book);
            } else {
                System.out.println("书籍未被借阅或不存在：" + book);
            }
        }
    }

    String viewBorrowRecords(String username) {
        if (borrowRecords.containsKey(username)) {
            System.out.println(username + " 的借阅记录：" + borrowRecords.get(username));
            return username + " 的借阅记录：" + borrowRecords.get(username);
        } else {
            System.out.println(username + " 没有借阅记录。");
            return username + " 没有借阅记录。";
        }
    }

    void modifyBookInfo(String name, String newName, String newId, double newPrice,int newnums) {
        if(name.equals("新增")){
            mp.put(newName,new Book(newName,newId,newPrice,newnums));
            return;
        }
        if (mp.containsKey(name)) {
            Book book = mp.get(name);
            book.name = newName;
            book.id = newId;
            book.price = newPrice;
            book.nums = newnums;
            mp.remove(name);
            mp.put(newName,book);
            System.out.println("书籍信息已更新：" + newName);
        } else {
            System.out.println("书籍不存在：" + name);
        }
    }

    void viewAllBooks() {
        System.out.println("当前所有书籍信息：");
        for (Book book : mp.values()) {
            System.out.println(String.format("书名: %s, ID: %s, 库存: %d, 价格: %.2f", book.name, book.id, book.nums, book.price));
        }
    }
    public List<Book> getAllBooks() {
        return new ArrayList<>(mp.values());
    }
    public Map<String, List<String>> viewBorrowRecords() {
        return new HashMap<>(borrowRecords);
    }
}