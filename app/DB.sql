CREATE DATABASE IF NOT EXISTS coloniaApp CHARSET utf8mb4 COLLATE utf8mb4_spanish2_ci;
USE coloniaApp;

-- 1. Ciudades
CREATE TABLE ciudades(
    idCiudad INT AUTO_INCREMENT,
    nombre_ciudad VARCHAR(100) NOT NULL,
    PRIMARY KEY (idCiudad)
);

-- 2. Colonias 
CREATE TABLE colonias(
    idColonia INT AUTO_INCREMENT,
    latitud_colonia VARCHAR(200), 
    longitud_colonia VARCHAR(200), 
    idCiudadFK INT,
    PRIMARY KEY (idColonia), 
    FOREIGN KEY (idCiudadFK) REFERENCES ciudades(idCiudad)
);

-- 3. Cuidadoras
CREATE TABLE cuidadoras(
    idCuidadora INT AUTO_INCREMENT,
    nombre_cuidadora VARCHAR(100) NOT NULL, 
    apellidos_cuidadora VARCHAR(200),
    telefono_cuidadora VARCHAR(20),
    idCiudadFK INT, 
    PRIMARY KEY (idCuidadora),
    FOREIGN KEY (idCiudadFK) REFERENCES ciudades(idCiudad)
);


CREATE TABLE cuidadoras_colonia(
    idCuidadora_colonia INT AUTO_INCREMENT,
    idCuidadoraFK INT, 
    idColoniaFK INT,
    PRIMARY KEY (idCuidadora_colonia), 
    FOREIGN KEY (idCuidadoraFK) REFERENCES cuidadoras(idCuidadora), 
    FOREIGN KEY (idColoniaFK) REFERENCES colonias(idColonia) ON DELETE CASCADE
);

-- 5. Clínicas
CREATE TABLE clinicas(
    idClinica INT AUTO_INCREMENT, 
    nombre_clinica VARCHAR(200) NOT NULL, 
    direccion_clinica VARCHAR(200), 
    telefono_clinica VARCHAR(20),
    idCiudadFK INT,
    PRIMARY KEY (idClinica), 
    FOREIGN KEY (idCiudadFK) REFERENCES ciudades(idCiudad)
);

-- 6. Tratamientos
CREATE TABLE tratamientos(
    idTratamiento INT AUTO_INCREMENT, 
    descripcion_tratamiento VARCHAR(3000),
    PRIMARY KEY (idTratamiento)
);

-- 7. Usuarios
CREATE TABLE usuarios(
    idUsuario INT AUTO_INCREMENT, 
    nombreUsuario VARCHAR(300) UNIQUE NOT NULL, 
    password_usuario VARCHAR(255), -- espacio para hashes como Argon2 o Bcrypt
    tipo_usuario ENUM('admin', 'cuidador', 'voluntario'),
    PRIMARY KEY (idUsuario)
);

-- 8. Gatos
CREATE TABLE gatos(
    idGato INT AUTO_INCREMENT, 
    nombre_gato VARCHAR(100), 
    num_chip_gato VARCHAR(30) UNIQUE,
    fotoPpal_gato LONGBLOB, -- Considera cambiar a VARCHAR para URLs en el futuro
    sexo_gato ENUM('Macho', 'Hembra', 'Desconocido') DEFAULT 'Desconocido',
    edad_gato INT,
    idColoniaFK INT, 
    idCuidadoraFK INT, 
    idClinicaFK INT,
    
    PRIMARY KEY (idGato),
    FOREIGN KEY (idColoniaFK) REFERENCES colonias(idColonia),
    FOREIGN KEY (idCuidadoraFK) REFERENCES cuidadoras(idCuidadora),
    FOREIGN KEY (idClinicaFK) REFERENCES clinicas(idClinica)
    
    
);

CREATE TABLE gato_tratamiento(
	idGato_tratamiento int auto_increment,
    idGatoFK int,
    idTratamientoFK int ,
    primary key(idGato_tratamiento),
    FOREIGN KEY (idTratamientoFK) REFERENCES tratamientos(idTratamiento),
    FOREIGN KEY (idGatoFK) REFERENCES gatos (idGato)
);

-- 9. Galería de imágenes adicional
CREATE TABLE imagenes(
    idImagen INT AUTO_INCREMENT,
    idGatoFK INT,
    imagen LONGBLOB,
    PRIMARY KEY (idImagen),
    FOREIGN KEY (idGatoFK) REFERENCES gatos(idGato) ON DELETE CASCADE
);
