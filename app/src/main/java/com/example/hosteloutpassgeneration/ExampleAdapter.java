package com.example.hosteloutpassgeneration;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private ArrayList<ExampleItem> mExampleList;
    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1,mTextView3;
        public TextView mTextView2,mTextView4,mTextView5;
        public ExampleViewHolder(View itemView) {
            super(itemView);
            mTextView3 = itemView.findViewById(R.id.outtimee);
            mTextView4 =itemView.findViewById(R.id.recomno);
            mTextView1 = itemView.findViewById(R.id.studid);
            mTextView2 = itemView.findViewById(R.id.outdatee);
            mTextView5 = itemView.findViewById(R.id.recomname);
        }
    }
    public ExampleAdapter(ArrayList<ExampleItem> exampleList) {
        mExampleList = exampleList;
    }
    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item,parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }
    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        ExampleItem currentItem = mExampleList.get(position);
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
        holder.mTextView3.setText(currentItem.getText3());
        holder.mTextView4.setText(currentItem.getText4());
        holder.mTextView5.setText(currentItem.getText5());
    }
    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
    public void filterList(ArrayList<ExampleItem> filteredList) {
        mExampleList = filteredList;
        notifyDataSetChanged();
    }
}