<?php
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE");
header("Access-Control-Allow-Headers: Content-Type");

$conexion = mysqli_connect("localhost", "root", "", "coloniaApp");

if (!$conexion) {
    echo json_encode([
        "success" => false,
        "mensaje" => "Error de conexión: " . mysqli_connect_error()
    ]);
    exit();
}

mysqli_set_charset($conexion, "utf8mb4");

function getPutData() {
    parse_str(file_get_contents("php://input"), $datos);
    return $datos;
}
?>