package com.project.paradoxplatformer.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import com.project.paradoxplatformer.view.javafx.PageIdentifier;

public class EventManager {

    private static EventManager instance;
    private final Map<String, BiConsumer<PageIdentifier, String>> eventMap = new HashMap<>();

    // Singleton pattern to ensure only one instance
    public static synchronized EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }

    // Method to subscribe to events with two parameters
    public void subscribe(String eventName, BiConsumer<PageIdentifier, String> action) {
        eventMap.put(eventName, action);
    }

    // Method to publish events with two parameters
    public void publish(String eventName, PageIdentifier pageIdentifier, String param) {
        // if (eventMap.containsKey(eventName)) {
        //     eventMap.get(eventName).accept(pageIdentifier, param);
        // }
        Optional.of(eventName)
            .filter(eventMap::containsKey)
            .map(eventMap::get)
            .ifPresentOrElse(
                f -> f.accept(pageIdentifier, param),
                () -> System.out.println("Could not find event "+eventName+", event not published")
            );
    }
}
