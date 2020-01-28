package alireza.model;

import java.util.Date;
import java.util.Objects;

public class Purchase {
    private String firstName;
    private String lastName;
    private String customerId;
    private String creditCardNumber;
    private String itemPurchased;
    private String department;
    private String employeeId;
    private int quantity;
    private double price;
    private Date purchaseDate;
    private String zipCode;
    private String storeId;

    public static Builder builder(){return new Builder();}
    public static Builder builder(Purchase purchase){return new Builder(purchase);}

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public String getItemPurchased() {
        return itemPurchased;
    }

    public String getDepartment() {
        return department;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getStoreId() {
        return storeId;
    }
    private Purchase(Builder builder) {
        firstName = builder.firstName;
        lastName = builder.lastName;
        customerId = builder.customerId;
        creditCardNumber = builder.creditCardNumber;
        itemPurchased = builder.itemPurchased;
        quantity = builder.quantity;
        price = builder.price;
        purchaseDate = builder.purchaseDate;
        zipCode = builder.zipCode;
        employeeId = builder.employeeId;
        department = builder.department;
        storeId = builder.storeId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Purchase)) return false;

        Purchase purchase = (Purchase) o;

        if (quantity != purchase.quantity) return false;
        if (Double.compare(purchase.price, price) != 0) return false;
        if (firstName != null ? !firstName.equals(purchase.firstName) : purchase.firstName != null) return false;
        if (lastName != null ? !lastName.equals(purchase.lastName) : purchase.lastName != null) return false;
        if (customerId != null ? !customerId.equals(purchase.customerId) : purchase.customerId != null) return false;
        if (creditCardNumber != null ? !creditCardNumber.equals(purchase.creditCardNumber) : purchase.creditCardNumber != null)
            return false;
        if (itemPurchased != null ? !itemPurchased.equals(purchase.itemPurchased) : purchase.itemPurchased != null)
            return false;
        if (department != null ? !department.equals(purchase.department) : purchase.department != null) return false;
        if (employeeId != null ? !employeeId.equals(purchase.employeeId) : purchase.employeeId != null) return false;
        if (zipCode != null ? !zipCode.equals(purchase.zipCode) : purchase.zipCode != null) return false;
        return storeId != null ? storeId.equals(purchase.storeId) : purchase.storeId == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = firstName != null ? firstName.hashCode() : 0;
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (customerId != null ? customerId.hashCode() : 0);
        result = 31 * result + (creditCardNumber != null ? creditCardNumber.hashCode() : 0);
        result = 31 * result + (itemPurchased != null ? itemPurchased.hashCode() : 0);
        result = 31 * result + (department != null ? department.hashCode() : 0);
        result = 31 * result + (employeeId != null ? employeeId.hashCode() : 0);
        result = 31 * result + quantity;
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));;
        result = 31 * result + (zipCode != null ? zipCode.hashCode() : 0);
        result = 31 * result + (storeId != null ? storeId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", customerId='" + customerId + '\'' +
                ", creditCardNumber='" + creditCardNumber + '\'' +
                ", itemPurchased='" + itemPurchased + '\'' +
                ", department='" + department + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", purchaseDate=" + purchaseDate +
                ", zipCode='" + zipCode + '\'' +
                ", storeId='" + storeId + '\'' +
                '}';
    }

    public static final class Builder{

        private String firstName;
        private String lastName;
        private String customerId;
        private String creditCardNumber;
        private String itemPurchased;
        private String department;
        private String employeeId;
        private int quantity;
        private double price;
        private Date purchaseDate;
        private String zipCode;
        private String storeId;

        private static final String CC_NUMBER_REPLACEMENT= "xxxx-xxxx-xxxx-";

        private Builder(Purchase purchase) {
            this.firstName = purchase.getFirstName();
            this.lastName = purchase.getLastName();
            this.customerId = purchase.getCustomerId();
            this.creditCardNumber = purchase.getCreditCardNumber();
            this.itemPurchased = purchase.getItemPurchased();
            this.department = purchase.getDepartment();
            this.employeeId = purchase.getEmployeeId();
            this.quantity = purchase.getQuantity();
            this.price = purchase.getPrice();
            this.purchaseDate = purchase.getPurchaseDate();
            this.zipCode = purchase.getZipCode();
            this.storeId = purchase.getStoreId();
        }

        public Builder maskCreditCard(){
            Objects.requireNonNull(this.creditCardNumber, "Credit Card can't be null");
            String[] parts = this.creditCardNumber.split("-");
            if (parts.length < 4 ) {
                this.creditCardNumber = "xxxx";
            } else {
                String last4Digits = this.creditCardNumber.split("-")[3];
                this.creditCardNumber = CC_NUMBER_REPLACEMENT + last4Digits;
            }
            return this;
        }

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder setCreditCardNumber(String creditCardNumber) {
            this.creditCardNumber = creditCardNumber;
            return this;
        }

        public Builder setItemPurchased(String itemPurchased) {
            this.itemPurchased = itemPurchased;
            return this;
        }

        public Builder setDepartment(String department) {
            this.department = department;
            return this;
        }

        public Builder setEmployeeId(String employeeId) {
            this.employeeId = employeeId;
            return this;
        }

        public Builder setQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder setPrice(double price) {
            this.price = price;
            return this;
        }

        public Builder setPurchaseDate(Date purchaseDate) {
            this.purchaseDate = purchaseDate;
            return this;
        }

        public Builder setZipCode(String zipCode) {
            this.zipCode = zipCode;
            return this;
        }

        public Builder setStoreId(String storeId) {
            this.storeId = storeId;
            return this;
        }

        private Builder(){}
        public Purchase build(){return new Purchase(this);}



    }
}


