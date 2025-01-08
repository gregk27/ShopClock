package ca.gregk.quickclock;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

public class Person {

    @DocumentId
    public DocumentReference ID;
    public String name;
    public long totalTime;
    @Nullable
    public DocumentReference currentSession = null;

    public Person(){

    }

    public Person(String name, @Nullable DocumentReference currentSession, long totalTime){
        this.name = name;
        this.totalTime = totalTime;
        this.currentSession = currentSession;
    }

    @Exclude
    public boolean isClockedIn(){
        return currentSession != null;
    }
}
