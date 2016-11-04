package com.guofeng.weather.util;


import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * RxBus 模式
 * 事件总线可以使各组件之间的通信变得简单，深度解耦。
 * Created by GUOFENG on 2016/10/28.
 */

// RxJava 的基本实现主要有三点：
// 1) 创建 Observer 即观察者，它决定事件触发的时候将有怎样的行为。
// 有 Observer 接口的实现方式,还有内置了一个实现了 Observer 的抽象类：Subscriber。
// 2) 创建 Observable 即被观察者，它决定什么时候触发事件以及触发怎样的事件。
// 3) Subscribe (订阅)
// 创建了 Observable 和 Observer 之后，再用 subscribe() 方法将它们联结起来,代码形式很简单：
// observable.subscribe(observer); 或者 observable.subscribe(subscriber);

public class RxBus {

    private final Subject<Object, Object> bus;

    //Subject同时充当了Observer和Observable的角色，Subject是非线程安全的，
    // 要避免该问题，需要将 Subject转换为一个 SerializedSubject，
    // 把线程非安全的PublishSubject包装成线程安全的Subject。
    //PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据 发射给观察者。
    private RxBus() {
        bus = new SerializedSubject<>(PublishSubject.create());
    }

    public static RxBus getDefault() {
        return RxBusHolder.mInstance;
    }

    private static class RxBusHolder {
        private static final RxBus mInstance = new RxBus();
    }

    // 提供了一个新的事件
    public void post(Object o) {
        bus.onNext(o);
    }

    // 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
    // ofType操作符只发射指定类型的数据，其内部就是filter+cast
    public <T> Observable<T> toObserverable(Class<T> eventType) {
        return bus.ofType(eventType);
    }


}
