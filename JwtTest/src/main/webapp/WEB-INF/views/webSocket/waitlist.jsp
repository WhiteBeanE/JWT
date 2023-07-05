<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
	#wait-list{
		border-collapse: collapse;
	}
	#wait-list td, th{
		border: 1px solid black;
	}
</style>
</head>
<body>
	<table id="wait-list">
	<caption>대기자 명단</caption>
	<thead>
		<tr>
			<th>순번</th>
			<th>성명</th>
		</tr>
	</thead>
	<tbody>
			
	</tbody>
	</table>
	<script type="text/javascript">
		$(function(){
			$.ajax({
				url : "/clinics/진료대기",
				type : "GET",
				dataType: 'json',
		        success: function(clinicList){
		        	clinicList.forEach(function(clinic, index){
		        		let waitTable = $('#wait-list tbody');
		        		
		        		let waitTableTr = $('<tr>');
		        		let indexTd = $('<td>').text(index + 1);
		        		let patientTd = $('<td>').text(clinic.patient);
		        		
		        		waitTableTr.append(indexTd).append(patientTd);
		        		waitTable.append(waitTableTr);
		        	});
					
				}
			});
		});
		
		//웹소켓 객체 생성
		let webSocket = new WebSocket("ws://localhost:8585/wait-ws");

		webSocket.onopen = function(){
			console.log("webSocket opened");
		}

		webSocket.onmessage = function(message) {
			console.log(message);
			let clinicDto = JSON.parse(message.data);
			console.log(clinicDto);
		};
			
		webSocket.onclose = function(){
			console.log("server closed");
		}
	</script>
</body>
</html>