<?php
require_once "connection.php";
$response = array();

if(isset($_GET["apicall"]))
{
    $apicall=$_GET["apicall"];
    
    switch($apicall)
{
    case "signup":
        require_once "signup.php";
        break;
    case "login":
        require_once "login.php"; 
        break;
    
    default:
        $response["error"]=true;
        $response["message"]="Wrong ApiCall Parameter";
}
}
else{
    $response["error"]=true;
    $response["message"]="APICALL is NULL";
}
//check required Fields are not Null 
function isNotNull($params){
	
    foreach($params as $param){
        //if the paramter is not available
        if(!isset($_POST[$param])){
            //return false 
            return false; 
        }
    }
    //return true if every param is available 
    return true; 
}
echo json_encode($response);
?>