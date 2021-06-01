package t1psp;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.JButton;

public class Ventana extends JFrame {
	//Creamos las variables para guardar el PID
	int pidApp;
	int pidPaint;
	int pidBloc;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table; //Creamos la tabla
	private static JTextField textFieldEjecutar;
	ArrayList<String> listaEjecutar = new ArrayList<String>(); //Creamos el array para guardar los comandos utilizados
	static ArrayList<String> procesos = new ArrayList<String>();//Creamos el array para la tabla de tasklist
	
	/**
	 * Create the frame.
	 */
	public Ventana() {
		setTitle("Pr\u00E1ctica T1"); //Creamos el titulo
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		setBounds(100, 100, 770, 477);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		table = new JTable();
		//Aplicamos a la tabla un formato
		table.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"Nombre", "PID"
				}
				) {
			
			private static final long serialVersionUID = 1L;
			//Indicamos que los datos introducidos en la tabla son String
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] {
					String.class, String.class
			};
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			//Indicamos que la columna no sea editable
			boolean[] columnEditables = new boolean[] {
					false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		//Fin de los parametros de formato de la tabla
		// Establecemos pociciones y añadimos al panel 
		table.setBounds(100, 180, 300, 250);
		contentPane.add(table);
		JScrollPane textPan = new JScrollPane(table);
		textPan.setBounds(385, 100, 350, 250);
		contentPane.add(textPan);
		textFieldEjecutar = new JTextField();
		textFieldEjecutar.setBounds(10, 30, 271, 25);
		contentPane.add(textFieldEjecutar);
		textFieldEjecutar.setColumns(10);
		TextArea textArea = new TextArea();
		textArea.setBounds(0, 76, 380, 352);
		contentPane.add(textArea);
		textArea.setEditable(false);
		/*
		 * Boton Ejecutar
		 */
		JButton btnRun = new JButton("Run");
		btnRun.setBounds(290, 30, 90, 20);
		contentPane.add(btnRun);
		//Añadimos el actionListener
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ejecutar = (textFieldEjecutar.getText());//Optenemos el texto del textfield y lo guardamos en una variable
				//Comprobamos que se halla escrito algo.
				if (ejecutar.length() <= 0)
				{
					System.err.println("Introduzca un programa a ejecutar");
				}else {
					try {
						Resultado("cmd + /C " +ejecutar);
						Thread.sleep(200);//Paramos para poder actualizar la tabla.
						//Añadimos al texArea el proceso ejecutado
						textArea.append(ejecutar);
						rellenar2(textArea);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					rellenar(getTable());
					//Rellenamos el textArea
					
				}
			}
		});
		/*
		 * Boton Bloc de Notas
		 */
		JButton btnBlocDeNotas = new JButton("Bloc de notas");
		btnBlocDeNotas.setBounds(400, 30, 150, 20);
		contentPane.add(btnBlocDeNotas);
		btnBlocDeNotas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					ejecutarComandos("notepad");//Ejecutamos el notepad
					try {
						Thread.sleep(200);//Paramos para poder actualizar la tabla.
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					rellenar(getTable());//Rellenamos la tabla de procesos
					//Guardamos el ultimo PID Ejecutado. Mirando el numero de lineas que tiene la table con getRowCount-1.(-1 ya que emplieza la tabla en 0, pero cuenta en 1)
					//Y la segunda columna que es donde tenemos el PDI, como es la ultima podemos usar .getColumnCount-1.(-1 ya que emplieza la tabla en 0, pero cuenta en 1)
					pidBloc = Integer.parseInt((String)(getTable().getModel().getValueAt(getTable().getRowCount()-1, getTable().getColumnCount()-1)));
					btnBlocDeNotas.setEnabled(false);//Desactivamos el boton
			}
		});
		/*
		 * Boton Paint
		 */
		JButton btnPaint = new JButton("Paint");
		btnPaint.setBounds(600, 30, 150, 20);
		contentPane.add(btnPaint);
		btnPaint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					ejecutarComandos("mspaint");//Ejecutamos el paint
					try {
						Thread.sleep(200);//Paramos para poder actualizar la tabla.
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					rellenar(getTable());//Rellenamos la tabla de procesos
					//Guardamos el ultimo PID Ejecutado. Mirando el numero de lineas que tiene la table con getRowCount-1.(-1 ya que emplieza la tabla en 0, pero cuenta en 1)
					//Y la segunda columna que es donde tenemos el PDI, como es la ultima podemos usar .getColumnCount-1.(-1 ya que emplieza la tabla en 0, pero cuenta en 1)
					pidPaint = Integer.parseInt((String)(getTable().getModel().getValueAt(getTable().getRowCount()-1, getTable().getColumnCount()-1)));
					btnPaint.setEnabled(false);				
			}
		});
		/*
		 * Boton App
		 */
		JButton btnApp = new JButton("JAR ejecutable");
		btnApp.setBounds(500, 60, 150, 20);
		contentPane.add(btnApp);
		btnApp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String arg1 = "java";
				String arg2 = "-jar";
				String arg3 = ".\\app.jar";
				String[] ejecutarApp = { arg1, arg2, arg3 };
				ejecutarComandosArray(ejecutarApp);//Ejecutamos el programa 
				try {
					Thread.sleep(200);//Paramos para poder actualizar la tabla.
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				rellenar(getTable());//Rellenamos la tabla de procesos
				//Guardamos el ultimo PID Ejecutado. Mirando el numero de lineas que tiene la table con getRowCount-1.(-1 ya que emplieza la tabla en 0, pero cuenta en 1)
				//Y la segunda columna que es donde tenemos el PDI, como es la ultima podemos usar .getColumnCount-1.(-1 ya que emplieza la tabla en 0, pero cuenta en 1)
				pidApp = Integer.parseInt((String)(getTable().getModel().getValueAt(getTable().getRowCount()-1, getTable().getColumnCount()-1)));
				btnApp.setEnabled(false);
			}
		});
		/*
		 * Boton Terminar
		 */
		JButton btnStop = new JButton("Stop");
		btnStop.setBounds(655, 398, 89, 23);
		contentPane.add(btnStop);
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//optenemos el dato de la linea seleccionada de la tabla en la posicion 1 y la guardamos en una variable
				String pid = (String) getTable().getModel().getValueAt(getTable().getSelectedRow(), 1);
					ejecutarComandos("cmd /C taskkill /F /PID " + pid); //Ejecutamos el comando para eliminar el proceso
					try {
						Thread.sleep(100); //Esperamos a que se elimine el proceso
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					rellenar(getTable());//Rellenamos la tabla de procesos
					//Comprobamos si el proceso eliminado, es alguno de los ejecutados mediante los botones 
					//Si es alguno de los botones lo vuelve a activar
					activarBotones(btnBlocDeNotas, btnPaint, btnApp, pid);
			}
		});
	}
	//Metodos get y set para obtener el pid y activar los botones
	public JTable getTable() {
		return table;
	}
	public void setTable(JTable table) {
		this.table = table;
	}
	
	private void activarBotones(JButton btnBlocDeNotas, JButton btnPaint, JButton btnApp, String pid) {
		if (Integer.parseInt(pid) == pidBloc) {
			btnBlocDeNotas.setEnabled(true);
		}
		else if (Integer.parseInt(pid) == pidPaint) {
			btnPaint.setEnabled(true);
		}
		else if (Integer.parseInt(pid) == pidApp) {
			btnApp.setEnabled(true);
		}
	}
