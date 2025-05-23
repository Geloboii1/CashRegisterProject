import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.regex.*;

public class transactions {
    private static ArrayList<String> usernames = new ArrayList<>();
    private static ArrayList<String> passwords = new ArrayList<>();
    private static ArrayList<String> itemNames = new ArrayList<>();
    private static ArrayList<Integer> quantities = new ArrayList<>();
    private static ArrayList<Double> prices = new ArrayList<>();
    private static String currentUser = null;
    private static Scanner scanner = new Scanner(System.in);
    private static final String TRANSACTIONS_FILE = "transactions.txt";
    private static final String USERS_FILE = "users.txt";

    public static void main(String[] args) {
        loadUsers();
        boolean running = true;

        while (running) {
            if (currentUser == null) {
                showMainMenu();
                int choice = getIntInput("Enter choice: ");
                switch (choice) {
                    case 1: login(); break;
                    case 2: signup(); break;
                    case 3: running = false; break;
                    default: System.out.println("Invalid choice");
                }
            } else {
                showCashierMenu();
                int choice = getIntInput("Enter choice: ");
                switch (choice) {
                    case 1: addOrder(); break;
                    case 2: updateOrder(); break;
                    case 3: removeOrder(); break;
                    case 4: displayOrders(); break;
                    case 5: checkout(); break;
                    case 6: currentUser = null; break;
                    default: System.out.println("Invalid choice");
                }
            }
        }
        saveUsers();
    }

    private static void showMainMenu() {
        System.out.println("\nCash Register System");
        System.out.println("1. Login");
        System.out.println("2. Signup");
        System.out.println("3. Exit");
    }

    private static void showCashierMenu() {
        System.out.println("\nWelcome, " + currentUser);
        System.out.println("1. Add Order");
        System.out.println("2. Update Order Quantity");
        System.out.println("3. Remove Order");
        System.out.println("4. Display Orders");
        System.out.println("5. Checkout");
        System.out.println("6. Logout");
    }

    private static void login() {
        System.out.println("\nLogin");
        String username = getStringInput("Username: ");
        String password = getStringInput("Password: ");

        for (int i = 0; i < usernames.size(); i++) {
            if (usernames.get(i).equals(username) && passwords.get(i).equals(password)) {
                currentUser = username;
                System.out.println("Login successful");
                return;
            }
        }
        System.out.println("Invalid username or password");
    }

    private static void signup() {
        System.out.println("\nSignup");
        String username;
        while (true) {
            username = getStringInput("Username: ");
            if (isValidUsername(username)) {
                if (usernames.contains(username)) {
                    System.out.println("Username already taken");
                } else {
                    break;
                }
            } else {
                System.out.println("Invalid username (3-20 chars, letters and numbers only)");
            }
        }

        String password;
        while (true) {
            password = getStringInput("Password: ");
            if (isValidPassword(password)) {
                break;
            } else {
                System.out.println("Invalid password (6+ chars, at least one letter and number)");
            }
        }

        usernames.add(username);
        passwords.add(password);
        System.out.println("Signup successful");
    }

    private static boolean isValidUsername(String username) {
        return Pattern.matches("^[a-zA-Z0-9]{3,20}$", username);
    }

    private static boolean isValidPassword(String password) {
        return Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d).{6,}$", password);
    }

    private static void addOrder() {
        System.out.println("\nAdd Order");
        String name = getStringInput("Item name: ");
        int quantity = getIntInput("Quantity: ");
        double price = getDoubleInput("Price: ");

        itemNames.add(name);
        quantities.add(quantity);
        prices.add(price);
        System.out.println("Order added");
    }

    private static void updateOrder() {
        if (itemNames.isEmpty()) {
            System.out.println("No orders to update");
            return;
        }

        displayOrders();
        int index = getIntInput("Enter order number to update: ") - 1;

        if (index >= 0 && index < itemNames.size()) {
            int newQuantity = getIntInput("Enter new quantity: ");
            quantities.set(index, newQuantity);
            System.out.println("Order updated");
        } else {
            System.out.println("Invalid order number");
        }
    }

    private static void removeOrder() {
        if (itemNames.isEmpty()) {
            System.out.println("No orders to remove");
            return;
        }

        displayOrders();
        int index = getIntInput("Enter order number to remove: ") - 1;

        if (index >= 0 && index < itemNames.size()) {
            itemNames.remove(index);
            quantities.remove(index);
            prices.remove(index);
            System.out.println("Order removed");
        } else {
            System.out.println("Invalid order number");
        }
    }

    private static void displayOrders() {
        if (itemNames.isEmpty()) {
            System.out.println("No orders");
            return;
        }

        System.out.println("\nOrders:");
        System.out.println("--------------------------------------------------");
        System.out.printf("%-5s %-20s %-10s %-10s %-10s%n", 
                         "No.", "Item", "Quantity", "Price", "Total");
        System.out.println("--------------------------------------------------");

        double grandTotal = 0;
        for (int i = 0; i < itemNames.size(); i++) {
            double total = quantities.get(i) * prices.get(i);
            grandTotal += total;
            System.out.printf("%-5d %-20s %-10d $%-9.2f $%-9.2f%n", 
                             (i + 1), itemNames.get(i), quantities.get(i), 
                             prices.get(i), total);
        }

        System.out.println("--------------------------------------------------");
        System.out.printf("%46s $%-9.2f%n", "Grand Total:", grandTotal);
    }

    private static void checkout() {
        if (itemNames.isEmpty()) {
            System.out.println("No orders to checkout");
            return;
        }

        displayOrders();
        System.out.println("\nCheckout completed");
        logTransaction();
        itemNames.clear();
        quantities.clear();
        prices.clear();
    }

    private static void logTransaction() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TRANSACTIONS_FILE, true))) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            writer.println("Transaction Date: " + timestamp);
            writer.println("Cashier: " + currentUser);
            writer.println("Items Purchased:");

            double totalAmount = 0;
            for (int i = 0; i < itemNames.size(); i++) {
                double total = quantities.get(i) * prices.get(i);
                totalAmount += total;
                writer.printf("- %s (Qty: %d, Price: $%.2f, Total: $%.2f)%n", 
                            itemNames.get(i), quantities.get(i), 
                            prices.get(i), total);
            }

            writer.printf("Total Amount: $%.2f%n", totalAmount);
            writer.println("----------------------------------------");
        } catch (IOException e) {
            System.out.println("Error saving transaction");
        }
    }

    private static void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    usernames.add(parts[0]);
                    passwords.add(parts[1]);
                }
            }
        } catch (IOException e) {
        }
    }

    private static void saveUsers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (int i = 0; i < usernames.size(); i++) {
                writer.println(usernames.get(i) + "," + passwords.get(i));
            }
        } catch (IOException e) {
            System.out.println("Error saving users");
        }
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
}