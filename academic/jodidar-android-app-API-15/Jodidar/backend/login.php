<?php require_once "connection.php";

$data=json_decode($_POST["data"]);//json_decode(file_get_contents('php://input'),true); //

$email = $data->email;
$password = md5($data->password);

$response=array();
$user=array();

if(userEmailExist($conn,$email))
{
    $setOn = $conn->prepare("update user set online=true where email=? and password=?"); 
    $setOn->bind_param("ss",$email,$password);
    $setOn->execute();  //Executing online Status=true query
    $setOn->close();

    $stmt = $conn->prepare("select * from user where email=? AND password=?");
    $stmt->bind_param("ss",$email,$password);
    $stmt->execute();
    $stmt->store_result();
    if($stmt->num_rows>0)
    {
             
        $stmt->bind_result($id,$firstname,$lastname,$email,$password,$bday,$gender,$religion,$caste,$location,$education,$profession,$mothertongue,$height,$eating,$manglik,$lookingfor,$about,$image,$online);
        $stmt->fetch();
        
        $user= array(
            'id' => $id,
            'firstname' => $firstname, 
            'lastname' => $lastname, 
            'email' => $email, 
            'password' => $password, 
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
            // 'drinking' => $drinking, 
            //'smoking' => $smoking, 
            'manglik' => $manglik, 
            'lookingfor' => $lookingfor, 
            //'physicalcondition' => $physicalcondition, 
            'about' => $about, 
            'image' => $image,
            'online' => $online
        );
       
        //$response["online"]=false;
        $response["error"]=false;
        $response["message"]=" Logged in Successfully. ";
        $response["user"]=$user;
        $stmt->close();
    }
    else{
        $response["error"]=true;
        $response["message"]=" Wrong Password !";
    }
}
else{
    $response["error"]=true;
    $response["message"]=" Invalid Email Address !";
}
echo json_encode($response);
?>