package ca.gregk.quickclock.ui.personcard;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ca.gregk.quickclock.R;
import ca.gregk.quickclock.databinding.FragmentPersonCardBinding;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class PersonCard extends Fragment {

    private FragmentPersonCardBinding binding;
    private PersonCardViewModel viewModel;

    private Handler handler;
    private Runnable runnable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(PersonCardViewModel.class);

        binding = FragmentPersonCardBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);

        View root = binding.getRoot();

        // Setup periodic update
        handler = new Handler(Looper.getMainLooper());
        runnable = () -> {
            boolean clockedIn = viewModel.getIsClockedIn().getValue();
            if (clockedIn) {
                long clockedInTime = System.currentTimeMillis() - viewModel.getClockIn().getValue();
                binding.sessionTime.setText(timeToStr(clockedInTime));
                binding.totalTime.setText(timeToStr(viewModel.getTotalTime().getValue() + clockedInTime));
            } else {
                binding.sessionTime.setText("N/A");
                binding.totalTime.setText(timeToStr(viewModel.getTotalTime().getValue()));
            }
            handler.postDelayed(runnable, 1000);
        };
        runnable.run();

        return root;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
        handler.getLooper().quit();
    }

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