<?php
require_once 'connection.php';
$response = array();

$data=json_decode($_POST["data"]);
//$data=json_decode(file_get_contents('php://input'),true); 

$senderid=isset($data->id)?($data->id):-404; //ProfileID of otherUser
$receiverid=isset($data->receiverid)?($data->receiverid):-404; //Id of current Loggedin User
$friendAction=isset($data->friendAction)?($data->friendAction):"none"; // FriendReqList|FriendsList
$friendReq=mysqli_query($conn,"select senderid from friends where receiverid=".$receiverid." and status='pending");

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

//$searchname=(isset($data->searchname))?$data->searchname:'';
$searchQuery = (isset($data->searchname))?
"AND ((concat(firstname,' ',lastname) LIKE '%".$searchname."%')  OR (concat(lastname,' ',firstname) like '%".$searchname."%')  OR  (firstname like '%".$searchname."%')  OR  (lastname LIKE '%".$searchname."%'))":"";

//Filters:gender,profession,religion,education,location,mothertongue,age,manglik and Searched Name

$finalQuery = "select ".$columnsWithoutPassword." from user where  (".$gender.")   AND  (".$religion.") AND (".$mothertongue.")  AND  (".$location.") AND (".$education.") AND  (".$profession.")  AND  (".$ageQuery.")  AND  (".$manglik.") AND (".$id.")".$searchQuery;

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

    $friendQuery = $conn->prepare("select status,senderid from friends where senderid IN(?,?) AND receiverid IN(?,?)");
    $friendQuery->bind_param("ssss",$senderid,$receiverid,$senderid,$receiverid);
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
        $response["status"]= "none"; //not in FriendsTable, bcz Req (Not sent Or Rejected)
        $response["whoSent"]=-404; //Just So the intent doesnot get blank object,So no need to check further if this property avaailable in JsonObject
    }

} else {
    $response["error"]=true;
    $response["message"]="No Matching Record Found !";
}
echo json_encode($response);
?>