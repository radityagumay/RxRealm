package com.radityalabs.android

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import java.util.*

/**
 * Created by radityagumay on 1/14/17.
 */

class MainFragment : Fragment() {
    companion object {
        val TAG: String = MainFragment::class.java.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        insertNewDatas()
    }

    private fun insertNewDatas() {
        addDatas().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ retriveDatas() }, { t -> Log.e(TAG, "[InsertNewDatas]", t) })
    }

    private fun retriveDatas() {
        Observable.create(ObservableOnSubscribe<List<ProductObject>> { e ->
            val realm = Realm.getDefaultInstance()
            val products = realm.where(ProductObject::class.java).findAll() // dont use async, we actually in worker thread
            if (products != null) {
                e!!.onNext(realm.copyFromRealm(products))
            } else {
                e!!.onError(Throwable("ProductObject List is Null"))
            }
            realm.close()
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { t -> Log.d(TAG, "Total data : " + t!!.size) },
                        { t -> Log.e(TAG, "[RetriveDatas]", t) }
                )
    }

    private fun addDatas(): Observable<List<ProductObject>> {
        return Observable.create<List<ProductObject>> { emitter ->
            val products = ArrayList<ProductObject>()
            for (i in 1..1000) {
                val obj = ProductObject()
                obj.id = i.toLong()
                obj.image = "http://www.alsglobal.com/~/media/Images/Divisions/Life%20Sciences/Food/ALS-Food-Hero.jpg"
                obj.name = "food" + i
                obj.number = Random().nextInt(100000).toString()
                products.add(obj)
            }

            /**
             * You can add you own realm configuration
             * like dev or release
             *
             * note: in each rx thread you shoud take a realm instance
             * i did this, because. realm has their own configuration
             * which prevent called in diff thread
             * and it will avoid invalid manage
             * (anyway so many bugs when you separated realm thred)
             * this is proper way to achieve
             */
            val realm = Realm.getDefaultInstance()
            realm.executeTransaction { realm -> realm.insertOrUpdate(products) }
            emitter.onNext(products)
            realm.close()
        }.map { t -> t }
    }
}