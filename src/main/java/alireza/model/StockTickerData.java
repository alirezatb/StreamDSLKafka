package alireza.model;

public class StockTickerData {
    private String symbol;
    private double price;

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public StockTickerData(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    @Override
    public String toString() {
        return "StockTickerData{" +
                "symbol='" + symbol + '\'' +
                ", price=" + price +
                '}';
    }
}
