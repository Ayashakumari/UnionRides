package mx.bigapps.unionrides.Activity;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by admin on 14-06-2018.
 */

public class Scrollingstop extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public Scrollingstop(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}