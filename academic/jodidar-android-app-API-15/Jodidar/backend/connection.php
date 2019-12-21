<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname= "jodidar";

// Create connection
$conn = new mysqli($servername, $username, $password,$dbname);

// Check connection
if ($conn->connect_error) {
    die("DB Connection failed: " . $conn->connect_error);
} 

function isNotNull($data,$params)
{
    foreach($params as $param){
        if(!isset($data->$param)){
            return false;  //if the paramter is not available
        }
    }
    return true; 
}

function userEmailExist($conn,$email)
{
    //$stmt = mysqli_query($conn,"select id from user where email='$email'");
    $stmt = $conn->prepare("select id from user where email=?");
    $stmt->bind_param("s",$email);
    $stmt->execute();
    $stmt->store_result();
    
    return ($stmt->num_rows>0) ? $stmt->close() : false;
}
?>