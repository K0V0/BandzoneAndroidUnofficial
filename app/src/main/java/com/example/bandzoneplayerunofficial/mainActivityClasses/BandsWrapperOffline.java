package com.example.bandzoneplayerunofficial.mainActivityClasses;

public class BandsWrapperOffline extends BandsWrapper implements BandsWrapperInterface {


    @Override
    public int setDataSourceType() {
        return DATA_SOURCE_LOCAL;
    }
}
