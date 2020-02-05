package alireza.model;

import java.util.Date;

public class StockTransaction {
    private String symbol;
    private String sector;
    private String industry;
    private int shares;
    private double sharePrice;
    private String customerId;
    private Date transactionTimestamp;
    private boolean purchase;

    private StockTransaction(){}
    private StockTransaction(Builder builder){
        this.symbol = builder.symbol;
        this.sector = builder.sector;
        this.industry = builder.industry;
        this.shares = builder.shares;
        this.sharePrice = builder.sharePrice;
        this.customerId = builder.customerId;
        this.transactionTimestamp = builder.transactionTimestamp;
        this.purchase = builder.purchase;
    }
    public static Builder builder(){return new Builder();}
    public static Builder builder(StockTransaction stockTransaction){return new Builder(stockTransaction);}

    public String getSymbol() {
        return symbol;
    }

    public String getSector() {
        return sector;
    }

    public String getIndustry() {
        return industry;
    }

    public int getShares() {
        return shares;
    }

    public double getSharePrice() {
        return sharePrice;
    }

    public String getCustomerId() {
        return customerId;
    }

    public Date getTransactionTimestamp() {
        return transactionTimestamp;
    }

    public boolean isPurchase() {
        return purchase;
    }

    @Override
    public String toString() {
        return "StockTransaction{" +
                "symbol='" + symbol + '\'' +
                ", sector='" + sector + '\'' +
                ", industry='" + industry + '\'' +
                ", shares=" + shares +
                ", sharePrice=" + sharePrice +
                ", customerId='" + customerId + '\'' +
                ", transactionTimestamp=" + transactionTimestamp +
                ", purchase=" + purchase +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StockTransaction)) return false;

        StockTransaction that = (StockTransaction) o;

        if (shares != that.shares) return false;
        if (Double.compare(that.sharePrice, sharePrice) != 0) return false;
        if (purchase != that.purchase) return false;
        if (symbol != null ? !symbol.equals(that.symbol) : that.symbol != null) return false;
        if (sector != null ? !sector.equals(that.sector) : that.sector != null) return false;
        if (industry != null ? !industry.equals(that.industry) : that.industry != null) return false;
        if (customerId != null ? !customerId.equals(that.customerId) : that.customerId != null) return false;
        return transactionTimestamp != null ? transactionTimestamp.equals(that.transactionTimestamp) : that.transactionTimestamp == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = symbol != null ? symbol.hashCode() : 0;
        result = 31 * result + (sector != null ? sector.hashCode() : 0);
        result = 31 * result + (industry != null ? industry.hashCode() : 0);
        result = 31 * result + shares;
        temp = Double.doubleToLongBits(sharePrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (customerId != null ? customerId.hashCode() : 0);
        result = 31 * result + (transactionTimestamp != null ? transactionTimestamp.hashCode() : 0);
        result = 31 * result + (purchase ? 1 : 0);
        return result;
    }


    public static final class Builder {

        private String symbol;
        private String sector;
        private String industry;
        private int shares;
        private double sharePrice;
        private String customerId;
        private Date transactionTimestamp;
        private boolean purchase;
        private Builder(){}

        private Builder(StockTransaction stockTransaction) {
            this.symbol = stockTransaction.symbol;
            this.sector = stockTransaction.sector;
            this.industry = stockTransaction.industry;
            this.shares = stockTransaction.shares;
            this.sharePrice = stockTransaction.sharePrice;
            this.customerId = stockTransaction.customerId;
            this.transactionTimestamp = stockTransaction.transactionTimestamp;
            this.purchase = stockTransaction.purchase;
        }

        public Builder setSymbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder setSector(String sector) {
            this.sector = sector;
            return this;
        }

        public Builder setIndustry(String industry) {
            this.industry = industry;
            return this;
        }

        public Builder setShares(int shares) {
            this.shares = shares;
            return this;
        }

        public Builder setSharePrice(double sharePrice) {
            this.sharePrice = sharePrice;
            return this;
        }

        public Builder setCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder setTransactionTimestamp(Date transactionTimestamp) {
            this.transactionTimestamp = transactionTimestamp;
            return this;
        }

        public Builder setPurchase(boolean purchase) {
            this.purchase = purchase;
            return this;
        }
        public StockTransaction build(){return new StockTransaction(this);}
    }
}
