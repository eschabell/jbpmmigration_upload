<html>
<head>
<title>File Uploading Form</title>
</head>
<body>

<center>
	<img src='rowducks.jpg' width='60%' height='30%'/>
	<h1>Welcome to the jBPM Migration in the Cloud!</h1>
	<h3>Do you have your ducks in a row?</h3>
</center>

<hr>                    

<h4>Upload jBPM 3.2 Process Defintion:</h4>
This site allows you to test the latest published version of the jBPM Migration Tool 
which will transform your jBPM 3.2 process definition into a validated BPMN2 process 
definition. Be warned that there is currently very little error checking in this web 
tool, so should you have problems please feel free to contact us through the project 
at <a href="https://github.com/droolsjbpm/jbpmmigration" target="_blank">Drools jBPM Github</a>
or <a href="https://github.com/droolsjbpm/jbpmmigration/issues/new" target="_blank">raise an issue</a>
 for us. Don't forget to attach your process definition!
<br/>
<br/>
If you are looking for a working test process that will convert to BPMN2, you can download 
<a href="https://raw.github.com/droolsjbpm/jbpmmigration/master/src/test/resources/jpdl3/claimFinanceProcess/processdefinition.xml" target="_blank">
this one</a> from our test suite and upload it.
<br/>
<br/>
Select a file to upload: 
<form action="UploadServlet.jsp" method="post" enctype="multipart/form-data">
	<input type="file" name="file" size="50" />
	<br/>
	<input type="submit" value="Upload File" />
</form>

<center>
	<img src='jbpm_logo.png'>
</center>
</body>
</html>
