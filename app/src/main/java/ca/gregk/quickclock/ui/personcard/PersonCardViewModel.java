package ca.gregk.quickclock.ui.personcard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PersonCardViewModel extends ViewModel {
    private LiveData<String> name;
    private MutableLiveData<Long> clockIn;
    private MutableLiveData<Long> totalTime;

    private MediatorLiveData<Boolean> isClockedIn;

    public PersonCardViewModel(String name, long clockIn, long totalTime){
        this.name = new MutableLiveData<>(name);
        this.clockIn = new MutableLiveData<>(clockIn);
        this.totalTime = new MutableLiveData<>(totalTime);

        this.isClockedIn = new MediatorLiveData<Boolean>();
        this.isClockedIn.addSource(this.clockIn, value -> isClockedIn.setValue(this.clockIn.getValue() > 0));
    }

    public LiveData<String> getName(){
        return name;
    }

    public LiveData<Long> getClockIn(){
        return clockIn;
    }

    public LiveData<Long> getTotalTime(){
        return totalTime;
    }

    public LiveData<Boolean> getIsClockedIn(){
        return isClockedIn;
    }
}