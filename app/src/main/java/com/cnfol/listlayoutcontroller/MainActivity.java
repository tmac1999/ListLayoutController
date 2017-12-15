package com.cnfol.listlayoutcontroller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {
    ListLayoutController listLayoutController;
    private int pageSize = 10;
    private AbstractXListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listLayoutController = new ListLayoutController(this, (ViewGroup) findViewById(R.id.content_view), pageSize);
        listView = new AbstractXListView() {
            @Override
            public void setPullLoadEnable(boolean b) {

            }

            @Override
            public void stopRefresh() {

            }

            @Override
            public void setVisibility(int visible) {

            }

            @Override
            public void stopLoadMore() {

            }

            @Override
            public XListViewFooter getFooterView() {
                return null;
            }
        };
    }

    public void Empty(View view) {

        listLayoutController.manageListViewAndShowErrorPage(ListLayoutController.PageStatus.Empty, listView);
    }

    public void Error(View view) {
        listLayoutController.manageListViewAndShowErrorPage(ListLayoutController.PageStatus.Error, listView);
    }

    public void NetworkError(View view) {
        listLayoutController.manageListViewAndShowErrorPage(ListLayoutController.PageStatus.NetworkError, listView);
    }

    public void LessThanPageSize(View view) {
        listLayoutController.manageListViewAndShowErrorPage(ListLayoutController.PageStatus.LessThanPageSize, listView);
    }

    public void Default(View view) {
        listLayoutController.manageListViewAndShowErrorPage(ListLayoutController.PageStatus.Default, listView);
    }
}
