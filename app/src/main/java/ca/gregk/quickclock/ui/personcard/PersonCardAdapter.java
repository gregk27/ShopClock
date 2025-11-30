package ca.gregk.quickclock.ui.personcard;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ca.gregk.quickclock.Person;
import ca.gregk.quickclock.R;

public class PersonCardAdapter extends RecyclerView.Adapter<PersonCardAdapter.ViewHolder> {

    private final Context context;
    private final List<Person> people;

    private ViewHolder selectedCard = null;

    private final ClockOutListener clockButtonCallback;

    private Handler updateHandler = new Handler();

    public PersonCardAdapter(Context context, List<Person> people, ClockOutListener clockButtonCallback) {
        this.context = context;
        this.people = people;
        this.clockButtonCallback = clockButtonCallback;
    }

    @NonNull
    @Override
    public PersonCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_person_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        Person person = people.get(position);
        holder.nameView.setText(person.name);

        // If the person is clocked in, create an updater to manage the session time view
        if (person.isClockedIn()) {
            new ClockUpdater(person, holder);
        }

        holder.expandedView.setVisibility(View.GONE);

        holder.itemView.setOnClickListener((View v) -> {
            if(selectedCard != null)
                selectedCard.expandedView.setVisibility(View.GONE);
            holder.expandedView.setVisibility(View.VISIBLE);
            selectedCard = holder;
        });

        holder.clockButton.setOnClickListener((View v) -> {
            clockButtonCallback.clockButtonClicked(person);
        });

        if (person.isClockedIn()){
            holder.clockButton.setText("Clock Out");
        } else {
            holder.clockButton.setText("Clock In");
        }
    }

    @Override
    public int getItemCount(){
        return people.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameView;
        public View expandedView;
        public TextView sessionTimeView;

        public Button clockButton;
        public ViewHolder(@NonNull View view){
            super(view);
            nameView = view.findViewById(R.id.nameView);
            expandedView = view.findViewById(R.id.expandedView);
            sessionTimeView = view.findViewById(R.id.sessionTimeView);
            clockButton = view.findViewById(R.id.clockInButton);
        }
    }

    private class ClockUpdater {
        private final Person person;
        private final ViewHolder holder;

        public ClockUpdater(Person person, ViewHolder holder){
            this.person = person;
            this.holder = holder;
            update();
        }

        private void update(){
            // Extra null check to be safe
            if (person.sessionInstance != null)
                holder.sessionTimeView.setText(timeToStr(person.sessionInstance.computeDuration().toMillis()));
            updateHandler.postDelayed(this::update, 1000);
        }

        public String timeToStr(long time){
            // Convert to seconds
            time /= 1000;
            long seconds = time % 60;
            long minutes = (time / 60) % 60;
            long hours = time / 3600;

            StringBuilder builder = new StringBuilder();
            // Show hours if more than one
            if(hours > 0){
                builder.append(hours);
                builder.append(" hours ");
            }
            // Always show minutes
            builder.append(minutes);
            builder.append(" minutes ");
            // Only show seconds up to 15 minutes
            if (minutes < 15){
                builder.append(seconds);
                builder.append(" seconds");
            }

            return builder.toString();
        }
    }

    public interface ClockOutListener {
        void clockButtonClicked(Person person);
    }
}
