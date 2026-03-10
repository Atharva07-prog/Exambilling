package com.example.Exambilling;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.example.Exambilling.AssignedBundle;


public class AssignedBundleAdapter extends RecyclerView.Adapter<AssignedBundleAdapter.AssignedBundleViewHolder> {

    private final List<AssignedBundle> assignedBundles;  // ✅ Remove MainActivity.

    public AssignedBundleAdapter(List<AssignedBundle> assignedBundles) {
        this.assignedBundles = assignedBundles;
    }


    @NonNull
    @Override
    public AssignedBundleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assigned_bundle_item, parent, false);
        return new AssignedBundleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignedBundleViewHolder holder, int position) {
        AssignedBundle bundle = assignedBundles.get(position);
        holder.bundleNameTextView.setText("Bundle Name: " + bundle.getBundleName());
        holder.bundleIdTextView.setText("Bundle ID: " + bundle.getBundleId());
        holder.paperNameTextView.setText("Paper Name: " + bundle.getPaperName());
        holder.paperIdTextView.setText("Paper ID: " + bundle.getPaperId());
    }

    @Override
    public int getItemCount() {
        return assignedBundles.size();
    }

    public static class AssignedBundleViewHolder extends RecyclerView.ViewHolder {
        public final TextView bundleNameTextView;
        public final TextView bundleIdTextView;
        public final TextView paperNameTextView;
        public final TextView paperIdTextView;

        public AssignedBundleViewHolder(View itemView) {
            super(itemView);
            bundleNameTextView = itemView.findViewById(R.id.bundleNameTextView);
            bundleIdTextView = itemView.findViewById(R.id.bundleIdTextView);
            paperNameTextView = itemView.findViewById(R.id.paperNameTextView);
            paperIdTextView = itemView.findViewById(R.id.paperIdTextView);
        }
    }
}
