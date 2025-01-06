package ca.gregk.quickclock;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

public class Person {
    public final String name;
    public final long clockIn;
    public final long totalTime;
    public final boolean isClockedIn;

    public Person(String name, long clockIn, long totalTime, boolean isClockedIn){
        this.name = name;
        this.clockIn = clockIn;
        this.totalTime = totalTime;
        this.isClockedIn = isClockedIn;
    }
}
