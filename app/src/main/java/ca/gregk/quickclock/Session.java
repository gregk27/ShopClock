package ca.gregk.quickclock;


import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.PropertyName;

import java.time.Duration;

import androidx.annotation.Nullable;

public class Session {
    @DocumentId
    public DocumentReference ID;
    public DocumentReference person;
    @Nullable
    public Timestamp start;
    @Nullable
    public Timestamp end = null;

    // Duration computed at clock-out for database storage
    @Nullable
    @PropertyName("duration")
    public Long savedDuration = null;

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
        savedDuration = computeDuration().toMillis();
        return this;
    }

    public Duration computeDuration(){
        long startMS, endMS;

        // Compute start and end times
        if (start == null)
            startMS = 0;
        else
            startMS = start.toDate().getTime();

        if (end == null)
            endMS = System.currentTimeMillis();
        else
            endMS = end.toDate().getTime();

        return Duration.ofMillis(endMS - startMS);
    }
}
