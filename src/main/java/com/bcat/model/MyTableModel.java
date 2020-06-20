package com.bcat.model;

import com.bcat.dao.RmsDao;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class MyTableModel extends AbstractTableModel {

    private String[] columnNames = {"ID", "项目(业务)名称", "子名称", "详情", "级别","时间","状态","链接"};

    public List<String[]> list;

    public MyTableModel(){
        getTotal();
    }

       @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String[] row =  list.get(rowIndex);
        return row[columnIndex];
    }

    public void getTotal(){
        list = new RmsDao().getTotal();
    }

}
