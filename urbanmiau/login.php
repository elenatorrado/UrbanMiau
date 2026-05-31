<?php
include("conexion.php");

$nombreUsuario = $_POST["nombreUsuario"] ?? "";
$password = $_POST["password"] ?? "";

$sql = "SELECT idUsuario, nombreUsuario, tipo_usuario 
        FROM usuarios 
        WHERE nombreUsuario = ? AND password_usuario = ?";

$stmt = $conexion->prepare($sql);
$stmt->bind_param("ss", $nombreUsuario, $password);
$stmt->execute();

$resultado = $stmt->get_result();

if ($resultado->num_rows > 0) {
    $usuario = $resultado->fetch_assoc();

    echo json_encode([
        "success" => true,
        "idUsuario" => $usuario["idUsuario"],
        "nombreUsuario" => $usuario["nombreUsuario"],
        "tipo_usuario" => $usuario["tipo_usuario"]
    ]);
} else {
    echo json_encode([
        "success" => false,
        "mensaje" => "Usuario o contraseña incorrectos"
    ]);
}
?>