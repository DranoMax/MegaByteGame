package com.megabyte.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ascroggins on 8/22/2015.
 *
 * When creating maps,
 * x's = blocks
 * o's = empty space
 * s   = Player Character start location
 * m   = mom
 */
public class MapReader {

    private FileHandle file = Gdx.files.internal("maps/map1.txt");
    private BufferedReader reader = new BufferedReader(file.reader());

    private char map[][];

    /**
     * Takes a 'map' txt file and converts it into a double array.
     */
    public void readMap() {

        List<String> lines = new ArrayList<String>();
        String line = readLine();

        while (line != null) {
            lines.add(line);
            line = readLine();
        }

        map = new char[lines.size()][lines.get(0).length()];

        for (int i = 0; i < lines.size(); i++) {
            for (int x = 0; x < lines.get(0).length(); x++) {
                map[i][x] = lines.get(i).charAt(x);
            }
        }
    }

    /**
     * Simple function to move the throw/catch required for reading lines out of the main code
     * @return
     */
    private String readLine() {
        String line = "";
        try {
            line = reader.readLine();
        } catch (IOException e) {}

        return line;
    }

    public char[][] getMap() {
        return map;
    }
}
