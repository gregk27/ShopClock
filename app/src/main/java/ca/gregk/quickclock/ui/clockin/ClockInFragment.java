package ca.gregk.quickclock.ui.clockin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import ca.gregk.quickclock.Person;
import ca.gregk.quickclock.Session;
import ca.gregk.quickclock.databinding.FragmentClockInBinding;
import ca.gregk.quickclock.ui.personcard.PersonCardAdapter;

public class ClockInFragment extends Fragment {

    private FragmentClockInBinding binding;
    private PersonCardAdapter adapter;

    List<Person> people;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ClockInViewModel clockInViewModel =
                new ViewModelProvider(this).get(ClockInViewModel.class);

        binding = FragmentClockInBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize local copy of people
        // Copy data over so mutating later doesn't alter the master data
        people = new ArrayList<>();
        List<Person> tmp = clockInViewModel.getPeople().getValue();
        if (tmp != null)
            people.addAll(tmp);

        adapter = new PersonCardAdapter(getContext(), people, person -> clockInViewModel.clockIn(person, getContext()));

        LinearLayoutManager layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        binding.recycler.setLayoutManager(layout);
        binding.recycler.setAdapter(adapter);

        clockInViewModel.getPeople().observe(getViewLifecycleOwner(), ppl -> {
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