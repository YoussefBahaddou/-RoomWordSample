package com.emsi.roomwordsample.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emsi.roomwordsample.R;
import com.emsi.roomwordsample.model.Word;

import java.util.ArrayList;
import java.util.List;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {
    private List<Word> mWords; // Copie en cache des mots
    private WordItemClickListener mListener;

    public WordListAdapter(WordItemClickListener listener) {
        mListener = listener;
        mWords = new ArrayList<>();
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        if (mWords != null) {
            Word current = mWords.get(position);
            holder.wordItemView.setText(current.getWord());

            // Set long click listener
            holder.itemView.setOnLongClickListener(v -> {
                if (mListener != null) {
                    mListener.onWordLongClick(current);
                    return true;
                }
                return false;
            });
        } else {
            // Couvre le cas où les données ne sont pas encore prêtes.
            holder.wordItemView.setText("No Word");
        }
    }

    public void setWords(List<Word> words){
        mWords = words;
        notifyDataSetChanged();
    }

    // getItemCount() est appelé plusieurs fois, et lorsqu'il est appelé pour la première fois,
    // mWords n'a pas été mis à jour (cela signifie qu'initialement, il est nul, et nous ne pouvons pas retourner nul).
    @Override
    public int getItemCount() {
        if (mWords != null)
            return mWords.size();
        else return 0;
    }

    class WordViewHolder extends RecyclerView.ViewHolder {
        private final TextView wordItemView;

        private WordViewHolder(View itemView) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.textView);
        }
    }
}