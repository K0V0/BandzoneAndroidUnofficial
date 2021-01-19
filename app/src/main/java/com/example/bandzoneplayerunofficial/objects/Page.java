package com.example.bandzoneplayerunofficial.objects;

import java.util.ArrayList;
import java.util.List;

public class Page {
    private int currentPage;
    private int itemsPerPage;
    private int itemsOnCurrentPage;
    private int pages;
    private int itemsTotal;
    List<Band> bands = new ArrayList<>();

    public Page(int currentPage, int itemsPerPage, int itemsOnCurrentPage, int pages, int itemsTotal, List<Band> bands) {
        this.currentPage = currentPage;
        this.itemsPerPage = itemsPerPage;
        this.itemsOnCurrentPage = itemsOnCurrentPage;
        this.pages = pages;
        this.itemsTotal = itemsTotal;
        this.bands = bands;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public int getItemsOnCurrentPage() {
        return itemsOnCurrentPage;
    }

    public void setItemsOnCurrentPage(int itemsOnCurrentPage) {
        this.itemsOnCurrentPage = itemsOnCurrentPage;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getItemsTotal() {
        return itemsTotal;
    }

    public void setItemsTotal(int itemsTotal) {
        this.itemsTotal = itemsTotal;
    }

    public List<Band> getBands() {
        return bands;
    }

    public void setBands(List<Band> bands) {
        this.bands = bands;
    }

     @Override public String toString() {
        return this.bands.toString();
     }
}
