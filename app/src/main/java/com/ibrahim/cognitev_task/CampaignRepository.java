package com.ibrahim.cognitev_task;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SimpleSQLiteQuery;
import android.util.Log;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class CampaignRepository {

    private static final String TAG = "MessageRepository";
    private CampaignDao campaignDao;
    private LiveData<List<Campaign>> mListLiveData;

    public CampaignRepository(Application application){
        CampaignDatabase database = CampaignDatabase.getInstance(application);
        campaignDao = database.CampaignDao();

    }


    public List<Campaign> getCampaigns(final String query) {
        Log.d(TAG, "getCampaigns: query "+query);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<List<Campaign>> callable = new Callable<List<Campaign>>() {
            @Override
            public List<Campaign> call() {
                SimpleSQLiteQuery simpleSQLiteQuery = new SimpleSQLiteQuery(query);
                List<Campaign> campaigns = campaignDao.getCount(simpleSQLiteQuery);
                return campaigns;
            }
        };

        Future<List<Campaign>> future =  executor.submit(callable);
        try {
            return future.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
        return null;
    }

    public void insertCampaigns(final List<Campaign> campaignList) {
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    //insert campaign list into database in background thread
                campaignDao.insertAll(campaignList);
                }
            });

    }

    public String[] getFieldsOf(final String field) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<String[]> callable = new Callable<String[]>() {
            @Override
            public String[] call() {
//                String [] values ;
                SimpleSQLiteQuery sqLiteQuery = new SimpleSQLiteQuery("SELECT "+field+" FROM Campaign group by "+field);
                return campaignDao.getQuery(sqLiteQuery);
            }
        };

        Future<String[]> future =  executor.submit(callable);

        try {
            return future.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
