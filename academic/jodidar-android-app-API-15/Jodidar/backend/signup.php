<?php
require_once "connection.php";

$data=json_decode($_POST["data"]); //json_decode(file_get_contents('php://input'),true); //
$response=array();
    if(isNotNull($data,array('firstname','lastname','email','password','bday','gender','image')))
    {
        $firstname = $data->firstname;
        $lastname = $data->lastname;
        $email = $data->email;
        $password = md5($data->password);
        $bday = date($data->bday);
        $gender = $data->gender;
        $image = $data->image;

        if(userEmailExist($conn,$email))
        {
            $response['error'] = true;
            $response['message'] = ' User already registered !';
        }
        else
        {
            $stmt = $conn->prepare("insert into user(firstname,lastname,email,password,gender,image,bday) values(?,?,?,?,?,?,?)");
            $stmt->bind_param("sssssss",$firstname,$lastname,$email, $password,$gender,$image,$bday);
           
            if ($stmt->execute())
            {
                $response['error'] = false; 
                $response['message'] = ' User registered successfully. '; 
            }
            else{
                $response['error'] = true; 
                $response['message'] = ' User could not be registerd due to DB problem '; 
            }
        }
    }
    else{
        $response['error'] = true; 
        $response['message'] = 'Some Fields are Empty'; 
    }

    echo json_encode($response);
?>