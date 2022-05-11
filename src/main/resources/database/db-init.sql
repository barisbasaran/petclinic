CREATE TABLE IF NOT EXISTS vets
(
    id   SERIAL,
    name TEXT NOT NULL
);
ALTER TABLE vets
    ADD UNIQUE (name);

CREATE TABLE IF NOT EXISTS vet_specialties
(
    vet_id    INTEGER,
    specialty TEXT,
    PRIMARY KEY (vet_id, specialty)
);


CREATE TABLE IF NOT EXISTS pets
(
    id      SERIAL,
    name    TEXT    NOT NULL,
    age     INTEGER NOT NULL,
    species TEXT    NOT NULL
);
ALTER TABLE pets
    ADD UNIQUE (name);

CREATE TABLE IF NOT EXISTS visits
(
    id        SERIAL,
    pet_id    INTEGER   NOT NULL,
    vet_id    INTEGER   NOT NULL,
    date      TIMESTAMP NOT NULL,
    treatment TEXT      NOT NULL
);
