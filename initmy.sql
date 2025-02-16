CREATE DATABASE projWatch;

USE projWatch;

CREATE TABLE IF NOT EXISTS Watch (
    id VARCHAR(32) PRIMARY KEY,
    HR FLOAT,
    Temp INT,
    wDate VARCHAR(21) NOT NULL
);

plus léger que les machines virtuelles
premet de faire des imgs