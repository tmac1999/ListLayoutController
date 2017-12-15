package com.cnfol.listlayoutcontroller;

/**
 * Created by mrz on 17/12/13.
 */

public abstract class AbstractXRecyclerView {

    public abstract void setVisibility(int gone);

    public abstract void setNoMore(boolean b);

    public abstract void loadMoreComplete();

    public abstract void refreshComplete();

    public abstract void setLoadingMoreEnabled(boolean b);
}
