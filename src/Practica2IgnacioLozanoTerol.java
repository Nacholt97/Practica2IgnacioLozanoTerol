
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Practica2IgnacioLozanoTerol extends JFrame implements MouseListener, ItemListener, ActionListener {
    CardLayout tarjetas;
    File directorio,archivo;
    JTextArea rutafinal;
    JLabel[] labels = new JLabel[4];
    String[] nombres = new String[]{"Nombre", "Apellidos", "Email", "Contraseña"};
    JTextField[] textos = new JTextField[4];
    JEditorPane primeraTarjeta;
    JPanel segundaTarjeta,cuartaTarjeta,tercercaTarjeta,quintaTarjeta,panelTarjetas,panelBoton;
    JButton comenzar,seleccionRuta,siguienteTarjeta,anteriorTarjeta,salir;
    JComboBox pais,provincia;
    JPasswordField contrasena;
    JLabel[] resultados = new JLabel[5];
    JCheckBox volcarDatos;
    JFileChooser ruta;
    boolean valido;

    public Practica2IgnacioLozanoTerol() {

        Font fuenteBotones = new Font("italic", 0, 15);
        final Font fuenteResultados = new Font("Calibri", 0, 30);

        //Aquí nos declaramos un panel para los botones de siguiente y anterior, Border Layout para pegarlo a la parte inferior

        this.setLayout(new BorderLayout());
        this.panelBoton = new JPanel();
        this.panelBoton.setLayout(new FlowLayout(2, 20, 20));
        this.panelBoton.setVisible(false);

        //El botón de siguiente tarjeta que añadimos al panelBoton

        this.siguienteTarjeta = new JButton("Siguiente  \ud83e\udc72");
        this.siguienteTarjeta.setFont(fuenteBotones);
        this.siguienteTarjeta.setBounds(10, 10, 20, 30);
        this.siguienteTarjeta.setBackground(new Color(252, 252, 141, 255));
        this.siguienteTarjeta.setFocusable(false);
        this.siguienteTarjeta.addMouseListener(this);
        this.siguienteTarjeta.addActionListener(new ActionListener() {

            //Aquí tenemos lo más importante el momento en el que se hace click en el botón siguinete.

            public void actionPerformed(ActionEvent event) {


                //En el que caso de que la Segunda tarjeta sea visible, (los campos con el nombre, etc) tendrá unas validaciones

                if (segundaTarjeta.isShowing()) {


                    //Creamos dos patterns para el correo y la contraseña. El pattern del email lo más importante saber es que
                    //obliga a poner un '@' y un '.' y al final mínimo dos caracteres después del punto.
                    //El pattern de la contraseña dice que mínimo un número, una letra may, una letra min, un carácter especial y entre 8 y 16.

                    String mensajeError = "";
                    Pattern patronEmail = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
                    Matcher coincideEmail = patronEmail.matcher(textos[2].getText());
                    Pattern patronContr = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=*]).{8,16}$");
                    Matcher coincideContr = patronContr.matcher(contrasena.getText());

                    //Nos hacemos dos Matcher para ver si coinciden los campos introducidos, en el caso de que no coincidan
                    //se pondrá el color de fondo rojo y añadiremos al mensaje de error el mensaje que el campo está mal.
                    //En caso contrario volvemos a cambiar el color de los fondos

                    if (!coincideEmail.find()) {
                        mensajeError = mensajeError + " Mail no valido\n";
                        textos[2].setBackground(new Color(255, 190, 190, 255));
                        textos[2].setBorder(new LineBorder(new Color(255, 70, 70, 255)));
                    } else {
                        textos[2].setBackground(Color.white);
                        textos[2].setBorder(new LineBorder(Color.black));
                    }

                    if (!coincideContr.find()) {
                        mensajeError = mensajeError + " Contraseña no valida\n";
                        contrasena.setBackground(new Color(255, 190, 190, 255));
                        contrasena.setBorder(new LineBorder(new Color(255, 70, 70, 255)));
                    } else {
                        contrasena.setBackground(Color.white);
                        contrasena.setBorder(new LineBorder(Color.black));
                    }

                    if (textos[0].getText().isEmpty()) {
                        mensajeError = mensajeError + " Nombre no valido\n";
                        textos[0].setBackground(new Color(255, 190, 190, 255));
                        textos[0].setBorder(new LineBorder(new Color(255, 70, 70, 255)));
                    } else {
                        textos[0].setBackground(Color.white);
                        textos[0].setBorder(new LineBorder(Color.black));
                    }

                    if (textos[1].getText().isEmpty()) {
                        mensajeError = mensajeError + " Apellidos no validos\n";
                        textos[1].setBackground(new Color(255, 190, 190, 255));
                        textos[1].setBorder(new LineBorder(new Color(255, 70, 70, 255)));
                    } else {
                        textos[1].setBackground(Color.white);
                        textos[1].setBorder(new LineBorder(Color.black));
                    }

                    //En el caso de que el mensaje de error tenga contenido, lo mostrara y no dejará pasar de tarjeta,
                    //en caso contrario activa boolean para pasar de tarjeta

                    if (!mensajeError.equals("")) {
                        JOptionPane.showMessageDialog((Component) null, mensajeError);
                        valido = false;
                    } else {
                        valido = true;
                    }
                }


                //En el caso de hacer click cuando se vea la tercera tarjeta (elección de país) haremos las siguientes cosas

                if (tercercaTarjeta.isShowing()) {

                    //Por si el usuario se mueve aleatoriamente por el formulario reinicio los labels donde va a estar la información
                    //para que no se creen mas labels

                    for (int ix = 0; ix < resultados.length; ++ix) {
                        resultados[ix].setText("");
                    }

                    String[] strings = new String[]{textos[0].getText(), textos[1].getText(), textos[2].getText(), (String) pais.getSelectedItem(), (String) provincia.getSelectedItem()};

                    //Aquí coloco los labels donde quiera al poner el Layout en null

                    for (int i = 0; i < 3; ++i) {
                        JLabel var10000 = resultados[i];
                        String var10001 = nombres[i];
                        var10000.setText(var10001 + ": " + strings[i]);
                        resultados[i].setBounds(50, 10 + i * 40, 400, 40);
                        cuartaTarjeta.add(resultados[i]);
                    }

                    //Aquí dependiendo de lo que haya seleccionado el usuario (España o EEUU) cambio provincia o país

                    resultados[3].setText("País: " + strings[3]);
                    cuartaTarjeta.add(resultados[3]);
                    resultados[3].setBounds(50, 130, 400, 40);
                    if (pais.getSelectedItem() == "España") {
                        resultados[4].setText("Provincia: " + strings[4]);
                        cuartaTarjeta.add(resultados[4]);
                    } else {
                        resultados[4].setText("Estado: " + strings[4]);
                        cuartaTarjeta.add(resultados[4]);
                    }

                    //Aquí el check box con el botón para elegir ruta

                    resultados[4].setBounds(50, 170, 400, 40);
                    volcarDatos = new JCheckBox();
                    volcarDatos.setBounds(523, 175, 20, 20);
                    volcarDatos.setBackground(new Color(252, 252, 141, 255));
                    seleccionRuta = new JButton("Seleccionar ruta");
                    seleccionRuta.setBounds(563, 170, 200, 30);
                    seleccionRuta.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            JOptionPane.showMessageDialog(cuartaTarjeta, "Si selecciona un archivo ya creado se sobreescribirá");
                            ruta = new JFileChooser();
                            ruta.showSaveDialog((Component) null);
                            ruta.setFileSelectionMode(2);
                            directorio = ruta.getSelectedFile();
                            System.out.println(directorio);
                            archivo = new File(String.valueOf(directorio));
                            FileWriter fw = null;

                            //Veo si el archivo ya existe o no para avisar al usuario si a sobreescerito el fichero o ha creado uno nuevo

                            try {
                                if (archivo.exists()) {
                                    JOptionPane.showMessageDialog(cuartaTarjeta, "Ha sobreescrito el archivo");

                                }

                                else {
                                    JOptionPane.showMessageDialog(cuartaTarjeta, "Archivo creado");
                                }

                                //BufferedWritter para escribir en el txt con la ruta seleccionada por el ususario

                                archivo.createNewFile();
                                fw = new FileWriter(archivo);
                                BufferedWriter bw = new BufferedWriter(fw);

                                for (int i = 0; i < resultados.length; ++i) {
                                    bw.write(resultados[i].getText());
                                    bw.newLine();
                                }

                                bw.close();
                            } catch (IOException var6) {
                            }

                        }
                    });
                    seleccionRuta.setEnabled(false);
                    volcarDatos.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            if (volcarDatos.isSelected()) {
                                seleccionRuta.setEnabled(true);
                            }

                            if (!volcarDatos.isSelected()) {
                                seleccionRuta.setEnabled(false);
                            }

                        }
                    });
                    cuartaTarjeta.add(volcarDatos);
                    cuartaTarjeta.add(seleccionRuta);
                }


                //En la última tarjeta veo si el fichero se ha creado bien y muestro un mensaje u otro

                if (cuartaTarjeta.isShowing()) {

                    rutafinal.setText("");
                    rutafinal.setEditable(false);
                    rutafinal.setFont(fuenteResultados);
                    rutafinal.setBackground(new Color(253, 253, 172, 255));

                    rutafinal.setBounds(30, 20, 1000,120);
                    String campos[];

                    //Hago split a la ruta y selecciono el último valor y veo si contiene un .txt, en caso de que no lo tenga
                    //el fichero se ha creado mal y no le dejo acabar el formulario, le digo que vuelva y elija bien el fichero


                    campos = String.valueOf(directorio).split("\\\\");

                    salir = new JButton("Salir");
                    salir.setVisible(false);
                    salir.setBounds(340,150,100,30);
                    salir.setBackground(new Color(252, 252, 141, 255));
                    salir.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            System.exit(0);

                        }
                    });

                    if (!campos[campos.length-1].contains(".txt")) {

                        rutafinal.setText("\nEl archivo no se ha creado correctamente, \n" +
                                "por favor vuelva a la pestaña anterior e intentelo otra vez");

                    } else {

                        rutafinal.setText("¡Gracias por registrarte!\n" +
                                "El archivo se ha creado en el directorio: \n"+ String.valueOf(directorio));
                        salir.setVisible(true);

                    }

                    siguienteTarjeta.setVisible(false);
                    quintaTarjeta.add(rutafinal);
                    quintaTarjeta.add(salir);


                }

                if (valido && !quintaTarjeta.isShowing()) {
                    tarjetas.next(panelTarjetas);
                }

            }
        });

        //Aquí me creo el botón de anterior tarjeta que simplemente va hacia la anterior

        this.anteriorTarjeta = new JButton("\ud83e\udc70  Anterior");
        this.anteriorTarjeta.setFont(fuenteBotones);
        this.anteriorTarjeta.setBounds(100, 100, 20, 30);
        this.anteriorTarjeta.setBackground(new Color(252, 252, 141, 255));
        this.anteriorTarjeta.setFocusable(false);
        this.anteriorTarjeta.addMouseListener(this);
        this.anteriorTarjeta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (segundaTarjeta.isShowing()) {
                    tarjetas.show(panelTarjetas, "Bienvenida");
                    panelBoton.setVisible(false);
                    comenzar.setVisible(true);
                }
                if (quintaTarjeta.isShowing()) {

                    siguienteTarjeta.setVisible(true);

                }

                if (!primeraTarjeta.isShowing()) {
                    tarjetas.previous(panelTarjetas);
                }




            }
        });

        //Añado los dos botones al panel

        this.panelBoton.add(this.anteriorTarjeta);
        this.panelBoton.add(this.siguienteTarjeta);
        this.panelBoton.setBackground(new Color(253, 253, 172, 255));
        this.panelBoton.setBorder(new LineBorder(Color.white));
        this.add(this.panelBoton, "South");
        this.tarjetas = new CardLayout();
        this.panelTarjetas = new JPanel();
        this.panelTarjetas.setLayout(this.tarjetas);

        //Me creo la primera tarjeta de bienvenida con las instrucciones del formulario

        this.primeraTarjeta = new JEditorPane();
        this.primeraTarjeta.setEditable(false);
        this.primeraTarjeta.setBackground(new Color(253, 253, 172, 255));
        this.primeraTarjeta.setBorder(new LineBorder(Color.white));
        this.primeraTarjeta.setContentType("text/html");
        this.primeraTarjeta.setText("<style>        body {            text-align: center;}\n    </style></head><body> " +
                "   <h1>\n¡Bienvenido!</h1><h2>En este formulario encontrarás cuatro pestañas</h2>" +
                "<p>En la primera pestaña tendrás que rellenar los datos, No puedes dejar campos vacíos, tiene que ser un email valido\n" +
                "y la contraseña de 8 a 16 caracteres, una mayúscula una minúscula y un número.</p><p>En la segunda pestaña " +
                "tendrás dos cajas, la primera para seleccionar el país y la segunda para seleccionar la provincia/Estado, dependiendo " +
                "de la elección del país.</p><p>En la tercera pestaña estarán todos tus datos, confirma que estén bien y selecciona un directorio donde " +
                "guardarlos.</p><p>En la ultima pestaña verás si se ha guardado bien el archivo o no, en casi negativo vuelva para " +
                "guardarlo bien.</p></body>");

        //Añado el logo al formulario

        JLabel foto = new JLabel();
        foto.setBounds(10, 10, 100, 100);
        Image img = (new ImageIcon(this.getClass().getResource("/logo.png"))).getImage();
        Image newimg = img.getScaledInstance(foto.getWidth(), foto.getHeight(), 4);
        ImageIcon icon = new ImageIcon(newimg);
        foto.setIcon(icon);
        foto.setVisible(true);
        this.primeraTarjeta.add(foto);
        this.comenzar = new JButton("Comenzar");
        this.comenzar.setBounds(640, 45, 100, 30);
        this.comenzar.setBackground(new Color(252, 252, 141, 255));
        this.comenzar.addMouseListener(this);

        //En el botón comenzar inicializo varías variables

        this.comenzar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tarjetas.show(panelTarjetas, "Datos");
                comenzar.setVisible(false);
                panelBoton.setVisible(true);

                rutafinal = new JTextArea();


                for (int i = 0; i < resultados.length; ++i) {
                    resultados[i] = new JLabel();
                    resultados[i].setFont(fuenteResultados);
                }

            }
        });


        //Aquí creo la segunda tarjeta donde coloco los labels y textos

        this.primeraTarjeta.add(this.comenzar);
        this.segundaTarjeta = new JPanel();
        this.segundaTarjeta.setBackground(new Color(253, 253, 172, 255));
        this.segundaTarjeta.setLayout((LayoutManager) null);

        for (int i = 0; i < this.labels.length; ++i) {
            int j = 0;
            if (i != 3) {
                if (i == 2) {
                    j = 2;
                }

                this.labels[i] = new JLabel(this.nombres[i]);
                this.labels[i].setBounds(30 + 370 * (i - j), 40 + j * 40, 120, 30);
                this.segundaTarjeta.add(this.labels[i]);
                this.textos[i] = new JTextField();
                this.textos[i].setPreferredSize(new Dimension(260, 30));
                this.textos[i].setBounds(90 + 390 * (i - j), 40 + j * 40, 230, 30);
                this.textos[i].setBorder(new LineBorder(Color.black));
                this.segundaTarjeta.add(this.textos[i]);
            } else {
                j = 2;
                this.labels[i] = new JLabel(this.nombres[i]);
                this.labels[i].setBounds(30 + 370 * (i - j), 40 + j * 40, 120, 30);
                this.segundaTarjeta.add(this.labels[i]);
                this.contrasena = new JPasswordField();
                this.contrasena.setPreferredSize(new Dimension(260, 30));
                this.contrasena.setBounds(90 + 390 * (i - j), 40 + j * 40, 230, 30);
                this.contrasena.setBorder(new LineBorder(Color.black));
                this.segundaTarjeta.add(this.contrasena);
            }
        }





        this.tercercaTarjeta = new JPanel();
        this.tercercaTarjeta.setLayout(new GridBagLayout());
        this.tercercaTarjeta.setBackground(new Color(253, 253, 172, 255));

        //En init pais tenemos la inicialización de las dos cajas con el pais y las provincias o estados

        this.initPais();


        //La cuarta tarjeta la inicializo en el boton siguiente tarjeta como ya hemos visto arriba

        this.cuartaTarjeta = new JPanel();
        this.cuartaTarjeta.setLayout((LayoutManager) null);
        this.resultados = new JLabel[5];
        this.cuartaTarjeta.setBackground(new Color(253, 253, 172, 255));

        //La quinta tarjeta igual, al depender de la anterior tarjeta


        this.quintaTarjeta = new JPanel();
        this.quintaTarjeta.setBackground(new Color(253, 253, 172, 255));
        this.quintaTarjeta.setLayout(null);

        //Añado todas las tarjetas al grupo de tarjetas

        this.panelTarjetas.add(this.primeraTarjeta, "Bienvenida");
        this.panelTarjetas.add(this.segundaTarjeta, "Datos");
        this.panelTarjetas.add(this.tercercaTarjeta, "Pais");
        this.panelTarjetas.add(this.cuartaTarjeta, "Resultado");
        this.panelTarjetas.add(this.quintaTarjeta, "Completado");
        this.tarjetas.show(this.panelTarjetas, "Bienvenida");
        this.add(this.panelTarjetas, "Center");
        this.initPantalla();
    }


    private void initPais() {
        String a = "España";
        String b = "Estados Unidos";
        this.pais = new JComboBox();
        this.pais.addItemListener(this);
        this.pais.setBackground(Color.white);
        this.tercercaTarjeta.add(this.pais);
        this.provincia = new JComboBox();
        this.provincia.addItemListener(this);
        this.provincia.setBackground(Color.white);
        this.tercercaTarjeta.add(this.provincia);
        this.pais.addItem(a);
        this.pais.addItem(b);
        this.provincia.addItem(".");
    }

    //Inicializo pantalla

    private void initPantalla() {
        this.setTitle("Practica2IgnacioLozanoTerol");
        this.setSize(800, 330);
        this.setDefaultCloseOperation(3);
        this.setLocationRelativeTo((Component) null);
        this.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        new Practica2IgnacioLozanoTerol();
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    //Hovers para hacer más visual el formulario. Hover de entrada

    public void mouseEntered(MouseEvent e) {
        if (e.getSource() == this.siguienteTarjeta) {
            this.siguienteTarjeta.setBackground(Color.white);
        }

        if (e.getSource() == this.anteriorTarjeta) {
            this.anteriorTarjeta.setBackground(Color.white);
        }

        if (e.getSource() == this.comenzar) {
            this.comenzar.setBackground(Color.white);
        }


    }

    //Hover de salida

    public void mouseExited(MouseEvent e) {
        if (e.getSource() == this.siguienteTarjeta) {
            this.siguienteTarjeta.setBackground(new Color(252, 252, 141, 255));
        }

        if (e.getSource() == this.anteriorTarjeta) {
            this.anteriorTarjeta.setBackground(new Color(252, 252, 141, 255));
        }

        if (e.getSource() == this.comenzar) {
            this.comenzar.setBackground(new Color(252, 252, 141, 255));
        }


    }

    //Dependiendo del campo seleccionado en país tendremos otros campos en la segunda caja, este método limpia todos los campos
    //y vuelve a leer el fichero correspondiente añadiendo el contenido a la caja

    private void cambiarProvincias(String pais2) throws IOException {
        this.provincia.removeAllItems();
        FileReader fr;
        if (pais2 == "España") {
            fr = new FileReader("C:\\Users\\nacho\\IdeaProjects\\Practica2IgnacioLozanoTerol\\src\\provincias.txt");
        } else {
            fr = new FileReader("C:\\Users\\nacho\\IdeaProjects\\Practica2IgnacioLozanoTerol\\src\\estados.txt");
        }

        //Leo los archivos con BufferedReader

        BufferedReader br = new BufferedReader(fr);

        String linea;
        while ((linea = br.readLine()) != null) {
            this.provincia.addItem(linea);
        }

        br.close();
    }


    //En el itemChanged es donde llamo al metodo cambiar provincias, que es cuando cambia el pais de España a EEUU

    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == this.pais) {
            try {
                this.cambiarProvincias((String) this.pais.getSelectedItem());
            } catch (IOException var3) {
                throw new RuntimeException(var3);
            }
        }

    }

    public void actionPerformed(ActionEvent e) {
    }

}
