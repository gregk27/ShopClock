package ca.gregk.quickclock.ui.home;

import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.common.util.concurrent.HandlerExecutor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import kotlinx.coroutines.scheduling.CoroutineScheduler;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    private final MutableLiveData<String> timeText;
    private final MutableLiveData<String> ampmText;
    private final MutableLiveData<String> dateText;

    private final Handler clockTicker = new Handler();
    private final SimpleDateFormat clockFormat = new SimpleDateFormat("h:mm");
    private final SimpleDateFormat ampmFormat = new SimpleDateFormat("a");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE MMMM dd, yyyy");

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");

        timeText = new MutableLiveData<>();
        timeText.setValue("00:00");

        ampmText = new MutableLiveData<>();
        ampmText.setValue("AM");

        dateText = new MutableLiveData<>();
        dateText.setValue("Day, 0, Month, 20XX");

        updateClock();

        // Schedule first update 10ms after the next minute, then will schedule every 60s
        int minute = 60 * 1000;
        long secondsLeftInMinute = minute - (System.currentTimeMillis() % minute);
        clockTicker.postDelayed(this::updateAndSchedule, secondsLeftInMinute + 10);
    }

    private void updateClock(){
        Date date = new Date();
        timeText.setValue(clockFormat.format(date));
        ampmText.setValue(ampmFormat.format(date));
        dateText.setValue(dateFormat.format(date));
    }

    private void updateAndSchedule(){
        updateClock();
        // Set next occurrence
        clockTicker.postDelayed(this::updateAndSchedule, 60 * 1000);
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<String> getTime() { return timeText; }
    public LiveData<String> getAmPm() { return ampmText; }
    public LiveData<String> getDate() { return dateText; }
}