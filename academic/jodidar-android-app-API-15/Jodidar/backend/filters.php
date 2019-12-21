<?php
require_once 'connection.php';
$response = array();

$data=json_decode(file_get_contents('php://input'),true); //json_decode($_POST["data"]);
function putComma(array $arr,$jsonField)
{ 
    $columnName=null;
    $comma=null;
    foreach ($arr as $item) 
    {
        if(strcasecmp($item,"ANY")==0 || (strcasecmp($columnName,"")==0) || (strcasecmp($columnName,"NULL`")==0) || (strcasecmp($columnName,null)==0)){ return $jsonField;  }
        else{
            $columnName.=$comma."'".$item."'";
            $comma=",";
        }
    }
    return $columnName;
}

function setDataFromJsonField($jsonField){
    $allRecord = $jsonField;
    
    $data=$GLOBALS['data'];
    if(isset($data[$jsonField]))
    {
        $columnName = $data[$jsonField];

        return (is_array($columnName))
        ?putComma($columnName,$jsonField)
        :(strcasecmp($columnName,"Any")==0) || (strcasecmp($columnName,"")==0) ||(strcasecmp($columnName,"NULL")==0) || (strcasecmp($columnName,null)==0) 
                ? $allRecord
                : "'".$columnName."'";   
     }
    else{  echo "\n#JsonFiledstart#".$jsonField."#endJsonField#\n";
         return $allRecord;  }
 }

$education = setDataFromJsonField('education');
$language = setDataFromJsonField('mothertongue');
 echo "\n#startEdut#".$education."#endEducation#"."\n----#startLang#".$language."#endLang#\n";
$sql = "select id,firstname,lastname,email,education,mothertongue from user where mothertongue IN(".$language.")
         AND education IN(".$education.")";
        
$result=mysqli_query($conn,$sql);

if (mysqli_num_rows($result) > 0) {
    while($row = mysqli_fetch_assoc($result)) {
        array_push($response,$row);
    }
} else {echo "0 results";}
echo json_encode($response);
?>