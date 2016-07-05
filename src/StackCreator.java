
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StackCreator {

	public static final int NUM_NOISEY_IMAGES = 10;
	
	private File source;
	private File destRoot;

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		StackCreator converter = new StackCreator();
		StackCreator.Chooser chooser = converter.new Chooser();
		chooser.setVisible(true);
	}

	private void processFiles() throws IOException {
		File[] originalImages = source.listFiles(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				String tmp = name.toLowerCase();
				return tmp.endsWith("jpg") || tmp.endsWith("gif")
						|| tmp.endsWith("bmp") || tmp.endsWith("png");
			}
		});

		for (File original : originalImages) {
			String filename = original.getName();
			filename = filename.substring(0, filename.length() - 4);

			File destDir = new File(destRoot, filename);
			if (!destDir.exists()) {
				destDir.mkdir();
			}
			System.out.println("Processing " + destDir.getCanonicalPath());

			try {
				NoiseProcessor creator = new NoiseProcessor(original, destDir);
				creator.process(NUM_NOISEY_IMAGES);
			} catch (IOException e) {
				System.err.println("Error processing " + original);
				e.printStackTrace(System.err);
			}
		}

		JOptionPane.showMessageDialog(null, "Done.");

	}

	class Chooser extends JFrame implements ActionListener {
		private static final long serialVersionUID = -8389660714995462075L;

		private JTextField sourceDirTF;
		private JTextField destDirTF;
		private JButton sourceButton;
		private JButton destButton;

		public Chooser() throws IOException {
			JPanel panel = new JPanel(new GridBagLayout());
			GridBagConstraints constraints = new GridBagConstraints();

			// source
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.gridx = 0;
			constraints.gridy = 0;
			panel.add(new JLabel("Source: "), constraints);

			source = new File(".");
			sourceDirTF = new JTextField();
			sourceDirTF.setText(source.getCanonicalPath());
			constraints.gridx++;
			panel.add(sourceDirTF, constraints);

			sourceButton = new JButton("Browse");
			sourceButton.addActionListener(this);
			constraints.gridx++;
			panel.add(sourceButton, constraints);

			// destination
			constraints.gridx = 0;
			constraints.gridy++;
			panel.add(new JLabel("Destination: "), constraints);

			destRoot = new File(".");
			destDirTF = new JTextField();
			destDirTF.setText(destRoot.getCanonicalPath());
			constraints.gridx++;
			panel.add(destDirTF, constraints);

			destButton = new JButton("Browse");
			destButton.addActionListener(this);
			constraints.gridx++;
			panel.add(destButton, constraints);

			// process
			JButton processButton = new JButton("Process");
			processButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					
					try {
						processFiles();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			});
			constraints.gridx = 0;
			constraints.gridy++;
			panel.add(processButton, constraints);

			add(panel);

			pack();
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setLocationByPlatform(true);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			File current;
			String title;
			if (e.getSource() == sourceButton) {
				current = source;
				title = "Choose source folder";
			} else {
				current = destRoot;
				title = "Choose destination folder";
			}

			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle(title);
			chooser.setCurrentDirectory(current);
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);

			int result = chooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				File dir = chooser.getSelectedFile();
				try {
					if (e.getSource() == sourceButton) {
						source = dir;
						sourceDirTF.setText(source.getCanonicalPath());
					} else {
						destRoot = dir;
						destDirTF.setText(destRoot.getCanonicalPath());
					}
				} catch (IOException ex) {
				}
			}
		}

	}

}
