package ru.runa.gpd.bot;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.widgets.TreeItem;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;

import ru.runa.gpd.Localization;
import ru.runa.gpd.PluginLogger;
import ru.runa.gpd.util.IOUtils;
import ru.runa.gpd.util.files.ParFileImporter;
import ru.runa.gpd.util.files.ProcessDefinitionImportInfo;

public class DeployBotGS {
	public DeployBotGS(){
		
	}
	public void deploy(String taskName, String BotName, byte[] GlobalBytes){
		List<ProcessDefinitionImportInfo> importInfos = Lists.newArrayList();
		try {
            IFolder selectedProject = getSelectedProject(taskName,BotName);
            String definitionName = ".globalSection";
            String fileName = taskName + File.separator + definitionName;
            InputStream GlobalFile = new ByteArrayInputStream(GlobalBytes);            
            IOUtils.extractArchiveToFolder(GlobalFile, selectedProject);
                        
            final ParFileImporter importer = new ParFileImporter(selectedProject);            
            for (ProcessDefinitionImportInfo importInfo : importInfos) {
                if (importer.importFile(importInfo) == null) {
                    PluginLogger.logError(Localization.getString("ImportParWizardPage.error.processWithSameNameExists", importInfo.getPath()),null);
                    return;
                }
            }
		} catch (Exception exception) {
	        PluginLogger.logErrorWithoutDialog("import par", exception);	        
	        return;
	    } finally {
	        for (ProcessDefinitionImportInfo importInfo : importInfos) {
	            try {
	                importInfo.close();
	            } catch (IOException e) {
	            }
	        }
	    }
    }
	


	private IFolder getSelectedProject(String taskName, String BotName) {
		IPath path = new Path("BotGlobalSection").append(BotName+Path.SEPARATOR+taskName);
        IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(path);
		return folder;
	}
}
