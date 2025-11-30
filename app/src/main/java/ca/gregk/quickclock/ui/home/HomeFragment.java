package ca.gregk.quickclock.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import ca.gregk.quickclock.FirebaseDB;
import ca.gregk.quickclock.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getAmPm().observe(getViewLifecycleOwner(), binding.amPmView::setText);
        homeViewModel.getTime().observe(getViewLifecycleOwner(), binding.clockView::setText);
        homeViewModel.getDate().observe(getViewLifecycleOwner(), binding.dateText::setText);

        // Set counter for number of people in the shop
        FirebaseDB.getInstance().getClockedIn().observe(getViewLifecycleOwner(), (clockedIn) -> {
            int people = clockedIn.size();
            if (people == 1)
                textView.setText("1 person in the shop.");
            else
                textView.setText(people + " people in the shop.");
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}