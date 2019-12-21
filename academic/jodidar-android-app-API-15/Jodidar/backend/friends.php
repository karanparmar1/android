<?php require_once "connection.php";

$data=$data=json_decode($_POST["data"]);//json_decode(file_get_contents('php://input'),true); 
$senderid=isset($data->senderid)?($data->senderid):"-404"; //ProfileID of otherUser
$receiverid=isset($data->receiverid)?($data->receiverid):"-404"; //Id of current Loggedin User
$action=isset($data->action)?($data->action):"none"; //ACcepted/Pending
$response = array();
$isValid=false;
$response["error"]=true;
$response["message"]="";

if(strcasecmp($action,"send")==0){
    $stmt = $conn->prepare("insert into friends(senderid,receiverid,status) values(?,?,'pending')");
    $stmt->bind_param("ss",$senderid,$receiverid);
    if ($stmt->execute()) {
        $isValid=true;
    }
    else {
        $isValid=false;
    }
}
elseif(strcasecmp($action,"accept")==0){
    $stmt = $conn->prepare("update friends set status='accepted' where senderid=? and receiverid=?");
    $stmt->bind_param("ss",$senderid,$receiverid);
    if ($stmt->execute())  {
        if(($stmt->affected_rows)) {
            $isValid=true;
        }
        else {
            $isValid=false;
        } 
    }
     else {
        $isValid=false;
    } 
}
elseif((strcasecmp($action,"cancel")==0) || (strcasecmp($action,"reject")==0) || (strcasecmp($action,"unfriend")==0)){
    $stmt = $conn->prepare("delete from friends where (senderid in(?,?) AND receiverid in(?,?))");
    $stmt->bind_param("ssss",$senderid,$receiverid,$senderid,$receiverid);
    if ($stmt->execute())  {
        if(($stmt->affected_rows)>0) {
            $isValid=true;
        }
        else {
            $isValid=false;
        }  
    }
    else {
        $isValid=false;
    } 
}
else{
    $response["error"]=true;
    $response["message"]="Wrong Action Passed |";
}


if($isValid){
    $response["error"]=false;
    $response["message"]=(strcasecmp($action,"unfriend")==0)?" Unfriended Successfully ":$action."ing request successful ";
}
else{
    $response["error"]=true;
    $response["message"].=" Failed to ".$action.((strcasecmp($action,"unfriend")==0)?" user ":" request ");
}

echo json_encode($response);
?>