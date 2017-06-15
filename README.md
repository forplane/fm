# flux
<h4>使用flux模式，自己编写一个类似的功能，主要用于通信（通知）方面功能</h4><br/>

功能特点：<br/>
1：注解接受者指定标签函数<br/>
2：基于int标签类型，不是基于某某bus的类型，免去其他subscriber也会接受<br/>
由于post是根据key（类名）+int标签的形式，所以能做到一对一的形式<br/>
3:目前暂未支持进程间通信，望谅解







参考前端的做法，所以接受线程都是主线程，暂还没支持子线程接受处理的情况<br/>
PS：建议各位在自己的BaseUIA（Activity）以及BaseUIF（Fragment）中的onStart跟onDestroy方法中进行相关注册反注册操作


<br/><br/>
使用发送说明<br/>
```Java
public class BActivity extends Activity{
    @Override
        protected void onCreate(Bundle savedInstanceState) {
        
            //post到某个类的用法
           
            //post的key默认使用类名的形式
            //1:直接post某一个类
            NormalActionCreator.get().post(MainActivity.class.getCanonicalName());        
            //2:直接post到某一个类，定义int标签
            NormalActionCreator.get().post(MainActivity.class.getCanonicalName(),222);        
            //3:直接post到某一个类，定义int标签，并且携带数据
            NormalActionCreator.get().post(MainActivity.class.getCanonicalName(),333,"MyObject");        
        }

}

//实现IFluxBaseHelper接口
public class MainActivity extends Activity implements IFluxBaseHelper{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        
    }
    
    
    //指定post的标签
    private final int ddd=222;
    
    
    //当post为这个ddd的标签的时候，会执行此方法
    @FluxOpe(ope = ddd)
    public void opeOne(Object event){
        //这里如果post的是有携带数据的，此时类型是NormalDataStoreEvent
        NormalStore.NormalDataStoreEvent e = (NormalStore.NormalDataStoreEvent) event;
        Object obj = e.getObj();  //这个就是携带数据过来的
        //没有携带数据的
        NormalStore.NormalStoreEvent ee = (NormalStore.NormalStoreEvent) event;
    }



    //这个是用于没有post没有指定int标签的时候或者指定的int标签在当前类的方法没有找到
    @Override
    public void onViewUpdate(Object event) {
        //这里如果post的是有携带数据的，此时类型是NormalDataStoreEvent
        NormalStore.NormalDataStoreEvent e = (NormalStore.NormalDataStoreEvent) event;
        Object obj = e.getObj();  //这个就是携带数据过来的
        //没有携带数据的
        NormalStore.NormalStoreEvent ee = (NormalStore.NormalStoreEvent) event;
        
    }

    @Override
    public Store initStore() {
        //默认使用NormalStroe
        return new NormalStore();
    }

    @Override
    public void initDependencies() {
        //不需要做什么
    }

    @Override
    public Context getCtx() {
        return this;//主要就是需要一个Context对象
    }
    
    private Store store;


    @Override
    protected void onStart() {
        super.onStart();
        
        //注册
        if (store==null) {
            store = initStore();
            if (store != null) {
                Dispatcher.get().register(this, store);
                store.onStart();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //反注册
        if(store!=null){
            Dispatcher.get().unregister(this, store);
        }
    }
}
```
