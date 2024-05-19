public class Transaction {
    private double amount;
    private int originatingAccountId;
    private int resultingAccountId;
    private String reason;

    public Transaction(double amount, int originatingAccountId, int resultingAccountId, String reason) {
        this.amount = amount;
        this.originatingAccountId = originatingAccountId;
        this.resultingAccountId = resultingAccountId;
        this.reason = reason;
    }

    public double getAmount() {
        return amount;
    }

    public int getOriginatingAccountId() {
        return originatingAccountId;
    }

    public int getResultingAccountId() {
        return resultingAccountId;
    }

    public String getReason() {
        return reason;
    }
}
