package alireza.model;

import java.util.Date;
import java.util.List;

public class CorrelatedPurchase {
    private String customerID;
    private double totalAmount;
    private Date firstPurchasedDate;
    private Date secondPurchasedDate;
    private List<String> purchasedItems;

    public String getCustomerID() {
        return customerID;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public Date getfirstPurchasedDate() {
        return firstPurchasedDate;
    }

    public Date getsecondPurchasedDate() {
        return secondPurchasedDate;
    }

    public List<String> getPurchasedItems() {
        return purchasedItems;
    }
    private CorrelatedPurchase(){}
    private CorrelatedPurchase(Builder builder){
        this.customerID = builder.customerID;
        this.firstPurchasedDate = builder.firstPurchasedDate;
        this.secondPurchasedDate = builder.secondPurchasedDate;
        this.purchasedItems = builder.purchasedItem;
        this.totalAmount = builder.totalAmount;
    }

    @Override
    public String toString() {
        return "CorrelatedPurchase{" +
                "customerID='" + customerID + '\'' +
                ", totalAmount=" + totalAmount +
                ", firstPurchasedDate=" + firstPurchasedDate +
                ", secondPurchasedDate=" + secondPurchasedDate +
                ", purchasedItems=" + purchasedItems +
                '}';
    }

    public static Builder builder(CorrelatedPurchase correlatedPurchase){
        return new Builder(correlatedPurchase);
    }
    public static Builder builder(){
        return new Builder();
    }

    public static final class Builder {
        private String customerID;
        private double totalAmount;
        private Date firstPurchasedDate;
        private Date secondPurchasedDate;
        private List<String> purchasedItem;

        private Builder(){}
        private Builder(CorrelatedPurchase correlatedPurchase){
            this.customerID = correlatedPurchase.customerID;
            this.totalAmount = correlatedPurchase.totalAmount;
            this.firstPurchasedDate = correlatedPurchase.firstPurchasedDate;
            this.secondPurchasedDate = correlatedPurchase.secondPurchasedDate;
            this.purchasedItem = correlatedPurchase.purchasedItems;
        }

        public Builder setCustomerID(String customerID) {
            this.customerID = customerID;
            return this;
        }

        public Builder setTotalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public Builder setFirstPurchasedDate(Date firstPurchasedDate) {
            this.firstPurchasedDate = firstPurchasedDate;
            return this;
        }

        public Builder setSecondPurchasedDate(Date secondPurchasedDate) {
            this.secondPurchasedDate = secondPurchasedDate;
            return this;
        }

        public Builder setPurchasedItem(List<String> purchasedItem) {
            this.purchasedItem = purchasedItem;
            return this;
        }
        public CorrelatedPurchase build(){return new CorrelatedPurchase(this);}
    }
}
