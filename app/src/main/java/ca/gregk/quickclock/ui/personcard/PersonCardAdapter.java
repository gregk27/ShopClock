package ca.gregk.quickclock.ui.personcard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;
import ca.gregk.quickclock.Person;
import ca.gregk.quickclock.R;

public class PersonCardAdapter extends RecyclerView.Adapter<PersonCardAdapter.ViewHolder> {

    private final Context context;
    private final List<Person> people;

    private ViewHolder selectedCard = null;

    private final ClockOutListener clockButtonCallback;

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
    }

    @Override
    public int getItemCount(){
        return people.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameView;
        public View expandedView;

        public Button clockButton;
        public ViewHolder(@NonNull View view){
            super(view);
            nameView = view.findViewById(R.id.nameView);
            expandedView = view.findViewById(R.id.expandedView);
            clockButton = view.findViewById(R.id.clockInButton);
        }
    }

    public interface ClockOutListener {
        void clockButtonClicked(Person person);
    }
}
