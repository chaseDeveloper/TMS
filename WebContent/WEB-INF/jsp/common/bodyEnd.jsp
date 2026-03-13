<%@ page contentType="text/html; charset=UTF-8"%>

<!-- 
<div id="loadingImage" class="popBack">
	<div class="popFlex" >
		
		<div class="lds-default"><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div></div>
	</div>
</div>
 -->
 
<div id="loadingImage" class="popBack">
	<div class="popFlex" >		
		<img src="<%=request.getContextPath() %>/images/loading.gif" />
	</div>
</div>

<div id="uploadProgress"  class="popBack">
	<div class="popFlex" >		
		<div style="height:40px;display:flex;flex-direction:column;width:400px;background:#ddd;box-sizing:border-box;padding:10px;">
			<div class="progress">
			    <div id="uploadProgressBar" class="progress-bar" style="width: 0%;"></div>
			</div>
		</div>
	</div>
</div>


<iframe id="emptyFrm" name="emptyFrm" class="emptyFrm"></iframe>

<form id="fileForm" method="post" target="emptyFrm">
</form>

<div id="alertDlg" style="display:none;"><p id="alertDlgMsg"></p></div>

<div id="popFilePdfView" class="popupSearch" style="display:none;">
	<iframe id="popFilePdfViewFrm" style="width:100%;height:100%;border:none;margin:0;padding:0;"></iframe>
</div>

<div id="pdfView" class="popupSearch" style="display:none;">
	<iframe id="pdfViewFrm" style="width:100%;height:100%;border:none;margin:0;padding:0;"></iframe>
</div>