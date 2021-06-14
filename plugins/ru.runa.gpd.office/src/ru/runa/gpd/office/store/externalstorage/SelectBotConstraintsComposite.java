package ru.runa.gpd.office.store.externalstorage;

import java.util.List;
import java.util.function.Predicate;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.google.common.base.Strings;

import ru.runa.gpd.lang.model.Delegable;
import ru.runa.gpd.lang.model.Variable;
import ru.runa.gpd.office.InputOutputModel;
import ru.runa.gpd.office.Messages;
import ru.runa.gpd.office.store.StorageConstraintsModel;

public class SelectBotConstraintsComposite extends AbstractOperatingVariableComboBasedConstraintsCompositeBuilder{
	private Delegable delegable;
	private InputOutputModel inOutModel;
	protected Combo resultCombo;
	public SelectBotConstraintsComposite(Composite parent, int style, StorageConstraintsModel constraintsModel, VariableProvider variableProvider,
            String variableTypeName,InputOutputModel inOutModel, Delegable delegable) {
        super(parent, style, constraintsModel, variableProvider, variableTypeName);
        this.delegable = delegable;
        this.inOutModel = inOutModel;
    }

    @Override
    protected Predicate<? super Variable> getFilterPredicate(String variableTypeName) {
        return variable -> variable.getUserType().getName().equals(variableTypeName);
    }
    
    protected Predicate<? super Variable> getFilter(String variableTypeName) {
        return variable -> variable.getFormatComponentClassNames()[0].equals(variableTypeName);
    }
    @Override
    protected String getComboTitle() {    	
        return Messages.getString("label.SelectVariable");
    }
    @Override
    public void build() {
    	super.build();
    	new Label(getParent(), SWT.NONE).setText(Messages.getString("label.SelectResultVariable"));
        addResultCombo();        
    }
    
    protected void addResultCombo() {
        resultCombo = new Combo(getParent(), SWT.READ_ONLY);
        delegable.getVariableNames(true,List.class.getName()).forEach(resultCombo::add); ;

        resultCombo.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> {
            final String text = combo.getText();
            inOutModel.outputVariable = combo.getText();
            if (Strings.isNullOrEmpty(text)) {
                return;
            }            
            onWidgetSelected(text);
        }));

        if (inOutModel.outputVariable != null) {
        	resultCombo.setText(inOutModel.outputVariable);
        	
        }
    }    
    
}
