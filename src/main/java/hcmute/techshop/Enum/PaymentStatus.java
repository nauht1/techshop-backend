package hcmute.techshop.Enum;

public enum PaymentStatus {
    PENDING("Payment is pending"),
    SUCCESS("Payment was successful"),
    FAILED("Payment failed");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
