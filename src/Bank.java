import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bank {
    private String name;
    private Map<Integer, Account> accounts;
    private double totalTransactionFeeAmount;
    private double totalTransferAmount;
    private double transactionFlatFee;
    private double transactionPercentFee;

    public Bank(String name, double transactionFlatFee, double transactionPercentFee) {
        this.name = name;
        this.accounts = new HashMap<>();
        this.totalTransactionFeeAmount = 0;
        this.totalTransferAmount = 0;
        this.transactionFlatFee = transactionFlatFee;
        this.transactionPercentFee = transactionPercentFee;
    }

    public void createAccount(int accountId, String userName, double initialBalance) {
        if (accounts.containsKey(accountId)) {
            throw new IllegalArgumentException("Account with ID " + accountId + " already exists.");
        }
        accounts.put(accountId, new Account(accountId, userName, initialBalance));
    }

    public void performTransaction(Transaction transaction) {
        Account origin = accounts.get(transaction.getOriginatingAccountId());
        Account result = accounts.get(transaction.getResultingAccountId());

        if (origin == null || result == null) {
            throw new IllegalArgumentException("One or both accounts not found.");
        }

        double fee = calculateFee(transaction.getAmount());
        if (origin.getBalance() < transaction.getAmount() + fee) {
            throw new IllegalArgumentException("Insufficient funds.");
        }

        origin.withdraw(transaction.getAmount() + fee);
        result.deposit(transaction.getAmount());
        totalTransactionFeeAmount += fee;
        totalTransferAmount += transaction.getAmount();

        origin.addTransaction(transaction);
        result.addTransaction(transaction);
    }

    public void deposit(int accountId, double amount, String reason) {
        Account account = accounts.get(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Account not found.");
        }
        account.deposit(amount);
        account.addTransaction(new Transaction(amount, -1, accountId, reason));
    }

    public void withdraw(int accountId, double amount, String reason) {
        Account account = accounts.get(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Account not found.");
        }
        double fee = calculateFee(amount);
        if (account.getBalance() < amount + fee) {
            throw new IllegalArgumentException("Insufficient funds.");
        }
        account.withdraw(amount + fee);
        totalTransactionFeeAmount += fee;
        account.addTransaction(new Transaction(amount, accountId, -1, reason));
    }

    private double calculateFee(double amount) {
        return Math.max(transactionFlatFee, amount * transactionPercentFee / 100);
    }

    public double getTotalTransactionFeeAmount() {
        return totalTransactionFeeAmount;
    }

    public double getTotalTransferAmount() {
        return totalTransferAmount;
    }

    public Account getAccount(int accountId) {
        return accounts.get(accountId);
    }

    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }

    public String getName() {
        return name;
    }
}
