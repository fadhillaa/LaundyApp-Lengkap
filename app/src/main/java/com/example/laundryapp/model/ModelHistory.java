package com.example.laundryapp.model;

public class ModelHistory {
    private int id;
    private String title;
    private int items;
    private int price;

    public ModelHistory(int id, String title, int items, int price) {
        this.id = id;
        this.title = title;
        this.items = items;
        this.price = price;
    }

    public ModelHistory() {

    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
