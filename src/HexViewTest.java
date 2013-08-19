import com.williballenthin.HexView.HexView;
import javax.swing.*;
import java.io.File;
import java.nio.ByteBuffer;

public class HexViewTest {
    public static void main(String[] args) {
        final String hivePath = args[0];
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(
                            UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }

                JFrame frame = new JFrame("RejViewTest");
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


                ByteBuffer buf = ByteBuffer.allocate(256 * 2);
                buf.position(0x0);
                for (int i = 0; i < 255; i++) {
                    System.out.println("" + i);
                    buf.put((byte)i);
                }
                for (int i = 0; i < 255; i++) {
                    System.out.println("" + i);
                    buf.put((byte)i);
                }

                JComponent pane = new HexView(buf);

                frame.setContentPane(pane);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
