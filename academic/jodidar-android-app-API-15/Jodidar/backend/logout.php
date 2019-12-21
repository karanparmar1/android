<?php
require_once "connection.php";
$data=json_decode($_POST["data"]);//json_decode(file_get_contents('php://input'),true); //

$response=array();
$user=array();
$email = $data->email;
//$password= md5($data['password']);

if(userEmailExist($conn,$email))
{
    $setOn = $conn->prepare("update user set online=false where email=?"); 
    $setOn->bind_param("s",$email);
    $setOn->execute();  //Executing online Status=False query
    if($setOn->affected_rows === 1)
    {
     
            $response["error"]=false;
            $response["message"]=" Logged out Successfully. ";
            $setOn->close();
    }
    
    else{
        $response["error"]=true;
        $response["message"]=" Already Logged Out ! ";
    }
}
else{
    $response["error"]=true;
    $response["message"]="  No Account Found ! ";// $response["message"]=" Invalid Email Address ! ";
}
echo json_encode($response);
?>