package ca.gregk.quickclock.ui.clockout;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ca.gregk.quickclock.FirebaseDB;
import ca.gregk.quickclock.Person;

public class ClockOutViewModel extends ViewModel {

    private final LiveData<List<Person>> people;

    public ClockOutViewModel() {
        people = FirebaseDB.getInstance().getClockedIn();
    }

    public void clockOut(Person person, Context context){
        FirebaseDB.getInstance().clockOut(person, err -> {
            Toast.makeText(context, "Error creating session", Toast.LENGTH_LONG)
                    .show();
        });
    }

    public LiveData<List<Person>> getPeople() {
        return people;
    }
}