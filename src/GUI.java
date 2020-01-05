import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GUI {
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static int rows = 10;
	private static int cols = 20;
	private String file = "programa.txt";
	private String[] metas = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
	private String cadena = "";
	private JFrame frame;
	private JLabel commandsTitle, archivoTitle, numMetas;
	private JComboBox<String> selectMetas;
	private Canvas canvas;
	private JTextArea commands;
	private JTextField archivo;
	private JScrollPane scrollpane;
	private JButton iniciar, resetGame, getText;
	
	public GUI() {
		frame = new JFrame("Proyecto");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		GridBagConstraints gbc = new GridBagConstraints();
		frame.setSize(new Dimension(WIDTH, HEIGHT));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLayout(new GridBagLayout());
		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
		
		canvas = new Canvas(this);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.6;
		gbc.weighty = 1.0;
		gbc.gridheight = 7;
		gbc.gridwidth = 3;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.BOTH;
		frame.add(canvas, gbc);
		
		commandsTitle = new JLabel("Comandos a ejecutar");
		commandsTitle.setFont(new Font("Arial", 1, 20));
		commandsTitle.setHorizontalAlignment(JLabel.CENTER);
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.weightx = 0.4;
		gbc.weighty = 0.2;
		gbc.gridheight = 1;
		gbc.gridwidth = 2;
		frame.add(commandsTitle, gbc);
		
		commands = new JTextArea(rows, cols);
		scrollpane = new JScrollPane(commands);
		gbc.gridx = 3;
		gbc.gridy = 1;
		gbc.weighty = 0.4;
		gbc.gridheight = 2;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		frame.add(scrollpane, gbc);
		
		numMetas = new JLabel("Seleccionar nivel");
		numMetas.setFont(new Font("Arial", 1, 18));
		gbc.gridx = 3;
		gbc.gridy = 3;
		gbc.weightx = 0.4;
		gbc.weighty = 0.2;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		frame.add(numMetas, gbc);
		
		selectMetas = new JComboBox<String>(metas);
		selectMetas.setSelectedIndex(5);
		selectMetas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                resetear(selectMetas.getSelectedIndex()+1);
            }
        });
		gbc.gridx = 4;
		gbc.gridy = 3;
		gbc.weightx = 0.4;
		gbc.weighty = 0.2;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		frame.add(selectMetas, gbc);
		
		iniciar = new JButton("INICIAR");
		iniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ejecutar();
			}
		});
		gbc.gridx = 3;
		gbc.gridy = 4;
		gbc.weighty = 0.2;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		frame.add(iniciar, gbc);
		
		resetGame = new JButton("NUEVO JUEGO");
		resetGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetear(selectMetas.getSelectedIndex()+1);
			}
		});
		gbc.gridx = 4;
		gbc.gridy = 4;
		gbc.weighty = 0.2;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;
		frame.add(resetGame, gbc);
		
		archivoTitle = new JLabel("Importar texto de archivo local");
		archivoTitle.setFont(new Font("Arial", 1, 18));
		archivoTitle.setHorizontalAlignment(JLabel.CENTER);
		gbc.gridx = 3;
		gbc.gridy = 5;
		gbc.weightx = 0.4;
		gbc.weighty = 0.2;
		gbc.gridheight = 1;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		frame.add(archivoTitle, gbc);
		
		archivo = new JTextField();
		archivo.setPreferredSize(new Dimension(200, 30));
		archivo.setMaximumSize(archivo.getPreferredSize());
		archivo.setText(file);
		gbc.gridx = 3;
		gbc.gridy = 6;
		gbc.weighty = 0.2;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		frame.add(archivo, gbc);
		
		getText = new JButton("IMPORTAR TEXTO");
		getText.addActionListener(new ActionListener() {
		  public void actionPerformed(ActionEvent e) {
			  file = archivo.getText();
			  try {
				commands.setText(readFile(file));
			} catch (IOException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(frame, "No se pudo leer el archivo!", "ERROR", JOptionPane.WARNING_MESSAGE);
			}
		  }
		});
		gbc.gridx = 4;
		gbc.gridy = 6;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		frame.add(getText, gbc);
		frame.pack();
	}
	public void showGUI() {
		frame.setVisible(true);
	}
	public void ejecutar() {
		cadena = commands.getText().toLowerCase();
		iniciar.setEnabled(false);
		resetGame.setEnabled(false);
		PseudoParser parser = new PseudoParser(this, cadena);
		parser.printTokens();
		parser.programa(); // Generar las tuplas
		parser.interpretar(); // Interpretar las tuplas
	}
	public void resetear(int nivel) {
		canvas.resetGame(nivel);
		iniciar.setEnabled(true);
	}
	String readFile(String fileName) throws IOException {
	    BufferedReader br = new BufferedReader(new FileReader(fileName));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        return sb.toString();
	    } finally {
	        br.close();
	    }
	}	
	
	public JFrame getFrame() {
		return this.frame;
	}
	public Canvas getCanvas() {
		return this.canvas;
	}
	public void setResetEnabled(boolean bool) {
		this.resetGame.setEnabled(bool);
	}
}
