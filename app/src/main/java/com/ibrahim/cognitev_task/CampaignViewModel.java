package com.ibrahim.cognitev_task;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import java.util.List;

public class CampaignViewModel extends AndroidViewModel {
    private static final String TAG = "CampaignViewModel";

    private CampaignRepository repository;
    public CampaignViewModel(@NonNull Application application) {
        super(application);

        repository = new CampaignRepository(application);
    }

    public void insertCampaigns(List<Campaign> campaignList) {
        repository.insertCampaigns(campaignList);
    }

    public int getCount( String yAxisField, String field1,String xAxisField , String field2) {
        String query ="SELECT * FROM Campaign WHERE "+yAxisField+"="+"'"+field1+"'and "+xAxisField+"="+"'"+field2+"'";
        List<Campaign> campaigns =repository.getCampaigns(query);

       return campaigns.size();
    }


    public List<Campaign> getCampaigns(String xAxisField , String title) {
        String query ="SELECT * FROM Campaign WHERE "+xAxisField+"="+"'"+title+"'";

        return  repository.getCampaigns(query);
    }

    public String[] getFields(String fields) {

        return repository.getFieldsOf(fields);
    }
}
