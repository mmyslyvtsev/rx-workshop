package com.griddynamics.workshop.rx;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * @author Max Myslyvtsev
 * @since 9/25/15
 */
public class _06_ForkJoin {

    public static void main(String[] args) {
        Observable<String> starting = Observable.just("start");

        starting = starting.subscribeOn(Schedulers.newThread()); // !!! make left and right maps to execute in parallel

        Observable<String> left = starting.map(s -> {
            System.out.println("Left executing on: " + Thread.currentThread().getName());
            Utils.sleep(1000 + 2000); // !!! make zip execute on same thread as left map
            return s.substring(0, 1); // take first character
        });
        Observable<String> right = starting.map(s -> {
            System.out.println("Right executing on: " + Thread.currentThread().getName());
            Utils.sleep(2000);
            return s.substring(s.length() - 1, s.length()); // take last character
        });
        Observable<String> zipped = right.zipWith(left, (a, b) -> {
            System.out.println("Zip executing on: " + Thread.currentThread().getName());
            return a + b;
        });

        long time = System.currentTimeMillis();
        String result = zipped.toBlocking().single();
        time = System.currentTimeMillis() - time;

        System.out.println("Result: " + result);
        System.out.println("Time: " + time);

        // TODO: make left and right maps to execute in parallel
        // TODO: make zip execute on same thread as left map
    }
}
