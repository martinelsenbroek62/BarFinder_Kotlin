package com.lemust.ui.base.views.base.models;

import android.graphics.Canvas;

import java.util.ArrayList;


public abstract class GameObject {
//    protected ObjectType objectType;
    public GameObject() {

        //this.objectType = objectType;
    }
    public abstract void render(Canvas canvas);

    public abstract void tick(ArrayList<GameObject> objects);

//    public ObjectType getObjectType() {
//        return objectType;
//    }
//
//    public void setObjectType(ObjectType objectType) {
//        this.objectType = objectType;
//    }
}
