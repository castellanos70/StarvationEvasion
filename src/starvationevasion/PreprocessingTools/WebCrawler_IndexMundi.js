function country(name, id){
    this.name = name;
    this.id = id;
    this.tables = [];
}
var objIntercept = [new country( "Afghanistan",	"af"),
                    new country( "Albania",	"al"),
                    new country( "Algeria",	"dz"),
                    new country( "Argentina",	"ar"),
                    new country( "Armenia",	"am"),
                    new country( "Australia",	"au"),
                    new country( "Azerbaijan",	"az"),
                    new country( "Belarus",	"by"),
                    new country( "Bolivia",	"bo"),
                    new country( "Bosnia and Herzegovina",	"ba"),
                    new country( "Brazil",	"br"),
                    new country( "Bulgaria",	"bg"),
                    new country( "Canada",	"ca"),
                    new country( "Chile",	"cl"),
                    new country( "China",	"cn"),
                    new country( "Colombia",	"co"),
                    new country( "Croatia",	"hr"),
                    new country( "Cuba",	"cu"),
                    new country( "Cyprus",	"cy"),
                    new country( "Czech Republic",	"cz"),
                    new country( "Czechoslovakia",	"c2"),
                    new country( "Ecuador",	"ec"),
                    new country( "Egypt",	"eg"),
                    new country( "Estonia",	"ee"),
                    new country( "Ethiopia",	"et"),
                    new country( "Georgia",	"ge"),
                    new country( "Hungary",	"hu"),
                    new country( "India",	"in"),
                    new country( "Iran, Islamic Republic Of",	"ir"),
                    new country( "Iraq",	"iq"),
                    new country( "Israel",	"il"),
                    new country( "Japan",	"jp"),
                    new country( "Kazakhstan",	"kz"),
                    new country( "Kenya",	"ke"),
                    new country( "Korea, Republic Of",	"kr"),
                    new country( "Kyrgyzstan",	"kg"),
                    new country( "Latvia",	"lv"),
                    new country( "Lebanon",	"lb"),
                    new country( "Lithuania",	"lt"),
                    new country( "Macedonia, The Former Yugoslav Republic Of",	"mk"),
                    new country( "Mexico",	"mx"),
                    new country( "Moldova, Republic Of",	"md"),
                    new country( "Morocco",	"ma"),
                    new country( "Norway",	"no"),
                    new country( "Pakistan",	"pk"),
                    new country( "Peru",	"pe"),
                    new country( "Poland",	"pl"),
                    new country( "Romania",	"ro"),
                    new country( "Russian Federation",	"ru"),
                    new country( "Serbia",	"rs"),
                    new country( "Serbia and Montenegro",	"cs"),
                    new country( "Singapore",	"sg"),
                    new country( "Slovakia",	"sk"),
                    new country( "Slovenia",	"si"),
                    new country( "South Africa",	"za"),
                    new country( "Switzerland",	"ch"),
                    new country( "Syrian Arab Republic",	"sy"),
                    new country( "Taiwan, Province Of China",	"tw"),
                    new country( "Tajikistan",	"tj"),
                    new country( "Tunisia",	"tn"),
                    new country( "Turkey",	"tr"),
                    new country( "Turkmenistan",	"tm"),
                    new country( "U.S.S.R.",	"su"),
                    new country( "Ukraine",	"ua"),
                    new country( "United States",	"us"),
                    new country( "Uruguay",	"uy"),
                    new country( "Uzbekistan",	"uz"),
                    new country( "Venezuela",	"ve"),
                    new country( "Yemen",	"ye"),
                    new country( "Yemen (Sanaa)",	"ys"),
                    new country( "Yugoslavia",	"yu"),
                    new country( "Zimbabwe",	"zw")];

var Commodities = ["barley"];
var Graphs = ["exports", "imports", "yield"];
for(var region of objIntercept)
{
    for(var item of Commodities)
    {
        var itemTable = [];
        for(var value of Graphs)
        {
            window.location = "http://www.indexmundi.com/agriculture/?country="+region.id+"&commodity="+item+"&graph="+value;
            var table = document.getElementById('gvData');
            var rows = table.getElementsByTagName('tr');
            var rowList = [];
            for(row of rows)
            	rowList.push(row.getElementsByTagName('td'));
            parseArray = [];
            for(var i = 0; i < rowList.length; i++)
            {
              if(rowList[i].length === 0);// console.log("null");
              else if(Number.parseInt(rowList[i][0].textContent) < 2000); //console.log(Number.parseInt(rowList[i][0].textContent) + "old");
              else
              {
                parseRow = [];
                for(var k = 0; k < rowList[i].length - 1;k++)
                {
                  parseRow.push(rowList[i][k].textContent);
                }
                parseArray.push(parseRow);
              }
            }
            itemTable.push(parseArray);
        }
        region.tables.push(itemTable);
    }
    console.log("finished" + region.name);
}
alert(objIntercept)

