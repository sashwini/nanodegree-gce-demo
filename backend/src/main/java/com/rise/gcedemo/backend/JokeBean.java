package com.rise.gcedemo.backend;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class JokeBean implements Serializable{
    private int jokeId;
    private String joke;

    public String getJoke() {
        return joke;
    }

    public void setJoke(String joke) {
        this.joke = joke;
    }

    public int getJokeId() {
        return jokeId;
    }

    public void setJokeId(int jokeId) {
        this.jokeId = jokeId;
    }

    public byte[] toByteArray() throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(this );
        return baos.toByteArray();
    }

    public JokeBean fromByteArray(byte[] byteArray) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
        ObjectInputStream ois = new ObjectInputStream( bais );
        Object o = ois.readObject();
        return (JokeBean) o;
    }
}
