-- PostgreSQL
CREATE DATABASE MontreConnectee;

CREATE TABLE Watch (
    id CHAR(32) PRIMARY KEY,  -- ID comme chaîne de 32 caractères
    HR DOUBLE PRECISION,
    Temp INT,
    wDate VARCHAR(21) NOT NULL
);


-- end
