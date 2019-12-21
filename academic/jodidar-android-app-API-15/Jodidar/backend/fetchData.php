<?php
require_once 'connection.php';

$response = array();
$user=array();
$columnsWithoutPassword=" id,firstname,lastname,email,bday,gender,religion,caste,location,education,profession,mothertongue,height,eating,manglik,lookingfor,about,image,online ";

$result=mysqli_query($conn,"select ".$columnsWithoutPassword." from user");

if (mysqli_num_rows($result) > 0) {
    $user=array();
    $response["error"]=false;
    $response["message"]=" ".mysqli_num_rows($result)." Records Found.";
    while($row = mysqli_fetch_assoc($result)) {
        array_push($user,$row);
    }
    $response["user"]=$user;
} else {
    $response["error"]=true;
    $response["message"]="No Records Found";
}

echo json_encode($response);
?>