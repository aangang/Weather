package com.guofeng.weather.util;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 *管理
 */
public class RxManager {

    private static CompositeSubscription compositeSubscription = new CompositeSubscription();

    public static void add(Subscription s) {
        compositeSubscription.add(s);
    }

    public static void clear() {
        if (!compositeSubscription.isUnsubscribed()){
            compositeSubscription.unsubscribe();
        }
    }
}
