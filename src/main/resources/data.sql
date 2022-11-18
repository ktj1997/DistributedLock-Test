INSERT INTO pessimistic(id, counter)
VALUES (1, 100);

INSERT INTO optimistic(id, counter, version)
VALUES (1, 100, 0);


INSERT INTO named(id, counter)
VALUES (1, 100);

INSERT INTO spin(id, counter)
VALUES (1, 100);

INSERT INTO pubsub(id, counter)
VALUES (1, 100);