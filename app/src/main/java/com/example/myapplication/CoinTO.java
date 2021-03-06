package com.example.myapplication;

public class CoinTO implements Comparable<CoinTO> {
    private int id;
    private String name;
    private String symbol;
    private String slug;
    private String date_added;
    private int cmc_rank;
    private String last_updated;
    private String price;

    private boolean isMonitoringCoin;
    private String minPrice;
    private String maxPrice;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public int getCmc_rank() {
        return cmc_rank;
    }

    public void setCmc_rank(int cmc_rank) {
        this.cmc_rank = cmc_rank;
    }

    public String getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public boolean isMonitoringCoin() {
        return isMonitoringCoin;
    }

    public void setMonitoringCoin(boolean monitoringCoin) {
        isMonitoringCoin = monitoringCoin;
    }

    @Override
    public String toString() {
        return "CoinTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", slug='" + slug + '\'' +
                ", date_added='" + date_added + '\'' +
                ", cmc_rank=" + cmc_rank +
                ", last_updated='" + last_updated + '\'' +
                ", price='" + price + '\'' +
                ", isMonitoringCoin=" + isMonitoringCoin +
                ", minPrice='" + minPrice + '\'' +
                ", maxPrice='" + maxPrice + '\'' +
                '}';
    }

    @Override
    public int compareTo(CoinTO coinTO) {
            return this.getName().compareTo(coinTO.getName());
    }
}
