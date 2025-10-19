package ca.gregk.quickclock;

import com.google.firebase.database.PropertyName;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;

import androidx.annotation.Nullable;

public class Person {

    @DocumentId
    public DocumentReference ID;
    public String name;
    public long totalTime;
    @Nullable
    @PropertyName("currentSession")
    public DocumentReference sessionRef = null;
    @Exclude @Nullable
    public Session sessionInstance = null;

    public Person(){

    }

    public Person(String name, @Nullable DocumentReference sessionRef, long totalTime){
        this.name = name;
        this.totalTime = totalTime;
        this.sessionRef = sessionRef;
    }

    @Exclude
    public boolean isClockedIn(){
        return sessionRef != null;
    }

    public void setSession(DocumentReference document, Session instance){
        sessionRef = document;
        sessionInstance = instance;
    }
}
