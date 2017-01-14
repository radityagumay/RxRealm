package com.radityalabs.android.kotlin

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.radityalabs.android.R
import com.radityalabs.android.realm.ProductObject
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import java.util.*
import kotlin.properties.Delegates

/**
 * Created by radityagumay on 1/14/17.
 */

class MainFragment : Fragment(), MainView {
    override fun onResult(obj: List<ProductObject>?) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        val TAG: String = MainFragment::class.java.simpleName
    }

    private var mPresenter: MainPresenter by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = MainPresenterImpl(context, this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.insertNewDatas()
    }
}