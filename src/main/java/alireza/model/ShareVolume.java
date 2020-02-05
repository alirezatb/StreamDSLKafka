package alireza.model;

public class ShareVolume {
    private String symbol;
    private int shares;
    private String industry;

    public String getSymbol() {
        return symbol;
    }

    public int getShares() {
        return shares;
    }

    public String getIndustry() {
        return industry;
    }

    public static Builder builder(){return new Builder();}
    public static Builder builder(ShareVolume shareVolume) { return new Builder(shareVolume); }
    public static Builder builder(StockTransaction stockTransaction){
        Builder builder = new Builder();
        builder.symbol = stockTransaction.getSymbol();
        builder.shares = stockTransaction.getShares();
        builder.industry = stockTransaction.getIndustry();
        return builder;
    }
    public static ShareVolume sum(ShareVolume record1, ShareVolume record2){
        Builder builder = builder(record1);
        builder.shares = record1.shares + record2.shares;
        return builder.build();
    }

    private ShareVolume(){}
    private ShareVolume(Builder builder){
        this.symbol = builder.symbol;
        this.industry = builder.industry;
        this.shares = builder.shares;
    }
    public static final class Builder{
        private String symbol;
        private int shares;
        private String industry;

        private Builder(){}
        private Builder(ShareVolume shareVolume){
            this.industry = shareVolume.industry;
            this.shares = shareVolume.shares;
            this.symbol = shareVolume.symbol;
        }

        public Builder setSymbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder setShares(int shares) {
            this.shares = shares;
            return this;
        }

        public Builder setIndustry(String industry) {
            this.industry = industry;
            return this;
        }
        public ShareVolume build(){return new ShareVolume(this);}
    }
}
