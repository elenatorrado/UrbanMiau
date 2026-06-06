<?php
include("conexion.php");

$metodo = $_SERVER["REQUEST_METHOD"];

if ($metodo == "GET") {
    // Modificamos el SQL para cruzar las tablas por la clave foránea
	$sql = "SELECT c.*, ci.nombre_ciudad 
        FROM clinicas c 
        INNER JOIN ciudades ci ON c.idCiudadFK = ci.idCiudad 
        ORDER BY c.idClinica DESC";

	$resultado = mysqli_query($conexion, $sql);

	$clinicas = [];

	while ($fila = mysqli_fetch_assoc($resultado)) {
    $clinicas[] = [
        "idClinica" => $fila["idClinica"],
        "nombre" => $fila["nombre_clinica"],
        "direccion" => $fila["direccion_clinica"],
        "telefono" => $fila["telefono_clinica"],
        "ciudad" => $fila["nombre_ciudad"] // 🔴 Nos traemos el nombre de la ciudad mapeado
    ];
}

    echo json_encode($clinicas);
}

if ($metodo == "POST") {
    $nombre = $_POST["nombre_clinica"] ?? "";
    $direccion = $_POST["direccion_clinica"] ?? "";
    $telefono = $_POST["telefono_clinica"] ?? "";
    $nombreCiudad = $_POST["ciudad"] ?? ""; // 🔴 Cambiado: Recibimos el nombre de la ciudad

    // Validación de campos vacíos
    if ($nombre == "" || $direccion == "" || $telefono == "" || $nombreCiudad == "") {
        echo json_encode(["success" => false, "mensaje" => "Faltan datos obligatorios en el formulario"]);
        exit();
    }

    $idCiudadEncontrado = null;
    
    $sqlCiudad = "SELECT idCiudad FROM ciudades WHERE nombre_ciudad = ? LIMIT 1";
    $stmtCiudad = $conexion->prepare($sqlCiudad);
    $stmtCiudad->bind_param("s", $nombreCiudad);
    $stmtCiudad->execute();
    $resultadoCiudad = $stmtCiudad->get_result();

    if ($fila = $resultadoCiudad->fetch_assoc()) {
        $idCiudadEncontrado = (int)$fila["idCiudad"];
    }
    $stmtCiudad->close();

    // Si la ciudad no existe en tu base de datos, abortamos para evitar el fallo
    if ($idCiudadEncontrado === null) {
        echo json_encode(["success" => false, "mensaje" => "La ciudad '" . $nombreCiudad . "' no existe en la base de datos"]);
        exit();
    }

    $sql = "INSERT INTO clinicas 
            (nombre_clinica, direccion_clinica, telefono_clinica, idCiudadFK)
            VALUES (?, ?, ?, ?)";

    $stmt = $conexion->prepare($sql);
    
    // Pasamos el ID encontrado ($idCiudadEncontrado) como entero "i"
    $stmt->bind_param("sssi", $nombre, $direccion, $telefono, $idCiudadEncontrado);

    if ($stmt->execute()) {
        echo json_encode(["success" => true, "mensaje" => "Clínica creada correctamente"]);
    } else {
        echo json_encode(["success" => false, "mensaje" => "Error de MySQL al insertar: " . $stmt->error]);
    }
    
    $stmt->close();
}

if ($metodo == "PUT") {
    $datos = getPutData();

    $idClinica = $datos["idClinica"] ?? 0;
    $nombre = $datos["nombre"] ?? "";
    $direccion = $datos["direccion"] ?? "";
    $telefono = $datos["telefono"] ?? "";
    $ciudad = $datos["ciudad"] ?? ""; // Nombre de la ciudad

    // Validación de datos 
    if ($idClinica == 0 || $nombre == "" || $direccion == "" || $telefono == "") {
        echo json_encode(["success" => false, "mensaje" => "Faltan datos obligatorios"]);
        exit();
    }

    $idCiudadFinal = NULL;

    // CORRECCIÓN DE LOGICA: Manejo seguro de la ciudad
    if ($ciudad != "") {
        // 1. Primero miramos si la ciudad YA EXISTE en la base de datos
        $sqlBuscar = "SELECT idCiudad FROM ciudades WHERE nombre_ciudad = ? LIMIT 1";
        $stmtBuscar = $conexion->prepare($sqlBuscar);
        $stmtBuscar->bind_param("s", $ciudad);
        $stmtBuscar->execute();
        $resultadoBuscar = $stmtBuscar->get_result();

        if ($fila = $resultadoBuscar->fetch_assoc()) {
            // Si ya existe, nos quedamos con su ID existente
            $idCiudadFinal = (int)$fila["idCiudad"];
        } else {
            // Si no existe, solo entonces la insertamos de cero
            $sqlCiudad = "INSERT INTO ciudades (nombre_ciudad) VALUES (?)";
            $stmtCiudad = $conexion->prepare($sqlCiudad);
            $stmtCiudad->bind_param("s", $ciudad);
            $stmtCiudad->execute();
            $idCiudadFinal = $conexion->insert_id;
            $stmtCiudad->close();
        }
        $stmtBuscar->close();
    }

    // CORRECCIÓN 2: Usar 'idCiudadFK' (que descubrimos en el paso anterior que era el nombre real)
    $sql = "UPDATE clinicas 
            SET nombre_clinica = ?, direccion_clinica = ?, telefono_clinica = ?, idCiudadFK = ?
            WHERE idClinica = ?";

    $stmt = $conexion->prepare($sql);
    
    // Vinculamos los tipos correspondientes ("sssii")
    $stmt->bind_param("sssii", $nombre, $direccion, $telefono, $idCiudadFinal, $idClinica);

    if ($stmt->execute()) {
        echo json_encode(["success" => true, "mensaje" => "Clínica actualizada correctamente"]);
    } else {
        echo json_encode(["success" => false, "mensaje" => "Error de MySQL al actualizar: " . $stmt->error]);
    }
    
    $stmt->close();
}
if ($metodo == "DELETE") {
    $idClinica = $_GET["idClinica"] ?? 0;

    $sql = "DELETE FROM clinicas WHERE idClinica = ?";
    $stmt = $conexion->prepare($sql);
    $stmt->bind_param("i", $idClinica);

    if ($stmt->execute()) {
        echo json_encode(["success" => true, "mensaje" => "Clínica eliminada"]);
    } else {
        echo json_encode(["success" => false, "mensaje" => "Error al eliminar clínica"]);
    }
}
?>