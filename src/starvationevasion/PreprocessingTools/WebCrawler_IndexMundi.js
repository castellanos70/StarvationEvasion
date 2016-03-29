var table = document.getElementById('gvData');
var rows = table.getElementsByTagName('tr');
var rowList = [];
for(row of rows)
	rowList.push(row.getElementsByTagName('td'));
while(rowList[0].length === 0) rowList.shift(); //There usually is an empty formatting cell
while(Number.parseInt(rowList[0][0].textContent) < 2000) rowList.shift();
for(var i = 0; i < rowList.length; i++)
	rowList[0].pop(); //Remove growth rate
