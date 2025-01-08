package ca.gregk.quickclock;


import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import androidx.annotation.Nullable;

public class Session {
    public DocumentReference person;
    @Nullable
    public Timestamp start;
    @Nullable
    public Timestamp end = null;

    public Session() { }

    public Session(Person person){
        this.person = person.ID;
        start = null;
        end = null;
    }

    public Session clockIn(){
        if(start == null)
            start = Timestamp.now();
        return this;
    }

    public Session clockOut(){
        if(start != null)
            end = Timestamp.now();
        return this;
    }
}
