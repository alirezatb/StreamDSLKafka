package alireza.ch4.joiner;

import alireza.model.CorrelatedPurchase;
import alireza.model.Purchase;
import org.apache.kafka.streams.kstream.ValueJoiner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PurchaseJoiner implements ValueJoiner<Purchase, Purchase, CorrelatedPurchase> {
    @Override
    public CorrelatedPurchase apply(Purchase purchase, Purchase purchase2) {
        CorrelatedPurchase.Builder builder = CorrelatedPurchase.builder();

        Date purchaseDate = purchase != null ? purchase.getPurchaseDate(): null;
        Double purchasePrice = purchase != null ? purchase.getPrice(): null;
        String purchaseItemPurchased = purchase != null ? purchase.getItemPurchased(): null;
        String purchaseCustomerID = purchase != null ? purchase.getCustomerId(): null;

        Date otherPurchaseDate = purchase2 != null ? purchase2.getPurchaseDate(): null;
        Double otherPurchasePrice = purchase2 != null ? purchase2.getPrice(): null;
        String otherPurchaseItemPurchased = purchase2 != null ? purchase2.getItemPurchased(): null;
        String otherPurchaseCustomerID = purchase2 != null ? purchase2.getCustomerId(): null;

        List<String> lstPurchaseItems = new ArrayList<>();
        if(purchaseItemPurchased != null){
            lstPurchaseItems.add(purchaseItemPurchased);
        }
        if(otherPurchaseItemPurchased !=null){
            lstPurchaseItems.add(otherPurchaseItemPurchased);
        }
        builder.setCustomerID(purchaseCustomerID !=null ? purchaseCustomerID: otherPurchaseCustomerID)
                .setFirstPurchasedDate(purchaseDate)
                .setSecondPurchasedDate(otherPurchaseDate)
                .setPurchasedItem(lstPurchaseItems)
                .setTotalAmount(purchasePrice + otherPurchasePrice);
        return builder.build();





    }
}
