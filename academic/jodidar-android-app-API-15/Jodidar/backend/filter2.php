<?php
require_once 'connection.php';
$response = array();

$data=json_decode($_POST["data"]);
//$data=json_decode(file_get_contents('php://input'),true); 
$columnsWithoutPassword="id,firstname,lastname,email,bday,gender,religion,caste,location,education,profession,mothertongue,height,eating,manglik,lookingfor,about,image,online";

//When Passed "Any" Values that are set(Not Null) will be displayed-
//Only if Profile is set with Further values Those users will be displayed
//When "" Only null records displayed (Records with null values) No Profile Set But Registered
function putComma(array $arr,$columnName)
{ 
    $record=null;
    $comma=null;
    foreach ($arr as $item) 
    {   
        if(strcasecmp($item,"ANY")==0 )
        { return " IN(".$columnName.")";  }
        else if((strcasecmp($item,"")==0) || (strcasecmp($item,"NULL")==0) || (strcasecmp($item,null)==0))
        { return " IN(".$columnName.") OR ".$columnName." is null";} //Displays EveryRecord in db.
        else{
            $record.=$comma."'".$item."'";
            $comma=",";
        }
    }
    return " IN(".$record.")";
}

function setQueryFromJsonField($columnName)
{
    $data=$GLOBALS['data'];
    if(isset($data->$columnName))
    {
        
        $record = $data->$columnName;
       
        if (is_array($record))
        {    
            $withcoma = "'".implode("','",$record)."'";
            return $columnName." ".putComma($record,$columnName); //$columnName." IN(".$withcoma.")";
        }
        else if(strcasecmp($record,"Any")==0)
        {   return  $columnName." LIKE '%'";    
        }
        else if( (strcasecmp($record,"")==0) || (strcasecmp($record,"NULL")==0) || (strcasecmp($record,null)==0))
        {   return $columnName."  is null"; 
        }
        else
        {   return $columnName." LIKE '".$record."'";   }
        }
    else{ return $columnName." LIKE '%' OR ".$columnName."  is null";  }
 }

//  function getUserById($id){
//     $stmt = $conn->prepare("select id,firstname,lastname,email,bday,gender,religion,caste,location,education,profession,mothertongue,height,eating,manglik,lookingfor,about,image,online from user where id=?");
//     $stmt->bind_param("s",$id);
//     $stmt->execute();
//     $stmt->store_result();
    
    

$education = setQueryFromJsonField('education');
$mothertongue = setQueryFromJsonField('mothertongue');
$religion = setQueryFromJsonField('religion');
$location = setQueryFromJsonField('location');
$gender = setQueryFromJsonField('gender');
$profession = setQueryFromJsonField('profession');
$eating = setQueryFromJsonField('eating');
$manglik = setQueryFromJsonField('manglik');
$id = setQueryFromJsonField('id');
//$sql = isset($data["id"]) ? getUserById($data["id"]):"";

$minAge=(isset($data->minAge))?$data->minAge:'18'; //else display age>18
$maxAge=(isset($data->maxAge))?$data->maxAge:'100'; //else display age<100
$ageQuery="bday between date_add(curdate(), interval -".$maxAge." year) AND date_add(curdate(), interval -".$minAge." year)";

//Filters:gender,profession,religion,education,location,mothertongue,age,manglik

$finalQuery = "select ".$columnsWithoutPassword." from user where  (".$gender.")   AND  (".$religion.") AND (".$mothertongue.")  AND  (".$location.") AND (".$education.") AND  (".$profession.")  AND  (".$ageQuery.")  AND  (".$manglik.") AND (".$id.")";

//echo "\nQUERY=".$finalQuery."\n";
        
$result=mysqli_query($conn,$finalQuery);

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
    $response["message"]="No Record Found.";
}
echo json_encode($response);
?>