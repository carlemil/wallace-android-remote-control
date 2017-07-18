package se.jayway.wallaceremotecontrol;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.subjects.PublishSubject;

/**
 * Created by carlemil on 7/18/17.
 */

public class Java {
    public static void setListener(View v) {
//        Observable obs = Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                try {
//                    String result = "doSomeTimeTakingIoOperation()";
//                    Log.d("TAG", " Observable.create ");
//
//                    subscriber.onNext(result);    // Pass on the data to subscriber
//                    subscriber.onCompleted();     // Signal about the completion subscriber
//                } catch (Exception e) {
//                    subscriber.onError(e);        // Signal about the error to subscriber
//                }
//            }
//        });

        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.d("TAG", "onCompleted ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("TAG", "onError ");
            }

            @Override
            public void onNext(String s) {
                Log.d("TAG", "onNext " + s);
            }
        };

        final PublishSubject<String> stringPublishSubject = PublishSubject.create();
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                stringPublishSubject.onNext("B");
                return false;
            }
        });
        stringPublishSubject.sample(500, TimeUnit.MILLISECONDS)
                .subscribe(subscriber);

    }
}
