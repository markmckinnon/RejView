package com.williballenthin.RejistryView;

import com.williballenthin.rejistry.HexDump;
import com.williballenthin.rejistry.RegistryParseException;
import com.williballenthin.rejistry.RegistryValue;
import com.williballenthin.rejistry.ValueData;

import javax.swing.*;
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
                data[i][0] = val.getName();
                data[i][1] = val.getValueType().toString();


                ValueData valdata = val.getValue();
                String valString = "";
                switch(valdata.getValueType()) {
                    case REG_SZ:
                    case REG_EXPAND_SZ:
                        valString = valdata.getAsString();
                        break;
                    case REG_MULTI_SZ: {
                        StringBuilder sb = new StringBuilder();
                        Iterator<String> it = valdata.getAsStringList().iterator();
                        while (it.hasNext()) {
                            sb.append(it.next());
                            if (it.hasNext()) {
                                sb.append(", ");
                            }
                        }
                        valString = sb.toString();
                        break;
                    }
                    case REG_DWORD:
                    case REG_QWORD:
                    case REG_BIG_ENDIAN:
                        valString = String.format("0x%x", valdata.getAsNumber());
                        break;
                    default: {
                        valString = HexDump.toHexString(valdata.getAsRawData().array());
                    }
                }

                data[i][2] = valString;
                i++;
            }
        } catch (RegistryParseException e) { }
          catch (UnsupportedEncodingException e) {  }

        JTable table = new JTable(data, columnNames);
        table.setAutoCreateRowSorter(true);
        table.setCellSelectionEnabled(false);
        JScrollPane valuesPane = new JScrollPane(table);
        valuesPane.setBorder(BorderFactory.createTitledBorder("Values"));

        this.add(metadataLabel, BorderLayout.NORTH);
        this.add(valuesPane, BorderLayout.CENTER);
    }
}
