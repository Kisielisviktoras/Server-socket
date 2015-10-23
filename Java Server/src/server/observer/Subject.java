package server.observer;

import server.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saji on 10/22/2015.
 */
public abstract class Subject extends Thread {

    private List<Observer> observerList = new ArrayList<>();

    public void notifyObservers(Message message) {
        for (Observer observer : observerList) {
            if (!observer.equals(message.getOwner())) {
                observer.update(message);
            }
        }
    }

    public void registerObserver(Observer observer, Message notificationMesssage) {
        notifyObservers(notificationMesssage);
        this.observerList.add(observer);
    }

}
