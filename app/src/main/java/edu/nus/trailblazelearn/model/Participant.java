package edu.nus.trailblazelearn.model;

public class Participant extends User {

    public Participant() {
        super();
        data.put("isParticipant", true);
        addUser();
    }
}
