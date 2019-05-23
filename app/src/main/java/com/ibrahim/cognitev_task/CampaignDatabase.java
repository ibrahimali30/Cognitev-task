package com.ibrahim.cognitev_task;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;



@Database(entities = {Campaign.class} , version = 1 )
public abstract class CampaignDatabase extends RoomDatabase {
    private static final String TAG = "MessageDatabase";
    private static CampaignDatabase instance;

    public abstract CampaignDao CampaignDao();

    public static synchronized CampaignDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                   CampaignDatabase.class ,"note database" )
                    .fallbackToDestructiveMigration()
                    .addCallback(callback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback callback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new populateDbAsyncTask(instance).execute();
        }

    };

    private static class populateDbAsyncTask extends AsyncTask<Void , Void , Void>{

        private CampaignDao mNoteDao;

        public populateDbAsyncTask(CampaignDatabase db) {
            mNoteDao = db.CampaignDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(TAG, "doInBackground: called");

            return null;
        }
    }
}
