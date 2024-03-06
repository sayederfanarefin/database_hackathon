package com.databasecourse.erfan.web.util;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HackathonTeamNameGenerator {

    private static final String[] ADJECTIVES = {
            "Agile", "Blazing", "CodeBurst", "Daring", "EagleEye",
            "Fierce", "Geeky", "Hackerz", "Innovative", "Jedi",
            "Killer", "Luminous", "Maverick", "Ninja", "Outliers",
            "Phoenix", "Quantum", "Rapid", "Stealthy", "Techie",
            "Ultra", "Vortex", "Wired", "Xtreme", "Yolo"
    };

    private static final String[] NOUNS = {
            "Avengers", "ByteBenders", "CyberPunks", "DataDragons", "Ethereal",
            "FusionForce", "GalacticGurus", "HackMasters", "Infinity", "Javaholics",
            "Kryptonite", "LaserLords", "MegaMinds", "Nebula", "Optimus",
            "PixelPirates", "Quasar", "Robotics", "Skywalkers", "Technocrats",
            "Unicorn", "Vector", "WebWarriors", "Xenophobes", "Yottabytes"
    };

    public static String generateTeamName() {
        Random random = new Random();
        String adjective = ADJECTIVES[random.nextInt(ADJECTIVES.length)];
        String noun = NOUNS[random.nextInt(NOUNS.length)];

        return adjective + " " + noun;
    }

    public static List<String> generateMultipleTeamNames(int numberOfNames) {
        List<String> teamNames = new ArrayList<>();
        for (int i = 0; i < numberOfNames; i++) {
            teamNames.add(generateTeamName());
        }
        return teamNames;
    }


}
