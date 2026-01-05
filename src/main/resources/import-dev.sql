-- Sample events for development
INSERT INTO events (id, title, description, location, startDate, endDate, capacity, availableSeats, status, createdAt,
                    updatedAt)
VALUES (1, 'Tech Conference 2026',
        'Annual technology conference featuring the latest innovations in AI and Cloud Computing',
        'San Francisco Convention Center', '2026-03-15 09:00:00', '2026-03-17 18:00:00', 500, 450, 'SCHEDULED',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 'Java User Group Meetup', 'Monthly meetup for Java developers to discuss best practices and new features',
        'Tech Hub Downtown', '2026-02-20 18:00:00', '2026-02-20 21:00:00', 50, 30, 'SCHEDULED', CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       (3, 'Quarkus Workshop', 'Hands-on workshop covering Quarkus fundamentals and reactive programming', 'Online',
        '2026-02-10 14:00:00', '2026-02-10 17:00:00', 100, 75, 'SCHEDULED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (4, 'DevOps Summit', 'Summit focusing on DevOps practices, CI/CD, and cloud-native technologies',
        'Austin Tech Center', '2026-04-05 08:00:00', '2026-04-06 17:00:00', 300, 250, 'SCHEDULED', CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       (5, 'Past Conference Example', 'An example of a completed conference', 'Virtual Event', '2025-12-01 10:00:00',
        '2025-12-01 16:00:00', 200, 0, 'COMPLETED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

ALTER SEQUENCE events_SEQ RESTART WITH 6;
