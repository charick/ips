package TOI.model;

public class TradeItem
{
    public String id;
    public int quantity;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String price;
    public String name;
    public String type;
    public String picUrl;
    public String stockInfo;
    public int stockInfoCode;
    public int total;

    public String getFacts() {
        return facts;
    }

    public String facts;

    public String getId()
    {
        return this.id;
    }

    public String getQuantity() {
        return this.quantity + "";
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public String getPicUrl() {
        return this.picUrl;
    }

    public String getStockInfo()
    {
        return this.stockInfo;
    }

    public String getTotal()
    {
        return this.total + "";
    }

    public String getStockInfoCode() {
        return this.stockInfoCode + "";
    }

    public TradeItem(String id)
    {
        this.id = id;
        this.quantity = 0;
    }
}