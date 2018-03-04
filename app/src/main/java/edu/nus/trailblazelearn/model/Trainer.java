package edu.nus.trailblazelearn.model;

public class Trainer extends User {

    public Trainer() {
        super();
        data.put("isTrainer", true);
        addUser();
    }
}

