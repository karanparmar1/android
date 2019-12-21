<?php
require_once 'connection.php';
//$data=json_decode(file_get_contents('php://input'),true);
$data=json_decode($_POST["data"]);
$id=isset($data->id)?($data->id):"-404"; //ProfileID of otherUser
$receiverid=isset($data->receiverid)?($data->receiverid):"-404"; //Id of current Loggedin User

$response = array();
$user=array();
$columnsWithoutPassword=" id,firstname,lastname,email,bday,gender,religion,caste,location,education,profession,mothertongue,height,eating,manglik,lookingfor,about,image,online ";
$query="select ".$columnsWithoutPassword." from user where id=?";
$stmt = $conn->prepare($query);
$stmt->bind_param("s",$id);
$stmt->execute();
$stmt->store_result();

//printf("\nQuery:".str_replace('?', '%s', $query)."\n", $id);
if($stmt->num_rows>0) 
{
    $stmt->bind_result($id,$firstname,$lastname,$email,$bday,$gender,$religion,$caste,$location,$education,$profession,$mothertongue,$height,$eating,$manglik,$lookingfor,$about,$image,$online);
    $stmt->fetch();
    
    $user= array(
        'id' => $id,
        'firstname' => $firstname, 
        'lastname' => $lastname, 
        'email' => $email, 
        'bday' => $bday, 
        'gender' => $gender, 
        'religion' => $religion, 
        'caste' => $caste, 
        'location' => $location, 
        'education' => $education, 
        'profession' => $profession, 
        'mothertongue' => $mothertongue, 
        'height' => $height, 
        'eating' => $eating, 
        'manglik' => $manglik, 
        'lookingfor' => $lookingfor, 
        'about' => $about, 
        'image' => $image,
        'online' => $online
    );
    $response["error"]=false;
    $response["message"]=" ".$stmt->num_rows." Records Found.";
    $response["user"]=$user;
    $stmt->close(); 

    //$friendQuery = $conn->prepare("select status from friends where senderid=? AND receiverid=?");
    //$friendQuery->bind_param("ss",$id,$receiverid);
    $friendQuery = $conn->prepare("select status,senderid from friends where senderid IN(?,?) AND receiverid IN(?,?)");
    $friendQuery->bind_param("ssss",$id,$receiverid,$id,$receiverid);
    $friendQuery->execute();
    $friendQuery->store_result();
    
    if($friendQuery->num_rows>0) 
    {
        $friendQuery->bind_result($status,$whoSent);
        $friendQuery->fetch();
        $response["status"]= $status;
        $response["whoSent"]=$whoSent;
    }
    else{
        $response["status"]= "none";  //not in FriendsTable, bcz Req (Not sent Or Rejected)
        $response["whoSent"]="-404";
    }
} 
else{
    $response["error"]=true;
    $response["message"]=" No Matching Record Found !";
}

//return ".$columnsWithoutPassword." from user where id=".$id;
echo json_encode($response);
?>