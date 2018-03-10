package com.kmurawska.playground.akka.routing.sentence;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SentenceGenerator {
    private final List<String> art = Collections.unmodifiableList(Arrays.asList(
            "the", "my", "your", "our", "that", "this", "every", "one", "the only", "his", "her"
    ));

    private final List<String> adj = Collections.unmodifiableList(Arrays.asList(
            "happy", "rotating", "red", "fast", "elastic", "smily", "unbelievable", "infinte", "surprising",
            "mysterious", "glowing", "green", "blue", "tired", "hard", "soft", "transparent", "long", "short",
            "excellent", "noisy", "silent", "rare", "normal", "typical", "living", "clean", "glamorous",
            "fancy", "handsome", "lazy", "scary", "helpless", "skinny", "melodic", "silly",
            "kind", "brave", "nice", "ancient", "modern", "young", "sweet", "wet", "cold",
            "dry", "heavy", "industrial", "complex", "accurate", "awesome", "shiny", "cool", "glittering",
            "fake", "unreal", "naked", "intelligent", "smart", "curious", "strange", "unique", "empty",
            "gray", "saturated", "blurry"
    ));

    private final List<String> nou = Collections.unmodifiableList(Arrays.asList(
            "forest", "tree", "flower", "sky", "grass", "mountain", "car", "computer", "man", "woman", "dog",
            "elephant", "ant", "road", "butterfly", "phone", "computer program", "grandma", "school", "bed", "mouse",
            "keyboard", "bicycle", "spaghetti", "drink", "cat", "t-shirt", "carpet", "wall", "poster",
            "airport", "bridge", "road", "river", "beach", "sculpture", "piano", "guitar", "fruit",
            "banana", "apple", "strawberry", "rubber band", "saxophone", "window", "linux computer",
            "skate board", "piece of paper", "photograph", "painting", "hat", "space", "fork",
            "mission", "goal", "project", "tax", "wind mill", "light bulb", "microphone",
            "cpu", "hard drive", "screwdriver"
    ));

    private final List<String> ver = Collections.unmodifiableList(Arrays.asList(
            "sings", "dances", "was dancing", "runs", "will run", "walks",
            "flies", "moves", "moved", "will move", "glows", "glowed", "spins", "promised",
            "hugs", "cheated", "waits", "is waiting", "is studying", "swims",
            "travels", "traveled", "plays", "played", "enjoys", "will enjoy",
            "illuminates", "arises", "eats", "drinks", "calculates", "kissed", "faded", "listens",
            "navigated", "responds", "smiles", "will smile", "will succeed",
            "is wondering", "is thinking", "is", "was", "will be", "might be", "was never"
    ));

    public String generate() {
        return art.get(ThreadLocalRandom.current().nextInt(art.size()))
                + " "
                + nou.get(ThreadLocalRandom.current().nextInt(nou.size()))
                + " "
                + ver.get(ThreadLocalRandom.current().nextInt(ver.size()))
                + " "
                + adj.get(ThreadLocalRandom.current().nextInt(adj.size()));
    }
}