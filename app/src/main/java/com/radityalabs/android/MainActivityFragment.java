package com.radityalabs.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String TAG = MainActivityFragment.class.getSimpleName();

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        insertNewDatas();
    }

    private Observable<List<ProductObject>> addDatas() {
        return Observable.create(new ObservableOnSubscribe<List<ProductObject>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ProductObject>> e) throws Exception {
                final List<ProductObject> products = new ArrayList<>(1000);
                for (int i = 1; i <= 1000; i++) {
                    ProductObject obj = new ProductObject();
                    obj.id = i;
                    obj.image = "http://www.alsglobal.com/~/media/Images/Divisions/Life%20Sciences/Food/ALS-Food-Hero.jpg";
                    obj.name = "food" + i;
                    obj.number = String.valueOf(new Random().nextInt(100000));
                    products.add(obj);
                }

                /**
                 * You can add you own realm configuration
                 * like dev or release
                 *
                 * note: in each rx thread you shoud take a realm instance
                 * i did this, because. realm has their own configuration
                 * which prevent called in diff thread
                 * and it will avoid invalid manage
                 */
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.insertOrUpdate(products);
                    }
                });
                e.onNext(products);
                realm.close(); // dont forget close
            }
        }).map(new Function<List<ProductObject>, List<ProductObject>>() {
            @Override
            public List<ProductObject> apply(List<ProductObject> productObjects) throws Exception {
                return productObjects;
            }
        });
    }

    private void insertNewDatas() {
        addDatas().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ProductObject>>() {
                    @Override
                    public void accept(List<ProductObject> productObjects) throws Exception {
                        retriveDatas();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "[InsertNewDatas]", throwable);
                    }
                });
    }

    private void retriveDatas() {
        Observable.create(new ObservableOnSubscribe<List<ProductObject>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ProductObject>> e) throws Exception {
                final Realm realm = Realm.getDefaultInstance();
                final RealmResults<ProductObject> products = realm.where(ProductObject.class).findAll(); // dont use async, we actually in worker thread
                if (products != null) {
                    e.onNext(realm.copyFromRealm(products));
                } else {
                    e.onError(new Throwable("ProductObject List is Null"));
                }
                realm.close();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ProductObject>>() {
                    @Override
                    public void accept(List<ProductObject> productObjects) throws Exception {
                        Log.d(TAG, "Total data : " + productObjects.size());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "[RetriveDatas]", throwable);
                    }
                });
    }
}
