package org.egzi.model;

import org.egzi.algo.GenType;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class CustomTableModel extends AbstractTableModel {

    private java.util.List<VectorConfig> vectorConfigs = new ArrayList<VectorConfig>();

    private static final String[] columns = new String[]{
            "Name", "Distribution Type", "Low", "Up", "Input Error Type", "Input Error Low Edge", "Input Error Up Edge", "Clear value"};

    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public int getRowCount() {
        return vectorConfigs.size();
    }

    @Override
    public int getColumnCount() {
        return 8;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        VectorConfig vectorConfig = vectorConfigs.get(rowIndex);
        switch (columnIndex) {
            case 0: return vectorConfig.getName();
            case 1: return vectorConfig.getDistributionType();
            case 2: return vectorConfig.getLowEdge();
            case 3: return vectorConfig.getUpEdge();
            case 4: return vectorConfig.getInputErrorType();
            case 5: return vectorConfig.getInputErrorLow();
            case 6: return vectorConfig.getInputErrorUp();
            case 7: return vectorConfig.getU() != null ? vectorConfig.getU().toString() : "";
            default: return null;
        }
    }


    public boolean isCellEditable(int row, int col) {
        return true;
    }

    public void setValueAt(Object value, int row, int col) {
        VectorConfig vectorConfig = vectorConfigs.get(row);
        switch (col) {
            case 0:
                vectorConfig.setName(value.toString());
                break;
            case 1:
                vectorConfig.setDistributionType(GenType.valueOf(value.toString()));
                break;
            case 2:
                vectorConfig.setLowEdge(Double.valueOf(value.toString()));
                break;
            case 3:
                vectorConfig.setUpEdge(Double.valueOf(value.toString()));
                break;
            case 4:
                vectorConfig.setInputErrorType(GenType.valueOf(value.toString()));
                break;
            case 5:
                vectorConfig.setInputErrorLow(Double.valueOf(value.toString()));
                break;
            case 6:
                vectorConfig.setInputErrorUp(Double.valueOf(value.toString()));
                break;
            case 7:
                vectorConfig.setU(value.toString());
                break;
        }
        fireTableCellUpdated(row, col);
    }

    public void setVectorConfigs(List<VectorConfig> vectorConfigs) {
        this.vectorConfigs = vectorConfigs;
        fireTableDataChanged();
    }

    public void newRow(){
        vectorConfigs.add(new VectorConfig());
        fireTableDataChanged();
    }

    public void removeRow(int row){
        if (row != -1) {
            vectorConfigs.remove(row);
            fireTableDataChanged();
        }
    }

    public List<VectorConfig> getVectorConfigs() {
        return vectorConfigs;
    }
}
