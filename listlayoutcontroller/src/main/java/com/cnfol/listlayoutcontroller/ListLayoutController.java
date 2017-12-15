package com.cnfol.listlayoutcontroller;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by mrz on 17/6/23.
 */

public class ListLayoutController implements View.OnClickListener {

    Activity activity;
    int pageSize;
    int firstPageSize = -1;
    private OnReloadButtonClickListener onReloadButtonClickListener;


    public interface OnReloadButtonClickListener {
        void onReloadButtonClick();
    }

    /**
     * @param listener 要使得重新加载按钮可以点击，需要添加listner 并在onReloadButtonClick方法中调用响应刷新方法
     *                 <p>或者{@link #activity}本身实现 XListView.IXListViewListener
     * @return
     */
    public ListLayoutController setOnReloadButtonClickListener(OnReloadButtonClickListener listener) {
        this.onReloadButtonClickListener = listener;
        return this;
    }


    public ListLayoutController(Activity activity, ViewGroup contentView, int pageSize) {
        init(activity, contentView, pageSize);
    }


    public ListLayoutController(Activity activity, ViewGroup contentView, int pageSize, int firstPageSize) {
        this.firstPageSize = firstPageSize;
        init(activity, contentView, pageSize);
    }

    private void init(Activity activity, ViewGroup contentView, int pageSize) {
        this.activity = activity;
        this.pageSize = pageSize;
        View include_institution_load_error = View.inflate(activity, R.layout.include_institution_load_error, null);
        contentView.addView(include_institution_load_error, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btn_institution_refresh = (Button) include_institution_load_error.findViewById(R.id.btn_institution_refresh);
        btn_institution_refresh.setOnClickListener(this);
        ll_empty = include_institution_load_error.findViewById(R.id.ll_empty);
        tv_error_msg = (TextView) include_institution_load_error.findViewById(R.id.tv_error_msg);
        firstPageSize = (firstPageSize == -1) ? pageSize : firstPageSize;//若firstPageSize未赋值(==-1)，则其值等于pageSize
    }

    /**
     * 能否 继承自此基类之后自动加入 ll_empty等错误展示的布局,而不去每个页面手动在xml里面补这个布局。--already done
     */
    //处理请求并解析正确后应该显示的样式并保存状态 doc
    public void manageListViewAndShowSuccessfulPage(int pageNum, int listSize, AbstractXListView listView) {
        listView.getFooterView().setFootTextView("查看更多");//已加载完全部后，防止又重新刷新文字没有改变过来。
        if (pageNum == 1) {
            if (listSize == 0) {

                listView.setPullLoadEnable(false);
                showErrorPage(PageStatus.Empty, listView);
                listView.stopRefresh();
            } else if (listSize > 0 && listSize < Integer.valueOf(pageSize)) {
                listView.setPullLoadEnable(false);
                listView.stopRefresh();
                ll_empty.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            } else {
                ll_empty.setVisibility(View.GONE);
                listView.setPullLoadEnable(true);
                listView.setVisibility(View.VISIBLE);
                listView.stopRefresh();
            }
        } else {
            if (listSize < Integer.valueOf(pageSize)) {
                setNoMoreData(listView);
            }
            //加载更多
            listView.stopLoadMore();
        }

    }

    //处理请求并解析出现错误后应该显示的样式并保存状态
    public void manageListViewAndShowErrorPage(PageStatus pageStatus, AbstractXListView listView) {
        listView.setPullLoadEnable(false);
        showErrorPage(pageStatus, listView);
        listView.stopRefresh();
    }

    /**
     * 能否 继承自此基类之后自动加入 ll_empty等错误展示的布局,而不去每个页面手动在xml里面补这个布局。--already done
     *
     * @param listSize 注意这个size是当前次请求的listsize,而不是多次分页加载累计之后的size
     */
    //处理请求并解析正确后应该显示的样式并保存状态 doc
    public void manageListViewAndShowSuccessfulPage(int pageNum, int listSize, AbstractXRecyclerView recyclerView) {

        if (pageNum == 1) {
            recyclerView.refreshComplete();
            if (listSize == 0) {
                //activity_details_list.mFooterView.hide();
//                recyclerView.setPullRefreshEnabled(false);
                showErrorPage(PageStatus.Empty, recyclerView);
            } else if (listSize > 0 && listSize < Integer.valueOf(firstPageSize)) {
                setNoMoreData(recyclerView);
//                recyclerView.setPullRefreshEnabled(false);
                recyclerView.setVisibility(View.VISIBLE);
                ll_empty.setVisibility(View.GONE);
            } else {
                ll_empty.setVisibility(View.GONE);
//                recyclerView.setPullRefreshEnabled(true);
                recyclerView.setVisibility(View.VISIBLE);
            }

        } else {
            recyclerView.loadMoreComplete();
            if (listSize < Integer.valueOf(pageSize)) {
                //recyclerView.setLoadingMoreEnabled(false);
                setNoMoreData(recyclerView);
            }
            //加载更多

        }
    }

    //处理请求并解析出现错误后应该显示的样式并保存状态
    public void manageListViewAndShowErrorPage(PageStatus pageStatus, AbstractXRecyclerView listView) {
        listView.refreshComplete();
        listView.loadMoreComplete();
        listView.setLoadingMoreEnabled(false);
        showErrorPage(pageStatus, listView);

    }

    public enum PageStatus {
        Empty,//空页面,无数据
        Error,//错误数据,解析出错
        NetworkError,//网络错误
        LessThanPageSize,//该页面条目少于10,用于标记不显示loadmore
        Default//正常值 未出错
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_institution_refresh) {
            if (activity instanceof AbstractXListView.IXListViewListener) {
                ((AbstractXListView.IXListViewListener) activity).onRefresh();
            } else if (onReloadButtonClickListener != null) {
                onReloadButtonClickListener.onReloadButtonClick();
            }
        }

    }

    private void setNoMoreData(AbstractXListView listView) {
        listView.setPullLoadEnable(false);
        listView.getFooterView().show();
        listView.getFooterView().setFootTextView("已加载完全部");
    }

    private void setNoMoreData(AbstractXRecyclerView listView) {
        listView.setNoMore(true);
    }


    public View ll_empty;
    private TextView tv_error_msg;
    private Button btn_institution_refresh;


    private void showErrorPage(PageStatus pageStatus, AbstractXRecyclerView listView) {
        listView.setVisibility(View.GONE);
        showErrorPageInner(pageStatus);
    }

    private void showErrorPage(PageStatus pageStatus, AbstractXListView listView) {
        listView.setVisibility(View.GONE);
        showErrorPageInner(pageStatus);
    }

    private void showErrorPageInner(PageStatus pageStatus) {
        ll_empty.setVisibility(View.VISIBLE);

        Drawable drawable = null;
        switch (pageStatus) {
            case Empty:
                tv_error_msg.setText(activity.getString(R.string.no_content));
                drawable = activity.getResources().getDrawable(R.mipmap.play_the_piano);
                btn_institution_refresh.setVisibility(View.GONE);
                break;
            case NetworkError:
                tv_error_msg.setText(activity.getString(R.string.network_not_working));
                drawable = activity.getResources().getDrawable(R.mipmap.nowifi);
                btn_institution_refresh.setVisibility(View.VISIBLE);

                break;
            case Error:
                tv_error_msg.setText(activity.getString(R.string.data_error));
                drawable = activity.getResources().getDrawable(R.mipmap.data_error);
                btn_institution_refresh.setVisibility(View.VISIBLE);
                break;
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv_error_msg.setCompoundDrawables(null, drawable, null, null);
    }

}
