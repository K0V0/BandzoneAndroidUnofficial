package com.example.bandzoneplayerunofficial.mainActivityClasses;

import com.example.bandzoneplayerunofficial.objects.Page;

public interface BandsWrapperInterface {
    void search(String s);
    void loadNextContent();
    void add(Page page);
    void update(Page page);
    void clear();
    int setDataSourceType();
}
