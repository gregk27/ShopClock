package ca.gregk.quickclock.ui.clockout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import ca.gregk.quickclock.databinding.FragmentClockOutBinding;

public class ClockOutFragment extends Fragment {

    private FragmentClockOutBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ClockOutViewModel clockOutViewModel =
                new ViewModelProvider(this).get(ClockOutViewModel.class);

        binding = FragmentClockOutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        clockOutViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}