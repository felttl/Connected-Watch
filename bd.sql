-- PostgreSQL
CREATE DATABASE MontreConnectee;

CREATE TABLE Infos (
    id CHAR(32) PRIMARY KEY,  -- ID comme chaîne de 32 caractères
    FC DOUBLE PRECISION,
    Temp INT
);


-- end