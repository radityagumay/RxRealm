package com.radityalabs.android.realm;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by radityagumay on 1/14/17.
 */

public class ProductObject extends RealmObject {

    @Ignore
    public static final String ID = "id";
    @Ignore
    public static final String NAME = "name";
    @Ignore
    public static final String IMAGE = "image";
    @Ignore
    public static final String NUMBER = "number";

    @PrimaryKey
    public long id;
    public String name;
    public String image;
    public String number;

    public ProductObject() {

    }
}
