package com.cnfol.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cnfol.listlayoutcontroller.AbstractXListView;
import com.cnfol.listlayoutcontroller.ListLayoutController;
import com.cnfol.listlayoutcontroller.XListViewFooter;

public class MainActivity extends Activity {
    ListLayoutController listLayoutController;
    private int pageSize = 10;
    private AbstractXListView listView;
    private int pageNum = 1;
    private int listSize = 5;

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
        listLayoutController.setOnReloadButtonClickListener(new ListLayoutController.OnReloadButtonClickListener() {
            @Override
            public void onReloadButtonClick() {
                Toast.makeText(MainActivity.this,"onReloadButtonClick",Toast.LENGTH_SHORT).show();
            }
        });
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
        //listLayoutController.manageListViewAndShowErrorPage(ListLayoutController.PageStatus.LessThanPageSize, listView);
        listLayoutController.manageListViewAndShowSuccessfulPage(pageNum,listSize,listView);
        Toast.makeText(this,"LessThanPageSize",Toast.LENGTH_SHORT).show();
    }

    public void Default(View view) {
        listLayoutController.manageListViewAndShowSuccessfulPage(pageNum,pageSize,listView);
        Toast.makeText(this,"Default",Toast.LENGTH_SHORT).show();
    }
}
