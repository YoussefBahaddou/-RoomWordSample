package com.emsi.roomwordsample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.emsi.roomwordsample.adapter.WordItemClickListener;
import com.emsi.roomwordsample.adapter.WordListAdapter;
import com.emsi.roomwordsample.databinding.ActivityMainBinding;
import com.emsi.roomwordsample.model.Word;

public class MainActivity extends AppCompatActivity implements WordItemClickListener {

    private ActivityMainBinding binding;
    private WordViewModel mWordViewModel;

    // Define request codes
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    public static final int UPDATE_WORD_ACTIVITY_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        // Set up the RecyclerView with the listener
        WordListAdapter mAdapter = new WordListAdapter(this);
        binding.contentMain.recyclerview.setAdapter(mAdapter);
        binding.contentMain.recyclerview.setHasFixedSize(true);

        // Get a ViewModel from the ViewModelProvider
        mWordViewModel = new ViewModelProvider(this).get(WordViewModel.class);

        // Add an observer on the LiveData
        mWordViewModel.getAllWords().observe(this, words -> {
            // Update the cached copy of the words in the adapter
            mAdapter.setWords(words);
        });

        // Update FAB click listener to start NewWordActivity
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewWordActivity.class);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    // Implement the long click listener method
    @Override
    public void onWordLongClick(Word word) {
        // Show options dialog
        String[] options = {"Update", "Delete"};

        new AlertDialog.Builder(this)
                .setTitle("Word Options")
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0: // Update
                            Intent intent = new Intent(MainActivity.this, UpdateWordActivity.class);
                            intent.putExtra(UpdateWordActivity.EXTRA_WORD_ID, word.getId());
                            intent.putExtra(UpdateWordActivity.EXTRA_WORD_TEXT, word.getWord());
                            startActivityForResult(intent, UPDATE_WORD_ACTIVITY_REQUEST_CODE);
                            break;
                        case 1: // Delete
                            // Show delete confirmation dialog
                            new AlertDialog.Builder(this)
                                    .setTitle("Delete Word")
                                    .setMessage("Do you want to delete the word '" + word.getWord() + "'?")
                                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                                        // Delete the word if user confirms
                                        mWordViewModel.delete(word);
                                    })
                                    .setNegativeButton("No", null) // Do nothing if user cancels
                                    .show();
                            break;
                    }
                })
                .show();
    }

    // Handle results from both NewWordActivity and UpdateWordActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE) {
                // Handle new word
                Word word = new Word(data.getStringExtra(NewWordActivity.EXTRA_REPLY));
                mWordViewModel.insert(word);
            } else if (requestCode == UPDATE_WORD_ACTIVITY_REQUEST_CODE) {
                // Handle updated word
                String wordText = data.getStringExtra(UpdateWordActivity.EXTRA_REPLY);
                int wordId = data.getIntExtra(UpdateWordActivity.EXTRA_WORD_ID, -1);

                if (wordId != -1) {
                    Word word = new Word(wordText);
                    word.setId(wordId);
                    mWordViewModel.update(word);
                    Toast.makeText(getApplicationContext(), "Word updated", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
