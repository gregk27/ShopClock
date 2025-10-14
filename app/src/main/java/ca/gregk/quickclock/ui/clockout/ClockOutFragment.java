package ca.gregk.quickclock.ui.clockout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import ca.gregk.quickclock.Person;
import ca.gregk.quickclock.databinding.FragmentClockOutBinding;
import ca.gregk.quickclock.ui.personcard.PersonCardAdapter;

public class ClockOutFragment extends Fragment {

    private FragmentClockOutBinding binding;
    private PersonCardAdapter adapter;

    List<Person> people;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ClockOutViewModel clockOutViewModel =
                new ViewModelProvider(this).get(ClockOutViewModel.class);

        binding = FragmentClockOutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize local copy of people
        // Copy data over so mutating later doesn't alter the master data
        people = new ArrayList<>();
        List<Person> tmp = clockOutViewModel.getPeople().getValue();
        if (tmp != null)
            people.addAll(tmp);

        adapter = new PersonCardAdapter(getContext(), people, person -> clockOutViewModel.clockOut(person, getContext()));

        LinearLayoutManager layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        binding.recycler.setLayoutManager(layout);
        binding.recycler.setAdapter(adapter);

        clockOutViewModel.getPeople().observe(getViewLifecycleOwner(), ppl -> {
            people.clear();
            people.addAll(ppl);
            adapter.notifyDataSetChanged();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}