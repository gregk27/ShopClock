package ca.gregk.quickclock.ui.clockin;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ca.gregk.quickclock.FirebaseDB;
import ca.gregk.quickclock.Person;
import ca.gregk.quickclock.Session;

public class ClockInViewModel extends ViewModel {

    private final LiveData<List<Person>> people;

    public ClockInViewModel() {
        people = FirebaseDB.getInstance().getClockedOut();
    }

    public void clockIn(Person person, Context context){
        FirebaseDB.getInstance().clockIn(person, err -> {
            Toast.makeText(context, "Error creating session", Toast.LENGTH_LONG)
                    .show();
        });
    }

    public LiveData<List<Person>> getPeople() {
        return people;
    }
}