<#if includeMetaData>
<div class="form-cell <#if element.properties.label == "">no_label</#if> ${element.properties.id!}" ${elementMetaData!}>
    <#if element.properties.label != "">
        <label class="label">${element.properties.label!} <span class="form-cell-validator">${decoration}</span><#if error??> <span class="form-error-message">${error}</span></#if></label>
    </#if>
<#else>
<div class="form-cell <#if element.properties.label == "">no_label</#if> ${element.properties.id!}" ${elementMetaData!}>
    <#if element.properties.label != "">
        <label class="label">${element.properties.label!} <span class="form-cell-validator">${decoration}</span><#if error??> <span class="form-error-message">${error}</span></#if></label>
    </#if>
    <div class="form-cell-value <#if element.properties.label == ''>form-cell-full</#if>">
        <div id="drawBox" style="padding: 20px 0;">
            <img src="${image}" id="myimgSrc" style="max-width: 100%;" onload="setSourceImage(this);"/>
            <img src="${image}" id="myimg" style="max-width: 100%; position: absolute; left: 0px;" onclick="showMarkerArea(this);"/>
            <input id="${elementParamName!}" name="${elementParamName!}" type="hidden" value="${value!?html}"/>
        </div>
    </div>
    <div style="clear:both;"></div>
</div>
<style>
    .form-cell, .subform-cell {
        padding-left: 0px !important;
    }
</style>
</#if>
<script src="${request.contextPath}/plugin/org.joget.marketplace.MarkerArea/js/markerjs2.js"></script>

<style>
    div.__markerjs2_ *{
        font-size: unset !important;
    }
</style>

<script>

    var maState, sourceImage, targetRoot, markerArea;
    
    function setSourceImage(source) {
      sourceImage = source;
      targetRoot = source.parentElement;
    }
    
    function showMarkerArea(target) {
        markerArea = new markerjs2.MarkerArea(sourceImage);
        
        //markerArea.settings.displayMode = 'popup';
        markerArea.targetRoot = targetRoot;
        //markerArea.availableMarkerTypes = ['FrameMarker', markerjs2.ArrowMarker, markerjs2.LineMarker];
        
        markerArea.addRenderEventListener((imgURL, state) => {
            //save button is pressed
            target.src = imgURL;
            maState = state;
            FormUtil.getField("${elementParamName!}").val(JSON.stringify(state));
            FormUtil.getField("annotationImage").val($("#myimg").attr("src"));
        });
        
        markerArea.show();
        
        if(maState){
            markerArea.restoreState(maState);
        }
        
        
    }
    
    $(document).ready(function(){
        if( FormUtil.getField("${elementParamName!}").val() != "" ){
            maState = JSON.parse(FormUtil.getField("${elementParamName!}").val());
        }
        setSourceImage($("#myimgSrc")[0]);
        showMarkerArea($("#myimg")[0]);
        markerArea.renderClicked();
    });
    
</script>