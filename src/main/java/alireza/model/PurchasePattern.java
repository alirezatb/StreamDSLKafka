package alireza.model;

import java.util.Date;
import java.util.Objects;

public class PurchasePattern {
    private String zipCode;
    private String item;
    private Date date;
    private double amount;

    public String getZipCode() {
        return zipCode;
    }

    public String getItem() {
        return item;
    }

    public Date getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }
    private PurchasePattern(Builder builder){
        this.zipCode = builder.ZipCode;
        this.item = builder.item;
        this.amount = builder.amount;
        this.date = builder.date;
    }
    public static Builder builder(){return new Builder();}
    public static Builder builder(Purchase purchase){
        return new Builder(purchase);
    }

    @Override
    public String toString() {
        return "PurchasePattern{" +
                "zipCode='" + zipCode + '\'' +
                ", item='" + item + '\'' +
                ", date=" + date +
                ", amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PurchasePattern)) return false;

        PurchasePattern that = (PurchasePattern) o;

        if (Double.compare(that.amount, amount) != 0) return false;
        if (zipCode != null ? !zipCode.equals(that.zipCode) : that.zipCode != null) return false;
        return item != null ? item.equals(that.item) : that.item == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = zipCode != null ? zipCode.hashCode() : 0;
        result = 31 * result + (item != null ? item.hashCode() : 0);
        temp = Double.doubleToLongBits(amount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public static final class Builder {
        private String ZipCode;
        private String item;
        private Date date;
        private double amount;

        public Builder setZipCode(String zipCode) {
            ZipCode = zipCode;
            return this;
        }

        public Builder setItem(String item) {
            this.item = item;
            return this;
        }

        public Builder setDate(Date date) {
            this.date = date;
            return this;
        }

        public Builder setAmount(double amount) {
            this.amount = amount;
            return this;
        }
        private Builder(){}
        private Builder(Purchase purchase){
            this.ZipCode = purchase.getZipCode();
            this.item = purchase.getItemPurchased();
            this.date = purchase.getPurchaseDate();
            this.amount = purchase.getPrice() * purchase.getQuantity();
        }

        public PurchasePattern build(){return new PurchasePattern(this);}
    }
}
