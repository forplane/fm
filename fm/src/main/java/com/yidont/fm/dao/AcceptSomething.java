package com.yidont.fm.dao;

/**
 * Created by planes on 2016/8/12.
 *
 */

public interface AcceptSomething {
    /**
     * 接受fragment间的通信
     * o是发送fragment带过来的参数
     */
    void acceptHandler(Object o);
}
