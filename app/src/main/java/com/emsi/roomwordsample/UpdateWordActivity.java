package com.emsi.roomwordsample;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateWordActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY = "com.emsi.roomwordsample.REPLY";
    public static final String EXTRA_WORD_ID = "com.emsi.roomwordsample.WORD_ID";
    public static final String EXTRA_WORD_TEXT = "com.emsi.roomwordsample.WORD_TEXT";

    private EditText mEditWordView;
    private int wordId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_word); // Use the update word layout

        mEditWordView = findViewById(R.id.edit_word);

        // Get the word data from the intent
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_WORD_ID) && intent.hasExtra(EXTRA_WORD_TEXT)) {
            wordId = intent.getIntExtra(EXTRA_WORD_ID, -1);
            String wordText = intent.getStringExtra(EXTRA_WORD_TEXT);
            mEditWordView.setText(wordText);
        }

        final Button button = findViewById(R.id.button_save);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditWordView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String word = mEditWordView.getText().toString();
                    replyIntent.putExtra(EXTRA_REPLY, word);
                    replyIntent.putExtra(EXTRA_WORD_ID, wordId);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}
