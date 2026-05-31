<?php
include("conexion.php");

$metodo = $_SERVER["REQUEST_METHOD"];

if ($metodo == "GET") {
    $idColonia = $_GET["idColonia"] ?? "";

    $sql = "SELECT 
                g.idGato,
                g.nombre_gato,
                g.edad_gato,
                g.sexo_gato,
                g.idColoniaFK,
                IFNULL(GROUP_CONCAT(t.descripcion_tratamiento SEPARATOR ', '), 'Sin tratamiento') AS tratamiento
            FROM gatos g
            LEFT JOIN gato_tratamiento gt ON g.idGato = gt.idGatoFK
            LEFT JOIN tratamientos t ON gt.idTratamientoFK = t.idTratamiento";

    if ($idColonia != "") {
        $sql .= " WHERE g.idColoniaFK = " . intval($idColonia);
    }

    $sql .= " GROUP BY g.idGato ORDER BY g.idGato DESC";

    $resultado = mysqli_query($conexion, $sql);

    $gatos = [];

    while ($fila = mysqli_fetch_assoc($resultado)) {
        $gatos[] = [
            "idGato" => $fila["idGato"],
            "nombre" => $fila["nombre_gato"],
            "edad" => strval($fila["edad_gato"]),
            "personalidad" => $fila["sexo_gato"],
            "tratamiento" => $fila["tratamiento"],
            "foto" => "",
            "idColonia" => intval($fila["idColoniaFK"])
        ];
    }

    echo json_encode($gatos);
}

if ($metodo == "POST") {
    $nombre = $_POST["nombre"] ?? "";
    $edad = $_POST["edad"] ?? "";
    $personalidad = $_POST["personalidad"] ?? "Desconocido";
    $tratamiento = $_POST["tratamiento"] ?? "";
    $idColonia = $_POST["idColonia"] ?? 0;

    if ($nombre == "" || $edad == "" || $idColonia == 0) {
        echo json_encode(["success" => false, "mensaje" => "Faltan datos"]);
        exit();
    }

    $sexo = "Desconocido";
    $chip = NULL;
    $idCuidadora = NULL;
    $idClinica = NULL;

    $sql = "INSERT INTO gatos 
            (nombre_gato, num_chip_gato, sexo_gato, edad_gato, idColoniaFK, idCuidadoraFK, idClinicaFK)
            VALUES (?, ?, ?, ?, ?, ?, ?)";

    $stmt = $conexion->prepare($sql);
    $stmt->bind_param("sssiiii", $nombre, $chip, $sexo, $edad, $idColonia, $idCuidadora, $idClinica);

    if ($stmt->execute()) {
        $idGato = $conexion->insert_id;

        if ($tratamiento != "") {
            $sqlTrat = "INSERT INTO tratamientos (descripcion_tratamiento) VALUES (?)";
            $stmtTrat = $conexion->prepare($sqlTrat);
            $stmtTrat->bind_param("s", $tratamiento);
            $stmtTrat->execute();

            $idTratamiento = $conexion->insert_id;

            $sqlRel = "INSERT INTO gato_tratamiento (idGatoFK, idTratamientoFK)
                       VALUES (?, ?)";
            $stmtRel = $conexion->prepare($sqlRel);
            $stmtRel->bind_param("ii", $idGato, $idTratamiento);
            $stmtRel->execute();
        }

        echo json_encode(["success" => true, "mensaje" => "Gato creado"]);
    } else {
        echo json_encode(["success" => false, "mensaje" => "Error al crear gato"]);
    }
}

if ($metodo == "PUT") {
    $datos = getPutData();

    $idGato = $datos["idGato"] ?? 0;
    $nombre = $datos["nombre"] ?? "";
    $edad = $datos["edad"] ?? "";
    $idColonia = $datos["idColonia"] ?? 0;

    if ($idGato == 0 || $nombre == "" || $edad == "" || $idColonia == 0) {
        echo json_encode(["success" => false, "mensaje" => "Faltan datos"]);
        exit();
    }

    $sql = "UPDATE gatos 
            SET nombre_gato = ?, edad_gato = ?, idColoniaFK = ?
            WHERE idGato = ?";

    $stmt = $conexion->prepare($sql);
    $stmt->bind_param("siii", $nombre, $edad, $idColonia, $idGato);

    if ($stmt->execute()) {
        echo json_encode(["success" => true, "mensaje" => "Gato actualizado"]);
    } else {
        echo json_encode(["success" => false, "mensaje" => "Error al actualizar gato"]);
    }
}

if ($metodo == "DELETE") {
    $idGato = $_GET["idGato"] ?? 0;

    $sql = "DELETE FROM gatos WHERE idGato = ?";
    $stmt = $conexion->prepare($sql);
    $stmt->bind_param("i", $idGato);

    if ($stmt->execute()) {
        echo json_encode(["success" => true, "mensaje" => "Gato eliminado"]);
    } else {
        echo json_encode(["success" => false, "mensaje" => "Error al eliminar gato"]);
    }
}
?>