<?php
include("conexion.php");

$metodo = $_SERVER["REQUEST_METHOD"];

if ($metodo == "GET") {
    $sql = "SELECT 
                c.idCuidadora,
                c.nombre_cuidadora,
                c.telefono_cuidadora,
                IFNULL(ci.nombre_ciudad, '') AS zona
            FROM cuidadoras c
            LEFT JOIN ciudades ci ON c.idCiudadFK = ci.idCiudad
            ORDER BY c.idCuidadora DESC";

    $resultado = mysqli_query($conexion, $sql);

    $cuidadores = [];

    while ($fila = mysqli_fetch_assoc($resultado)) {
        $cuidadores[] = [
            "idCuidador" => $fila["idCuidadora"],
            "nombre" => $fila["nombre_cuidadora"],
            "telefono" => $fila["telefono_cuidadora"],
            "email" => "",
            "zona" => $fila["zona"] ?: "Sin zona"
        ];
    }

    echo json_encode($cuidadores);
}

if ($metodo == "POST") {
    $nombre = $_POST["nombre"] ?? "";
    $telefono = $_POST["telefono"] ?? "";
    $zona = $_POST["zona"] ?? "";

    if ($nombre == "" || $telefono == "" || $zona == "") {
        echo json_encode(["success" => false, "mensaje" => "Faltan datos"]);
        exit();
    }

    $sqlCiudad = "INSERT INTO ciudades (nombre_ciudad) VALUES (?)";
    $stmtCiudad = $conexion->prepare($sqlCiudad);
    $stmtCiudad->bind_param("s", $zona);
    $stmtCiudad->execute();

    $idCiudad = $conexion->insert_id;
    $apellidos = "";

    $sql = "INSERT INTO cuidadoras 
            (nombre_cuidadora, apellidos_cuidadora, telefono_cuidadora, idCiudadFK)
            VALUES (?, ?, ?, ?)";

    $stmt = $conexion->prepare($sql);
    $stmt->bind_param("sssi", $nombre, $apellidos, $telefono, $idCiudad);

    if ($stmt->execute()) {
        echo json_encode(["success" => true, "mensaje" => "Cuidador creado"]);
    } else {
        echo json_encode(["success" => false, "mensaje" => "Error al crear cuidador"]);
    }
}

if ($metodo == "PUT") {
    $datos = getPutData();

    $idCuidador = $datos["idCuidador"] ?? 0;
    $nombre = $datos["nombre"] ?? "";
    $telefono = $datos["telefono"] ?? "";
    $zona = $datos["zona"] ?? "";

    if ($idCuidador == 0 || $nombre == "" || $telefono == "" || $zona == "") {
        echo json_encode(["success" => false, "mensaje" => "Faltan datos"]);
        exit();
    }

    $sqlCiudad = "INSERT INTO ciudades (nombre_ciudad) VALUES (?)";
    $stmtCiudad = $conexion->prepare($sqlCiudad);
    $stmtCiudad->bind_param("s", $zona);
    $stmtCiudad->execute();

    $idCiudad = $conexion->insert_id;

    $sql = "UPDATE cuidadoras 
            SET nombre_cuidadora = ?, telefono_cuidadora = ?, idCiudadFK = ?
            WHERE idCuidadora = ?";

    $stmt = $conexion->prepare($sql);
    $stmt->bind_param("ssii", $nombre, $telefono, $idCiudad, $idCuidador);

    if ($stmt->execute()) {
        echo json_encode(["success" => true, "mensaje" => "Cuidador actualizado"]);
    } else {
        echo json_encode(["success" => false, "mensaje" => "Error al actualizar cuidador"]);
    }
}

if ($metodo == "DELETE") {
    $idCuidador = $_GET["idCuidador"] ?? 0;

    $sql = "DELETE FROM cuidadoras WHERE idCuidadora = ?";
    $stmt = $conexion->prepare($sql);
    $stmt->bind_param("i", $idCuidador);

    if ($stmt->execute()) {
        echo json_encode(["success" => true, "mensaje" => "Cuidador eliminado"]);
    } else {
        echo json_encode(["success" => false, "mensaje" => "Error al eliminar cuidador"]);
    }
}
?>