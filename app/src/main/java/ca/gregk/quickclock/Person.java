package ca.gregk.quickclock;

import com.google.firebase.firestore.DocumentId;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

public class Person {

    @DocumentId
    public String ID;
    public String name;
    public long totalTime;
    public boolean clockedIn;

    public Person(){

    }

    public Person(String name, boolean clockedIn, long totalTime){
        this.name = name;
        this.totalTime = totalTime;
        this.clockedIn = clockedIn;
    }
}
