-- ----------------------------------------------------------------------------
-- Model
-------------------------------------------------------------------------------
DROP TABLE Inscripcion;
DROP TABLE Curso;


-------------------------------------Curso-------------------------------------

CREATE TABLE Curso (
    idCurso BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(255) COLLATE latin1_bin NOT NULL,
    ciudad VARCHAR(255) COLLATE latin1_bin NOT NULL,
    plazasDisponibles INT NOT NULL,
    precio FLOAT NOT NULL,
    maxPlazas INT NOT NULL,
    fechaComienzo DATETIME NOT NULL,
    fechaAlta DATETIME NOT NULL,
    CONSTRAINT cursoPK PRIMARY KEY (idCurso)
) ENGINE = InnoDB;

-------------------------------------Inscripcion-------------------------------------

CREATE TABLE Inscripcion (
    idInscripcion BIGINT NOT NULL AUTO_INCREMENT,
    emailUser VARCHAR(255) NOT NULL,
    numTarjeta VARCHAR(255) NOT NULL,
    fechaHoraCreacion DATETIME NOT NULL,
    fechaCancelacion DATETIME,
    idCurso BIGINT NOT NULL,
    CONSTRAINT inscripcionPK PRIMARY KEY (idInscripcion),
    CONSTRAINT inscripcion_curso_FK FOREIGN KEY (idCurso) REFERENCES Curso(idCurso) ON DELETE CASCADE
) ENGINE = InnoDB;