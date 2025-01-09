package ca.gregk.quickclock;

import android.app.people.PeopleManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class FirebaseDB {

    CollectionReference peopleCollection, sessionCollection;

    private final MutableLiveData<List<Person>> peopleClockedIn;
    private final MutableLiveData<List<Person>> peopleClockedOut;

    private static final FirebaseDB instance = new FirebaseDB();
    public static FirebaseDB getInstance(){
        return instance;
    }

    private FirebaseDB() {
        peopleClockedIn = new MutableLiveData<>(new ArrayList<>());
        peopleClockedOut = new MutableLiveData<>(new ArrayList<>());

        FirebaseFirestore instance = FirebaseFirestore.getInstance();
        peopleCollection = instance.collection("/People");
        sessionCollection = instance.collection("/Sessions");

        peopleCollection.orderBy("name").addSnapshotListener(this::onPeopleChanged);
    }

    public LiveData<List<Person>> getClockedIn() {
        return peopleClockedIn;
    }

    public LiveData<List<Person>> getClockedOut() {
        return peopleClockedOut;
    }

    private void onPeopleChanged(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
        // When data changes, then update list of people
        // Currently this just updates everyone at once, could make it more efficient later
        if (value != null){
            ArrayList<Person> clockedIn = new ArrayList<>();
            ArrayList<Person> clockedOut = new ArrayList<>();
            for(DocumentSnapshot document : value.getDocuments()){
                Person person = document.toObject(Person.class);
                if(person.isClockedIn()){
                    clockedIn.add(person);
                } else {
                    clockedOut.add(person);
                }
            }
            peopleClockedIn.setValue(clockedIn);
            peopleClockedOut.setValue(clockedOut);
        }
    }

    public void clockIn(Person person, @Nullable OnFailureListener failureListener) {
        // Create a session and assign it to the person
        Session session = new Session(person).clockIn();
        Task<DocumentReference> task = sessionCollection.add(session)
                .addOnSuccessListener(sessionDocument -> {
                            person.currentSession = sessionDocument;
                            peopleCollection.document(person.ID.getId()).set(person);
                        }
                );
        if (failureListener != null)
            task.addOnFailureListener(failureListener);
    }
}
