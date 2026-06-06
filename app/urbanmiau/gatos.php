<?php
// Configuración básica
error_reporting(E_ALL);
ini_set('display_errors', 0); // Desactivar en producción
include("conexion.php");

header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST, DELETE");
header("Access-Control-Allow-Headers: Content-Type");
header("Content-Type: application/json; charset=UTF-8");

$baseUrl = "http://10.0.2.2/urbanmiau/"; 
$method = $_SERVER["REQUEST_METHOD"];

// ==========================================
// MÉTODO GET: LISTAR Y SPINNERS
// ==========================================
if ($method == "GET") {
    if (isset($_GET["tipo"])) {
        $tipo = $_GET["tipo"];
        $sql = ($tipo == "COLONIAS") ? "SELECT idColonia AS id, nombreColonia AS nombre FROM colonias" : 
               (($tipo == "CUIDADORAS") ? "SELECT idCuidadora AS id, nombre_cuidadora AS nombre FROM cuidadoras" : 
               "SELECT idClinica AS id, nombre_clinica AS nombre FROM clinicas");
        
        $res = mysqli_query($conexion, $sql);
        $data = [];
        while ($row = mysqli_fetch_assoc($res)) { $data[] = ["id" => intval($row["id"]), "nombre" => $row["nombre"]]; }
        echo json_encode($data);
        exit;
    }

    $sql = "SELECT g.idGato, g.nombre_gato AS nombre, g.edad_gato AS edad, g.sexo_gato AS sexo, 
            g.foto_gato AS foto, c.nombreColonia AS nombreColonia, 
            cu.nombre_cuidadora AS nombreCuidadora, cl.nombre_clinica AS nombreClinica 
            FROM gatos g 
            LEFT JOIN colonias c ON g.idColoniaFK = c.idColonia 
            LEFT JOIN cuidadoras cu ON g.idCuidadoraFK = cu.idCuidadora 
            LEFT JOIN clinicas cl ON g.idClinicaFK = cl.idClinica 
            ORDER BY g.idGato DESC";
    $res = mysqli_query($conexion, $sql);
    $data = [];
    while ($row = mysqli_fetch_assoc($res)) {
        if (!empty($row['foto']) && strpos($row['foto'], 'http') === false) {
            $row['foto'] = $baseUrl . $row['foto'];
        }
        $data[] = $row;
    }
    echo json_encode($data);
    exit;
}

// ==========================================
// MÉTODO POST: INSERTAR O ACTUALIZAR
// ==========================================
if ($method == "POST") {
    $operacion = $_POST['operacion'] ?? 'insert'; 
    $idGato = intval($_POST['idGato'] ?? 0);
    $nombre = trim($_POST['nombre'] ?? '');
    $edad = intval($_POST['edad'] ?? 0);
    $sexo = trim($_POST['sexo'] ?? 'Macho');
    $idColonia = intval($_POST['idColonia'] ?? 0);
    $idCuidadora = intval($_POST['idCuidadora'] ?? 0);
    $idClinica = intval($_POST['idClinica'] ?? 0);
    
    $rutaFoto = null; // Inicializamos como null

    // Manejo de la subida de archivos
    if (isset($_FILES['imagen']) && $_FILES['imagen']['error'] == 0) {
        $dir = "uploads/";
        if (!file_exists($dir)) mkdir($dir, 0777, true);
        $archivo = time() . "_" . preg_replace("/[^a-zA-Z0-9\.]/", "", basename($_FILES["imagen"]["name"]));
        if (move_uploaded_file($_FILES["imagen"]["tmp_name"], $dir . $archivo)) {
            $rutaFoto = $dir . $archivo;
        }
    }

    if ($operacion == 'update' && $idGato > 0) {
        if ($rutaFoto === null) {
            $stmt = $conexion->prepare("UPDATE gatos SET nombre_gato=?, edad_gato=?, sexo_gato=?, idColoniaFK=?, idCuidadoraFK=?, idClinicaFK=? WHERE idGato=?");
            $stmt->bind_param("sisiiii", $nombre, $edad, $sexo, $idColonia, $idCuidadora, $idClinica, $idGato);
        } else {
            $stmt = $conexion->prepare("UPDATE gatos SET nombre_gato=?, edad_gato=?, sexo_gato=?, idColoniaFK=?, idCuidadoraFK=?, idClinicaFK=?, foto_gato=? WHERE idGato=?");
            $stmt->bind_param("sisiiisi", $nombre, $edad, $sexo, $idColonia, $idCuidadora, $idClinica, $rutaFoto, $idGato);
        }
    } else {
        // CORRECCIÓN CLAVE: Si es INSERT y no hay foto, ponemos una por defecto
        $rutaFotoFinal = ($rutaFoto !== null) ? $rutaFoto : "uploads/defecto.jpg";
        $stmt = $conexion->prepare("INSERT INTO gatos (nombre_gato, edad_gato, sexo_gato, idColoniaFK, idCuidadoraFK, idClinicaFK, foto_gato) VALUES (?,?,?,?,?,?,?)");
        $stmt->bind_param("sisiiis", $nombre, $edad, $sexo, $idColonia, $idCuidadora, $idClinica, $rutaFotoFinal);
    }
    
    if ($stmt->execute()) {
        echo json_encode(["status" => "success", "message" => "Operación realizada"]);
    } else {
        echo json_encode(["status" => "error", "message" => $conexion->error]);
    }
    exit;
}

// ==========================================
// MÉTODO DELETE
// ==========================================
if ($method == "DELETE") {
    $idGato = intval($_GET['idGato'] ?? 0);
    $stmt = $conexion->prepare("DELETE FROM gatos WHERE idGato = ?");
    $stmt->bind_param("i", $idGato);
    $stmt->execute();
    echo json_encode(["status" => "success"]);
    exit;
}
?>