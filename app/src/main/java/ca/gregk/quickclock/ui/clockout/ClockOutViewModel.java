package ca.gregk.quickclock.ui.clockout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ClockOutViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ClockOutViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}