//Metodo que devuelve el resultado de los comandos
	public void Resultado(String comando) {
		listaEjecutar = new ArrayList<String>();
		String linea;
		Process proceso;
		try {
			proceso = Runtime.getRuntime().exec(comando);
			BufferedReader br = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
			while ((linea = br.readLine()) != null) {
				listaEjecutar.add(linea);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//Rellena los datos de la tabla con los datos de la lista creada a traves del comando tasklist
	public static void rellenar(JTable tabla) {
		DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
		procesos = new ArrayList<String>();
		String guardarLinea;
		Process proceso;//Creamos el proceso
		//Parametros usados en el comando tasklist
		//		/FO    	formato Especifica el formato de salida. Valores válidos: "TABLE", "LIST", "CSV".
		//		/NH   	Especifica que el "encabezado de columna" no no debe mostrarse en la salida. Válido sólo para formatos "TABLE" y "CSV".
		//		/FI		Filtro 	TASKLIST /FI "USERNAME ne NT AUTHORITY\SYSTEM" /FI "STATUS eq running"
		String comando="cmd /C tasklist /FO csv /NH /FI \"Status eq running\"";
		try {
			proceso = Runtime.getRuntime().exec(comando);//Ejecutamos el proceso
			//Guardamos el resultado del proceso en un BufferedReader
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
			//Mientras bufferedReader no sea null va guardando en cadenaGuardar la linea de bufferedReader, despues lo añade al array procesos
			while ((guardarLinea = bufferedReader.readLine()) != null) {
				procesos.add(guardarLinea);
			}
			bufferedReader.close();//Cerramos el flujo de entrada de datos
		} catch (IOException e) {
			e.printStackTrace();
		}
		modelo.setNumRows(0);
		//Mientras el tamaño de procesos sea mayor, hacer:
		for (int i = 0; i < procesos.size(); i++) {
			//creamos un array con los datos de la columna 0 y 1
			Object[] fila = { extraerDatos(0, i), extraerDatos(1, i) };
			
			modelo.addRow(fila);
		}
	}
	//Extrae los datos de la lista dando una columna y una linea
		public static String extraerDatos(int columna, int linea) {

			String datos = "";
			//Pedimos la linea del array. Optenemos algo parecido a:
			//"eclipse.exe","18096","Console","11","707.560 KB"
			datos = procesos.get(linea);
			//Separamos los datos por comas y vamos a la posicion columna
			//Si selecionamos posicion 1, optendremos "18096"
			datos = datos.split(",")[columna];
			//Optenemos el substring desde la posicion 1 hasta la posicion datos.length() - 1 opteniendo el dato sin comillas
			datos = datos.substring(1, datos.length() - 1);
			//Devolvemos datos
			return datos;
		}
		//Metodo ejecutado para ejecutar comandos
	@SuppressWarnings("unused")
	public void ejecutarComandos(String comando) {
		Process procesoEjecutarComando;
		try {
			procesoEjecutarComando = Runtime.getRuntime().exec(comando);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//Metodo para ejecutar comandos con entrada array
	@SuppressWarnings("unused")
	public void ejecutarComandosArray(String[] comando) {
		Process procesoEjecutarComando;
		try {
			procesoEjecutarComando = Runtime.getRuntime().exec(comando);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//Metodo para rellenar el textArea
	public void rellenar2(TextArea textArea) {

		for (String cadena : listaEjecutar) {
			textArea.append(cadena + "\n");
		}
		textArea.append("\n");
	}
	
	public static void main(String[] args) throws IOException
	{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ventana frame = new Ventana();
					rellenar(frame.getTable());
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}