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
<h1>현황 페이지</h1>
	<table id="wait-list">
	<caption>대기자 명단</caption>
	<thead>
		<tr>
			<th>선택</th>
			<th>순번</th>
			<th>성명</th>
		</tr>
	</thead>
	<tbody>
			
	</tbody>
	</table>
	<button type="button" id="clinic-btn">진료</button>
	<script type="text/javascript">
		function clinicList(){
			$.ajax({
				url : "/clinics/진료대기",
				type : "GET",
				dataType: 'json',
		        success: function(clinicList){
		        	clinicList.forEach(function(clinic, index){
		        		let waitTable = $('#wait-list tbody');
		        		
		        		let waitTableTr = $('<tr>');
		        		let radioTd = $('<td>').append($('<input type="radio" name="wait" class="wait">'));
		        		let indexTd = $('<td>').text(index + 1);
		        		let patientTd = $('<td>').text(clinic.patient);
		        		
		        		waitTableTr.append(radioTd).append(indexTd).append(patientTd);
		        		waitTable.append(waitTableTr);
		        	});
					
				}
			});
		}
		$(function(){
			clinicList();
		});
		let webSocket = new WebSocket("ws://localhost:8585/wait-ws");
		
		$('#clinic-btn').click(function(){
			const patient = $('.wait:checked').closest('tr').find('td:last').text();
			if(confirm(patient + "님을 진료상태로 변경하시겠습니까?")){
				$.ajax({
					url : "/clinics/" + patient,
					type : "PUT",
					datatype : "json",
					data : {status : "진료중"},
					success : function(msg){
						let clinicDto = [];
						clinicDto.push({patient : patient});
						webSocket.send(JSON.stringify(clinicDto));
						alert(msg);
						$('#wait-list tbody').empty();
						clinicList();
					},
				});
			}
		});
		
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