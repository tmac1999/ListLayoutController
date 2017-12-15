package com.cnfol.listlayoutcontroller;

/**
 * Created by mrz on 17/12/13.
 */

public interface  AbstractXListView {


    public abstract void setPullLoadEnable(boolean b);

    public abstract void stopRefresh();

    public abstract void setVisibility(int visible);

    public abstract void stopLoadMore();

    XListViewFooter getFooterView();

    public interface IXListViewListener {
        void onRefresh();
    }
}
