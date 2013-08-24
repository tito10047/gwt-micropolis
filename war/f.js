var data;
var re;
var reqid=Math.round(Math.random()*10000000);
var url = 'https://plus.google.com/b/106560435384632107269/_/socialgraph/lookup/visible/?o=%5Bnull%2Cnull%2C%22106560435384632107269%22%5D&_reqid='+reqid+'&rt=j';
xmlhttp=new XMLHttpRequest();
xmlhttp.open("GET",url,true);
xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
xmlhttp.onreadystatechange=function(){
    if (xmlhttp.readyState==4 && xmlhttp.status==200){
        re=xmlhttp.responseText.substr(5);
        data = eval(re);
    }
}
xmlhttp.send();

var usersId=[];
var users = data[0][1][2];
for(i in users){
    usersId[i]=users[i][0][2];
}
var notFind=[];
for(i in list){
    var find = false;
    for(e in usersId){
        if (list[i]==usersId[e]){
            find=true;
            break;
        }
    }
    if (!find){
        notFind[notFind.length]=list[i]
    }
}
var checked = ["116442465176807745041", "103245744268878460186", "100700242334136820985", "114160234548763573024", "106515158397123462426", "118234411342359340828", "117350267885827635019", "106935295832121460034", "110554670989270252538", "101253176382894198517", "101353711789538341031", "110986547664947889613", "114464060048812739196", "106427847023735628669", "103219191367440914421", "105734898196907423506", "113844248667779441353", "112979762618274396549", "112359522175624113998", "109615459845345496177", "101833703560859606489", "100148684576667986640", "109294831300359305691", "116839116284146830940"];
var notRealyFind=[];
for(i in notFind){
    var find = false;
    for(e in checked){
        if (notFind[i]==checked[e]){
            find=true;
            break;
        }
    }
    if (!find){
        notRealyFind[notRealyFind.length]=notFind[i]
    }
}