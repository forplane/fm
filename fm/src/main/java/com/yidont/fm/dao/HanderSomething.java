package com.yidont.fm.dao;

/**
 * Created by planes on 2016/8/12.
 *
 */

/**fragmentActivity实现这个接口，为fragment间通信*/
public interface HanderSomething{
    /**
     * Fragment通信的时候,调用此方法_mActivity.postHandler("KEY",O);
     * 当前Fragment跟KEY的Fragment进行通信,这种方式是栈顶对栈顶下边的通信
     * 是在此之前需要通信的fragment就已经在栈的
     * @param tag   需要通信的Fragment的对应的KEY
     * @param o     需要通信的Object对象
     */
    void postHandler(String tag, Object o);
}
