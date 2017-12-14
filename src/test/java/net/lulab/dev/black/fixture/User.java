package net.lulab.dev.black.fixture;

import java.time.ZonedDateTime;
import java.util.UUID;

public class User {
    private final UUID id;
    private final String name;
    private final int age;
    private final ZonedDateTime registeredAt;

    public User(UUID id, String name, int age, ZonedDateTime registeredAt) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.registeredAt = registeredAt;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public ZonedDateTime getRegisteredAt() {
        return registeredAt;
    }
}
