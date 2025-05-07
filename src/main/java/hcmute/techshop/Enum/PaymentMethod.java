package hcmute.techshop.Enum;

public enum PaymentMethod {
    COD("Cash on Delivery"),
    VNPAY("VNPay Payment");

    private final String description;

    PaymentMethod(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
