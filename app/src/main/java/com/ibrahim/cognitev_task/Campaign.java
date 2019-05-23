package com.ibrahim.cognitev_task;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Campaign {

    @NonNull
    @PrimaryKey
    private String name ;
    private String country ;
    private String budget;
    private String goal;
    private String category;

    //constructor
    public Campaign() {
    }


    //getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Campaign{" +
                "name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", budget='" + budget + '\'' +
                ", goal='" + goal + '\'' +
                ", category='" + category + '\'' +
                '}'+'\n';
    }
}
