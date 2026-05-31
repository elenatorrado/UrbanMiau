<?php
include("conexion.php");

$metodo = $_SERVER["REQUEST_METHOD"];

if ($metodo == "GET") {
    $sql = "SELECT 
                c.idColonia,
                c.latitud_colonia AS nombre,
                IFNULL(ci.nombre_ciudad, '') AS ciudad
            FROM colonias c
            LEFT JOIN ciudades ci ON c.idCiudadFK = ci.idCiudad
            ORDER BY c.idColonia DESC";

    $resultado = mysqli_query($conexion, $sql);

    $colonias = [];

    while ($fila = mysqli_fetch_assoc($resultado)) {
        $colonias[] = [
            "idColonia" => $fila["idColonia"],
            "nombre" => $fila["nombre"] ?: "Colonia " . $fila["idColonia"],
            "cuidador" => "Sin asignar",
            "ciudad" => $fila["ciudad"] ?: "Sin ciudad",
            "clinica" => "Sin clínica"
        ];
    }

    echo json_encode($colonias);
}

if ($metodo == "POST") {
    $nombre = $_POST["nombre"] ?? "";
    $ciudad = $_POST["ciudad"] ?? "";

    if ($nombre == "" || $ciudad == "") {
        echo json_encode(["success" => false, "mensaje" => "Faltan datos"]);
        exit();
    }

    $sqlCiudad = "INSERT INTO ciudades (nombre_ciudad) VALUES (?)";
    $stmtCiudad = $conexion->prepare($sqlCiudad);
    $stmtCiudad->bind_param("s", $ciudad);
    $stmtCiudad->execute();

    $idCiudad = $conexion->insert_id;

    $longitud = "";

    $sql = "INSERT INTO colonias (latitud_colonia, longitud_colonia, idCiudadFK)
            VALUES (?, ?, ?)";

    $stmt = $conexion->prepare($sql);
    $stmt->bind_param("ssi", $nombre, $longitud, $idCiudad);

    if ($stmt->execute()) {
        echo json_encode(["success" => true, "mensaje" => "Colonia creada"]);
    } else {
        echo json_encode(["success" => false, "mensaje" => "Error al crear colonia"]);
    }
}

if ($metodo == "PUT") {
    $datos = getPutData();

    $idColonia = $datos["idColonia"] ?? 0;
    $nombre = $datos["nombre"] ?? "";
    $ciudad = $datos["ciudad"] ?? "";

    if ($idColonia == 0 || $nombre == "" || $ciudad == "") {
        echo json_encode(["success" => false, "mensaje" => "Faltan datos"]);
        exit();
    }

    $sqlCiudad = "INSERT INTO ciudades (nombre_ciudad) VALUES (?)";
    $stmtCiudad = $conexion->prepare($sqlCiudad);
    $stmtCiudad->bind_param("s", $ciudad);
    $stmtCiudad->execute();

    $idCiudad = $conexion->insert_id;

    $sql = "UPDATE colonias 
            SET latitud_colonia = ?, idCiudadFK = ?
            WHERE idColonia = ?";

    $stmt = $conexion->prepare($sql);
    $stmt->bind_param("sii", $nombre, $idCiudad, $idColonia);

    if ($stmt->execute()) {
        echo json_encode(["success" => true, "mensaje" => "Colonia actualizada"]);
    } else {
        echo json_encode(["success" => false, "mensaje" => "Error al actualizar colonia"]);
    }
}

if ($metodo == "DELETE") {
    $idColonia = $_GET["idColonia"] ?? 0;

    $sql = "DELETE FROM colonias WHERE idColonia = ?";
    $stmt = $conexion->prepare($sql);
    $stmt->bind_param("i", $idColonia);

    if ($stmt->execute()) {
        echo json_encode(["success" => true, "mensaje" => "Colonia eliminada"]);
    } else {
        echo json_encode(["success" => false, "mensaje" => "Error al eliminar colonia"]);
    }
}
?>