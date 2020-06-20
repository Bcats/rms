package com.bcat.renderer;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LinkCellRenderer extends DefaultTableCellRenderer implements MouseInputListener {
    //鼠标事件所在的行
    private int row = -1;
    //鼠标事件所在的列
    private int col = -1;
    //当前监听的Table
    private JTable table = null;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //恢复默认状态
        this.table = table;
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        this.setForeground(Color.black);
        table.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        this.setText(value.toString());
        //如果当前需要渲染器的单元格就是鼠标事件所在的单元格
        if (row == this.row && column == this.col) {
            //如果是第二列(第二列是显示超链接的列)
            if (column == 7) {
                //改变前景色(文字颜色)
                this.setForeground(Color.blue);
                //改变鼠标形状
                table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                //显示超链接样式
                this.setText("<html><u>" + value.toString() + "</u></html>");
            }
            setBackground(table.getSelectionBackground());
        } else if (isSelected) {
            //如果单元格被选中,则改变前景色和背景色
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            //其他情况下恢复默认背景色
            setBackground(Color.white);
        }
        return this;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //获取事件所在的行列坐标信息
        Point p = e.getPoint();
        int c = table.columnAtPoint(p);
        if(c != 7){
            return;
        }
        int r = table.rowAtPoint(p);
        try {
            Object value = table.getValueAt(r, c);
            //取得目标单元格的值,即链接信息
            URL url = new URL(value.toString());
            //在系统默认浏览器中打开链接
            Desktop.getDesktop().browse(url.toURI());
        } catch (Exception ex) {
            Logger.getLogger(LinkCellRenderer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (table != null) {
            int oldRow = row;
            int oldCol = col;
            //鼠标移出目标表格后,恢复行列数据到默认值
            row = -1;
            col = -1;
            //当之前的行列数据有效时重画相关区域
            if (oldRow != -1 && oldCol != -1) {
                Rectangle rect = table.getCellRect(oldRow, oldCol, false);
                table.repaint(rect);
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (table != null) {
            Point p = e.getPoint();
            int oldRow = row;
            int oldCol = col;
            row = table.rowAtPoint(p);
            col = table.columnAtPoint(p);
            //重画原来的区域
            if (oldRow != -1 && oldCol != -1) {
                Rectangle rect = table.getCellRect(oldRow, oldCol, false);
                table.repaint(rect);
            }
            //重画新的区域
            if (row != -1 && col != -1) {
                Rectangle rect = table.getCellRect(row, col, false);
                table.repaint(rect);
            }
        }

    }
}
