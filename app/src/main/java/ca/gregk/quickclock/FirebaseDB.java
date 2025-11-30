package ca.gregk.quickclock;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.nio.charset.StandardCharsets;
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
            // List of tasks being waited on to query relationships
            List<Task<DocumentSnapshot>> tasks = new ArrayList<>();

            ArrayList<Person> clockedIn = new ArrayList<>();
            ArrayList<Person> clockedOut = new ArrayList<>();
            for(DocumentSnapshot document : value.getDocuments()){
                Person person = document.toObject(Person.class);
                if(person.isClockedIn()){
                    clockedIn.add(person);
                    // Add a task to query the person's session
                    tasks.add(person.sessionRef.get().addOnSuccessListener(documentSnapshot -> person.sessionInstance = documentSnapshot.toObject(Session.class)));
                } else {
                    // No extra query needed if clocked out
                    clockedOut.add(person);
                }
            }

            // Wait for all outstanding tasks, then update application
            Tasks.whenAll().addOnCompleteListener(task -> {
                peopleClockedIn.setValue(clockedIn);
                peopleClockedOut.setValue(clockedOut);
            });
        }
    }

    public void clockIn(Person person, @Nullable OnFailureListener failureListener) {
        // Create a session and assign it to the person
        Session session = new Session(person).clockIn();
        Task<DocumentReference> task = sessionCollection.add(session)
                .addOnSuccessListener(sessionDocument -> {
                            person.setSession(sessionDocument, session);
                            peopleCollection.document(person.ID.getId()).set(person);
                        }
                );
        if (failureListener != null)
            task.addOnFailureListener(failureListener);
    }

    public void clockOut(Person person, @Nullable OnFailureListener failureListener){
        // If the persson doesn't have a session, then they're already clocked out
        DocumentReference sessionDoc = person.sessionRef;
        if (sessionDoc == null)
            return;

        // Get the person's session object
        Task<DocumentSnapshot> task = sessionDoc.get()
                .addOnSuccessListener(snapshot -> {
            // Convert to instance
            Session session = snapshot.toObject(Session.class);
            if (session == null)
                return;
            // Apply the clock out
            session.clockOut();

            // Update document
            sessionCollection.document(session.ID.getId()).set(session);

            // Update person total time
            if (session.savedDuration != null)
                person.totalTime += session.savedDuration;
            else
                person.totalTime += session.computeDuration().toMillis();
            peopleCollection.document(person.ID.getId()).set(person);
        });

        // Clear current session
        person.setSession(null, null);
        peopleCollection.document(person.ID.getId()).set(person);

        if (failureListener != null)
            task.addOnFailureListener(failureListener);
    }
}
