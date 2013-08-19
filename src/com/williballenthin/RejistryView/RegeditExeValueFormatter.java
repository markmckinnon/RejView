package com.williballenthin.RejistryView;

import com.williballenthin.rejistry.HexDump;
import com.williballenthin.rejistry.RegistryParseException;
import com.williballenthin.rejistry.ValueData;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Iterator;

/**
 * Formats a ValueData to a string similar to Regedit.exe on Windows.
 *   For an example, see the nice sample here:
 *   http://raja5.files.wordpress.com/2009/08/wincleanup_regedit.jpg
 */
public class RegeditExeValueFormatter {
    public static String format(ValueData val) throws UnsupportedEncodingException, RegistryParseException {
        StringBuilder sb = new StringBuilder();

        switch(val.getValueType()) {
            case REG_SZ:
            case REG_EXPAND_SZ:{

                String valString = val.getAsString();
                if (valString.length() == 0) {
                    sb.append("(value not set)");
                } else {
                    sb.append(valString);
                }
                if (sb.length() > 48) {
                    sb.setLength(45);
                    sb.append("...");
                }
                break;
            }
            case REG_MULTI_SZ: {
                Iterator<String> it = val.getAsStringList().iterator();
                while (it.hasNext()) {
                    sb.append(it.next());
                    if (it.hasNext()) {
                        sb.append(", ");
                    }
                }
                if (sb.length() > 48) {
                    sb.setLength(45);
                    sb.append("...");
                }
                break;
            }
            case REG_DWORD:
            case REG_BIG_ENDIAN: {
                sb.append(String.format("0x%08x (%d)", val.getAsNumber(), val.getAsNumber()));
                break;
            }
            case REG_QWORD: {
                sb.append(String.format("0x%016x (%d)", val.getAsNumber(), val.getAsNumber()));   // can you even do %016x?
                break;
            }
            default: {
                ByteBuffer valData = val.getAsRawData();
                valData.position(0x0);
                for (int i = 0; i < Math.min(16, valData.limit()); i++) {
                    byte b = valData.get();
                    sb.append(HexDump.toHexString(b));
                    if (i != 15) { // magic number, sorry.
                        sb.append(' ');
                    }
                }
                if (valData.limit() > 16) {
                    sb.append("...");
                }
            }
        }
        return sb.toString();
    }
}
