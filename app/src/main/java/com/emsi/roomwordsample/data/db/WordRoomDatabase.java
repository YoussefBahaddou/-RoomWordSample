package com.emsi.roomwordsample.data.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.emsi.roomwordsample.model.Word;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Update the version number from 1 to 2
@Database(entities = {Word.class}, version = 2, exportSchema = false)
public abstract class WordRoomDatabase extends RoomDatabase {
    public abstract WordDao wordDao();

    private static volatile WordRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // Add the callback
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            // Si vous souhaitez conserver les données au redémarrage de l'application
            // commentez le bloc suivant
            databaseWriteExecutor.execute(() -> {
                // Remplir la base de données en arrière-plan
                // Si vous voulez commencer avec plus de mots, il suffit de les ajouter.
                WordDao dao = INSTANCE.wordDao();
                dao.deleteAll();
                Word word = new Word("Hello");
                dao.insert(word);
                word = new Word("World");
                dao.insert(word);
            });
        }
    };

    public static WordRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WordRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    WordRoomDatabase.class, "word_database")
                            .fallbackToDestructiveMigration() // This will recreate the database if the version changes
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
