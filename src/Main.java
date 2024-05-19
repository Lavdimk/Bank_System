import java.util.Scanner;

public class Main {
    private static Bank bank;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Bank System");
        System.out.print("Enter bank name: ");
        String bankName = scanner.nextLine();
        System.out.print("Enter transaction flat fee: ");
        double flatFee = scanner.nextDouble();
        System.out.print("Enter transaction percent fee: ");
        double percentFee = scanner.nextDouble();

        bank = new Bank(bankName, flatFee, percentFee);

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Create an account");
            System.out.println("2. Deposit money");
            System.out.println("3. Withdraw money");
            System.out.println("4. Perform a transaction");
            System.out.println("5. Check account balance");
            System.out.println("6. List all accounts");
            System.out.println("7. List account transactions");
            System.out.println("8. Check bank total transaction fee amount");
            System.out.println("9. Check bank total transfer amount");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    createAccount(scanner);
                    break;
                case 2:
                    depositMoney(scanner);
                    break;
                case 3:
                    withdrawMoney(scanner);
                    break;
                case 4:
                    performTransaction(scanner);
                    break;
                case 5:
                    checkAccountBalance(scanner);
                    break;
                case 6:
                    listAllAccounts();
                    break;
                case 7:
                    listAccountTransactions(scanner);
                    break;
                case 8:
                    System.out.printf("Total transaction fee amount: $%.2f%n", bank.getTotalTransactionFeeAmount());
                    break;
                case 9:
                    System.out.printf("Total transfer amount: $%.2f%n", bank.getTotalTransferAmount());
                    break;
                case 0:
                    System.out.println("Exiting. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void createAccount(Scanner scanner) {
        System.out.print("Enter account ID: ");
        int accountId = scanner.nextInt();
        scanner.nextLine();  // consume newline
        System.out.print("Enter user name: ");
        String userName = scanner.nextLine();
        System.out.print("Enter initial balance: ");
        double initialBalance = scanner.nextDouble();

        try {
            bank.createAccount(accountId, userName, initialBalance);
            System.out.println("Account created successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void depositMoney(Scanner scanner) {
        System.out.print("Enter account ID: ");
        int accountId = scanner.nextInt();
        System.out.print("Enter amount to deposit: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();  // consume newline
        System.out.print("Enter deposit reason: ");
        String reason = scanner.nextLine();

        bank.deposit(accountId, amount, reason);
        System.out.println("Deposit successful.");
    }

    private static void withdrawMoney(Scanner scanner) {
        System.out.print("Enter account ID: ");
        int accountId = scanner.nextInt();
        System.out.print("Enter amount to withdraw: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();  // consume newline
        System.out.print("Enter withdrawal reason: ");
        String reason = scanner.nextLine();

        try {
            bank.withdraw(accountId, amount, reason);
            System.out.println("Withdrawal successful.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void performTransaction(Scanner scanner) {
        System.out.print("Enter originating account ID: ");
        int originatingAccountId = scanner.nextInt();
        System.out.print("Enter resulting account ID: ");
        int resultingAccountId = scanner.nextInt();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();  // consume newline
        System.out.print("Enter transaction reason: ");
        String reason = scanner.nextLine();

        Account originatingAccount = bank.getAccount(originatingAccountId);
        Account resultingAccount = bank.getAccount(resultingAccountId);

        if (originatingAccount == null) {
            System.out.println("Error: Originating account ID " + originatingAccountId + " does not exist.");
            return;
        }

        if (resultingAccount == null) {
            System.out.println("Error: Resulting account ID " + resultingAccountId + " does not exist.");
            return;
        }

        Transaction transaction = new Transaction(amount, originatingAccountId, resultingAccountId, reason);
        try {
            bank.performTransaction(transaction);
            System.out.println("Transaction successful.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void checkAccountBalance(Scanner scanner) {
        System.out.print("Enter account ID: ");
        int accountId = scanner.nextInt();

        Account account = bank.getAccount(accountId);
        if (account != null) {
            System.out.printf("Account balance: $%.2f%n", account.getBalance());
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void listAllAccounts() {
        System.out.println("Accounts in " + bank.getName() + ":");
        for (Account account : bank.getAllAccounts()) {
            System.out.printf("ID: %d, User: %s, Balance: $%.2f%n", account.getAccountId(), account.getUserName(), account.getBalance());
        }
    }

    private static void listAccountTransactions(Scanner scanner) {
        System.out.print("Enter account ID: ");
        int accountId = scanner.nextInt();

        Account account = bank.getAccount(accountId);
        if (account != null) {
            System.out.println("Transactions for account " + accountId + ":");
            for (Transaction transaction : account.getTransactions()) {
                System.out.printf("Amount: $%.2f, Origin: %d, Result: %d, Reason: %s%n",
                        transaction.getAmount(),
                        transaction.getOriginatingAccountId(),
                        transaction.getResultingAccountId(),
                        transaction.getReason());
            }
        } else {
            System.out.println("Account not found.");
        }
    }
}
