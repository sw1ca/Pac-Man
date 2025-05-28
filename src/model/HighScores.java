package model;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighScores {
    private static final String FILE_NAME = "highscores.ser";
    private static List<PlayerScore> highScores = new ArrayList<>();

    static {
        loadHighScores();
    }
    public static void addScore(String name, int score) {
        highScores.add(new PlayerScore(name, score));
        highScores.sort((s1, s2) -> Integer.compare(s2.getScore(), s1.getScore()));
        saveHighScores();
    }
    public static List<PlayerScore> getHighScores() {
        return Collections.unmodifiableList(highScores);
    }
    private static void saveHighScores() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(highScores);
        } catch (IOException e) {
            System.err.println("Error saving high scores: " + e.getMessage());
        }
    }
    private static void loadHighScores() {
        File file = new File(FILE_NAME);
        if(!file.exists()) {
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?> list) {
                if (!list.isEmpty() && list.get(0) instanceof PlayerScore) {
                    highScores = (List<PlayerScore>) list;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading: " + e.getMessage());
        }
    }
    public static void clearHighScores() {
        highScores.clear();
        saveHighScores();
    }
}

