package com.yidont.fm;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.jpadapter.i.IHolderAccept;
import com.yidont.fm.base.UniversalSwipeBackActivity;
import com.yidont.fm.dao.HanderSomething;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import utils.ToolBarStatusUtils;


/**
 * Created by john on 16-7-5.
 *
 */
public class SingleFragmentUIA extends UniversalSwipeBackActivity implements HanderSomething, IHolderAccept {

    static {
        Application app = null;
        try {
            app = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null);
            if (app == null)
                throw new IllegalStateException("Static initialization of Applications must be on main thread.");
        } catch (final Exception e) {
            Log.e("SingleFragmentUIA","Failed to get current application from AppGlobals." + e.getMessage());
            try {
                app = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null);
            } catch (final Exception ex) {
                Log.e("SingleFragmentUIA","Failed to get current application from ActivityThread." + e.getMessage());
            }
        } finally {
        }
        ToolBarStatusUtils.statusHeight = ToolBarStatusUtils.getStatusBarH(app);
    }


    public static final String KEYINTENT = "key";
    public static final String BUNDLEKEY = "bundlekey";
    public LinearLayout statusLay;

    public LinearLayout getStatusLay() {
        return statusLay;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ToolBarStatusUtils.setStatusBar(this);
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_singlefragment);
        AndroidBug5497Workaround.assistActivity(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        String key = getIntent().getStringExtra(KEYINTENT);
        Bundle extras = getIntent().getExtras();
        if (savedInstanceState == null) {
            loadRootFragment(R.id.fragment_singleerjilayout, newInstance(key,extras));
        }
        statusLay = (LinearLayout) findViewById(R.id.status_height);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ToolBarStatusUtils.statusHeight);
        statusLay.setLayoutParams(params);
        statusLay.setBackgroundColor(getResources().getColor(R.color.theme));
//        ToolBarStatusUtils.setStatusGone(statusLay,true,R.color.theme);

    }

    public void setStatusLayVisibility(int visibility) {
        statusLay.setVisibility(visibility);
    }

    @Override
    public void onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        super.onBackPressedSupport();
    }

    private InHeadUIF newInstance(String key, Bundle bundle) {
        InHeadUIF fragment = null;
        try {
            fragment = (InHeadUIF) Class.forName(key).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (fragment == null) {
            throw new NullPointerException("fragment为null");
        }
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }


    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置横向(和安卓4.x动画相同)
        return new DefaultHorizontalAnimator();
    }

    @Override
    public void postHandler(String tag, Object o) {
        InHeadUIF fragmentByTag = (InHeadUIF) getSupportFragmentManager().findFragmentByTag(tag);
        if (fragmentByTag != null) {
            fragmentByTag.acceptHandler(o);
        } else {
            Log.i("SingleFragmentUIA", "找不到相对应的tag 片段");
        }

    }

    public final SupportFragment findUIFByTag(String tag) {
        SupportFragment fragmentByTag = (SupportFragment) getSupportFragmentManager().findFragmentByTag(tag);
        return fragmentByTag;
    }

    @Override
    public void acceptMessage(Object o) {
        HashMap<String, Object> hash = (HashMap<String, Object>) o;
        String key = (String) hash.get("key");
        Object obj = hash.get("obj");
        postHandler(key, obj);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (int i = 0; i < fragments.size(); i++) {
            if(fragments.get(i)!=null) {
                fragments.get(i).onActivityResult(requestCode, resultCode, data);
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public static class AndroidBug5497Workaround {
        public static int H;
        // For more information, see https://code.google.com/p/android/issues/detail?id=5497
        // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.

        public static void assistActivity(Activity activity) {
            new AndroidBug5497Workaround(activity);
        }

        private View mChildOfContent;
        private int usableHeightPrevious;
        private FrameLayout.LayoutParams frameLayoutParams;

        private AndroidBug5497Workaround(Activity activity) {
            if (H == 0) {
                DisplayMetrics metric = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
                H= metric.heightPixels;
            }
//            Bundle extras = activity.getIntent().getExtras();
//            if (!extras.getBoolean(InHeadUIF.TOPKEY)) {
//                return;
//            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                return;
            }

            FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
            mChildOfContent = content.getChildAt(0);
            mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    possiblyResizeChildOfContent();
                }
            });
            frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
        }


        /**
         * 真正的屏幕高度（包含状态栏啊，虚拟键盘等）
         * @return
         */
        private static int getRealMetrics(Context context){
            int dpi = 0;
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            @SuppressWarnings("rawtypes")
            Class c;
            try {
                c = Class.forName("android.view.Display");
                @SuppressWarnings("unchecked")
                Method method = c.getMethod("getRealMetrics",DisplayMetrics.class);
                method.invoke(display, displayMetrics);
                dpi=displayMetrics.heightPixels;
            }catch(Exception e){
                e.printStackTrace();
            }
            return dpi;
        }


        private void possiblyResizeChildOfContent() {
            int usableHeightNow = computeUsableHeight();
            if (usableHeightNow != usableHeightPrevious) {
                int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
//                if(usableHeightSansKeyboard!=usableHeightNow+ToolBarStatusUtils.statusHeight){
//                    int num =usableHeightSansKeyboard-(usableHeightNow+ToolBarStatusUtils.statusHeight);
//                    usableHeightNow+=num;
//                }
                int dpi = getRealMetrics(mChildOfContent.getContext());
                int screenHeight = H;
                usableHeightSansKeyboard=usableHeightSansKeyboard-(dpi-screenHeight);
                int heightDifference = usableHeightSansKeyboard - usableHeightNow;
                if (heightDifference > (usableHeightSansKeyboard / 4)) {
                    // keyboard probably just became visible
                    frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
                } else {
                    // keyboard probably just became hidden
                    frameLayoutParams.height = usableHeightSansKeyboard;
                }
                mChildOfContent.requestLayout();
                usableHeightPrevious = usableHeightNow;
            }
        }

        private int computeUsableHeight() {
            Rect r = new Rect();
            mChildOfContent.getWindowVisibleDisplayFrame(r);

            if (r.top ==0) {
                r.top = ToolBarStatusUtils.statusHeight;//状态栏目的高度
            }
            return (r.bottom - r.top);
        }

    }



    //设置状态栏颜色，其他人不要动，到时候会搬迁
    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colorResId));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}