import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.*;

public class finals {
    static Scanner sc = new Scanner(System.in);
    static Map<String, String> accounts = new HashMap<>();
    static ArrayList<String> items = new ArrayList<>();
    static ArrayList<Integer> qtys = new ArrayList<>();
    static ArrayList<Double> prices = new ArrayList<>();
    static String user = null;

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n[1] Register");
            System.out.println("[2] Login");
            System.out.println("[3] Exit");
            System.out.print("Choice: ");
            String c = sc.nextLine();

            if (c.equals("1")) {
                register();
            } else if (c.equals("2")) {
                login();
            } else if (c.equals("3")) {
                break;
            } else {
                System.out.println("Invalid.");
            }
        }
    }

    static void register() {
        System.out.print("Username: ");
        String u = sc.nextLine();
        if (accounts.containsKey(u)) {
            System.out.println("Exists.");
            return;
        }
        System.out.print("Password: ");
        String p = sc.nextLine();
        if (!Pattern.matches("^(?=.*[A-Z])(?=.*\\d).{6,}$", p)) {
            System.out.println("Invalid format.");
            return;
        }
        accounts.put(u, p);
        System.out.println("Registered.");
    }

    static void login() {
        System.out.print("Username: ");
        String u = sc.nextLine();
        System.out.print("Password: ");
        String p = sc.nextLine();
        if (accounts.containsKey(u) && accounts.get(u).equals(p)) {
            user = u;
            System.out.println("Welcome " + u);
            menu();
        } else {
            System.out.println("Wrong login.");
        }
    }

    static void menu() {
        items.clear();
        qtys.clear();
        prices.clear();
        while (true) {
            System.out.println("\n[1] Add");
            System.out.println("[2] Edit Qty");
            System.out.println("[3] Delete");
            System.out.println("[4] Show");
            System.out.println("[5] Pay");
            System.out.println("[6] Logout");
            System.out.print("Pick: ");
            String c = sc.nextLine();

            if (c.equals("1")) {
                add();
            } else if (c.equals("2")) {
                edit();
            } else if (c.equals("3")) {
                remove();
            } else if (c.equals("4")) {
                show();
            } else if (c.equals("5")) {
                pay();
                return;
            } else if (c.equals("6")) {
                return;
            } else {
                System.out.println("Invalid.");
            }
        }
    }

    static void add() {
        try {
            System.out.print("Item: ");
            String n = sc.nextLine();
            System.out.print("Qty: ");
            int q = Integer.parseInt(sc.nextLine());
            System.out.print("Price: ");
            double p = Double.parseDouble(sc.nextLine());
            items.add(n);
            qtys.add(q);
            prices.add(p);
            System.out.println("Added.");
        } catch (Exception e) {
            System.out.println("Error.");
        }
    }

    static void edit() {
        show();
        try {
            System.out.print("No: ");
            int i = Integer.parseInt(sc.nextLine()) - 1;
            if (i >= 0 && i < qtys.size()) {
                System.out.print("New qty: ");
                qtys.set(i, Integer.parseInt(sc.nextLine()));
                System.out.println("Changed.");
            } else {
                System.out.println("Wrong no.");
            }
        } catch (Exception e) {
            System.out.println("Error.");
        }
    }

    static void remove() {
        show();
        try {
            System.out.print("No: ");
            int i = Integer.parseInt(sc.nextLine()) - 1;
            if (i >= 0 && i < items.size()) {
                items.remove(i);
                qtys.remove(i);
                prices.remove(i);
                System.out.println("Removed.");
            } else {
                System.out.println("Wrong no.");
            }
        } catch (Exception e) {
            System.out.println("Error.");
        }
    }

    static void show() {
        if (items.isEmpty()) {
            System.out.println("None.");
            return;
        }
        System.out.println("List:");
        for (int i = 0; i < items.size(); i++) {
            double t = qtys.get(i) * prices.get(i);
            System.out.printf("%d. %s - %d x ₱%.2f = ₱%.2f\n", i + 1, items.get(i), qtys.get(i), prices.get(i), t);
        }
    }

    static void pay() {
        if (items.isEmpty()) {
            System.out.println("Empty.");
            return;
        }

        double all = 0;
        StringBuilder rec = new StringBuilder();
        rec.append("Time: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("\n");
        rec.append("Cashier: ").append(user).append("\n");
        rec.append("Items:\n");

        for (int i = 0; i < items.size(); i++) {
            double t = qtys.get(i) * prices.get(i);
            rec.append("- ").append(items.get(i)).append(" x ").append(qtys.get(i)).append(" @ ₱").append(prices.get(i)).append(" = ₱").append(t).append("\n");
            all += t;
        }

        rec.append("Total: ₱").append(String.format("%.2f", all)).append("\n");
        rec.append("--------------------------\n");

        try (FileWriter fw = new FileWriter("transactions.txt", true)) {
            fw.write(rec.toString());
            System.out.println("Saved.");
        } catch (IOException e) {
            System.out.println("Write failed.");
        }
    }
}
