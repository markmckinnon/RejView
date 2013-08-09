import com.williballenthin.RejistryView.RejView;
import com.williballenthin.rejistry.RegistryHiveFile;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class RejViewTest {
    public static void main(String[] args) {
        final String hivePath = args[0];
        final File hiveFile = new File(hivePath);
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
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                try {
                    JComponent pane = new RejView(new RegistryHiveFile(hiveFile));
                    frame.setContentPane(pane);
                    frame.pack();
                    frame.setVisible(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