/*
var table = document.getElementById('gvData');
var rows = table.getElementsByTagName('tr');
var rowList = [];
for(row of rows)
	rowList.push(row.getElementsByTagName('td'));
parseArray = [];
for(var i = 0; i < rowList.length; i++)
{
  console.log(i);
  if(rowList[i].length === 0) console.log("null");
  else if(Number.parseInt(rowList[i][0].textContent) < 2000) console.log(Number.parseInt(rowList[i][0].textContent) + "old");
  else
  {
    parseRow = [];
    for(var k = 0; k < rowList[i].length - 1;k++)
    {
      parseRow.push(rowList[i][k].textContent);
    }
    parseArray.push(parseRow);
  }
}

var Intercept= ["Afghanistan",
                "Albania",
                "Algeria",
                "Argentina",
                "Armenia",
                "Australia",
                "Azerbaijan",
                "Belarus",
                "Bolivia",
                "Bosnia and Herzegovina",
                "Brazil",
                "Bulgaria",
                "Canada",
                "Chile",
                "China",
                "Colombia",
                "Croatia",
                "Cuba",
                "Cyprus",
                "Czech Republic",
                "Czechoslovakia",
                "Ecuador",
                "Egypt",
                "Estonia",
                "Ethiopia",
                "Georgia",
                "Hungary",
                "India",
                "Iran, Islamic Republic Of",
                "Iraq",
                "Israel",
                "Japan",
                "Kazakhstan",
                "Kenya",
                "Korea, Republic Of",
                "Kyrgyzstan",
                "Latvia",
                "Lebanon",
                "Lithuania",
                "Macedonia, The Former Yugoslav Republic Of",
                "Mexico",
                "Moldova, Republic Of",
                "Morocco",
                "Norway",
                "Pakistan",
                "Peru",
                "Poland",
                "Romania",
                "Russian Federation",
                "Serbia",
                "Serbia and Montenegro",
                "Singapore",
                "Slovakia",
                "Slovenia",
                "South Africa",
                "Switzerland",
                "Syrian Arab Republic",
                "Taiwan, Province Of China",
                "Tajikistan",
                "Tunisia",
                "Turkey",
                "Turkmenistan",
                "U.S.S.R.",
                "Ukraine",
                "United States",
                "Uruguay",
                "Uzbekistan",
                "Venezuela",
                "Yemen",
                "Yemen (Sanaa)",
                "Yugoslavia",
                "Zimbabwe"];

var Barley = ["Afghanistan "
              ,"Albania"
              ,"Algeria"
              ,"Argentina"
              ,"Armenia"
              ,"Australia"
              ,"Azerbaijan"
              ,"Bangladesh"
              ,"Belarus"
              ,"Bhutan"
              ,"Bolivia"
              ,"Bosnia and Herzegovina"
              ,"Brazil"
              ,"Bulgaria"
              ,"Canada"
              ,"Chile"
              ,"China"
              ,"Colombia"
              ,"Croatia"
              ,"Cuba"
              ,"Cyprus"
              ,"Czech Republic"
              ,"Czechoslovakia"
              ,"Ecuador"
              ,"Egypt"
              ,"Eritrea"
              ,"Estonia"
              ,"Ethiopia"
              ,"EU-15"
              ,"EU-27"
              ,"Georgia"
              ,"Hungary"
              ,"Iceland"
              ,"India"
              ,"Iran, Islamic Republic Of"
              ,"Iraq"
              ,"Israel"
              ,"Japan"
              ,"Jordan"
              ,"Kazakhstan"
              ,"Kenya"
              ,"Korea, Republic Of"
              ,"Kuwait"
              ,"Kyrgyzstan"
              ,"Latvia"
              ,"Lebanon"
              ,"Libya"
              ,"Lithuania"
              ,"Macedonia, The Former Yugoslav Republic Of"
              ,"Malta"
              ,"Mexico"
              ,"Moldova, Republic Of"
              ,"Morocco"
              ,"Nepal"
              ,"New Zealand"
              ,"Norway"
              ,"Pakistan"
              ,"Peru"
              ,"Poland"
              ,"Romania"
              ,"Russian Federation"
              ,"Saudi Arabia"
              ,"Serbia"
              ,"Serbia and Montenegro"
              ,"Singapore"
              ,"Slovakia"
              ,"Slovenia"
              ,"South Africa"
              ,"Switzerland"
              ,"Syrian Arab Republic"
              ,"Taiwan, Province Of China"
              ,"Tajikistan"
              ,"Tunisia"
              ,"Turkey"
              ,"Turkmenistan"
              ,"U.S.S.R."
              ,"Ukraine"
              ,"United Arab Emirates"
              ,"United States"
              ,"Uruguay"
              ,"Uzbekistan"
              ,"Venezuela"
              ,"Yemen"
              ,"Yemen (Sanaa)"
              ,"Yugoslavia"
              ,"Yugoslavia (after May 1992)"
              ,"Zimbabwe"]

var Corn = ["Afghanistan "
            ,"Albania"
            ,"Algeria"
            ,"Angola"
            ,"Argentina"
            ,"Armenia"
            ,"Australia"
            ,"Azerbaijan"
            ,"Belarus"
            ,"Benin"
            ,"Bhutan"
            ,"Bolivia"
            ,"Bosnia and Herzegovina"
            ,"Botswana"
            ,"Brazil"
            ,"Bulgaria"
            ,"Burkina Faso"
            ,"Burundi"
            ,"Cambodia"
            ,"Cameroon"
            ,"Canada"
            ,"Cape Verde"
            ,"Central African Republic"
            ,"Chad"
            ,"Chile"
            ,"China"
            ,"Colombia"
            ,"Congo"
            ,"Congo, The Democratic Republic Of The"
            ,"Costa Rica"
            ,"Côte D'ivoire"
            ,"Croatia"
            ,"Cuba"
            ,"Cyprus"
            ,"Czech Republic"
            ,"Czechoslovakia"
            ,"Dominican Republic"
            ,"Ecuador"
            ,"Egypt"
            ,"El Salvador"
            ,"Eritrea"
            ,"Estonia"
            ,"Ethiopia"
            ,"EU-15"
            ,"EU-27"
            ,"Gambia"
            ,"Georgia"
            ,"Ghana"
            ,"Guatemala"
            ,"Guinea"
            ,"Guinea-Bissau"
            ,"Guyana"
            ,"Haiti"
            ,"Honduras"
            ,"Hong Kong"
            ,"Hungary"
            ,"India"
            ,"Indonesia"
            ,"Iran, Islamic Republic Of"
            ,"Iraq"
            ,"Israel"
            ,"Jamaica"
            ,"Japan"
            ,"Jordan"
            ,"Kazakhstan"
            ,"Kenya"
            ,"Korea, Democratic People's Republic Of"
            ,"Korea, Republic Of"
            ,"Kuwait"
            ,"Kyrgyzstan"
            ,"Lao People's Democratic Republic "
            ,"Latvia"
            ,"Lebanon"
            ,"Lesotho"
            ,"Libya"
            ,"Lithuania"
            ,"Macedonia, The Former Yugoslav Republic Of"
            ,"Madagascar"
            ,"Malawi"
            ,"Malaysia"
            ,"Mali"
            ,"Malta"
            ,"Mauritania"
            ,"Mexico"
            ,"Moldova, Republic Of"
            ,"Morocco"
            ,"Mozambique"
            ,"Myanmar"
            ,"Namibia"
            ,"Nepal"
            ,"New Zealand"
            ,"Nicaragua"
            ,"Nigeria"
            ,"Norway"
            ,"Pakistan"
            ,"Panama"
            ,"Paraguay"
            ,"Peru"
            ,"Philippines"
            ,"Poland"
            ,"Romania"
            ,"Russian Federation"
            ,"Rwanda"
            ,"Saudi Arabia"
            ,"Senegal"
            ,"Serbia"
            ,"Serbia and Montenegro"
            ,"Sierra Leone"
            ,"Singapore"
            ,"Slovakia"
            ,"Slovenia"
            ,"Somalia"
            ,"South Africa"
            ,"Swaziland"
            ,"Switzerland"
            ,"Syrian Arab Republic"
            ,"Taiwan, Province Of China"
            ,"Tajikistan"
            ,"Tanzania, United Republic Of"
            ,"Thailand"
            ,"Togo"
            ,"Trinidad and Tobago"
            ,"Tunisia"
            ,"Turkey"
            ,"Turkmenistan"
            ,"U.S.S.R."
            ,"Uganda"
            ,"Ukraine"
            ,"United States"
            ,"Uruguay"
            ,"Uzbekistan"
            ,"Venezuela"
            ,"Viet Nam"
            ,"Yemen"
            ,"Yemen (Sanaa)"
            ,"Yugoslavia"
            ,"Zambia"
            ,"Zimbabwe"]

var Cotton =["Afghanistan "
             ,"Albania"
             ,"Algeria"
             ,"Angola"
             ,"Argentina"
             ,"Armenia"
             ,"Australia"
             ,"Austria"
             ,"Azerbaijan"
             ,"Bahrain"
             ,"Bangladesh"
             ,"Belarus"
             ,"Belgium-Luxembourg"
             ,"Benin"
             ,"Bolivia"
             ,"Bosnia and Herzegovina"
             ,"Brazil"
             ,"Bulgaria"
             ,"Burkina Faso"
             ,"Cambodia"
             ,"Cameroon"
             ,"Canada"
             ,"Central African Republic"
             ,"Chad"
             ,"Chile"
             ,"China"
             ,"Colombia"
             ,"Congo, The Democratic Republic Of The"
             ,"Costa Rica"
             ,"Côte D'ivoire"
             ,"Croatia"
             ,"Cuba"
             ,"Cyprus"
             ,"Czech Republic"
             ,"Czechoslovakia"
             ,"Denmark"
             ,"Dominican Republic"
             ,"Ecuador"
             ,"Egypt"
             ,"El Salvador"
             ,"Estonia"
             ,"Ethiopia"
             ,"Federal Republic of Germany"
             ,"Finland"
             ,"France"
             ,"Georgia"
             ,"German Democratic Republic"
             ,"Germany"
             ,"Ghana"
             ,"Greece"
             ,"Guatemala"
             ,"Guinea"
             ,"Haiti"
             ,"Honduras"
             ,"Hong Kong"
             ,"Hungary"
             ,"India"
             ,"Indonesia"
             ,"Iran, Islamic Republic Of"
             ,"Iraq"
             ,"Ireland"
             ,"Israel"
             ,"Italy"
             ,"Japan"
             ,"Kazakhstan"
             ,"Kenya"
             ,"Korea, Democratic People's Republic Of"
             ,"Korea, Republic Of"
             ,"Kyrgyzstan"
             ,"Latvia"
             ,"Lebanon"
             ,"Lesotho"
             ,"Lithuania"
             ,"Macedonia, The Former Yugoslav Republic Of"
             ,"Madagascar"
             ,"Malawi"
             ,"Malaysia"
             ,"Mali"
             ,"Mauritius"
             ,"Mexico"
             ,"Moldova, Republic Of"
             ,"Morocco"
             ,"Mozambique"
             ,"Myanmar"
             ,"Netherlands"
             ,"Nicaragua"
             ,"Niger"
             ,"Nigeria"
             ,"Norway"
             ,"Pakistan"
             ,"Panama"
             ,"Paraguay"
             ,"Peru"
             ,"Philippines"
             ,"Poland"
             ,"Portugal"
             ,"Romania"
             ,"Russian Federation"
             ,"Senegal"
             ,"Serbia"
             ,"Serbia and Montenegro"
             ,"Singapore"
             ,"Slovakia"
             ,"Slovenia"
             ,"Somalia"
             ,"South Africa"
             ,"Spain"
             ,"Sri Lanka"
             ,"Sudan"
             ,"Sweden"
             ,"Switzerland"
             ,"Syrian Arab Republic"
             ,"Taiwan, Province Of China"
             ,"Tajikistan"
             ,"Tanzania, United Republic Of"
             ,"Thailand"
             ,"Togo"
             ,"Tunisia"
             ,"Turkey"
             ,"Turkmenistan"
             ,"U.S.S.R."
             ,"Uganda"
             ,"Ukraine"
             ,"United Kingdom"
             ,"United States"
             ,"Uruguay"
             ,"Uzbekistan"
             ,"Venezuela"
             ,"Viet Nam"
             ,"Yemen"
             ,"Yemen (Aden)"
             ,"Yemen (Sanaa)"
             ,"Yugoslavia"
             ,"Zambia"
             ,"Zimbabwe"]