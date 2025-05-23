import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continueTransactions = true;

        while (continueTransactions) {
            ArrayList<Integer> quantities = new ArrayList<>();
            ArrayList<String> names = new ArrayList<>();
            ArrayList<Double> prices = new ArrayList<>();

            System.out.print("Enter customer name: ");
            String customerName = scanner.nextLine();
            System.out.print("Enter customer contact number: ");
            String contactNumber = scanner.nextLine();
           
            boolean addingProducts = true;
            while (addingProducts) {
                System.out.print("Enter product name (or 'done' to finish): ");
                String name = scanner.nextLine();

                if (name.equalsIgnoreCase("done")) {
                    addingProducts = false;
                } else {
                    System.out.print("Enter quantity: ");
                    int quantity = scanner.nextInt();
                    System.out.print("Enter price: ");
                    double price = scanner.nextDouble();
                    scanner.nextLine();

                    quantities.add(quantity);
                    names.add(name);
                    prices.add(price);
                }
            }

            double totalPrice = 0;
            System.out.println("\n--- Receipt ---");
            System.out.println("Customer Name: " + customerName);
            System.out.println("Contact Number: " + contactNumber);

            for (int i = 0; i < quantities.size(); i++) {
                System.out.println(quantities.get(i) + " x " + names.get(i) + " at PHP " + prices.get(i) + " = PHP " + (quantities.get(i) * prices.get(i)));
                totalPrice += quantities.get(i) * prices.get(i);
            }

            System.out.println("Total: PHP " + totalPrice);

            System.out.print("Enter mode of payment (GCash, PayMaya, Bank): ");
            String paymentMode = scanner.nextLine();

            System.out.print("Enter payment: PHP ");
            double payment = scanner.nextDouble();
            double change = payment - totalPrice;

            if (change >= 0) {
                System.out.println("Change: PHP " + change);
                System.out.println("Payment Mode: " + paymentMode);
            } else {
                System.out.println("Insufficient payment.");
            }

            System.out.print("Are you done? (yes/no): ");
            String choice = scanner.next();
            scanner.nextLine();
            continueTransactions = !choice.equalsIgnoreCase("yes");
        }

        System.out.println("Thank you !");
        scanner.close();
    }
}