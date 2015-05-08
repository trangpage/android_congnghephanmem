package com.trangpig.myapp.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TrangPig on 05/07/2015.
 */
public class Subject {
    List<Observer> observerList;

    public Subject(){
        this.observerList = new ArrayList<>();
    }

   public void addObserver(Observer observer){
       this.observerList.add(observer);
   }

    public void notifyChange(){
        for (Observer observer : observerList) {
            observer.update();
        }
    }
}
