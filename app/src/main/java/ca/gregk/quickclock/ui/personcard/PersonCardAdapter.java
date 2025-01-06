package ca.gregk.quickclock.ui.personcard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ca.gregk.quickclock.Person;
import ca.gregk.quickclock.R;

public class PersonCardAdapter extends RecyclerView.Adapter<PersonCardAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Person> people;

    public PersonCardAdapter(Context context, ArrayList<Person> people) {
        this.context = context;
        this.people = people;
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
    }

    @Override
    public int getItemCount(){
        return people.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameView;
        public ViewHolder(@NonNull View view){
            super(view);
            nameView = view.findViewById(R.id.nameView);
        }
    }
}
