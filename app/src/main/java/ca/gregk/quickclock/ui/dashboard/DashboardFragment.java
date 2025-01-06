package ca.gregk.quickclock.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import ca.gregk.quickclock.Person;
import ca.gregk.quickclock.databinding.FragmentDashboardBinding;
import ca.gregk.quickclock.ui.personcard.PersonCardAdapter;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ArrayList<Person> people = new ArrayList<Person>();
        people.add(new Person("Person 1", -1, System.currentTimeMillis(), false));
        people.add(new Person("Person 2", -1, System.currentTimeMillis(), false));
        people.add(new Person("Person 3", -1, System.currentTimeMillis(), false));
        people.add(new Person("Person 4", -1, System.currentTimeMillis(), false));

        PersonCardAdapter adapter = new PersonCardAdapter(getContext(), people);

        LinearLayoutManager layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        binding.recycler.setLayoutManager(layout);
        binding.recycler.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}