<?php
include("conexion.php");

$metodo = $_SERVER["REQUEST_METHOD"];

if ($metodo == "GET") {
    if (isset($_GET["idTratamiento"])) {
        $idTratamiento = $_GET["idTratamiento"];

        $sql = "SELECT 
                    g.idGato,
                    g.nombre_gato AS nombre,
                    g.edad_gato AS edad,
                    g.sexo_gato AS personalidad,
                    g.idColoniaFK AS idColonia
                FROM gatos g
                INNER JOIN gato_tratamiento gt ON g.idGato = gt.idGatoFK
                WHERE gt.idTratamientoFK = ?";

        $stmt = $conexion->prepare($sql);
        $stmt->bind_param("i", $idTratamiento);
        $stmt->execute();

        $resultado = $stmt->get_result();
        $gatos = [];

        while ($fila = $resultado->fetch_assoc()) {
            $gatos[] = [
                "idGato" => $fila["idGato"],
                "nombre" => $fila["nombre"],
                "edad" => strval($fila["edad"]),
                "personalidad" => $fila["personalidad"],
                "tratamiento" => "",
                "foto" => "",
                "idColonia" => intval($fila["idColonia"])
            ];
        }

        echo json_encode($gatos);
    } else {
        $sql = "SELECT * FROM tratamientos ORDER BY idTratamiento DESC";
        $resultado = mysqli_query($conexion, $sql);

        $tratamientos = [];

        while ($fila = mysqli_fetch_assoc($resultado)) {
            $tratamientos[] = [
                "idTratamiento" => $fila["idTratamiento"],
                "descripcion" => $fila["descripcion_tratamiento"]
            ];
        }

        echo json_encode($tratamientos);
    }
}

if ($metodo == "POST") {
    $descripcion = $_POST["descripcion"] ?? "";

    if ($descripcion == "") {
        echo json_encode(["success" => false, "mensaje" => "Faltan datos"]);
        exit();
    }

    $sql = "INSERT INTO tratamientos (descripcion_tratamiento)
            VALUES (?)";

    $stmt = $conexion->prepare($sql);
    $stmt->bind_param("s", $descripcion);

    if ($stmt->execute()) {
        echo json_encode(["success" => true, "mensaje" => "Tratamiento creado"]);
    } else {
        echo json_encode(["success" => false, "mensaje" => "Error al crear tratamiento"]);
    }
}

if ($metodo == "DELETE") {
    $idTratamiento = $_GET["idTratamiento"] ?? 0;

    $sql = "DELETE FROM tratamientos WHERE idTratamiento = ?";
    $stmt = $conexion->prepare($sql);
    $stmt->bind_param("i", $idTratamiento);

    if ($stmt->execute()) {
        echo json_encode(["success" => true, "mensaje" => "Tratamiento eliminado"]);
    } else {
        echo json_encode(["success" => false, "mensaje" => "Error al eliminar tratamiento"]);
    }
}
?>