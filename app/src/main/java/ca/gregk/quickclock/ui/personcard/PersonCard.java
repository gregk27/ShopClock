package ca.gregk.quickclock.ui.personcard;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ca.gregk.quickclock.Person;
import ca.gregk.quickclock.R;
import ca.gregk.quickclock.databinding.FragmentPersonCardBinding;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class PersonCard extends View {

//    private Handler handler;
//    private Runnable runnable;

    public PersonCard(Context context) {
        super(context);
    }

//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//
//        binding = FragmentPersonCardBinding.inflate(inflater, container, false);
//
//        View root = binding.getRoot();
//
//        // Setup periodic update
//        handler = new Handler(Looper.getMainLooper());
//        runnable = () -> {
//            boolean clockedIn = person.isClockedIn;
//            if (clockedIn) {
//                long clockedInTime = System.currentTimeMillis() - person.clockIn;
//                binding.sessionTimeView.setText(timeToStr(clockedInTime));
//                binding.totalTimeView.setText(timeToStr(person.totalTime + clockedInTime));
//            } else {
//                binding.sessionTimeView.setText("N/A");
//                binding.totalTimeView.setText(timeToStr(person.totalTime));
//            }
//            handler.postDelayed(runnable, 1000);
//        };
//        runnable.run();
//
//        return root;
//    }
//
//    @Override
//    public void onResume(){
//        super.onResume();
//    }
//
//    @Override
//    public void onPause(){
//        super.onPause();
//    }
//
//    @Override
//    public void onDestroyView(){
//        super.onDestroyView();
//        binding = null;
//        handler.getLooper().quit();
//    }

    public String timeToStr(long time){
        // Convert to seconds
        time /= 1000;
        long seconds = time % 60;
        long minutes = (time / 60) % 60;
        long hours = time / 3600;

        StringBuilder builder = new StringBuilder();
        if(hours > 0){
            builder.append(hours);
            builder.append(" hours ");
        }
        builder.append(minutes);
        builder.append(" minutes ");
        builder.append(seconds);
        builder.append(" seconds");

        return builder.toString();
    }
}