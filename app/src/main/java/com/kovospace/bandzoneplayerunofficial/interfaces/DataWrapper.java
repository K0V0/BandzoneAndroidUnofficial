package com.kovospace.bandzoneplayerunofficial.interfaces;

public interface DataWrapper {
    int ITEMS_PER_PAGE = 20;
    int DATA_SOURCE_INTERNET = 1;
    int DATA_SOURCE_LOCAL = 2;
    int OPERATION_NEXTPAGE = 2;
    int OPERATION_SEARCH = 1;
    int setDataSourceType();
}
