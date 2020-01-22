package alireza.model;

import java.util.Date;
import java.util.Objects;

public class PurchaseKey {
    private String customerId;
    private Date transactionDate;

    public PurchaseKey(String customerId, Date transactionDate) {
        this.customerId = customerId;
        this.transactionDate = transactionDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Override
    public boolean equals(Object o){
        if (this == o)
            return true;
        if (!(o instanceof PurchaseKey))
            return false;
        PurchaseKey key = (PurchaseKey) o;
        return Objects.equals(customerId, key.customerId) &&
                Objects.equals(transactionDate, key.transactionDate);
    }

    @Override
    public int hashCode(){return Objects.hash(customerId,transactionDate);}

//    Collections such as HashMap and HashSet use a hashcode value of an object to determine
//    how it should be stored inside a collection, and the hashcode is used again in order to
//    locate the object in its collection.
//
//    Hashing retrieval is a two-step process:
//
//    Find the right bucket (using hashCode())
//    Search the bucket for the right element (using equals() )

//    @Override
//    public boolean equals(Object obj) {
//        if (obj == this)
//            return true;
//        if (!(obj instanceof Employee))
//            return false;
//        Employee employee = (Employee) obj;
//        return employee.getAge() == this.getAge()
//                && employee.getName() == this.getName();
//    }


}
