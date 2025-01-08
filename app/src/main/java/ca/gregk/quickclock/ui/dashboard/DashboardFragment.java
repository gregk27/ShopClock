package ca.gregk.quickclock.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import ca.gregk.quickclock.Person;
import ca.gregk.quickclock.Session;
import ca.gregk.quickclock.databinding.FragmentDashboardBinding;
import ca.gregk.quickclock.ui.personcard.PersonCardAdapter;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    private PersonCardAdapter adapter;

    private CollectionReference peopleDB, sessionDB;

    ArrayList<Person> people;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        people = new ArrayList<>();

        FirebaseFirestore instance = FirebaseFirestore.getInstance();
        peopleDB = instance.collection("/People");
        sessionDB = instance.collection("/Sessions");

        adapter = new PersonCardAdapter(getContext(), people, (Person person) -> {
            // Create a session and assign it to the person
            Session session = new Session(person).clockIn();
            sessionDB.add(session)
                    .addOnSuccessListener(sessionDocument -> {
                            person.currentSession = sessionDocument;
                            peopleDB.document(person.ID.getId()).set(person);
                        }
                    )
                    .addOnFailureListener(err -> {
                        Toast.makeText(getContext(), "Error creating session", Toast.LENGTH_LONG)
                                .show();
                    });
        });

        peopleDB.orderBy("name").addSnapshotListener(this::onPeopleChanged);

        LinearLayoutManager layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        binding.recycler.setLayoutManager(layout);
        binding.recycler.setAdapter(adapter);

        return root;
    }

    private void onPeopleChanged(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
        // When data changes, then update list of people
        // Currently this just updates everyone at once, could make it more efficient later
        if (value != null){
            people.clear();
            for(DocumentSnapshot document : value.getDocuments()){
                Person person = document.toObject(Person.class);
                if(!person.isClockedIn()){
                    people.add(person);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}