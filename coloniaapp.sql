-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 07-06-2026 a las 00:11:29
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `coloniaapp`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ciudades`
--

CREATE TABLE `ciudades` (
  `idCiudad` int(11) NOT NULL,
  `nombre_ciudad` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

--
-- Volcado de datos para la tabla `ciudades`
--

INSERT INTO `ciudades` (`idCiudad`, `nombre_ciudad`) VALUES
(1, 'Madrid'),
(2, 'Sevilla'),
(3, 'Valencia');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clinicas`
--

CREATE TABLE `clinicas` (
  `idClinica` int(11) NOT NULL,
  `nombre_clinica` varchar(200) DEFAULT NULL,
  `direccion_clinica` varchar(200) DEFAULT NULL,
  `telefono_clinica` varchar(20) DEFAULT NULL,
  `idCiudadFK` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

--
-- Volcado de datos para la tabla `clinicas`
--

INSERT INTO `clinicas` (`idClinica`, `nombre_clinica`, `direccion_clinica`, `telefono_clinica`, `idCiudadFK`) VALUES
(1, 'Clínica Madrid Felina', 'Calle Sol 12', '911111111', 1),
(2, 'Clínica Sevilla Cats', 'Av. Andalucía 45', '922222222', 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `colonias`
--

CREATE TABLE `colonias` (
  `idColonia` int(11) NOT NULL,
  `nombreColonia` varchar(300) DEFAULT NULL,
  `latitud_colonia` varchar(200) DEFAULT NULL,
  `longitud_colonia` varchar(200) DEFAULT NULL,
  `idCiudadFK` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

--
-- Volcado de datos para la tabla `colonias`
--

INSERT INTO `colonias` (`idColonia`, `nombreColonia`, `latitud_colonia`, `longitud_colonia`, `idCiudadFK`) VALUES
(1, 'Blanca', '17485415615641', '154465465456464', 1),
(2, 'Esmeralda', '12341654653416', '12312316341564', 2),
(3, 'Esperanza', '465465416534163541', '215634156456', 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cuidadoras`
--

CREATE TABLE `cuidadoras` (
  `idCuidadora` int(11) NOT NULL,
  `nombre_cuidadora` varchar(100) DEFAULT NULL,
  `apellidos_cuidadora` varchar(200) DEFAULT NULL,
  `telefono_cuidadora` varchar(20) DEFAULT NULL,
  `idCiudadFK` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

--
-- Volcado de datos para la tabla `cuidadoras`
--

INSERT INTO `cuidadoras` (`idCuidadora`, `nombre_cuidadora`, `apellidos_cuidadora`, `telefono_cuidadora`, `idCiudadFK`) VALUES
(1, 'Ana', 'Pérez López', '600111222', 1),
(2, 'Laura', 'Gómez Ruiz', '600333444', 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `gatos`
--

CREATE TABLE `gatos` (
  `idGato` int(11) NOT NULL,
  `nombre_gato` varchar(100) DEFAULT NULL,
  `num_chip_gato` varchar(30) DEFAULT NULL,
  `foto_gato` varchar(500) DEFAULT NULL,
  `sexo_gato` enum('Macho','Hembra','Desconocido') DEFAULT 'Desconocido',
  `edad_gato` int(11) DEFAULT NULL,
  `idColoniaFK` int(11) DEFAULT NULL,
  `idCuidadoraFK` int(11) DEFAULT NULL,
  `idClinicaFK` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

--
-- Volcado de datos para la tabla `gatos`
--

INSERT INTO `gatos` (`idGato`, `nombre_gato`, `num_chip_gato`, `foto_gato`, `sexo_gato`, `edad_gato`, `idColoniaFK`, `idCuidadoraFK`, `idClinicaFK`) VALUES
(16, 'Mona', NULL, 'uploads/1780782170_gato_editado.jpg', 'Macho', 8, 1, 1, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `gato_tratamiento`
--

CREATE TABLE `gato_tratamiento` (
  `id` int(11) NOT NULL,
  `idGatoFK` int(11) DEFAULT NULL,
  `idTratamientoFK` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tratamientos`
--

CREATE TABLE `tratamientos` (
  `idTratamiento` int(11) NOT NULL,
  `descripcion_tratamiento` varchar(3000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

--
-- Volcado de datos para la tabla `tratamientos`
--

INSERT INTO `tratamientos` (`idTratamiento`, `descripcion_tratamiento`) VALUES
(1, 'Vacunación completa'),
(2, 'Desparasitación interna'),
(3, 'Antibiótico para infección leve');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `ciudades`
--
ALTER TABLE `ciudades`
  ADD PRIMARY KEY (`idCiudad`);

--
-- Indices de la tabla `clinicas`
--
ALTER TABLE `clinicas`
  ADD PRIMARY KEY (`idClinica`),
  ADD KEY `idCiudadFK` (`idCiudadFK`);

--
-- Indices de la tabla `colonias`
--
ALTER TABLE `colonias`
  ADD PRIMARY KEY (`idColonia`),
  ADD KEY `idCiudadFK` (`idCiudadFK`);

--
-- Indices de la tabla `cuidadoras`
--
ALTER TABLE `cuidadoras`
  ADD PRIMARY KEY (`idCuidadora`),
  ADD KEY `idCiudadFK` (`idCiudadFK`);

--
-- Indices de la tabla `gatos`
--
ALTER TABLE `gatos`
  ADD PRIMARY KEY (`idGato`),
  ADD UNIQUE KEY `num_chip_gato` (`num_chip_gato`),
  ADD KEY `idColoniaFK` (`idColoniaFK`),
  ADD KEY `idCuidadoraFK` (`idCuidadoraFK`),
  ADD KEY `idClinicaFK` (`idClinicaFK`);

--
-- Indices de la tabla `gato_tratamiento`
--
ALTER TABLE `gato_tratamiento`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idGatoFK` (`idGatoFK`),
  ADD KEY `idTratamientoFK` (`idTratamientoFK`);

--
-- Indices de la tabla `tratamientos`
--
ALTER TABLE `tratamientos`
  ADD PRIMARY KEY (`idTratamiento`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `ciudades`
--
ALTER TABLE `ciudades`
  MODIFY `idCiudad` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `clinicas`
--
ALTER TABLE `clinicas`
  MODIFY `idClinica` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `colonias`
--
ALTER TABLE `colonias`
  MODIFY `idColonia` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `cuidadoras`
--
ALTER TABLE `cuidadoras`
  MODIFY `idCuidadora` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `gatos`
--
ALTER TABLE `gatos`
  MODIFY `idGato` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT de la tabla `gato_tratamiento`
--
ALTER TABLE `gato_tratamiento`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `tratamientos`
--
ALTER TABLE `tratamientos`
  MODIFY `idTratamiento` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `clinicas`
--
ALTER TABLE `clinicas`
  ADD CONSTRAINT `clinicas_ibfk_1` FOREIGN KEY (`idCiudadFK`) REFERENCES `ciudades` (`idCiudad`);

--
-- Filtros para la tabla `colonias`
--
ALTER TABLE `colonias`
  ADD CONSTRAINT `colonias_ibfk_1` FOREIGN KEY (`idCiudadFK`) REFERENCES `ciudades` (`idCiudad`);

--
-- Filtros para la tabla `cuidadoras`
--
ALTER TABLE `cuidadoras`
  ADD CONSTRAINT `cuidadoras_ibfk_1` FOREIGN KEY (`idCiudadFK`) REFERENCES `ciudades` (`idCiudad`);

--
-- Filtros para la tabla `gatos`
--
ALTER TABLE `gatos`
  ADD CONSTRAINT `gatos_ibfk_1` FOREIGN KEY (`idColoniaFK`) REFERENCES `colonias` (`idColonia`),
  ADD CONSTRAINT `gatos_ibfk_2` FOREIGN KEY (`idCuidadoraFK`) REFERENCES `cuidadoras` (`idCuidadora`),
  ADD CONSTRAINT `gatos_ibfk_3` FOREIGN KEY (`idClinicaFK`) REFERENCES `clinicas` (`idClinica`);

--
-- Filtros para la tabla `gato_tratamiento`
--
ALTER TABLE `gato_tratamiento`
  ADD CONSTRAINT `gato_tratamiento_ibfk_1` FOREIGN KEY (`idGatoFK`) REFERENCES `gatos` (`idGato`) ON DELETE CASCADE,
  ADD CONSTRAINT `gato_tratamiento_ibfk_2` FOREIGN KEY (`idTratamientoFK`) REFERENCES `tratamientos` (`idTratamiento`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
