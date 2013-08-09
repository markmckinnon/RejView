package com.williballenthin.RejistryView;

import com.williballenthin.rejistry.HexDump;
import com.williballenthin.rejistry.RegistryParseException;
import com.williballenthin.rejistry.ValueData;

import javax.swing.*;
import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Iterator;

public class RejTreeValueView extends RejTreeNodeView {
    private final RejTreeValueNode _node;

    public RejTreeValueView(RejTreeValueNode node) {
        super(new BorderLayout());
        this._node = node;

        /**
         * @param 1 Name
         * @param 2 Type
         */
        String metadataTemplate = "" +
                "<html>" +
                "<i>Name:</i>  <b>%1$s</b><br/>" +
                "<i>Type:</i>   %2$s" +
                "</html>";
        String valueName;
        String valueType;

        /**
         * @param 1 Value
         */
        String valueTemplate = "" +
                "<html>" +
                "%1$s" +
                "</html>";
        String valueValue;
        try {
            valueName = this._node.getValue().getName();
        } catch (UnsupportedEncodingException e) {
            valueName = "FAILED TO DECODE VALUE NAME";
        }

        try {
            valueType = this._node.getValue().getValueType().toString();
        } catch (RegistryParseException e ) {
            valueType = "FAILED TO PARSE VALUE TYPE";
        }

        try {
            String prefix = "";
            ValueData data = this._node.getValue().getValue();
            switch(data.getValueType()) {
                case REG_SZ:
                case REG_EXPAND_SZ:
                    valueValue = data.getAsString();
                    break;
                case REG_MULTI_SZ: {
                    StringBuilder sb = new StringBuilder();
                    Iterator<String> it = data.getAsStringList().iterator();
                    while (it.hasNext()) {
                        sb.append(it.next());
                        sb.append("<br />");
                    }
                    valueValue = sb.toString();
                    break;
                }
                case REG_DWORD:
                case REG_QWORD:
                case REG_BIG_ENDIAN:
                    valueValue = String.format("0x%x", data.getAsNumber());
                    break;
                default: {
                    valueValue = HexDump.dumpHexString(data.getAsRawData()).replace("\n", "<br />");
                }
            }
        } catch (RegistryParseException e) {
            valueValue = "FAILED TO PARSE VALUE VALUE";
        } catch (UnsupportedEncodingException e) {
            valueValue = "FAILED TO PARSE VALUE VALUE";
        }

        JLabel metadataLabel = new JLabel(String.format(metadataTemplate, valueName, valueType), JLabel.LEFT);
        metadataLabel.setBorder(BorderFactory.createTitledBorder("Metadata"));
        metadataLabel.setVerticalAlignment(SwingConstants.TOP);

        JLabel valueLabel = new JLabel(String.format(valueTemplate, valueValue),  JLabel.LEFT);
        valueLabel.setBorder(BorderFactory.createTitledBorder("Value"));
        valueLabel.setVerticalAlignment(SwingConstants.TOP);

        this.add(metadataLabel, BorderLayout.NORTH);
        this.add(valueLabel, BorderLayout.CENTER);
    }
}

