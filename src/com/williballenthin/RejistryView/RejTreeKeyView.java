package com.williballenthin.RejistryView;

import com.williballenthin.rejistry.RegistryParseException;
import com.williballenthin.rejistry.RegistryValue;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

public class RejTreeKeyView extends RejTreeNodeView {
    private final RejTreeKeyNode _node;

    public RejTreeKeyView(RejTreeKeyNode node) {
        super(new BorderLayout());
        this._node = node;

        /**
         * @param 1 Name
         * @param 2 Number of subkeys
         * @param 3 Number of values
         */
        String metadataTemplate = "" +
                "<html>" +
                "<i>Name:</i>  <b>%1$s</b><br/>" +
                "<i>Number of subkeys:</i>   %2$d<br/>" +
                "<i>Number values:</i>  %3$d<br/>" +
                "</html>";
        String keyName;
        int numSubkeys;
        int numValues;

        try {
            keyName = this._node.getKey().getName();
        } catch (UnsupportedEncodingException e) {
            keyName = "FAILED TO PARSE KEY NAME";
        }

        try {
            numSubkeys = this._node.getKey().getSubkeyList().size();
        } catch (RegistryParseException e) {
            numSubkeys = -1;
        }

        try {
            numValues = this._node.getKey().getValueList().size();
        } catch (RegistryParseException e) {
            numValues = -1;
        }

        JLabel metadataLabel = new JLabel(String.format(metadataTemplate, keyName, numSubkeys, numValues), JLabel.LEFT);
        metadataLabel.setBorder(BorderFactory.createTitledBorder("Metadata"));
        metadataLabel.setVerticalAlignment(SwingConstants.TOP);

        String[] columnNames = {"Name", "Type", "Value"};
        Object[][] data = new Object[numValues][3];

        try {
            Iterator<RegistryValue> valit = this._node.getKey().getValueList().iterator();
            int i = 0;
            while (valit.hasNext()) {
                RegistryValue val = valit.next();
                if (val.getName().length() == 0) {
                    data[i][0] = "(Default)";
                } else {
                    data[i][0] = val.getName();
                }
                data[i][1] = val.getValueType().toString();
                data[i][2] = RegeditExeValueFormatter.format(val.getValue());
                i++;
            }
        } catch (RegistryParseException e) {
            // TODO(wb): need to add some warning here...
            // not sure how to do it, though, since some data may have already been added
            // but not necessarily all of it
        }
        catch (UnsupportedEncodingException e) {
            // TODO(wb): need to add some warning here...
        }

        JTable table = new JTable(data, columnNames);
        table.setAutoCreateRowSorter(true);
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(true);
        table.setIntercellSpacing(new Dimension(10, 1));

        // inspiration for packing the columns from:
        //   http://jroller.com/santhosh/entry/packing_jtable_columns
        if (table.getColumnCount() > 0) {
            int width[] = new int[table.getColumnCount()];
            int total = 0;
            for (int j = 0; j < width.length; j++) {
                TableColumn column = table.getColumnModel().getColumn(j);
                int w = (int)table.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(table, column.getIdentifier(), false, false, -1, j).getPreferredSize().getWidth();

                if (table.getRowCount() > 0) {
                    for (int i = 0; i < table.getRowCount(); i++) {
                        int pw = (int)table.getCellRenderer(i, j).getTableCellRendererComponent(table, table.getValueAt(i, j), false, false, i, j).getPreferredSize().getWidth();
                        w = Math.max(w, pw);
                    }
                }
                width[j] += w + table.getIntercellSpacing().width;
                total += w + table.getIntercellSpacing().width;
            }
            width[width.length - 1] += table.getVisibleRect().width - total;
            TableColumnModel columnModel = table.getColumnModel();
            for (int j = 0; j < width.length; j++) {
                TableColumn column = columnModel.getColumn(j);
                table.getTableHeader().setResizingColumn(column);
                column.setWidth(width[j]);
            }
        }

        JScrollPane valuesPane = new JScrollPane(table);
        valuesPane.setBorder(BorderFactory.createTitledBorder("Values"));

        this.add(metadataLabel, BorderLayout.NORTH);
        this.add(valuesPane, BorderLayout.CENTER);
    }
}
