package org.joget.marketplace;

import java.io.File;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Properties;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.Element;
import org.joget.apps.form.model.FormBuilderPalette;
import org.joget.apps.form.model.FormBuilderPaletteElement;
import org.joget.apps.form.model.FormData;
import org.joget.apps.form.model.FormRow;
import org.joget.apps.form.model.FormRowSet;
import org.joget.apps.form.service.FormUtil;
import org.joget.commons.util.FileManager;
import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.service.AppService;


public class MarkerArea extends Element implements FormBuilderPaletteElement {
    @Override
    public String getName() {
        return "Marker Area Field";
    }

    @Override
    public String getVersion() {
        return "7.0.0";
    }

    @Override
    public String getDescription() {
        return "Marker Area Field Element";
    }

    @Override
    public String getLabel() {
        return "Marker Area";
    }
    
    @Override
    public String getClassName() {
        return getClass().getName();
    }

    @Override
    public String getPropertyOptions() {
        return AppUtil.readPluginResource(getClass().getName(), "/properties/form/markerArea.json", null, true, "messages/form/markerArea");
    }

    @Override
    public String getFormBuilderCategory() {
        return FormBuilderPalette.CATEGORY_CUSTOM;
    }

    @Override
    public int getFormBuilderPosition() {
        return 100;
    }

    @Override
    public String getFormBuilderIcon() {
        return "<i class=\"fas fa-image\"></i>";
    }

    @Override
    public String getFormBuilderTemplate() {
        return "<label class='label'>markerArea</label><input type='text' />";
    }

    @Override
    public String renderTemplate(FormData formData, Map dataModel) {
        String template = "markerArea.ftl";

        // set value
        String value = FormUtil.getElementPropertyValue(this, formData);
        dataModel.put("value", value);

        String img = getPropertyString("annotationimage");
        AppDefinition appDef = AppUtil.getCurrentAppDefinition();
        String formDefId = getPropertyString("formDefId");
        String imgfilePath = "";
        String image = "";
        AppService appService = (AppService) AppUtil.getApplicationContext().getBean("appService");
        Properties properties = new Properties();
        
        //get the id of this record   
        String primaryKeyValue = getPrimaryKeyValue(formData);
        
        FormRow row = new FormRow();
        FormRowSet rowSet = appService.loadFormData(appDef.getAppId(), appDef.getVersion().toString(), formDefId, primaryKeyValue);
        if (!rowSet.isEmpty()) {
            row = rowSet.get(0);
            image = row.getProperty(img);
        }
        
        
        
        
        
        
        try {
            File file = FileManager.getFileByPath(image);
            if (primaryKeyValue != null && !primaryKeyValue.isEmpty() && file == null) {
                String encodedFileName = URLEncoder.encode(image, "UTF8").replaceAll("\\+", "%20");
                String filePath = AppUtil.getRequestContextPath() + "/web/client/app/" + appDef.getAppId() + "/" + appDef.getVersion() + "/form/download/" + formDefId + "/" + primaryKeyValue + "/" + encodedFileName;
                imgfilePath = filePath + ".";
            } 
        } catch (Exception e) {}
        
         dataModel.put("image", imgfilePath);                               
        String html = FormUtil.generateElementHtml(this, formData, template, dataModel);
        return html;
    }
}
