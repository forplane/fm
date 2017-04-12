package com.yidont.fm;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yidont.fm.base.UniversalSwipeBackFragment;
import com.yidont.fm.dao.AcceptSomething;
import com.yidont.fm.dao.InHeadBackListener;

import utils.ToolBarStatusUtils;
import view.HeadToolBar;

/**
 * Created by jon on 2016/10/26.<br/>
 */

public abstract class InHeadUIF extends UniversalSwipeBackFragment implements AcceptSomething {

    public static final String TOPKEY = "topkey";

    private ViewGroup parentView;//最外层的view，包含头部
    protected ViewGroup includeView;//头部下方的view，也就是要包含其他view的

    protected HeadToolBar headCustomView;

    private InHeadBackListener backListener;


    /**
     * 设置返回按钮的点击事件
     */
    public void setBackListener(InHeadBackListener backListener) {
        this.backListener = backListener;
    }

    @Override
    protected ViewGroup baseCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        int layoutId = R.layout.fragment_head_layout;
        boolean aBoolean = arguments.getBoolean(TOPKEY);
        if (arguments != null && aBoolean) {
            ToolBarStatusUtils.setStatusBar(_mActivity);
            layoutId = R.layout.fragment_tophead_layout;
        }

        parentView = (ViewGroup) LayoutInflater.from(getActivity()).inflate(layoutId, null);
        parentView.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        initHeadView();
        includeView = (LinearLayout) parentView.findViewById(R.id.fragment_oncontentview);
        onContentView(includeView);
        return parentView;
    }

    private void initHeadView() {
        //获得头部对象
        ViewGroup viewGroup = headView();
        if (viewGroup == null) {
            headCustomView = (HeadToolBar) parentView.findViewById(R.id.head_toolbar);
//            _mActivity.setSupportActionBar(headCustomView);
//            _mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            headCustomView.setNavigationIcon(R.drawable.head_back_sub_line);
            headCustomView.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (backListener == null) {
                        _mActivity.onBackPressedSupport();
                    } else {
                        backListener.onBack(InHeadUIF.this);
                    }
                }
            });
            initTheHeadCustom(headCustomView);
            Bundle arguments = getArguments();
            boolean aBoolean = arguments.getBoolean(TOPKEY);
            if (arguments != null && aBoolean) {
                headCustomView.setBackgroundColor(Color.TRANSPARENT);
                headCustomView.setNavigationIcon(R.drawable.head_back);
                ((SingleFragmentUIA)_mActivity).getStatusLay().setVisibility(View.GONE);
                ToolBarStatusUtils.setStatusGone((LinearLayout) parentView.findViewById(R.id.status_height));
            }
            headCustomView.setContentInsetStartWithNavigation(0);
        } else {
            View viewById = parentView.findViewById(R.id.fragment_single_head);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewById.getLayoutParams();
            ViewGroup parent = (ViewGroup) viewById.getParent();
            parent.removeView(viewById);
            parent.addView(viewGroup, layoutParams);
            initMyHeadCustom(viewGroup);
        }


    }


    /**
     * 自己的头部view
     */
    protected ViewGroup headView() {
        return null;
    }

    protected abstract void onContentView(ViewGroup mViewGroup);

    //默认头部重写//这里要改成灿辉弄得头部
    protected void initTheHeadCustom(HeadToolBar toolbar) {

    }

    //自己头部重写
    protected void initMyHeadCustom(ViewGroup toolbar) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideSoftInput();
    }

    @Override
    public void acceptHandler(Object o) {

    }
}
