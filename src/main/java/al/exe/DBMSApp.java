package al.exe;


import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.io.FileOutputStream;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Element;
import com.itextpdf.text.Document;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class DBMSApp extends JFrame 
{
    private static final Logger LOGGER = Logger.getLogger(DBMSApp.class.getName());
    private static FileHandler fileHandler;
    // Hibernate
    private Session session;
    private Transaction transaction;
    private SessionFactory sessionFactory;
    // Swing gui components
    private JTabbedPane tabbedPane;
    private JTable brandTable, chipsetTable, cpuTable, gpuTable, pcbTable, socketTable;
    private DefaultTableModel brandTableModel, chipsetTableModel, cpuTableModel, gpuTableModel, pcbTableModel, socketTableModel;
    // Buttons
    private JButton addCPUButton, deleteCPUButton, updateCPUButton, pdfExportCPUButton;
    private JButton addGPUButton, deleteGPUButton, updateGPUButton, pdfExportGPUButton;
    private JButton addPCBButton, deletePCBButton, updatePCBButton, pdfExportPCBButton;
    private JButton addBrandButton, deleteBrandButton, updateBrandButton, pdfExportBrandButton;
    private JButton addSocketButton, deleteSocketButton, updateSocketButton, pdfExportSocketButton;
    private JButton addChipsetButton, deleteChipsetButton, updateChipsetButton, pdfExportChipsetButton;
    private JButton pdfExportUnitButton;
    // CPU Fields
    private JTextField cpuModelField;
    private JTextField cpuPriceField;
    private JTextField cpuCoresField;
    private JTextField cpuThreadsField;
    private JTextField cpuFrequencyField;
    private JComboBox<String> cpuBrandComboBox;
    private JComboBox<String> cpuSocketComboBox;
    // GPU Fields
    private JTextField gpuModelField;
    private JTextField gpuPriceField;
    private JTextField gpuCoresField;
    private JTextField gpuMemoryField;
    private JTextField gpuFrequencyField;
    private JComboBox<String> gpuBrandComboBox;
    // PCB Fields
    private JTextField pcbModelField;
    private JTextField pcbPriceField;
    private JComboBox<String> pcbBrandComboBox;
    private JComboBox<String> pcbSocketComboBox;
    private JComboBox<String> pcbChipsetComboBox;
    // Brand Fields
    private JTextField brandNameField;
    // Socket Fields
    private JTextField socketNameField;
    // Chipset Fields
    private JTextField chipsetNameField;
    // Create filter components
    private JComboBox<String> filterCPUComboBox, filterPCBComboBox, filterGPUComboBox;
    private JTextField filterCPUTextField, filterPCBTextField, filterGPUTextField;
    // unit tab
    private JTable unitTable;
    private DefaultTableModel unitTableModel;
    private JComboBox<String> socketComboBox;
    private JComboBox<String> pcbComboBox;
    private JComboBox<String> cpuComboBox;
    private JComboBox<String> gpuComboBox;
    private JButton addButton;
    private JButton removeButton;
    private List<ClassPCB> pcbs;
    private List<ClassCPU> cpus;
    private List<ClassGPU> gpus;
    private List<ClassSocket> sockets;
    // Create regular expression
    private String modelRegex = "^[A-Z][a-zA-Z0-9\\s-+]*$";
    private String priceRegex = "\\d+(\\.\\d{1,2})?";
    private String coresRegex = "\\d+";
    private String threadsRegex = "\\d+";
    private String frequencyRegex = "\\d+";
    private String memoryRegex = "\\d+";
    private String brandNameRegex = "^[A-Z][a-zA-Z0-9\\s-+]*$";
    private String socketNameRegex = "^[A-Z][a-zA-Z0-9\\s-+]*$";
    private String chipsetNameRegex = "^[A-Z][a-zA-Z0-9\\s-+]*$";

    //exception class
    private class TextFieldException extends Exception 
    {
        public TextFieldException() 
        {
            super ("Fill all text fields!");
        }
    }
    // exception function
    public void checkIfEmpty (JTextField field) throws TextFieldException,NullPointerException
    {
        String sName = field.getText();
        if (sName.isBlank()) throw new TextFieldException();
        if (sName.length() == 0) throw new NullPointerException();
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //  _______   _______   __       __   ______    ______   _______   _______         ______  __    __  ______  ________  ______   ______   __        ______  ________   ______   ________  ______   ______   __    __  //
    // /       \ /       \ /  \     /  | /      \  /      \ /       \ /       \       /      |/  \  /  |/      |/        |/      | /      \ /  |      /      |/        | /      \ /        |/      | /      \ /  \  /  | //
    // $$$$$$$  |$$$$$$$  |$$  \   /$$ |/$$$$$$  |/$$$$$$  |$$$$$$$  |$$$$$$$  |      $$$$$$/ $$  \ $$ |$$$$$$/ $$$$$$$$/ $$$$$$/ /$$$$$$  |$$ |      $$$$$$/ $$$$$$$$/ /$$$$$$  |$$$$$$$$/ $$$$$$/ /$$$$$$  |$$  \ $$ | //
    // $$ |  $$ |$$ |__$$ |$$$  \ /$$$ |$$ \__$$/ $$ |__$$ |$$ |__$$ |$$ |__$$ |        $$ |  $$$  \$$ |  $$ |     $$ |     $$ |  $$ |__$$ |$$ |        $$ |      /$$/  $$ |__$$ |   $$ |     $$ |  $$ |  $$ |$$$  \$$ | //
    // $$ |  $$ |$$    $$< $$$$  /$$$$ |$$      \ $$    $$ |$$    $$/ $$    $$/         $$ |  $$$$  $$ |  $$ |     $$ |     $$ |  $$    $$ |$$ |        $$ |     /$$/   $$    $$ |   $$ |     $$ |  $$ |  $$ |$$$$  $$ | //
    // $$ |  $$ |$$$$$$$  |$$ $$ $$/$$ | $$$$$$  |$$$$$$$$ |$$$$$$$/  $$$$$$$/          $$ |  $$ $$ $$ |  $$ |     $$ |     $$ |  $$$$$$$$ |$$ |        $$ |    /$$/    $$$$$$$$ |   $$ |     $$ |  $$ |  $$ |$$ $$ $$ | //
    // $$ |__$$ |$$ |__$$ |$$ |$$$/ $$ |/  \__$$ |$$ |  $$ |$$ |      $$ |             _$$ |_ $$ |$$$$ | _$$ |_    $$ |    _$$ |_ $$ |  $$ |$$ |_____  _$$ |_  /$$/____ $$ |  $$ |   $$ |    _$$ |_ $$ \__$$ |$$ |$$$$ | //
    // $$    $$/ $$    $$/ $$ | $/  $$ |$$    $$/ $$ |  $$ |$$ |      $$ |            / $$   |$$ | $$$ |/ $$   |   $$ |   / $$   |$$ |  $$ |$$       |/ $$   |/$$      |$$ |  $$ |   $$ |   / $$   |$$    $$/ $$ | $$$ | //
    // $$$$$$$/  $$$$$$$/  $$/      $$/  $$$$$$/  $$/   $$/ $$/       $$/             $$$$$$/ $$/   $$/ $$$$$$/    $$/    $$$$$$/ $$/   $$/ $$$$$$$$/ $$$$$$/ $$$$$$$$/ $$/   $$/    $$/    $$$$$$/  $$$$$$/  $$/   $$/  //
    //                                                                                                                                                                                                                   //
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public DBMSApp() 
    {   // Initialize window
        setTitle("DBMS App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // Initialize 
        tabbedPane = new JTabbedPane();

        // Initialize Hibernate session factory
        sessionFactory = new Configuration().configure().buildSessionFactory();
        session = sessionFactory.openSession();
        transaction = null;

        // Create table models
        cpuTableModel = new DefaultTableModel(new Object[]{"Model", "Price", "Cores", "Threads", "Frequency", "Brand", "Socket"}, 0);
        gpuTableModel = new DefaultTableModel(new Object[]{"Model", "Price", "Cores", "Memory", "Frequency", "Brand"}, 0);
        pcbTableModel = new DefaultTableModel(new Object[]{"Model", "Price", "Brand", "Socket", "Chipset"}, 0);
        brandTableModel = new DefaultTableModel(new Object[]{"Name"}, 0);
        socketTableModel = new DefaultTableModel(new Object[]{"Name"}, 0);
        chipsetTableModel = new DefaultTableModel(new Object[]{"Name"}, 0);
        unitTableModel = new DefaultTableModel(new Object[]{"PCB Model", "CPU Model", "GPU Model", "CPU Cores", "GPU Memory", "Total Price"}, 0);

        // Create tables
        cpuTable = new JTable(cpuTableModel);
        gpuTable = new JTable(gpuTableModel);
        pcbTable = new JTable(pcbTableModel);
        brandTable = new JTable(brandTableModel);
        socketTable = new JTable(socketTableModel);
        chipsetTable = new JTable(chipsetTableModel);
        unitTable = new JTable(unitTableModel);
        cpuTable.setDefaultEditor(Object.class, null);
        gpuTable.setDefaultEditor(Object.class, null);
        pcbTable.setDefaultEditor(Object.class, null);
        brandTable.setDefaultEditor(Object.class, null);
        socketTable.setDefaultEditor(Object.class, null);
        chipsetTable.setDefaultEditor(Object.class, null);
        unitTable.setDefaultEditor(Object.class, null);

        // Add tables to scroll panes
        JScrollPane cpuScrollPane = new JScrollPane(cpuTable);
        JScrollPane gpuScrollPane = new JScrollPane(gpuTable);
        JScrollPane pcbScrollPane = new JScrollPane(pcbTable);
        JScrollPane brandScrollPane = new JScrollPane(brandTable);
        JScrollPane socketScrollPane = new JScrollPane(socketTable);
        JScrollPane chipsetScrollPane = new JScrollPane(chipsetTable);

        // Create buttons
        addBrandButton = new JButton("Add");
        deleteBrandButton = new JButton("Delete");
        updateBrandButton = new JButton("Update");
        pdfExportBrandButton = new JButton("PDF");

        addChipsetButton = new JButton("Add");
        deleteChipsetButton = new JButton("Delete");
        updateChipsetButton = new JButton("Update");
        pdfExportChipsetButton = new JButton("PDF");

        addCPUButton = new JButton("Add");
        deleteCPUButton = new JButton("Delete");
        updateCPUButton = new JButton("Update");
        pdfExportCPUButton = new JButton("PDF");

        addGPUButton = new JButton("Add");
        deleteGPUButton = new JButton("Delete");
        updateGPUButton = new JButton("Update");
        pdfExportGPUButton = new JButton("PDF");

        addPCBButton = new JButton("Add");
        deletePCBButton = new JButton("Delete");
        updatePCBButton = new JButton("Update");
        pdfExportPCBButton = new JButton("PDF");

        addSocketButton = new JButton("Add");
        deleteSocketButton = new JButton("Delete");
        updateSocketButton = new JButton("Update");
        pdfExportSocketButton = new JButton("PDF");

        /////////////////////////////////////////
        //                                     //
        //    Create Fields and Combo Boxes    //
        //                                     //
        /////////////////////////////////////////
        // Create CPU Fields
        cpuModelField = new JTextField(255);
        cpuPriceField = new JTextField(10);
        cpuCoresField = new JTextField(5);
        cpuThreadsField = new JTextField(5);
        cpuFrequencyField = new JTextField(10);
        cpuBrandComboBox = new JComboBox<>();
        cpuSocketComboBox = new JComboBox<>();
        // Create GPU Fields
        gpuModelField = new JTextField(255);
        gpuPriceField = new JTextField(10);
        gpuCoresField = new JTextField(5);
        gpuMemoryField = new JTextField(5);
        gpuFrequencyField = new JTextField(10);
        gpuBrandComboBox = new JComboBox<>();
        // Create PCB Fields
        pcbModelField = new JTextField(255);
        pcbPriceField = new JTextField(10);
        pcbBrandComboBox = new JComboBox<>();
        pcbSocketComboBox = new JComboBox<>();
        pcbChipsetComboBox = new JComboBox<>();
        // Create Brand fields
        brandNameField = new JTextField(255);
        // Create Socket fields
        socketNameField = new JTextField(255);
        // Create Chipset fields
        chipsetNameField = new JTextField(255);

        /////////////////////////////
        //                         //
        //    Filter Components    //
        //                         //
        /////////////////////////////
        // Create filter components
        filterCPUComboBox = new JComboBox<>(new String[]{"Model", "Price", "Cores", "Threads", "Frequency", "Brand", "Socket"});
        filterCPUTextField = new JTextField(16);
        filterGPUComboBox = new JComboBox<>(new String[]{"Model", "Price", "Cores", "Memory", "Frequency", "Brand"});
        filterGPUTextField = new JTextField(16);
        filterPCBComboBox = new JComboBox<>(new String[]{"Model", "Price", "Brand", "Socket", "Chipset"});
        filterPCBTextField = new JTextField(16);
        // Add elements to the panel
        JPanel filterCPUPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterCPUPanel.add(new JLabel("Filter By:"));
        filterCPUPanel.add(filterCPUComboBox);
        filterCPUPanel.add(new JLabel("Value:"));
        filterCPUPanel.add(filterCPUTextField);
        // Add elements to the panel
        JPanel filterGPUPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterGPUPanel.add(new JLabel("Filter By:"));
        filterGPUPanel.add(filterGPUComboBox);
        filterGPUPanel.add(new JLabel("Value:"));
        filterGPUPanel.add(filterGPUTextField);
        // Add elements to the panel
        JPanel filterPCBPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPCBPanel.add(new JLabel("Filter By:"));
        filterPCBPanel.add(filterPCBComboBox);
        filterPCBPanel.add(new JLabel("Value:"));
        filterPCBPanel.add(filterPCBTextField);

        ////////////////////////
        //                    //
        //    Input Panels    //
        //                    //
        ////////////////////////
        // For combobox filling
        List<ClassBrand> tempbrands = retrieveBrands();
        List<ClassSocket> tempsockets = retrieveSockets();
        List<ClassChipset> tempchipsets = retrieveChipsets();
        // cpu panel
        JPanel cpuInputPanel = new JPanel(new GridLayout(7, 2));
        cpuInputPanel.add(new JLabel("Model:"));
        cpuInputPanel.add(cpuModelField);
        cpuInputPanel.add(new JLabel("Price:"));
        cpuInputPanel.add(cpuPriceField);
        cpuInputPanel.add(new JLabel("Cores:"));
        cpuInputPanel.add(cpuCoresField);
        cpuInputPanel.add(new JLabel("Threads:"));
        cpuInputPanel.add(cpuThreadsField);
        cpuInputPanel.add(new JLabel("Frequency:"));
        cpuInputPanel.add(cpuFrequencyField);
        cpuInputPanel.add(new JLabel("Brand:"));
        cpuInputPanel.add(cpuBrandComboBox);
        cpuInputPanel.add(new JLabel("Socket:"));
        cpuInputPanel.add(cpuSocketComboBox);
        for (ClassBrand tempbrand : tempbrands) 
        {
            cpuBrandComboBox.addItem(tempbrand.getName());
        }
        for (ClassSocket tempsocket : tempsockets) 
        {
            cpuSocketComboBox.addItem(tempsocket.getName());
        }
        // gpu panel
        JPanel gpuInputPanel = new JPanel(new GridLayout(6, 2));
        gpuInputPanel.add(new JLabel("Model:"));
        gpuInputPanel.add(gpuModelField);
        gpuInputPanel.add(new JLabel("Price:"));
        gpuInputPanel.add(gpuPriceField);
        gpuInputPanel.add(new JLabel("Cores:"));
        gpuInputPanel.add(gpuCoresField);
        gpuInputPanel.add(new JLabel("Memory:"));
        gpuInputPanel.add(gpuMemoryField);
        gpuInputPanel.add(new JLabel("Frequency:"));
        gpuInputPanel.add(gpuFrequencyField);
        gpuInputPanel.add(new JLabel("Brand:"));
        gpuInputPanel.add(gpuBrandComboBox);
        for (ClassBrand tempbrand : tempbrands) 
        {
            gpuBrandComboBox.addItem(tempbrand.getName());
        }
        // motherboard panel
        JPanel pcbInputPanel = new JPanel(new GridLayout(5, 2));
        pcbInputPanel.add(new JLabel("Model:"));
        pcbInputPanel.add(pcbModelField);
        pcbInputPanel.add(new JLabel("Price:"));
        pcbInputPanel.add(pcbPriceField);
        pcbInputPanel.add(new JLabel("Brand:"));
        pcbInputPanel.add(pcbBrandComboBox);
        pcbInputPanel.add(new JLabel("Socket:"));
        pcbInputPanel.add(pcbSocketComboBox);
        pcbInputPanel.add(new JLabel("Chipset:"));
        pcbInputPanel.add(pcbChipsetComboBox);
        for (ClassBrand tempbrand : tempbrands) 
        {
            pcbBrandComboBox.addItem(tempbrand.getName());
        }
        for (ClassSocket tempsocket : tempsockets) 
        {
            pcbSocketComboBox.addItem(tempsocket.getName());
        }
        for (ClassChipset tempchipset : tempchipsets) 
        {
            pcbChipsetComboBox.addItem(tempchipset.getName());
        }
        // brand panel
        JPanel brandInputPanel = new JPanel(new GridLayout(1, 2));
        brandInputPanel.add(new JLabel("Brand Name:"));
        brandInputPanel.add(brandNameField);
        // socket panel
        JPanel socketInputPanel = new JPanel(new GridLayout(1, 2));
        socketInputPanel.add(new JLabel("Socket Name:"));
        socketInputPanel.add(socketNameField);
        // chipset panel
        JPanel chipsetInputPanel = new JPanel(new GridLayout(1, 2));
        chipsetInputPanel.add(new JLabel("Chipset Name:"));
        chipsetInputPanel.add(chipsetNameField);

        /////////////////////////
        //                     //
        //    Button panels    //
        //                     //
        /////////////////////////
        JPanel cpuButtonPanel = createButtonPanel(addCPUButton, deleteCPUButton, updateCPUButton, pdfExportCPUButton);
        cpuButtonPanel.add(filterCPUPanel, BorderLayout.NORTH);
        JPanel gpuButtonPanel = createButtonPanel(addGPUButton, deleteGPUButton, updateGPUButton, pdfExportGPUButton);
        gpuButtonPanel.add(filterGPUPanel, BorderLayout.NORTH);
        JPanel pcbButtonPanel = createButtonPanel(addPCBButton, deletePCBButton, updatePCBButton, pdfExportPCBButton);
        pcbButtonPanel.add(filterPCBPanel, BorderLayout.NORTH);
        JPanel brandButtonPanel = createButtonPanel(addBrandButton, deleteBrandButton, updateBrandButton, pdfExportBrandButton);
        JPanel socketButtonPanel = createButtonPanel(addSocketButton, deleteSocketButton, updateSocketButton, pdfExportSocketButton);
        JPanel chipsetButtonPanel = createButtonPanel(addChipsetButton, deleteChipsetButton, updateChipsetButton, pdfExportChipsetButton);

        ////////////////////////////////////////////
        //                                        //
        //    Add tables and buttons to panels    //
        //                                        //
        ////////////////////////////////////////////
        // Add CPU panel
        JPanel cpuPanel = new JPanel(new BorderLayout());
        cpuPanel.add(cpuScrollPane, BorderLayout.CENTER);
        cpuPanel.add(cpuButtonPanel, BorderLayout.SOUTH);
        cpuPanel.add(cpuInputPanel, BorderLayout.NORTH);
        // Add GPU panel
        JPanel gpuPanel = new JPanel(new BorderLayout());
        gpuPanel.add(gpuScrollPane, BorderLayout.CENTER);
        gpuPanel.add(gpuButtonPanel, BorderLayout.SOUTH);
        gpuPanel.add(gpuInputPanel, BorderLayout.NORTH);
        // Add PCB panel
        JPanel pcbPanel = new JPanel(new BorderLayout());
        pcbPanel.add(pcbScrollPane, BorderLayout.CENTER);
        pcbPanel.add(pcbButtonPanel, BorderLayout.SOUTH);
        pcbPanel.add(pcbInputPanel, BorderLayout.NORTH);
        // Add Brand panel
        JPanel brandPanel = new JPanel(new BorderLayout());
        brandPanel.add(brandScrollPane, BorderLayout.CENTER);
        brandPanel.add(brandButtonPanel, BorderLayout.SOUTH);
        brandPanel.add(brandInputPanel, BorderLayout.NORTH);
        // Add Chipset panel
        JPanel chipsetPanel = new JPanel(new BorderLayout());
        chipsetPanel.add(chipsetScrollPane, BorderLayout.CENTER);
        chipsetPanel.add(chipsetButtonPanel, BorderLayout.SOUTH);
        chipsetPanel.add(chipsetInputPanel, BorderLayout.NORTH);
        // Add Socket panel
        JPanel socketPanel = new JPanel(new BorderLayout());
        socketPanel.add(socketScrollPane, BorderLayout.CENTER);
        socketPanel.add(socketButtonPanel, BorderLayout.SOUTH);
        socketPanel.add(socketInputPanel, BorderLayout.NORTH);

        //////////////////////
        //                  //
        //    Unit panel    //
        //                  //
        //////////////////////
        // Create comboboxes
        socketComboBox = new JComboBox<>();
        pcbComboBox = new JComboBox<>();
        cpuComboBox = new JComboBox<>();
        gpuComboBox = new JComboBox<>();
        // Create buttons
        addButton = new JButton("Add");
        removeButton = new JButton("Remove");
        pdfExportUnitButton = new JButton("PDF");
        // Button panel
        JPanel unitButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        unitButtonPanel.add(addButton);
        unitButtonPanel.add(removeButton);
        unitButtonPanel.add(pdfExportUnitButton);
        // ComboBox panel
        JPanel unitComboPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        unitComboPanel.add(socketComboBox);
        unitComboPanel.add(pcbComboBox);
        unitComboPanel.add(cpuComboBox);
        unitComboPanel.add(gpuComboBox);
        // Add tables and buttons to panels
        JPanel unitPanel = new JPanel(new BorderLayout());
        unitPanel.add(unitComboPanel, BorderLayout.NORTH);
        unitPanel.add(unitButtonPanel, BorderLayout.CENTER);
        unitPanel.add(new JScrollPane(unitTable), BorderLayout.SOUTH);

        ////////////////////
        //                //
        //    Add tabs    //
        //                //
        ////////////////////
        // Add panels to tabbed pane
        tabbedPane.addTab("CPU (Processors)", cpuPanel);
        tabbedPane.addTab("GPU (Graphics)", gpuPanel);
        tabbedPane.addTab("PCB (Motherboards)", pcbPanel);
        tabbedPane.addTab("Brands", brandPanel);
        tabbedPane.addTab("Sockets", socketPanel);
        tabbedPane.addTab("Chipsets", chipsetPanel);
        tabbedPane.addTab("Assemble Units", unitPanel);
        // Add tabbed pane to content pane
        add(tabbedPane);
        // Populate tables
        populateTables();


        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //  __    __  __    __  ______  ________        __        ______   ______   ________  ________  __    __  ________  _______    ______   //
        // /  |  /  |/  \  /  |/      |/        |      /  |      /      | /      \ /        |/        |/  \  /  |/        |/       \  /      \  //
        // $$ |  $$ |$$  \ $$ |$$$$$$/ $$$$$$$$/       $$ |      $$$$$$/ /$$$$$$  |$$$$$$$$/ $$$$$$$$/ $$  \ $$ |$$$$$$$$/ $$$$$$$  |/$$$$$$  | //
        // $$ |  $$ |$$$  \$$ |  $$ |     $$ |         $$ |        $$ |  $$ \__$$/    $$ |   $$ |__    $$$  \$$ |$$ |__    $$ |__$$ |$$ \__$$/  //
        // $$ |  $$ |$$$$  $$ |  $$ |     $$ |         $$ |        $$ |  $$      \    $$ |   $$    |   $$$$  $$ |$$    |   $$    $$< $$      \  //
        // $$ |  $$ |$$ $$ $$ |  $$ |     $$ |         $$ |        $$ |   $$$$$$  |   $$ |   $$$$$/    $$ $$ $$ |$$$$$/    $$$$$$$  | $$$$$$  | //
        // $$ \__$$ |$$ |$$$$ | _$$ |_    $$ |         $$ |_____  _$$ |_ /  \__$$ |   $$ |   $$ |_____ $$ |$$$$ |$$ |_____ $$ |  $$ |/  \__$$ | //
        // $$    $$/ $$ | $$$ |/ $$   |   $$ |         $$       |/ $$   |$$    $$/    $$ |   $$       |$$ | $$$ |$$       |$$ |  $$ |$$    $$/  //
        //  $$$$$$/  $$/   $$/ $$$$$$/    $$/          $$$$$$$$/ $$$$$$/  $$$$$$/     $$/    $$$$$$$$/ $$/   $$/ $$$$$$$$/ $$/   $$/  $$$$$$/   //
        //                                                                                                                                      //
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addUnit();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeUnit();
            }
        });
        
        socketComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateUnitComboBoxes(); // update PCB and CPU combo boxes
            }
        });

        pdfExportUnitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                try 
                {
                    JFileChooser fileChooser = new JFileChooser();
                    // Set default folder to current directory
                    fileChooser.setCurrentDirectory(new File("."));
                    // Set default file name
                    fileChooser.setSelectedFile(new File("exported_Units.pdf"));
                    int result = fileChooser.showSaveDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) 
                    {
                        File selectedFile = fileChooser.getSelectedFile();
                        String fileName = selectedFile.getAbsolutePath();
                        // Append .pdf extension if necessary
                        if (!fileName.endsWith(".pdf")) 
                        {
                            fileName += ".pdf";
                        }
                        Document document = new Document();
                        PdfWriter.getInstance(document, new FileOutputStream(fileName));
                        document.open();
                        PdfPTable pdfTable = new PdfPTable(unitTable.getColumnCount());
                        
                        // Create font for table headers
                        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK);
                        String[] headersPdfExport = {"\nPCB Model\n\n", "\nPCPU Model", "\nGPU Model", "\nCores", "\nMemory", "\nTotal Price"};

                        // Set column headers
                        for (int i = 0; i < unitTable.getColumnCount(); i++) 
                        {
                            PdfPCell header = new PdfPCell(new Phrase(headersPdfExport[i], headerFont));
                            header.setBackgroundColor(BaseColor.ORANGE);
                            header.setBorderWidth(2);
                            header.setHorizontalAlignment(Element.ALIGN_CENTER);
                            // Give more weight to the first row
                            pdfTable.addCell(header);
                        }
                        
                        // Create font for table data
                        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
                        
                        // Set custom widths for each row 
                        float[] columnWidths = {0.2f, 0.2f, 0.2f, 0.12f, 0.12f, 0.16f};
                        pdfTable.setWidths(columnWidths);
                        
                        // Add table data
                        for (int i = 0; i < unitTable.getRowCount(); i++) 
                        {
                            for (int j = 0; j < unitTable.getColumnCount(); j++) 
                            {
                                PdfPCell data = new PdfPCell(new Phrase(unitTable.getValueAt(i, j).toString(), dataFont));
                                if (i % 2 == 1)
                                {
                                    data.setBackgroundColor(BaseColor.LIGHT_GRAY);
                                }
                                else
                                {
                                    data.setBackgroundColor(BaseColor.WHITE);
                                }
                                data.setBorderWidth(1);
                                data.setHorizontalAlignment(Element.ALIGN_LEFT);
                                pdfTable.addCell(data);
                            }
                        }
                        document.add(pdfTable);
                        document.close();
                        JOptionPane.showMessageDialog(cpuPanel, "Exported table data to " + fileName);
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(cpuPanel, "Error exporting table data to PDF");
                }
            }
        });


        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //  ________  ______  __     ________  ________  _______         __        ______   ______   ________  ________  __    __  ________  _______    ______   //
        // /        |/      |/  |   /        |/        |/       \       /  |      /      | /      \ /        |/        |/  \  /  |/        |/       \  /      \  //
        // $$$$$$$$/ $$$$$$/ $$ |   $$$$$$$$/ $$$$$$$$/ $$$$$$$  |      $$ |      $$$$$$/ /$$$$$$  |$$$$$$$$/ $$$$$$$$/ $$  \ $$ |$$$$$$$$/ $$$$$$$  |/$$$$$$  | //
        // $$ |__      $$ |  $$ |      $$ |   $$ |__    $$ |__$$ |      $$ |        $$ |  $$ \__$$/    $$ |   $$ |__    $$$  \$$ |$$ |__    $$ |__$$ |$$ \__$$/  //
        // $$    |     $$ |  $$ |      $$ |   $$    |   $$    $$<       $$ |        $$ |  $$      \    $$ |   $$    |   $$$$  $$ |$$    |   $$    $$< $$      \  //
        // $$$$$/      $$ |  $$ |      $$ |   $$$$$/    $$$$$$$  |      $$ |        $$ |   $$$$$$  |   $$ |   $$$$$/    $$ $$ $$ |$$$$$/    $$$$$$$  | $$$$$$  | //
        // $$ |       _$$ |_ $$ |_____ $$ |   $$ |_____ $$ |  $$ |      $$ |_____  _$$ |_ /  \__$$ |   $$ |   $$ |_____ $$ |$$$$ |$$ |_____ $$ |  $$ |/  \__$$ | //
        // $$ |      / $$   |$$       |$$ |   $$       |$$ |  $$ |      $$       |/ $$   |$$    $$/    $$ |   $$       |$$ | $$$ |$$       |$$ |  $$ |$$    $$/  //
        // $$/       $$$$$$/ $$$$$$$$/ $$/    $$$$$$$$/ $$/   $$/       $$$$$$$$/ $$$$$$/  $$$$$$/     $$/    $$$$$$$$/ $$/   $$/ $$$$$$$$/ $$/   $$/  $$$$$$/   //
        //                                                                                                                                                       //
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        filterCPUComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyCPUFilter();
            }
        });

         filterCPUTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override   
            public void insertUpdate(DocumentEvent e) {
                applyCPUFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                applyCPUFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                applyCPUFilter();
            }
        });

        filterGPUComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyGPUFilter();
            }
        });

         filterGPUTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override   
            public void insertUpdate(DocumentEvent e) {
                applyGPUFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                applyGPUFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                applyGPUFilter();
            }
        });

        filterPCBComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyPCBFilter();
            }
        });

        filterPCBTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override   
            public void insertUpdate(DocumentEvent e) {
                applyPCBFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                applyPCBFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                applyPCBFilter();
            }
        });


{ // action listeners add/delete/update
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //   ______   _______   __    __        __        ______   ______   ________  ________  __    __  ________  _______    ______   //
        //  /      \ /       \ /  |  /  |      /  |      /      | /      \ /        |/        |/  \  /  |/        |/       \  /      \  //
        // /$$$$$$  |$$$$$$$  |$$ |  $$ |      $$ |      $$$$$$/ /$$$$$$  |$$$$$$$$/ $$$$$$$$/ $$  \ $$ |$$$$$$$$/ $$$$$$$  |/$$$$$$  | //
        // $$ |  $$/ $$ |__$$ |$$ |  $$ |      $$ |        $$ |  $$ \__$$/    $$ |   $$ |__    $$$  \$$ |$$ |__    $$ |__$$ |$$ \__$$/  //
        // $$ |      $$    $$/ $$ |  $$ |      $$ |        $$ |  $$      \    $$ |   $$    |   $$$$  $$ |$$    |   $$    $$< $$      \  //
        // $$ |   __ $$$$$$$/  $$ |  $$ |      $$ |        $$ |   $$$$$$  |   $$ |   $$$$$/    $$ $$ $$ |$$$$$/    $$$$$$$  | $$$$$$  | //
        // $$ \__/  |$$ |      $$ \__$$ |      $$ |_____  _$$ |_ /  \__$$ |   $$ |   $$ |_____ $$ |$$$$ |$$ |_____ $$ |  $$ |/  \__$$ | //
        // $$    $$/ $$ |      $$    $$/       $$       |/ $$   |$$    $$/    $$ |   $$       |$$ | $$$ |$$       |$$ |  $$ |$$    $$/  //
        //  $$$$$$/  $$/        $$$$$$/        $$$$$$$$/ $$$$$$/  $$$$$$/     $$/    $$$$$$$$/ $$/   $$/ $$$$$$$$/ $$/   $$/  $$$$$$/   //
        //                                                                                                                              //
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        addCPUButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
                if (cpuModelField.getText().isBlank())
                {
                    JOptionPane.showMessageDialog(cpuPanel, "You must fill all text fields first!");
                }
                else
                {
                    if (cpuModelField.getText().matches(modelRegex) && cpuPriceField.getText().matches(priceRegex) && cpuCoresField.getText().matches(coresRegex) && cpuThreadsField.getText().matches(threadsRegex) && cpuFrequencyField.getText().matches(frequencyRegex)) 
                    {
                        String model = cpuModelField.getText();
                        double price = Double.parseDouble(cpuPriceField.getText());
                        int cores = Integer.parseInt(cpuCoresField.getText());
                        int threads = Integer.parseInt(cpuThreadsField.getText());
                        int frequency = Integer.parseInt(cpuFrequencyField.getText());
                        String brand = cpuBrandComboBox.getSelectedItem().toString();
                        String socket = cpuSocketComboBox.getSelectedItem().toString();
                        
                        ClassBrand brandObj = (ClassBrand) session.createQuery("FROM ClassBrand WHERE name = :name").setParameter("name", brand).uniqueResult();
                        ClassSocket socketObj = (ClassSocket) session.createQuery("FROM ClassSocket WHERE name = :name").setParameter("name", socket).uniqueResult();
                        
                        Object[] rowData = {model, price, cores, threads, frequency, brand, socket};
                        ClassCPU cpu = new ClassCPU();
                        cpu.setModel(model);
                        cpu.setPrice(price);
                        cpu.setCores(cores);
                        cpu.setThreads(threads);
                        cpu.setFrequency(frequency);
                        cpu.setBrand(brandObj);
                        cpu.setSocket(socketObj);
                        try (Session session = getSession())
                        {
                            Transaction transaction = session.beginTransaction();
                            session.save(cpu);
                            transaction.commit();
                            cpuTableModel.addRow(rowData);
                            JOptionPane.showMessageDialog(null, "New cpu added successfully!");
                            updateAllDropBoxes();
                        } 
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Failed to add cpu: " + ex);
                        }
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "Invalid data format!");
                    }
                }
            }
        });

        updateCPUButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
                int row = cpuTable.getSelectedRow();
                if (row != -1) {
                    // create the popup window with yes/no options
                    int choice = JOptionPane.showConfirmDialog(cpuPanel, "Do you wish to continue? ", "Confirmation", JOptionPane.YES_NO_OPTION);
                    // handle the user's choice
                    if (choice == JOptionPane.YES_OPTION)
                    {
                        if (cpuModelField.getText().matches(modelRegex) && cpuPriceField.getText().matches(priceRegex) && cpuCoresField.getText().matches(coresRegex) && cpuThreadsField.getText().matches(threadsRegex) && cpuFrequencyField.getText().matches(frequencyRegex)) 
                        {
                            int selectedRow = cpuTable.getSelectedRow();
                            if (selectedRow != -1) {
                                String oldModelName = (String) cpuTableModel.getValueAt(selectedRow, 0);
                                String model = cpuModelField.getText();
                                double price = Double.parseDouble(cpuPriceField.getText());
                                int cores = Integer.parseInt(cpuCoresField.getText());
                                int threads = Integer.parseInt(cpuThreadsField.getText());
                                int frequency = Integer.parseInt(cpuFrequencyField.getText());
                                String brand = cpuBrandComboBox.getSelectedItem().toString();
                                String socket = cpuSocketComboBox.getSelectedItem().toString();
                                
                                ClassBrand brandObj = (ClassBrand) session.createQuery("FROM ClassBrand WHERE name = :name").setParameter("name", brand).uniqueResult();
                                ClassSocket socketObj = (ClassSocket) session.createQuery("FROM ClassSocket WHERE name = :name").setParameter("name", socket).uniqueResult();

                                if (model != null && !model.isEmpty()) {
                                    try (Session session = getSession()) {
                                        Transaction transaction = session.beginTransaction();
                                        ClassCPU cpu = (ClassCPU) session.createQuery("FROM ClassCPU WHERE model = :model").setParameter("model", oldModelName).uniqueResult();
                                        if (cpu != null) {
                                            cpu.setModel(model);
                                            cpu.setPrice(price);
                                            cpu.setCores(cores);
                                            cpu.setThreads(threads);
                                            cpu.setFrequency(frequency);
                                            cpu.setBrand(brandObj);
                                            cpu.setSocket(socketObj);
                                            session.update(cpu);
                                            populateTables();
                                            transaction.commit();
                                            populateTables();
                                            updateAllDropBoxes();
                                            JOptionPane.showMessageDialog(null, "cpu updated successfully.");
                                        } else {
                                            JOptionPane.showMessageDialog(null, "cpu not found.");
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        JOptionPane.showMessageDialog(null, "Failed to update cpu: " + ex);
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Invalid cpu name.");
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "No cpu selected.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid data format!");
                        }
                    } else {
                        System.out.println("User clicked NO");
                    }
                } else {
                    JOptionPane.showMessageDialog(cpuPanel, "Сan't update any record! Please select one!", "Error", row);
                }
            }
        });

        deleteCPUButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
                int selectedRow = cpuTable.getSelectedRow();
                if (selectedRow != -1) {
                    // create the popup window with yes/no options
                    int choice = JOptionPane.showConfirmDialog(cpuPanel, "Do you wish to continue? ", "Confirmation", JOptionPane.YES_NO_OPTION);
                    // handle the user's choice
                    if (choice == JOptionPane.YES_OPTION) 
                    {
                        String cpuModel = (String) cpuTableModel.getValueAt(selectedRow, 0);
                        try (Session session = getSession()) {
                            Transaction transaction = session.beginTransaction();
                            ClassCPU cpu = (ClassCPU) session.createQuery("FROM ClassCPU WHERE model = :model").setParameter("model", cpuModel).uniqueResult();
                            if (cpu != null) {
                                session.delete(cpu);
                                transaction.commit();
                                populateTables(); // Refresh the table data after deleting the cpu
                                updateAllDropBoxes();
                                JOptionPane.showMessageDialog(null, "cpu deleted successfully.");
                            } else {
                                JOptionPane.showMessageDialog(null, "cpu not found.");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Failed to delete cpu: " + ex);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No cpu selected.");
                }
            }
        });

        pdfExportCPUButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                LOGGER.addHandler(fileHandler);
                LOGGER.info("Trying to export data to PDF document");
                try 
                {
                    JFileChooser fileChooser = new JFileChooser();
                    // Set default folder to current directory
                    fileChooser.setCurrentDirectory(new File("."));
                    // Set default file name
                    fileChooser.setSelectedFile(new File("exported_CPUs.pdf"));
                    int result = fileChooser.showSaveDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) 
                    {
                        File selectedFile = fileChooser.getSelectedFile();
                        String fileName = selectedFile.getAbsolutePath();
                        // Append .pdf extension if necessary
                        if (!fileName.endsWith(".pdf")) 
                        {
                            fileName += ".pdf";
                        }
                        Document document = new Document();
                        PdfWriter.getInstance(document, new FileOutputStream(fileName));
                        document.open();
                        PdfPTable pdfTable = new PdfPTable(cpuTable.getColumnCount());
                        
                        // Create font for table headers
                        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK);
                        String[] headersPdfExport = {"\nModel\n\n", "\nPrice", "\nCores", "\nThreads", "\nFrequency", "\nBrand", "\nSocket"};

                        // Set column headers
                        for (int i = 0; i < cpuTable.getColumnCount(); i++) 
                        {
                            PdfPCell header = new PdfPCell(new Phrase(headersPdfExport[i], headerFont));
                            header.setBackgroundColor(BaseColor.ORANGE);
                            header.setBorderWidth(2);
                            header.setHorizontalAlignment(Element.ALIGN_CENTER);
                            // Give more weight to the first row
                            pdfTable.addCell(header);
                        }
                        
                        // Create font for table data
                        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
                        
                        // Set custom widths for each row 
                        float[] columnWidths = {0.25f, 0.12f, 0.1f, 0.12f, 0.15f, 0.1f, 0.2f};
                        pdfTable.setWidths(columnWidths);
                        
                        // Add table data
                        for (int i = 0; i < cpuTable.getRowCount(); i++) 
                        {
                            for (int j = 0; j < cpuTable.getColumnCount(); j++) 
                            {
                                PdfPCell data = new PdfPCell(new Phrase(cpuTable.getValueAt(i, j).toString(), dataFont));
                                if (i % 2 == 1)
                                {
                                    data.setBackgroundColor(BaseColor.LIGHT_GRAY);
                                }
                                else
                                {
                                    data.setBackgroundColor(BaseColor.WHITE);
                                }
                                data.setBorderWidth(1);
                                data.setHorizontalAlignment(Element.ALIGN_LEFT);
                                pdfTable.addCell(data);
                            }
                        }
                        document.add(pdfTable);
                        document.close();
                        JOptionPane.showMessageDialog(cpuPanel, "Exported table data to " + fileName);
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(cpuPanel, "Error exporting table data to PDF");
                }
            }
        });


        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //   ______   _______   __    __        __        ______   ______   ________  ________  __    __  ________  _______    ______   //
        //  /      \ /       \ /  |  /  |      /  |      /      | /      \ /        |/        |/  \  /  |/        |/       \  /      \  //
        // /$$$$$$  |$$$$$$$  |$$ |  $$ |      $$ |      $$$$$$/ /$$$$$$  |$$$$$$$$/ $$$$$$$$/ $$  \ $$ |$$$$$$$$/ $$$$$$$  |/$$$$$$  | //
        // $$ | _$$/ $$ |__$$ |$$ |  $$ |      $$ |        $$ |  $$ \__$$/    $$ |   $$ |__    $$$  \$$ |$$ |__    $$ |__$$ |$$ \__$$/  //
        // $$ |/    |$$    $$/ $$ |  $$ |      $$ |        $$ |  $$      \    $$ |   $$    |   $$$$  $$ |$$    |   $$    $$< $$      \  //
        // $$ |$$$$ |$$$$$$$/  $$ |  $$ |      $$ |        $$ |   $$$$$$  |   $$ |   $$$$$/    $$ $$ $$ |$$$$$/    $$$$$$$  | $$$$$$  | //
        // $$ \__$$ |$$ |      $$ \__$$ |      $$ |_____  _$$ |_ /  \__$$ |   $$ |   $$ |_____ $$ |$$$$ |$$ |_____ $$ |  $$ |/  \__$$ | //
        // $$    $$/ $$ |      $$    $$/       $$       |/ $$   |$$    $$/    $$ |   $$       |$$ | $$$ |$$       |$$ |  $$ |$$    $$/  //
        //  $$$$$$/  $$/        $$$$$$/        $$$$$$$$/ $$$$$$/  $$$$$$/     $$/    $$$$$$$$/ $$/   $$/ $$$$$$$$/ $$/   $$/  $$$$$$/   //
        //                                                                                                                              //
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        addGPUButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
                if (gpuModelField.getText().isBlank())
                {
                    JOptionPane.showMessageDialog(gpuPanel, "You must fill all text fields first!");
                }
                else
                {
                    if (gpuModelField.getText().matches(modelRegex) && gpuPriceField.getText().matches(priceRegex) && gpuCoresField.getText().matches(coresRegex) && gpuMemoryField.getText().matches(memoryRegex) && gpuFrequencyField.getText().matches(frequencyRegex)) 
                    {
                        String model = gpuModelField.getText();
                        double price = Double.parseDouble(gpuPriceField.getText());
                        int cores = Integer.parseInt(gpuCoresField.getText());
                        int memory = Integer.parseInt(gpuMemoryField.getText());
                        int frequency = Integer.parseInt(gpuFrequencyField.getText());
                        String brand = gpuBrandComboBox.getSelectedItem().toString();
                        
                        ClassBrand brandObj = (ClassBrand) session.createQuery("FROM ClassBrand WHERE name = :name").setParameter("name", brand).uniqueResult();
                        
                        Object[] rowData = {model, price, cores, memory, frequency, brand};
                        ClassGPU gpu = new ClassGPU();
                        gpu.setModel(model);
                        gpu.setPrice(price);
                        gpu.setCores(cores);
                        gpu.setMemory(memory);
                        gpu.setFrequency(frequency);
                        gpu.setBrand(brandObj);
                        try (Session session = getSession())
                        {
                            Transaction transaction = session.beginTransaction();
                            session.save(gpu);
                            transaction.commit();
                            gpuTableModel.addRow(rowData);
                            JOptionPane.showMessageDialog(null, "gpu added successfully.");
                            updateAllDropBoxes();
                        } 
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Failed to add gpu: " + ex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid data format!");
                    }
                }
            }
        });

        updateGPUButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
                int row = gpuTable.getSelectedRow();
                if (row != -1) {
                    // create the popup window with yes/no options
                    int choice = JOptionPane.showConfirmDialog(gpuPanel, "Do you wish to continue? ", "Confirmation", JOptionPane.YES_NO_OPTION);
                    // handle the user's choice
                    if (choice == JOptionPane.YES_OPTION)
                    {
                        if (gpuModelField.getText().matches(modelRegex) && gpuPriceField.getText().matches(priceRegex) && gpuCoresField.getText().matches(coresRegex) && gpuMemoryField.getText().matches(memoryRegex) && gpuFrequencyField.getText().matches(frequencyRegex)) 
                        {
                            int selectedRow = gpuTable.getSelectedRow();
                            if (selectedRow != -1) {
                                String oldModelName = (String) gpuTableModel.getValueAt(selectedRow, 0);
                                String model = gpuModelField.getText();
                                double price = Double.parseDouble(gpuPriceField.getText());
                                int cores = Integer.parseInt(gpuCoresField.getText());
                                int memory = Integer.parseInt(gpuMemoryField.getText());
                                int frequency = Integer.parseInt(gpuFrequencyField.getText());
                                String brand = gpuBrandComboBox.getSelectedItem().toString();

                                ClassBrand brandObj = (ClassBrand) session.createQuery("FROM ClassBrand WHERE name = :name").setParameter("name", brand).uniqueResult();

                                if (model != null && !model.isEmpty()) {
                                    try (Session session = getSession()) {
                                        Transaction transaction = session.beginTransaction();
                                        ClassGPU gpu = (ClassGPU) session.createQuery("FROM ClassGPU WHERE model = :model").setParameter("model", oldModelName).uniqueResult();
                                        if (gpu != null) {
                                            gpu.setModel(model);
                                            gpu.setPrice(price);
                                            gpu.setCores(cores);
                                            gpu.setMemory(memory);
                                            gpu.setFrequency(frequency);
                                            gpu.setBrand(brandObj);
                                            session.update(gpu);
                                            populateTables();
                                            transaction.commit();
                                            populateTables();
                                            updateAllDropBoxes();
                                            JOptionPane.showMessageDialog(null, "gpu updated successfully.");
                                        } else {
                                            JOptionPane.showMessageDialog(null, "gpu not found.");
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        JOptionPane.showMessageDialog(null, "Failed to update gpu: " + ex);
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Invalid gpu name.");
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "No gpu selected.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid data format!");
                        }
                    } else {
                        System.out.println("User clicked NO");
                    }
                } else {
                    JOptionPane.showMessageDialog(gpuPanel, "Сan't update any record! Please select one!", "Error", row);
                }
            }
        });

        deleteGPUButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
                int selectedRow = gpuTable.getSelectedRow();
                if (selectedRow != -1) {
                    // create the popup window with yes/no options
                    int choice = JOptionPane.showConfirmDialog(gpuPanel, "Do you wish to continue? ", "Confirmation", JOptionPane.YES_NO_OPTION);
                    // handle the user's choice
                    if (choice == JOptionPane.YES_OPTION) 
                    {
                        String gpuModel = (String) gpuTableModel.getValueAt(selectedRow, 0);
                
                        try (Session session = getSession()) {
                            Transaction transaction = session.beginTransaction();
                            ClassGPU gpu = (ClassGPU) session.createQuery("FROM ClassGPU WHERE model = :model").setParameter("model", gpuModel).uniqueResult();
                            if (gpu != null) {
                                session.delete(gpu);
                                transaction.commit();
                                populateTables(); // Refresh the table data after deleting the gpu
                                updateAllDropBoxes();
                                JOptionPane.showMessageDialog(null, "gpu deleted successfully.");
                            } else {
                                JOptionPane.showMessageDialog(null, "gpu not found.");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Failed to delete gpu: " + ex);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No gpu selected.");
                }
            }
        });
        
        pdfExportGPUButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                LOGGER.addHandler(fileHandler);
                LOGGER.info("Trying to export data to PDF document");
                try 
                {
                    JFileChooser fileChooser = new JFileChooser();
                    // Set default folder to current directory
                    fileChooser.setCurrentDirectory(new File("."));
                    // Set default file name
                    fileChooser.setSelectedFile(new File("exported_GPUs.pdf"));
                    int result = fileChooser.showSaveDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) 
                    {
                        File selectedFile = fileChooser.getSelectedFile();
                        String fileName = selectedFile.getAbsolutePath();
                        // Append .pdf extension if necessary
                        if (!fileName.endsWith(".pdf")) 
                        {
                            fileName += ".pdf";
                        }
                        Document document = new Document();
                        PdfWriter.getInstance(document, new FileOutputStream(fileName));
                        document.open();
                        PdfPTable pdfTable = new PdfPTable(gpuTable.getColumnCount());
                        
                        // Create font for table headers
                        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK);
                        String[] headersPdfExport = {"\nModel\n\n", "\nPrice", "\nCores", "\nThreads", "\nFrequency", "\nBrand"};

                        // Set column headers
                        for (int i = 0; i < gpuTable.getColumnCount(); i++) 
                        {
                            PdfPCell header = new PdfPCell(new Phrase(headersPdfExport[i], headerFont));
                            header.setBackgroundColor(BaseColor.ORANGE);
                            header.setBorderWidth(2);
                            header.setHorizontalAlignment(Element.ALIGN_CENTER);
                            // Give more weight to the first row
                            pdfTable.addCell(header);
                        }
                        
                        // Create font for table data
                        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
                        
                        // Set custom widths for each row 
                        float[] columnWidths = {0.25f, 0.12f, 0.1f, 0.12f, 0.15f, 0.1f};
                        pdfTable.setWidths(columnWidths);
                        
                        // Add table data
                        for (int i = 0; i < gpuTable.getRowCount(); i++) 
                        {
                            for (int j = 0; j < gpuTable.getColumnCount(); j++) 
                            {
                                PdfPCell data = new PdfPCell(new Phrase(gpuTable.getValueAt(i, j).toString(), dataFont));
                                if (i % 2 == 1)
                                {
                                    data.setBackgroundColor(BaseColor.LIGHT_GRAY);
                                }
                                else
                                {
                                    data.setBackgroundColor(BaseColor.WHITE);
                                }
                                data.setBorderWidth(1);
                                data.setHorizontalAlignment(Element.ALIGN_LEFT);
                                pdfTable.addCell(data);
                            }
                        }
                        document.add(pdfTable);
                        document.close();
                        JOptionPane.showMessageDialog(gpuPanel, "Exported table data to " + fileName);
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(gpuPanel, "Error exporting table data to PDF");
                }
            }
        });


        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //  _______    ______   _______         __        ______   ______   ________  ________  __    __  ________  _______    ______   //
        // /       \  /      \ /       \       /  |      /      | /      \ /        |/        |/  \  /  |/        |/       \  /      \  //
        // $$$$$$$  |/$$$$$$  |$$$$$$$  |      $$ |      $$$$$$/ /$$$$$$  |$$$$$$$$/ $$$$$$$$/ $$  \ $$ |$$$$$$$$/ $$$$$$$  |/$$$$$$  | //
        // $$ |__$$ |$$ |  $$/ $$ |__$$ |      $$ |        $$ |  $$ \__$$/    $$ |   $$ |__    $$$  \$$ |$$ |__    $$ |__$$ |$$ \__$$/  //
        // $$    $$/ $$ |      $$    $$<       $$ |        $$ |  $$      \    $$ |   $$    |   $$$$  $$ |$$    |   $$    $$< $$      \  //
        // $$$$$$$/  $$ |   __ $$$$$$$  |      $$ |        $$ |   $$$$$$  |   $$ |   $$$$$/    $$ $$ $$ |$$$$$/    $$$$$$$  | $$$$$$  | //
        // $$ |      $$ \__/  |$$ |__$$ |      $$ |_____  _$$ |_ /  \__$$ |   $$ |   $$ |_____ $$ |$$$$ |$$ |_____ $$ |  $$ |/  \__$$ | //
        // $$ |      $$    $$/ $$    $$/       $$       |/ $$   |$$    $$/    $$ |   $$       |$$ | $$$ |$$       |$$ |  $$ |$$    $$/  //
        // $$/        $$$$$$/  $$$$$$$/        $$$$$$$$/ $$$$$$/  $$$$$$/     $$/    $$$$$$$$/ $$/   $$/ $$$$$$$$/ $$/   $$/  $$$$$$/   //
        //                                                                                                                              //
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        addPCBButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
                if (pcbModelField.getText().isBlank())
                {
                    JOptionPane.showMessageDialog(pcbPanel, "You must fill all text fields first!");
                }
                else
                {
                    if (pcbModelField.getText().matches(modelRegex) && pcbPriceField.getText().matches(priceRegex)) 
                    {
                        String model = pcbModelField.getText();
                        double price = Double.parseDouble(pcbPriceField.getText());
                        String brand = pcbBrandComboBox.getSelectedItem().toString();
                        String socket = pcbSocketComboBox.getSelectedItem().toString();
                        String chipset= pcbChipsetComboBox.getSelectedItem().toString();
                        ClassBrand brandObj = (ClassBrand) session.createQuery("FROM ClassBrand WHERE name = :name").setParameter("name", brand).uniqueResult();
                        ClassSocket socketObj = (ClassSocket) session.createQuery("FROM ClassSocket WHERE name = :name").setParameter("name", socket).uniqueResult();
                        ClassChipset chipsetObj = (ClassChipset) session.createQuery("FROM ClassChipset WHERE name = :name").setParameter("name", chipset).uniqueResult();
                        Object[] rowData = {model, price, brand, socket, chipset};
                        ClassPCB pcb = new ClassPCB();
                        pcb.setModel(model);
                        pcb.setPrice(price);
                        pcb.setBrand(brandObj);
                        pcb.setSocket(socketObj);
                        pcb.setChipset(chipsetObj);
                        try (Session session = getSession())
                        {
                            Transaction transaction = session.beginTransaction();
                            session.save(pcb);
                            transaction.commit();
                            pcbTableModel.addRow(rowData);
                            JOptionPane.showMessageDialog(null, "pcb added successfully.");
                            updateAllDropBoxes();
                        } 
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Failed to add pcb: " + ex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid data format!");
                    }
                }
            }
        });

        updatePCBButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
                int row = pcbTable.getSelectedRow();
                if (row != -1) {
                    // create the popup window with yes/no options
                    int choice = JOptionPane.showConfirmDialog(pcbPanel, "Do you wish to continue? ", "Confirmation", JOptionPane.YES_NO_OPTION);
                    // handle the user's choice
                    if (choice == JOptionPane.YES_OPTION) 
                    {
                        if (pcbModelField.getText().matches(modelRegex) && pcbPriceField.getText().matches(priceRegex)) 
                        {
                            int selectedRow = pcbTable.getSelectedRow();
                            if (selectedRow != -1) {
                                String oldModelName = (String) pcbTableModel.getValueAt(selectedRow, 0);
                                String model = pcbModelField.getText();
                                double price = Double.parseDouble(pcbPriceField.getText());
                                String brand = pcbBrandComboBox.getSelectedItem().toString();
                                String socket = pcbSocketComboBox.getSelectedItem().toString();
                                String chipset= pcbChipsetComboBox.getSelectedItem().toString();
                                ClassBrand brandObj = (ClassBrand) session.createQuery("FROM ClassBrand WHERE name = :name").setParameter("name", brand).uniqueResult();
                                ClassSocket socketObj = (ClassSocket) session.createQuery("FROM ClassSocket WHERE name = :name").setParameter("name", socket).uniqueResult();
                                ClassChipset chipsetObj = (ClassChipset) session.createQuery("FROM ClassChipset WHERE name = :name").setParameter("name", chipset).uniqueResult();
                                if (model != null && !model.isEmpty()) {
                                    try (Session session = getSession()) {
                                        Transaction transaction = session.beginTransaction();
                                        ClassPCB pcb = (ClassPCB) session.createQuery("FROM ClassPCB WHERE model = :model").setParameter("model", oldModelName).uniqueResult();
                                        if (pcb != null) {
                                            pcb.setModel(model);
                                            pcb.setPrice(price);
                                            pcb.setBrand(brandObj);
                                            pcb.setSocket(socketObj);
                                            pcb.setChipset(chipsetObj);
                                            session.update(pcb);
                                            populateTables();
                                            transaction.commit();
                                            populateTables();
                                            updateAllDropBoxes();
                                            JOptionPane.showMessageDialog(null, "pcb updated successfully.");
                                        } else {
                                            JOptionPane.showMessageDialog(null, "pcb not found.");
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        JOptionPane.showMessageDialog(null, "Failed to update pcb: " + ex);
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Invalid pcb name.");
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "No pcb selected.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid data format!");
                        }
                    } else {
                        System.out.println("User clicked NO");
                    }
                } else {
                    JOptionPane.showMessageDialog(pcbPanel, "Сan't update any record! Please select one!", "Error", row);
                }
            }
        });

        deletePCBButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
                int selectedRow = pcbTable.getSelectedRow();
                if (selectedRow != -1) {
                    // create the popup window with yes/no options
                    int choice = JOptionPane.showConfirmDialog(pcbPanel, "Do you wish to continue? ", "Confirmation", JOptionPane.YES_NO_OPTION);
                    // handle the user's choice
                    if (choice == JOptionPane.YES_OPTION) 
                    {
                        String pcbModel = (String) pcbTableModel.getValueAt(selectedRow, 0);
                
                        try (Session session = getSession()) {
                            Transaction transaction = session.beginTransaction();
                            ClassPCB pcb = (ClassPCB) session.createQuery("FROM ClassPCB WHERE model = :model").setParameter("model", pcbModel).uniqueResult();
                            if (pcb != null) {
                                session.delete(pcb);
                                transaction.commit();
                                populateTables(); // Refresh the table data after deleting the pcb
                                updateAllDropBoxes();
                                JOptionPane.showMessageDialog(null, "pcb deleted successfully.");
                            } else {
                                JOptionPane.showMessageDialog(null, "pcb not found.");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Failed to delete pcb: " + ex);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No pcb selected.");
                }
            }
        });

        pdfExportPCBButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                LOGGER.addHandler(fileHandler);
                LOGGER.info("Trying to export data to PDF document");
                try 
                {
                    JFileChooser fileChooser = new JFileChooser();
                    // Set default folder to current directorys
                    fileChooser.setCurrentDirectory(new File("."));
                    // Set default file name
                    fileChooser.setSelectedFile(new File("exported_PCBs.pdf"));
                    int result = fileChooser.showSaveDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) 
                    {
                        File selectedFile = fileChooser.getSelectedFile();
                        String fileName = selectedFile.getAbsolutePath();
                        // Append .pdf extension if necessary
                        if (!fileName.endsWith(".pdf")) 
                        {
                            fileName += ".pdf";
                        }
                        Document document = new Document();
                        PdfWriter.getInstance(document, new FileOutputStream(fileName));
                        document.open();
                        PdfPTable pdfTable = new PdfPTable(pcbTable.getColumnCount());
                        
                        // Create font for table headers
                        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK);
                        String[] headersPdfExport = {"\nModel\n\n", "\nPrice", "\nBrand", "\nSocket", "\nChipset"};

                        // Set column headers
                        for (int i = 0; i < pcbTable.getColumnCount(); i++) 
                        {
                            PdfPCell header = new PdfPCell(new Phrase(headersPdfExport[i], headerFont));
                            header.setBackgroundColor(BaseColor.ORANGE);
                            header.setBorderWidth(2);
                            header.setHorizontalAlignment(Element.ALIGN_CENTER);
                            // Give more weight to the first row
                            pdfTable.addCell(header);
                        }
                        
                        // Create font for table data
                        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
                        
                        // Set custom widths for each row 
                        float[] columnWidths = {0.26f, 0.13f, 0.11f, 0.13f, 0.16f};
                        pdfTable.setWidths(columnWidths);
                        
                        // Add table data
                        for (int i = 0; i < pcbTable.getRowCount(); i++) 
                        {
                            for (int j = 0; j < pcbTable.getColumnCount(); j++) 
                            {
                                PdfPCell data = new PdfPCell(new Phrase(pcbTable.getValueAt(i, j).toString(), dataFont));
                                if (i % 2 == 1)
                                {
                                    data.setBackgroundColor(BaseColor.LIGHT_GRAY);
                                }
                                else
                                {
                                    data.setBackgroundColor(BaseColor.WHITE);
                                }
                                data.setBorderWidth(1);
                                data.setHorizontalAlignment(Element.ALIGN_LEFT);
                                pdfTable.addCell(data);
                            }
                        }
                        document.add(pdfTable);
                        document.close();
                        JOptionPane.showMessageDialog(pcbPanel, "Exported table data to " + fileName);
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(pcbPanel, "Error exporting table data to PDF");
                }
            }
        });

        
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //  _______   _______    ______   __    __  _______         __        ______   ______   ________  ________  __    __  ________  _______    ______   //
        // /       \ /       \  /      \ /  \  /  |/       \       /  |      /      | /      \ /        |/        |/  \  /  |/        |/       \  /      \  //
        // $$$$$$$  |$$$$$$$  |/$$$$$$  |$$  \ $$ |$$$$$$$  |      $$ |      $$$$$$/ /$$$$$$  |$$$$$$$$/ $$$$$$$$/ $$  \ $$ |$$$$$$$$/ $$$$$$$  |/$$$$$$  | //
        // $$ |__$$ |$$ |__$$ |$$ |__$$ |$$$  \$$ |$$ |  $$ |      $$ |        $$ |  $$ \__$$/    $$ |   $$ |__    $$$  \$$ |$$ |__    $$ |__$$ |$$ \__$$/  //
        // $$    $$< $$    $$< $$    $$ |$$$$  $$ |$$ |  $$ |      $$ |        $$ |  $$      \    $$ |   $$    |   $$$$  $$ |$$    |   $$    $$< $$      \  //
        // $$$$$$$  |$$$$$$$  |$$$$$$$$ |$$ $$ $$ |$$ |  $$ |      $$ |        $$ |   $$$$$$  |   $$ |   $$$$$/    $$ $$ $$ |$$$$$/    $$$$$$$  | $$$$$$  | //
        // $$ |__$$ |$$ |  $$ |$$ |  $$ |$$ |$$$$ |$$ |__$$ |      $$ |_____  _$$ |_ /  \__$$ |   $$ |   $$ |_____ $$ |$$$$ |$$ |_____ $$ |  $$ |/  \__$$ | //
        // $$    $$/ $$ |  $$ |$$ |  $$ |$$ | $$$ |$$    $$/       $$       |/ $$   |$$    $$/    $$ |   $$       |$$ | $$$ |$$       |$$ |  $$ |$$    $$/  //
        // $$$$$$$/  $$/   $$/ $$/   $$/ $$/   $$/ $$$$$$$/        $$$$$$$$/ $$$$$$/  $$$$$$/     $$/    $$$$$$$$/ $$/   $$/ $$$$$$$$/ $$/   $$/  $$$$$$/   //
        //                                                                                                                                                  //
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        addBrandButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
                if (brandNameField.getText().isBlank())
                {
                    JOptionPane.showMessageDialog(brandPanel, "You must fill all text fields first!");
                }
                else
                {
                    if (brandNameField.getText().matches(brandNameRegex)) 
                    {
                        String name = brandNameField.getText();
                        Object[] rowData = {name};
                        ClassBrand brand = new ClassBrand();
                        brand.setName(name);
                        try (Session session = getSession())
                        {
                            Transaction transaction = session.beginTransaction();
                            session.save(brand);
                            transaction.commit();
                            brandTableModel.addRow(rowData);
                            JOptionPane.showMessageDialog(null, "Brand added successfully.");
                            updateAllDropBoxes();
                        } 
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Failed to add brand: " + ex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid data format!");
                    }
                }
            }
        });

        updateBrandButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {         
                int row = brandTable.getSelectedRow();
                if (row != -1) {
                    // create the popup window with yes/no options
                    int choice = JOptionPane.showConfirmDialog(brandPanel, "Do you wish to continue? ", "Confirmation", JOptionPane.YES_NO_OPTION);
                    // handle the user's choice
                    if (choice == JOptionPane.YES_OPTION) 
                    {
                        if (brandNameField.getText().matches(brandNameRegex)) 
                        {
                            int selectedRow = brandTable.getSelectedRow();
                            if (selectedRow != -1) {
                                String oldBrandName = (String) brandTableModel.getValueAt(selectedRow, 0);
                                String newBrandName = brandNameField.getText();
                        
                                if (newBrandName != null && !newBrandName.isEmpty()) {
                                    try (Session session = getSession()) {
                                        Transaction transaction = session.beginTransaction();
                                        ClassBrand brand = (ClassBrand) session.createQuery("FROM ClassBrand WHERE name = :name").setParameter("name", oldBrandName).uniqueResult();
                                        if (brand != null) {
                                            brand.setName(newBrandName);
                                            session.update(brand);
                                            populateTables();
                                            transaction.commit();
                                            populateTables();
                                            updateAllDropBoxes();
                                            JOptionPane.showMessageDialog(null, "Brand updated successfully.");
                                        } else {
                                            JOptionPane.showMessageDialog(null, "Brand not found.");
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        JOptionPane.showMessageDialog(null, "Failed to update brand: " + ex);
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Invalid brand name.");
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "No brand selected.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid data format!");
                        }
                    } else {
                        System.out.println("User clicked NO");
                    }
                } else {
                    JOptionPane.showMessageDialog(brandPanel, "Сan't update any record! Please select one!", "Error", row);
                }
            }
        });

        deleteBrandButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
                int selectedRow = brandTable.getSelectedRow();
                if (selectedRow != -1) {
                    // create the popup window with yes/no options
                    int choice = JOptionPane.showConfirmDialog(brandPanel, "Do you wish to continue? ", "Confirmation", JOptionPane.YES_NO_OPTION);
                    // handle the user's choice
                    if (choice == JOptionPane.YES_OPTION) 
                    {
                        String brandName = (String) brandTableModel.getValueAt(selectedRow, 0);
                
                        try (Session session = getSession()) {
                            Transaction transaction = session.beginTransaction();
                            ClassBrand brand = (ClassBrand) session.createQuery("FROM ClassBrand WHERE name = :name").setParameter("name", brandName).uniqueResult();
                            if (brand != null) {
                                session.delete(brand);
                                transaction.commit();
                                populateTables(); // Refresh the table data after deleting the brand
                                updateAllDropBoxes();
                                JOptionPane.showMessageDialog(null, "Brand deleted successfully.");
                            } else {
                                JOptionPane.showMessageDialog(null, "Brand not found.");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Failed to delete brand: " + ex);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No brand selected.");
                }
            }
        });

        pdfExportBrandButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                LOGGER.addHandler(fileHandler);
                LOGGER.info("Trying to export data to PDF document");
                try 
                {
                    JFileChooser fileChooser = new JFileChooser();
                    // Set default folder to current directory
                    fileChooser.setCurrentDirectory(new File("."));
                    // Set default file name
                    fileChooser.setSelectedFile(new File("exported_Brands.pdf"));
                    int result = fileChooser.showSaveDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) 
                    {
                        File selectedFile = fileChooser.getSelectedFile();
                        String fileName = selectedFile.getAbsolutePath();
                        // Append .pdf extension if necessary
                        if (!fileName.endsWith(".pdf")) 
                        {
                            fileName += ".pdf";
                        }
                        Document document = new Document();
                        PdfWriter.getInstance(document, new FileOutputStream(fileName));
                        document.open();
                        PdfPTable pdfTable = new PdfPTable(brandTable.getColumnCount());
                        
                        // Create font for table headers
                        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK);
                        String[] headersPdfExport = {"\nBrand name\n\n"};

                        // Set column headers
                        for (int i = 0; i < brandTable.getColumnCount(); i++) 
                        {
                            PdfPCell header = new PdfPCell(new Phrase(headersPdfExport[i], headerFont));
                            header.setBackgroundColor(BaseColor.ORANGE);
                            header.setBorderWidth(2);
                            header.setHorizontalAlignment(Element.ALIGN_CENTER);
                            // Give more weight to the first row
                            pdfTable.addCell(header);
                        }
                        
                        // Create font for table data
                        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
                        
                        // Set custom widths for each row 
                        float[] columnWidths = {1f};
                        pdfTable.setWidths(columnWidths);
                        
                        // Add table data
                        for (int i = 0; i < brandTable.getRowCount(); i++) 
                        {
                            for (int j = 0; j < brandTable.getColumnCount(); j++) 
                            {
                                PdfPCell data = new PdfPCell(new Phrase(brandTable.getValueAt(i, j).toString(), dataFont));
                                if (i % 2 == 1)
                                {
                                    data.setBackgroundColor(BaseColor.LIGHT_GRAY);
                                }
                                else
                                {
                                    data.setBackgroundColor(BaseColor.WHITE);
                                }
                                data.setBorderWidth(1);
                                data.setHorizontalAlignment(Element.ALIGN_LEFT);
                                pdfTable.addCell(data);
                            }
                        }
                        document.add(pdfTable);
                        document.close();
                        JOptionPane.showMessageDialog(brandPanel, "Exported table data to " + fileName);
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(brandPanel, "Error exporting table data to PDF");
                }
            }
        });


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //   ______    ______    ______   __    __  ________  ________        __        ______   ______   ________  ________  __    __  ________  _______    ______   //
        //  /      \  /      \  /      \ /  |  /  |/        |/        |      /  |      /      | /      \ /        |/        |/  \  /  |/        |/       \  /      \  //
        // /$$$$$$  |/$$$$$$  |/$$$$$$  |$$ | /$$/ $$$$$$$$/ $$$$$$$$/       $$ |      $$$$$$/ /$$$$$$  |$$$$$$$$/ $$$$$$$$/ $$  \ $$ |$$$$$$$$/ $$$$$$$  |/$$$$$$  | //
        // $$ \__$$/ $$ |  $$ |$$ |  $$/ $$ |/$$/  $$ |__       $$ |         $$ |        $$ |  $$ \__$$/    $$ |   $$ |__    $$$  \$$ |$$ |__    $$ |__$$ |$$ \__$$/  //
        // $$      \ $$ |  $$ |$$ |      $$  $$<   $$    |      $$ |         $$ |        $$ |  $$      \    $$ |   $$    |   $$$$  $$ |$$    |   $$    $$< $$      \  //
        //  $$$$$$  |$$ |  $$ |$$ |   __ $$$$$  \  $$$$$/       $$ |         $$ |        $$ |   $$$$$$  |   $$ |   $$$$$/    $$ $$ $$ |$$$$$/    $$$$$$$  | $$$$$$  | //
        // /  \__$$ |$$ \__$$ |$$ \__/  |$$ |$$  \ $$ |_____    $$ |         $$ |_____  _$$ |_ /  \__$$ |   $$ |   $$ |_____ $$ |$$$$ |$$ |_____ $$ |  $$ |/  \__$$ | //
        // $$    $$/ $$    $$/ $$    $$/ $$ | $$  |$$       |   $$ |         $$       |/ $$   |$$    $$/    $$ |   $$       |$$ | $$$ |$$       |$$ |  $$ |$$    $$/  //
        //  $$$$$$/   $$$$$$/   $$$$$$/  $$/   $$/ $$$$$$$$/    $$/          $$$$$$$$/ $$$$$$/  $$$$$$/     $$/    $$$$$$$$/ $$/   $$/ $$$$$$$$/ $$/   $$/  $$$$$$/   //
        //                                                                                                                                                            //
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        addSocketButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
                if (socketNameField.getText().isBlank())
                {
                    JOptionPane.showMessageDialog(null, "You must fill all text fields first!");
                }
                else
                {
                    if (socketNameField.getText().matches(socketNameRegex)) 
                    {
                        String name = socketNameField.getText();
                        Object[] rowData = {name};
                        ClassSocket socket = new ClassSocket();
                        socket.setName(name);
                        try (Session session = getSession())
                        {
                            Transaction transaction = session.beginTransaction();
                            session.save(socket);
                            transaction.commit();
                            socketTableModel.addRow(rowData);
                            JOptionPane.showMessageDialog(null, "Socket added successfully.");
                            updateAllDropBoxes();
                        } 
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Failed to add socket: " + ex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid data format!");
                    }
                }
            }
        });

        updateSocketButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
                int row = socketTable.getSelectedRow();
                if (row != -1) {
                    // create the popup window with yes/no options
                    int choice = JOptionPane.showConfirmDialog(socketPanel, "Do you wish to continue? ", "Confirmation", JOptionPane.YES_NO_OPTION);
                    // handle the user's choice
                    if (choice == JOptionPane.YES_OPTION) 
                    {
                        if (socketNameField.getText().matches(socketNameRegex)) 
                        {
                            int selectedRow = socketTable.getSelectedRow();

                            if (selectedRow != -1) {
                                String oldSocketName = (String) socketTableModel.getValueAt(selectedRow, 0);
                                String newSocketName = socketNameField.getText();
                        
                                if (newSocketName != null && !newSocketName.isEmpty()) {
                                    try (Session session = getSession()) {
                                        Transaction transaction = session.beginTransaction();
                                        ClassSocket socket = (ClassSocket) session.createQuery("FROM ClassSocket WHERE name = :name").setParameter("name", oldSocketName).uniqueResult();
                                        if (socket != null) {
                                            socket.setName(newSocketName);
                                            session.update(socket);
                                            populateTables();
                                            transaction.commit();
                                            populateTables();
                                            updateAllDropBoxes();
                                            JOptionPane.showMessageDialog(null, "socket updated successfully.");
                                        } else {
                                            JOptionPane.showMessageDialog(null, "socket not found.");
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        JOptionPane.showMessageDialog(null, "Failed to update socket: " + ex);
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Invalid socket name.");
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "No socket selected.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid data format!");
                        }
                    } else {
                        System.out.println("User clicked NO");
                    }
                } else {
                    JOptionPane.showMessageDialog(socketPanel, "Сan't update any record! Please select one!", "Error", row);
                }
            }
        });

        deleteSocketButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
                int selectedRow = socketTable.getSelectedRow();
                if (selectedRow != -1) {
                    // create the popup window with yes/no options
                    int choice = JOptionPane.showConfirmDialog(socketPanel, "Do you wish to continue? ", "Confirmation", JOptionPane.YES_NO_OPTION);
                    // handle the user's choice
                    if (choice == JOptionPane.YES_OPTION) 
                    {
                        String socketName = (String) socketTableModel.getValueAt(selectedRow, 0);
                
                        try (Session session = getSession()) {
                            Transaction transaction = session.beginTransaction();
                            ClassSocket socket = (ClassSocket) session.createQuery("FROM ClassSocket WHERE name = :name").setParameter("name", socketName).uniqueResult();
                            if (socket != null) {
                                session.delete(socket);
                                transaction.commit();
                                populateTables(); // Refresh the table data after deleting the brand
                                updateAllDropBoxes();
                                JOptionPane.showMessageDialog(null, "socket deleted successfully.");
                            } else {
                                JOptionPane.showMessageDialog(null, "socket not found.");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Failed to delete socket: " + ex);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No socket selected.");
                }
            }
        });

        pdfExportSocketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                LOGGER.addHandler(fileHandler);
                LOGGER.info("Trying to export data to PDF document");
                try 
                {
                    JFileChooser fileChooser = new JFileChooser();
                    // Set default folder to current directory
                    fileChooser.setCurrentDirectory(new File("."));
                    // Set default file name
                    fileChooser.setSelectedFile(new File("exported_Sockets.pdf"));
                    int result = fileChooser.showSaveDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) 
                    {
                        File selectedFile = fileChooser.getSelectedFile();
                        String fileName = selectedFile.getAbsolutePath();
                        // Append .pdf extension if necessary
                        if (!fileName.endsWith(".pdf")) 
                        {
                            fileName += ".pdf";
                        }
                        Document document = new Document();
                        PdfWriter.getInstance(document, new FileOutputStream(fileName));
                        document.open();
                        PdfPTable pdfTable = new PdfPTable(socketTable.getColumnCount());
                        
                        // Create font for table headers
                        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK);
                        String[] headersPdfExport = {"\nSocket name\n\n"};

                        // Set column headers
                        for (int i = 0; i < socketTable.getColumnCount(); i++) 
                        {
                            PdfPCell header = new PdfPCell(new Phrase(headersPdfExport[i], headerFont));
                            header.setBackgroundColor(BaseColor.ORANGE);
                            header.setBorderWidth(2);
                            header.setHorizontalAlignment(Element.ALIGN_CENTER);
                            // Give more weight to the first row
                            pdfTable.addCell(header);
                        }
                        
                        // Create font for table data
                        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
                        
                        // Set custom widths for each row 
                        float[] columnWidths = {1f};
                        pdfTable.setWidths(columnWidths);
                        
                        // Add table data
                        for (int i = 0; i < socketTable.getRowCount(); i++) 
                        {
                            for (int j = 0; j < socketTable.getColumnCount(); j++) 
                            {
                                PdfPCell data = new PdfPCell(new Phrase(socketTable.getValueAt(i, j).toString(), dataFont));
                                if (i % 2 == 1)
                                {
                                    data.setBackgroundColor(BaseColor.LIGHT_GRAY);
                                }
                                else
                                {
                                    data.setBackgroundColor(BaseColor.WHITE);
                                }
                                data.setBorderWidth(1);
                                data.setHorizontalAlignment(Element.ALIGN_LEFT);
                                pdfTable.addCell(data);
                            }
                        }
                        document.add(pdfTable);
                        document.close();
                        JOptionPane.showMessageDialog(socketPanel, "Exported table data to " + fileName);
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(socketPanel, "Error exporting table data to PDF");
                }
            }
        });


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //   ______   __    __  ______  _______    ______   ________  ________        __        ______   ______   ________  ________  __    __  ________  _______    ______   //
        //  /      \ /  |  /  |/      |/       \  /      \ /        |/        |      /  |      /      | /      \ /        |/        |/  \  /  |/        |/       \  /      \  //
        // /$$$$$$  |$$ |  $$ |$$$$$$/ $$$$$$$  |/$$$$$$  |$$$$$$$$/ $$$$$$$$/       $$ |      $$$$$$/ /$$$$$$  |$$$$$$$$/ $$$$$$$$/ $$  \ $$ |$$$$$$$$/ $$$$$$$  |/$$$$$$  | //
        // $$ |  $$/ $$ |__$$ |  $$ |  $$ |__$$ |$$ \__$$/ $$ |__       $$ |         $$ |        $$ |  $$ \__$$/    $$ |   $$ |__    $$$  \$$ |$$ |__    $$ |__$$ |$$ \__$$/  //
        // $$ |      $$    $$ |  $$ |  $$    $$/ $$      \ $$    |      $$ |         $$ |        $$ |  $$      \    $$ |   $$    |   $$$$  $$ |$$    |   $$    $$< $$      \  //
        // $$ |   __ $$$$$$$$ |  $$ |  $$$$$$$/   $$$$$$  |$$$$$/       $$ |         $$ |        $$ |   $$$$$$  |   $$ |   $$$$$/    $$ $$ $$ |$$$$$/    $$$$$$$  | $$$$$$  | //
        // $$ \__/  |$$ |  $$ | _$$ |_ $$ |      /  \__$$ |$$ |_____    $$ |         $$ |_____  _$$ |_ /  \__$$ |   $$ |   $$ |_____ $$ |$$$$ |$$ |_____ $$ |  $$ |/  \__$$ | //
        // $$    $$/ $$ |  $$ |/ $$   |$$ |      $$    $$/ $$       |   $$ |         $$       |/ $$   |$$    $$/    $$ |   $$       |$$ | $$$ |$$       |$$ |  $$ |$$    $$/  //
        //  $$$$$$/  $$/   $$/ $$$$$$/ $$/        $$$$$$/  $$$$$$$$/    $$/          $$$$$$$$/ $$$$$$/  $$$$$$/     $$/    $$$$$$$$/ $$/   $$/ $$$$$$$$/ $$/   $$/  $$$$$$/   //
        //                                                                                                                                                                    //
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        addChipsetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
                if (chipsetNameField.getText().isBlank())
                {
                    JOptionPane.showMessageDialog(null, "You must fill all text fields first!");
                }
                else
                {
                    if (chipsetNameField.getText().matches(chipsetNameRegex)) 
                    {
                        String name = chipsetNameField.getText();
                        Object[] rowData = {name};
                        ClassChipset chipset = new ClassChipset();
                        chipset.setName(name);
                        try (Session session = getSession())
                        {
                            Transaction transaction = session.beginTransaction();
                            session.save(chipset);
                            transaction.commit();
                            chipsetTableModel.addRow(rowData);
                            JOptionPane.showMessageDialog(null, "chipset added successfully.");
                            updateAllDropBoxes();
                        } 
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Failed to add chipset: " + ex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid data format!");
                    }
                }
            }
        });

        updateChipsetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
                int row = chipsetTable.getSelectedRow();
                if (row != -1) {
                    // create the popup window with yes/no options
                    int choice = JOptionPane.showConfirmDialog(chipsetPanel, "Do you wish to continue? ", "Confirmation", JOptionPane.YES_NO_OPTION);
                    // handle the user's choice
                    if (choice == JOptionPane.YES_OPTION) 
                    {
                        if (chipsetNameField.getText().matches(chipsetNameRegex)) 
                        {
                            int selectedRow = chipsetTable.getSelectedRow();
                            if (selectedRow != -1) {
                                String oldChipsetName = (String) chipsetTableModel.getValueAt(selectedRow, 0);
                                String newChipsetName = chipsetNameField.getText();
                                if (newChipsetName != null && !newChipsetName.isEmpty()) {
                                    try (Session session = getSession()) {
                                        Transaction transaction = session.beginTransaction();
                                        ClassChipset chipset = (ClassChipset) session.createQuery("FROM ClassChipset WHERE name = :name").setParameter("name", oldChipsetName).uniqueResult();
                                        if (chipset != null) {
                                            chipset.setName(newChipsetName);
                                            session.update(chipset);
                                            populateTables();
                                            transaction.commit();
                                            populateTables();
                                            updateAllDropBoxes();
                                            JOptionPane.showMessageDialog(null, "chipset updated successfully.");
                                        } else {
                                            JOptionPane.showMessageDialog(null, "chipset not found.");
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        JOptionPane.showMessageDialog(null, "Failed to update chipset: " + ex);
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Invalid chipset name.");
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "No chipset selected.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid data format!");
                        }
                    } else {
                        System.out.println("User clicked NO");
                    }
                } else {
                    JOptionPane.showMessageDialog(chipsetPanel, "Сan't update any record! Please select one!", "Error", row);
                }
            }
        });

        deleteChipsetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
                int selectedRow = chipsetTable.getSelectedRow();
                if (selectedRow != -1) {
                    // create the popup window with yes/no options
                    int choice = JOptionPane.showConfirmDialog(chipsetPanel, "Do you wish to continue? ", "Confirmation", JOptionPane.YES_NO_OPTION);
                    // handle the user's choice
                    if (choice == JOptionPane.YES_OPTION) 
                    {
                        String chipsetName = (String) chipsetTableModel.getValueAt(selectedRow, 0);
                
                        try (Session session = getSession()) {
                            Transaction transaction = session.beginTransaction();
                            ClassChipset chipset = (ClassChipset) session.createQuery("FROM ClassChipset WHERE name = :name").setParameter("name", chipsetName).uniqueResult();
                            if (chipset != null) {
                                session.delete(chipset);
                                transaction.commit();
                                populateTables(); // Refresh the table data after deleting the brand
                                updateAllDropBoxes();
                                JOptionPane.showMessageDialog(null, "chipset deleted successfully.");
                            } else {
                                JOptionPane.showMessageDialog(null, "chipset not found.");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Failed to delete chipset: " + ex);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No chipset selected.");
                }
            }
        });

        pdfExportChipsetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                LOGGER.addHandler(fileHandler);
                LOGGER.info("Trying to export data to PDF document");
                try 
                {
                    JFileChooser fileChooser = new JFileChooser();
                    // Set default folder to current directory
                    fileChooser.setCurrentDirectory(new File("."));
                    // Set default file name
                    fileChooser.setSelectedFile(new File("exported_Chipsets.pdf"));
                    int result = fileChooser.showSaveDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) 
                    {
                        File selectedFile = fileChooser.getSelectedFile();
                        String fileName = selectedFile.getAbsolutePath();
                        // Append .pdf extension if necessary
                        if (!fileName.endsWith(".pdf"))
                        {
                            fileName += ".pdf";
                        }
                        Document document = new Document();
                        PdfWriter.getInstance(document, new FileOutputStream(fileName));
                        document.open();
                        PdfPTable pdfTable = new PdfPTable(chipsetTable.getColumnCount());
                        
                        // Create font for table headers
                        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK);
                        String[] headersPdfExport = {"\nChipset name\n\n"};

                        // Set column headers
                        for (int i = 0; i < chipsetTable.getColumnCount(); i++) 
                        {
                            PdfPCell header = new PdfPCell(new Phrase(headersPdfExport[i], headerFont));
                            header.setBackgroundColor(BaseColor.ORANGE);
                            header.setBorderWidth(2);
                            header.setHorizontalAlignment(Element.ALIGN_CENTER);
                            // Give more weight to the first row
                            pdfTable.addCell(header);
                        }
                        
                        // Create font for table data
                        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
                        
                        // Set custom widths for each row 
                        float[] columnWidths = {1f};
                        pdfTable.setWidths(columnWidths);
                        
                        // Add table data
                        for (int i = 0; i < chipsetTable.getRowCount(); i++) 
                        {
                            for (int j = 0; j < chipsetTable.getColumnCount(); j++) 
                            {
                                PdfPCell data = new PdfPCell(new Phrase(chipsetTable.getValueAt(i, j).toString(), dataFont));
                                if (i % 2 == 1)
                                {
                                    data.setBackgroundColor(BaseColor.LIGHT_GRAY);
                                }
                                else
                                {
                                    data.setBackgroundColor(BaseColor.WHITE);
                                }
                                data.setBorderWidth(1);
                                data.setHorizontalAlignment(Element.ALIGN_LEFT);
                                pdfTable.addCell(data);
                            }
                        }
                        document.add(pdfTable);
                        document.close();
                        // JOptionPane.showMessageDialog(chipsetPanel, "Exported table data to " + fileName);
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(chipsetPanel, "Error exporting table data to PDF");
                }
            }
        });


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //   ______   ________  __        ________   ______   ________  ______   ______   __    __        __        ______   ______   ________  ________  __    __  ________  _______    ______   //
        //  /      \ /        |/  |      /        | /      \ /        |/      | /      \ /  \  /  |      /  |      /      | /      \ /        |/        |/  \  /  |/        |/       \  /      \  //
        // /$$$$$$  |$$$$$$$$/ $$ |      $$$$$$$$/ /$$$$$$  |$$$$$$$$/ $$$$$$/ /$$$$$$  |$$  \ $$ |      $$ |      $$$$$$/ /$$$$$$  |$$$$$$$$/ $$$$$$$$/ $$  \ $$ |$$$$$$$$/ $$$$$$$  |/$$$$$$  | //
        // $$ \__$$/ $$ |__    $$ |      $$ |__    $$ |  $$/    $$ |     $$ |  $$ |  $$ |$$$  \$$ |      $$ |        $$ |  $$ \__$$/    $$ |   $$ |__    $$$  \$$ |$$ |__    $$ |__$$ |$$ \__$$/  //
        // $$      \ $$    |   $$ |      $$    |   $$ |         $$ |     $$ |  $$ |  $$ |$$$$  $$ |      $$ |        $$ |  $$      \    $$ |   $$    |   $$$$  $$ |$$    |   $$    $$< $$      \  //
        //  $$$$$$  |$$$$$/    $$ |      $$$$$/    $$ |   __    $$ |     $$ |  $$ |  $$ |$$ $$ $$ |      $$ |        $$ |   $$$$$$  |   $$ |   $$$$$/    $$ $$ $$ |$$$$$/    $$$$$$$  | $$$$$$  | //
        // /  \__$$ |$$ |_____ $$ |_____ $$ |_____ $$ \__/  |   $$ |    _$$ |_ $$ \__$$ |$$ |$$$$ |      $$ |_____  _$$ |_ /  \__$$ |   $$ |   $$ |_____ $$ |$$$$ |$$ |_____ $$ |  $$ |/  \__$$ | //
        // $$    $$/ $$       |$$       |$$       |$$    $$/    $$ |   / $$   |$$    $$/ $$ | $$$ |      $$       |/ $$   |$$    $$/    $$ |   $$       |$$ | $$$ |$$       |$$ |  $$ |$$    $$/  //
        //  $$$$$$/  $$$$$$$$/ $$$$$$$$/ $$$$$$$$/  $$$$$$/     $$/    $$$$$$/  $$$$$$/  $$/   $$/       $$$$$$$$/ $$$$$$/  $$$$$$/     $$/    $$$$$$$$/ $$/   $$/ $$$$$$$$/ $$/   $$/  $$$$$$/   //
        //                                                                                                                                                                                        //
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Set table selection listeners

        brandTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) 
            {
                int selectedRow = brandTable.getSelectedRow();
                if (selectedRow >= 0) 
                {
                    String name = brandTableModel.getValueAt(selectedRow, 0).toString();
                    brandNameField.setText(name);
                }
            }
        });
        socketTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) 
            {
                int selectedRow = socketTable.getSelectedRow();
                if (selectedRow >= 0) 
                {
                    String name = socketTableModel.getValueAt(selectedRow, 0).toString();
                    socketNameField.setText(name);
                }
            }
        });

        chipsetTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) 
            {
                int selectedRow = chipsetTable.getSelectedRow();
                if (selectedRow >= 0) 
                {
                    String name = chipsetTableModel.getValueAt(selectedRow, 0).toString();
                    chipsetNameField.setText(name);
                }
            }
        });

        cpuTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) 
            {
                int selectedRow = cpuTable.getSelectedRow();
                if (selectedRow != -1) 
                {   // Update CPU fields with selected row data
                    cpuModelField.setText(cpuTableModel.getValueAt(selectedRow, 0).toString());
                    cpuPriceField.setText(cpuTableModel.getValueAt(selectedRow, 1).toString());
                    cpuCoresField.setText(cpuTableModel.getValueAt(selectedRow, 2).toString());
                    cpuThreadsField.setText(cpuTableModel.getValueAt(selectedRow, 3).toString());
                    cpuFrequencyField.setText(cpuTableModel.getValueAt(selectedRow, 4).toString());
                    // Get the values from the table model
                    String brand = cpuTableModel.getValueAt(selectedRow, 5).toString();
                    String socket = cpuTableModel.getValueAt(selectedRow, 6).toString();
                    // Set the selected item in the comboboxes
                    for (int i = 0; i < cpuBrandComboBox.getItemCount(); i++) 
                    {
                        String item = cpuBrandComboBox.getItemAt(i).toString();
                        if (item.equals(brand)) 
                        {
                            cpuBrandComboBox.setSelectedItem(item);
                            break;
                        }
                    }
                    for (int i = 0; i < cpuSocketComboBox.getItemCount(); i++) 
                    {
                        String item = cpuSocketComboBox.getItemAt(i).toString();
                        if (item.equals(socket)) 
                        {
                            cpuSocketComboBox.setSelectedItem(item);
                            break;
                        }
                    }
                }
            }
        });

        gpuTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) 
            {
                int selectedRow = gpuTable.getSelectedRow();
                if (selectedRow != -1) 
                {   // Update CPU fields with selected row data
                    gpuModelField.setText(gpuTableModel.getValueAt(selectedRow, 0).toString());
                    gpuPriceField.setText(gpuTableModel.getValueAt(selectedRow, 1).toString());
                    gpuCoresField.setText(gpuTableModel.getValueAt(selectedRow, 2).toString());
                    gpuMemoryField.setText(gpuTableModel.getValueAt(selectedRow, 3).toString());
                    gpuFrequencyField.setText(gpuTableModel.getValueAt(selectedRow, 4).toString());
                    // Get the values from the table model
                    String brand = gpuTableModel.getValueAt(selectedRow, 5).toString();
                    // Set the selected item in the comboboxes
                    for (int i = 0; i < gpuBrandComboBox.getItemCount(); i++) 
                    {
                        String item = gpuBrandComboBox.getItemAt(i).toString();
                        if (item.equals(brand))
                        {
                            gpuBrandComboBox.setSelectedItem(item);
                            break;
                        }
                    }
                }
            }
        });

        pcbTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) 
            {
                int selectedRow = pcbTable.getSelectedRow();
                if (selectedRow != -1) 
                {   // Update CPU fields with selected row data
                    pcbModelField.setText(pcbTableModel.getValueAt(selectedRow, 0).toString());
                    pcbPriceField.setText(pcbTableModel.getValueAt(selectedRow, 1).toString());
                    // Get the values from the table model
                    String brand = pcbTableModel.getValueAt(selectedRow, 2).toString();
                    String socket = pcbTableModel.getValueAt(selectedRow, 3).toString();
                    String chipset = pcbTableModel.getValueAt(selectedRow, 4).toString();
                    // Set the selected item in the comboboxes
                    for (int i = 0; i < pcbBrandComboBox.getItemCount(); i++) 
                    {
                        String item = pcbBrandComboBox.getItemAt(i).toString();
                        if (item.equals(brand)) 
                        {
                            pcbBrandComboBox.setSelectedItem(item);
                            break;
                        }
                    }
                    for (int i = 0; i < pcbSocketComboBox.getItemCount(); i++) 
                    {
                        String item = pcbSocketComboBox.getItemAt(i).toString();
                        if (item.equals(socket)) 
                        {
                            pcbSocketComboBox.setSelectedItem(item);
                            break;
                        }
                    }
                    for (int i = 0; i < pcbChipsetComboBox.getItemCount(); i++) 
                    {
                        String item = pcbChipsetComboBox.getItemAt(i).toString();
                        if (item.equals(chipset)) 
                        {
                            pcbChipsetComboBox.setSelectedItem(item);
                            break;
                        }
                    }
                }
            }
        });

} // action listeners (end)

        // Populate comboboxes
        populateComboboxes();
        // Set the selected tab to the first tab
        tabbedPane.setSelectedIndex(0);
        setVisible(true);
    } // end of the class
    

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //  _______    ______   _______   __    __  __         ______   ________  ________        ________  ______   _______   __        ________   ______         __       __  ________  ________  __    __   ______   _______    ______   //
    // /       \  /      \ /       \ /  |  /  |/  |       /      \ /        |/        |      /        |/      \ /       \ /  |      /        | /      \       /  \     /  |/        |/        |/  |  /  | /      \ /       \  /      \  //
    // $$$$$$$  |/$$$$$$  |$$$$$$$  |$$ |  $$ |$$ |      /$$$$$$  |$$$$$$$$/ $$$$$$$$/       $$$$$$$$//$$$$$$  |$$$$$$$  |$$ |      $$$$$$$$/ /$$$$$$  |      $$  \   /$$ |$$$$$$$$/ $$$$$$$$/ $$ |  $$ |/$$$$$$  |$$$$$$$  |/$$$$$$  | //
    // $$ |__$$ |$$ |  $$ |$$ |__$$ |$$ |  $$ |$$ |      $$ |__$$ |   $$ |   $$ |__             $$ |  $$ |__$$ |$$ |__$$ |$$ |      $$ |__    $$ \__$$/       $$$  \ /$$$ |$$ |__       $$ |   $$ |__$$ |$$ |  $$ |$$ |  $$ |$$ \__$$/  //
    // $$    $$/ $$ |  $$ |$$    $$/ $$ |  $$ |$$ |      $$    $$ |   $$ |   $$    |            $$ |  $$    $$ |$$    $$< $$ |      $$    |   $$      \       $$$$  /$$$$ |$$    |      $$ |   $$    $$ |$$ |  $$ |$$ |  $$ |$$      \  //
    // $$$$$$$/  $$ |  $$ |$$$$$$$/  $$ |  $$ |$$ |      $$$$$$$$ |   $$ |   $$$$$/             $$ |  $$$$$$$$ |$$$$$$$  |$$ |      $$$$$/     $$$$$$  |      $$ $$ $$/$$ |$$$$$/       $$ |   $$$$$$$$ |$$ |  $$ |$$ |  $$ | $$$$$$  | //
    // $$ |      $$ \__$$ |$$ |      $$ \__$$ |$$ |_____ $$ |  $$ |   $$ |   $$ |_____          $$ |  $$ |  $$ |$$ |__$$ |$$ |_____ $$ |_____ /  \__$$ |      $$ |$$$/ $$ |$$ |_____    $$ |   $$ |  $$ |$$ \__$$ |$$ |__$$ |/  \__$$ | //
    // $$ |      $$    $$/ $$ |      $$    $$/ $$       |$$ |  $$ |   $$ |   $$       |         $$ |  $$ |  $$ |$$    $$/ $$       |$$       |$$    $$/       $$ | $/  $$ |$$       |   $$ |   $$ |  $$ |$$    $$/ $$    $$/ $$    $$/  //
    // $$/        $$$$$$/  $$/        $$$$$$/  $$$$$$$$/ $$/   $$/    $$/    $$$$$$$$/          $$/   $$/   $$/ $$$$$$$/  $$$$$$$$/ $$$$$$$$/  $$$$$$/        $$/      $$/ $$$$$$$$/    $$/    $$/   $$/  $$$$$$/  $$$$$$$/   $$$$$$/   //
    //                                                                                                                                                                                                                                  //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Populate the tables with data from the database
    private void populateTables() 
    {
        // Clear tables
        brandTableModel.setRowCount(0);
        socketTableModel.setRowCount(0);
        chipsetTableModel.setRowCount(0);
        cpuTableModel.setRowCount(0);
        gpuTableModel.setRowCount(0);
        pcbTableModel.setRowCount(0);
        // Populate the tables with data from the database  
        try (Session session = getSession()) {
            List<ClassBrand> brands = session.createQuery("FROM ClassBrand", ClassBrand.class).list();
            for (ClassBrand brand : brands) {
                brandTableModel.addRow(new Object[]{brand.getName()});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Session session = getSession()) {
            List<ClassChipset> chipsets = session.createQuery("FROM ClassChipset", ClassChipset.class).list();
            for (ClassChipset chipset : chipsets) {
                chipsetTableModel.addRow(new Object[]{chipset.getName()});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Session session = getSession()) {
            List<ClassSocket> sockets = session.createQuery("FROM ClassSocket", ClassSocket.class).list();
            for (ClassSocket socket : sockets) {
                socketTableModel.addRow(new Object[]{socket.getName()});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Session session = getSession()) {
            List<ClassCPU> cpus = session.createQuery("FROM ClassCPU", ClassCPU.class).list();
            for (ClassCPU cpu : cpus) {
                cpuTableModel.addRow(new Object[]{cpu.getModel(), cpu.getPrice(), cpu.getCores(), cpu.getThreads(), cpu.getFrequency(), cpu.getBrand().getName(), cpu.getSocket().getName()});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Session session = getSession()) {
            List<ClassGPU> gpus = session.createQuery("FROM ClassGPU", ClassGPU.class).list();
            for (ClassGPU gpu : gpus) {
                gpuTableModel.addRow(new Object[]{gpu.getModel(), gpu.getPrice(), gpu.getCores(), gpu.getMemory(), gpu.getFrequency(), gpu.getBrand().getName()});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Session session = getSession()) {
            List<ClassPCB> motherboards = session.createQuery("FROM ClassPCB", ClassPCB.class).list();
            for (ClassPCB motherboard : motherboards) {
                pcbTableModel.addRow(new Object[]{motherboard.getModel(), motherboard.getPrice(), motherboard.getBrand().getName(), motherboard.getSocket().getName(), motherboard.getChipset().getName()});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //  _______   ________  ________  _______   ______  __     __  ________        _______    ______   ________  ______         __       __  ________  ________  __    __   ______   _______    ______   //
    // /       \ /        |/        |/       \ /      |/  |   /  |/        |      /       \  /      \ /        |/      \       /  \     /  |/        |/        |/  |  /  | /      \ /       \  /      \  //
    // $$$$$$$  |$$$$$$$$/ $$$$$$$$/ $$$$$$$  |$$$$$$/ $$ |   $$ |$$$$$$$$/       $$$$$$$  |/$$$$$$  |$$$$$$$$//$$$$$$  |      $$  \   /$$ |$$$$$$$$/ $$$$$$$$/ $$ |  $$ |/$$$$$$  |$$$$$$$  |/$$$$$$  | //
    // $$ |__$$ |$$ |__       $$ |   $$ |__$$ |  $$ |  $$ |   $$ |$$ |__          $$ |  $$ |$$ |__$$ |   $$ |  $$ |__$$ |      $$$  \ /$$$ |$$ |__       $$ |   $$ |__$$ |$$ |  $$ |$$ |  $$ |$$ \__$$/  //
    // $$    $$< $$    |      $$ |   $$    $$<   $$ |  $$  \ /$$/ $$    |         $$ |  $$ |$$    $$ |   $$ |  $$    $$ |      $$$$  /$$$$ |$$    |      $$ |   $$    $$ |$$ |  $$ |$$ |  $$ |$$      \  //
    // $$$$$$$  |$$$$$/       $$ |   $$$$$$$  |  $$ |   $$  /$$/  $$$$$/          $$ |  $$ |$$$$$$$$ |   $$ |  $$$$$$$$ |      $$ $$ $$/$$ |$$$$$/       $$ |   $$$$$$$$ |$$ |  $$ |$$ |  $$ | $$$$$$  | //
    // $$ |  $$ |$$ |_____    $$ |   $$ |  $$ | _$$ |_   $$ $$/   $$ |_____       $$ |__$$ |$$ |  $$ |   $$ |  $$ |  $$ |      $$ |$$$/ $$ |$$ |_____    $$ |   $$ |  $$ |$$ \__$$ |$$ |__$$ |/  \__$$ | //
    // $$ |  $$ |$$       |   $$ |   $$ |  $$ |/ $$   |   $$$/    $$       |      $$    $$/ $$ |  $$ |   $$ |  $$ |  $$ |      $$ | $/  $$ |$$       |   $$ |   $$ |  $$ |$$    $$/ $$    $$/ $$    $$/  //
    // $$/   $$/ $$$$$$$$/    $$/    $$/   $$/ $$$$$$/     $/     $$$$$$$$/       $$$$$$$/  $$/   $$/    $$/   $$/   $$/       $$/      $$/ $$$$$$$$/    $$/    $$/   $$/  $$$$$$/  $$$$$$$/   $$$$$$/   //
    //                                                                                                                                                                                                   //
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private List<ClassBrand> retrieveBrands() 
    {
        List<ClassBrand> brands = null;
        try 
        {
            transaction = session.beginTransaction();
            brands = session.createQuery("FROM ClassBrand", ClassBrand.class).list();
            transaction.commit();
        } 
        catch (Exception e) 
        {
            if (transaction != null) 
            {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return brands;
    }

    private List<ClassChipset> retrieveChipsets() 
    {
        List<ClassChipset> chipsets = null;
        try 
        {
            transaction = session.beginTransaction();
            chipsets = session.createQuery("FROM ClassChipset", ClassChipset.class).list();
            transaction.commit();
        } 
        catch (Exception e) 
        {
            if (transaction != null) 
            {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return chipsets;
    }

    private List<ClassCPU> retrieveCPUs() 
    {
        List<ClassCPU> cpus = null;
        try 
        {
            transaction = session.beginTransaction();
            cpus = session.createQuery("FROM ClassCPU", ClassCPU.class).list();
            transaction.commit();
        } 
        catch (Exception e) 
        {
            if (transaction != null) 
            {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return cpus;
    }

    private List<ClassGPU> retrieveGPUs() 
    {
        List<ClassGPU> gpus = null;
        try 
        {
            transaction = session.beginTransaction();
            gpus = session.createQuery("FROM ClassGPU", ClassGPU.class).list();
            transaction.commit();
        } 
        catch (Exception e) 
        {
            if (transaction != null) 
            {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return gpus;
    }

    private List<ClassPCB> retrievePCBs() 
    {
        List<ClassPCB> pcbs = null;
        try 
        {
            transaction = session.beginTransaction();
            pcbs = session.createQuery("FROM ClassPCB", ClassPCB.class).list();
            transaction.commit();
        } 
        catch (Exception e) 
        {
            if (transaction != null) 
            {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return pcbs;
    }

    private List<ClassSocket> retrieveSockets() 
    {
        List<ClassSocket> sockets = null;
        try 
        {
            transaction = session.beginTransaction();
            sockets = session.createQuery("FROM ClassSocket", ClassSocket.class).list();
            transaction.commit();
        } 
        catch (Exception e) 
        {
            if (transaction != null) 
            {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return sockets;
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //  __    __  ________  __        _______   ________  _______         __       __  ________  ________  __    __   ______   _______    ______   //
    // /  |  /  |/        |/  |      /       \ /        |/       \       /  \     /  |/        |/        |/  |  /  | /      \ /       \  /      \  //
    // $$ |  $$ |$$$$$$$$/ $$ |      $$$$$$$  |$$$$$$$$/ $$$$$$$  |      $$  \   /$$ |$$$$$$$$/ $$$$$$$$/ $$ |  $$ |/$$$$$$  |$$$$$$$  |/$$$$$$  | //
    // $$ |__$$ |$$ |__    $$ |      $$ |__$$ |$$ |__    $$ |__$$ |      $$$  \ /$$$ |$$ |__       $$ |   $$ |__$$ |$$ |  $$ |$$ |  $$ |$$ \__$$/  //
    // $$    $$ |$$    |   $$ |      $$    $$/ $$    |   $$    $$<       $$$$  /$$$$ |$$    |      $$ |   $$    $$ |$$ |  $$ |$$ |  $$ |$$      \  //
    // $$$$$$$$ |$$$$$/    $$ |      $$$$$$$/  $$$$$/    $$$$$$$  |      $$ $$ $$/$$ |$$$$$/       $$ |   $$$$$$$$ |$$ |  $$ |$$ |  $$ | $$$$$$  | //
    // $$ |  $$ |$$ |_____ $$ |_____ $$ |      $$ |_____ $$ |  $$ |      $$ |$$$/ $$ |$$ |_____    $$ |   $$ |  $$ |$$ \__$$ |$$ |__$$ |/  \__$$ | //
    // $$ |  $$ |$$       |$$       |$$ |      $$       |$$ |  $$ |      $$ | $/  $$ |$$       |   $$ |   $$ |  $$ |$$    $$/ $$    $$/ $$    $$/  //
    // $$/   $$/ $$$$$$$$/ $$$$$$$$/ $$/       $$$$$$$$/ $$/   $$/       $$/      $$/ $$$$$$$$/    $$/    $$/   $$/  $$$$$$/  $$$$$$$/   $$$$$$/   //
    //                                                                                                                                             //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Helper method to create a panel with buttons
    private JPanel createButtonPanel(JButton addButton, JButton deleteButton, JButton updateButton,  JButton pdfExportButton) {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(pdfExportButton);
        return buttonPanel;
    }

    // Helper method to retrieve Hibernate session
    private Session getSession() {
        Configuration configuration = new Configuration().configure();
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        return sessionFactory.openSession();
    }

    private void updateAllDropBoxes() {
        cpuBrandComboBox.removeAllItems();
        cpuSocketComboBox.removeAllItems();
        gpuBrandComboBox.removeAllItems();
        pcbBrandComboBox .removeAllItems();
        pcbSocketComboBox.removeAllItems();
        pcbChipsetComboBox.removeAllItems();
        try (Session session = getSession()) {
            List<ClassBrand> brands = session.createQuery("FROM ClassBrand", ClassBrand.class).list();
            for (ClassBrand brand : brands) {
                cpuBrandComboBox.addItem(brand.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Session session = getSession()) {
            List<ClassSocket> sockets = session.createQuery("FROM ClassSocket", ClassSocket.class).list();
            for (ClassSocket socket : sockets) {
                cpuSocketComboBox.addItem(socket.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Session session = getSession()) {
            List<ClassBrand> brands = session.createQuery("FROM ClassBrand", ClassBrand.class).list();
            for (ClassBrand brand : brands) {
                gpuBrandComboBox.addItem(brand.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Session session = getSession()) {
            List<ClassBrand> brands = session.createQuery("FROM ClassBrand", ClassBrand.class).list();
            for (ClassBrand brand : brands) {
                pcbBrandComboBox.addItem(brand.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Session session = getSession()) {
            List<ClassSocket> sockets = session.createQuery("FROM ClassSocket", ClassSocket.class).list();
            for (ClassSocket socket : sockets) {
                pcbSocketComboBox.addItem(socket.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Session session = getSession()) {
            List<ClassChipset> chipsets = session.createQuery("FROM ClassChipset", ClassChipset.class).list();
            for (ClassChipset chipset : chipsets) {
                pcbChipsetComboBox.addItem(chipset.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void applyCPUFilter() {
        String column = filterCPUComboBox.getSelectedItem().toString();
        String value = filterCPUTextField.getText(); // .getText().trim();

        if (column.isEmpty() || value.isEmpty()) {
            // If either the column or value is empty, show all rows
            showAllRows(cpuTable, cpuTableModel);
        } else {
            // Filter the rows based on the selected column and value
            List<Integer> filteredRows = new ArrayList<>();
            for (int i = 0; i < cpuTableModel.getRowCount(); i++) 
            {
                Object cellValue = cpuTableModel.getValueAt(i, getCPUColumnIndex(column));
                if (cellValue != null && cellValue.toString().toLowerCase().contains(value.toLowerCase())) 
                {
                    filteredRows.add(i);
                }
            }
            // Show only the filtered rows
            showFilteredRows(cpuTable, filteredRows);
        }
    }
    
    private void applyGPUFilter() {
        String column = filterGPUComboBox.getSelectedItem().toString();
        String value = filterGPUTextField.getText(); // .getText().trim();

        if (column.isEmpty() || value.isEmpty()) {
            // If either the column or value is empty, show all rows
            showAllRows(gpuTable, gpuTableModel);
        } else {
            // Filter the rows based on the selected column and value
            List<Integer> filteredRows = new ArrayList<>();
            for (int i = 0; i < gpuTableModel.getRowCount(); i++) 
            {
                Object cellValue = gpuTableModel.getValueAt(i, getGPUColumnIndex(column));
                if (cellValue != null && cellValue.toString().toLowerCase().contains(value.toLowerCase())) 
                {
                    filteredRows.add(i);
                }
            }
            // Show only the filtered rows
            showFilteredRows(gpuTable, filteredRows);
        }
    }
    
    private void applyPCBFilter() {
        String column = filterPCBComboBox.getSelectedItem().toString();
        String value = filterPCBTextField.getText(); // .getText().trim();

        if (column.isEmpty() || value.isEmpty()) {
            // If either the column or value is empty, show all rows
            showAllRows(pcbTable, pcbTableModel);
        } else {
            // Filter the rows based on the selected column and value
            List<Integer> filteredRows = new ArrayList<>();
            for (int i = 0; i < pcbTableModel.getRowCount(); i++) 
            {
                Object cellValue = pcbTableModel.getValueAt(i, getPCBColumnIndex(column));
                if (cellValue != null && cellValue.toString().toLowerCase().contains(value.toLowerCase())) 
                {
                    filteredRows.add(i);
                }
            }
            // Show only the filtered rows
            showFilteredRows(pcbTable, filteredRows);
        }
    }

    private int getCPUColumnIndex(String column) {
        switch (column) {
            case "Model":
                return 0;
            case "Price":
                return 1;
            case "Cores":
                return 2;
            case "Threads":
                return 3;
            case "Frequency":
                return 4;
            case "Brand":
                return 5;
            case "Socket":
                return 6;
            default:
                return -1; // Return -1 for unknown options
        }
    }
    
    private int getGPUColumnIndex(String column) {
        switch (column) {
            case "Model":
                return 0;
            case "Price":
                return 1;
            case "Cores":
                return 2;
            case "Memory":
                return 3;
            case "Frequency":
                return 4;
            case "Brand":
                return 5;
            default:
                return -1; // Return -1 for unknown options
        }
    }
    
    private int getPCBColumnIndex(String column) {
        switch (column) {
            case "Model":
                return 0;
            case "Price":
                return 1;
            case "Brand":
                return 2;
            case "Socket":
                return 3;
            case "Chipset":
                return 4;
            default:
                return -1; // Return -1 for unknown options
        }
    }

    private void showAllRows(JTable table, DefaultTableModel model) {
        table.clearSelection();
        for (int i = 0; i < model.getRowCount(); i++) {
            table.addRowSelectionInterval(i, i);
        }
    }

    private void showFilteredRows(JTable table, List<Integer> rows) {
        table.clearSelection();
        for (int row : rows) {
            table.addRowSelectionInterval(row, row);
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //  __    __  __    __  ______  ________        ________  ______   _______         __       __  ________  ________  __    __   ______   _______    ______   //
    // /  |  /  |/  \  /  |/      |/        |      /        |/      \ /       \       /  \     /  |/        |/        |/  |  /  | /      \ /       \  /      \  //
    // $$ |  $$ |$$  \ $$ |$$$$$$/ $$$$$$$$/       $$$$$$$$//$$$$$$  |$$$$$$$  |      $$  \   /$$ |$$$$$$$$/ $$$$$$$$/ $$ |  $$ |/$$$$$$  |$$$$$$$  |/$$$$$$  | //
    // $$ |  $$ |$$$  \$$ |  $$ |     $$ |            $$ |  $$ |__$$ |$$ |__$$ |      $$$  \ /$$$ |$$ |__       $$ |   $$ |__$$ |$$ |  $$ |$$ |  $$ |$$ \__$$/  //
    // $$ |  $$ |$$$$  $$ |  $$ |     $$ |            $$ |  $$    $$ |$$    $$<       $$$$  /$$$$ |$$    |      $$ |   $$    $$ |$$ |  $$ |$$ |  $$ |$$      \  //
    // $$ |  $$ |$$ $$ $$ |  $$ |     $$ |            $$ |  $$$$$$$$ |$$$$$$$  |      $$ $$ $$/$$ |$$$$$/       $$ |   $$$$$$$$ |$$ |  $$ |$$ |  $$ | $$$$$$  | //
    // $$ \__$$ |$$ |$$$$ | _$$ |_    $$ |            $$ |  $$ |  $$ |$$ |__$$ |      $$ |$$$/ $$ |$$ |_____    $$ |   $$ |  $$ |$$ \__$$ |$$ |__$$ |/  \__$$ | //
    // $$    $$/ $$ | $$$ |/ $$   |   $$ |            $$ |  $$ |  $$ |$$    $$/       $$ | $/  $$ |$$       |   $$ |   $$ |  $$ |$$    $$/ $$    $$/ $$    $$/  //
    //  $$$$$$/  $$/   $$/ $$$$$$/    $$/             $$/   $$/   $$/ $$$$$$$/        $$/      $$/ $$$$$$$$/    $$/    $$/   $$/  $$$$$$/  $$$$$$$/   $$$$$$/   //
    //                                                                                                                                                          //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void populateComboboxes() {
        // Populate the comboboxes with data from the database
        sockets = retrieveSockets();
        for (ClassSocket socket : sockets) {
            socketComboBox.addItem(socket.getName());
        }
        gpus = retrieveGPUs();
        for (ClassGPU gpu : gpus) {
            gpuComboBox.addItem(gpu.getModel());
        }
        updateUnitComboBoxes(); // update PCB and CPU combo boxes
    }

    private void updateUnitComboBoxes() {
        pcbComboBox.removeAllItems();
        cpuComboBox.removeAllItems();
        ClassSocket socket = (ClassSocket) session.createQuery("FROM ClassSocket WHERE name = :name").setParameter("name", socketComboBox.getSelectedItem()).uniqueResult();
        pcbs = retrievePCBsBySocket(socket);
        for (ClassPCB pcb : pcbs) {
            pcbComboBox.addItem(pcb.getModel());
        }
        cpus = retrieveCPUsBySocket(socket);
        for (ClassCPU cpu : cpus) {
            cpuComboBox.addItem(cpu.getModel());
        }
    }

    private List<ClassPCB> retrievePCBsBySocket(ClassSocket socket) 
    {
        List<ClassPCB> pcbs = null;
        try 
        {
            transaction = session.beginTransaction();
            // pcbs = session.createQuery("FROM ClassPCB", ClassPCB.class).list();
            pcbs = session.createQuery("FROM ClassPCB WHERE socket_id = :socket", ClassPCB.class).setParameter("socket", socket).list();
            transaction.commit();
        } 
        catch (Exception e) 
        {
            if (transaction != null) 
            {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return pcbs;
    }

    private List<ClassCPU> retrieveCPUsBySocket(ClassSocket socket) 
    {
        List<ClassCPU> cpus = null;
        try 
        {
            transaction = session.beginTransaction();
            // cpus = session.createQuery("FROM ClassCPU", ClassCPU.class).list();
            cpus = session.createQuery("FROM ClassCPU WHERE socket_id = :socket", ClassCPU.class).setParameter("socket", socket).list();
            transaction.commit();
        } 
        catch (Exception e) 
        {
            if (transaction != null) 
            {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return cpus;
    }

    private void addUnit() {
        if (socketComboBox.getItemCount() > 0 && pcbComboBox.getItemCount() > 0 && cpuComboBox.getItemCount() > 0 && gpuComboBox.getItemCount() > 0) {
            DecimalFormat df = new DecimalFormat("#.##");
            String pcbModel = (String) pcbComboBox.getSelectedItem();
            String cpuModel = (String) cpuComboBox.getSelectedItem();
            String gpuModel = (String) gpuComboBox.getSelectedItem();
            // Get the selected PCB, CPU, and GPU objects from their respective lists
            ClassPCB selectedPCB = getPCBByModel(pcbModel);
            ClassCPU selectedCPU = getCPUByModel(cpuModel);
            ClassGPU selectedGPU = getGPUByModel(gpuModel);
            // Calculate the total price of the unit
            double totalPrice = selectedPCB.getPrice() + selectedCPU.getPrice() + selectedGPU.getPrice();
            String formattedPrice = df.format(totalPrice);
            // Add the unit to the table
            unitTableModel.addRow(new Object[]{pcbModel, cpuModel, gpuModel, selectedCPU.getCores(), selectedGPU.getMemory(), formattedPrice});
        } else {
            JOptionPane.showMessageDialog(null, "Combo boxes can't be empty. \nCheck if your db has required items.");
        }
    }

    private void removeUnit() {
        int selectedRow = unitTable.getSelectedRow();
        if (selectedRow >= 0) {
            unitTableModel.removeRow(selectedRow);
        }
    }

    // Methods to retrieve data from the database
    // You need to implement these methods based on your database access code
    private ClassCPU getCPUByModel(String cpuModel) {
        for (ClassCPU cpu : cpus) {
            if (cpu.getModel().equals(cpuModel)) {
                return cpu;
            }
        }
        return null;
    }
    private ClassGPU getGPUByModel(String gpuModel) {
        for (ClassGPU gpu : gpus) {
            if (gpu.getModel().equals(gpuModel)) {
                return gpu;
            }
        }
        return null;
    }
    private ClassPCB getPCBByModel(String pcbModel) {
        for (ClassPCB pcb : pcbs) {
            if (pcb.getModel().equals(pcbModel)) {
                return pcb;
            }
        }
        return null;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //  _______   __    __  __    __        __  _______   ________  _______   __    __   ______         _______   _______    ______    ______   _______    ______   __       __  //
    // /       \ /  |  /  |/  \  /  |      /  |/       \ /        |/       \ /  |  /  | /      \       /       \ /       \  /      \  /      \ /       \  /      \ /  \     /  | //
    // $$$$$$$  |$$ |  $$ |$$  \ $$ |     /$$/ $$$$$$$  |$$$$$$$$/ $$$$$$$  |$$ |  $$ |/$$$$$$  |      $$$$$$$  |$$$$$$$  |/$$$$$$  |/$$$$$$  |$$$$$$$  |/$$$$$$  |$$  \   /$$ | //
    // $$ |__$$ |$$ |  $$ |$$$  \$$ |    /$$/  $$ |  $$ |$$ |__    $$ |__$$ |$$ |  $$ |$$ | _$$/       $$ |__$$ |$$ |__$$ |$$ |  $$ |$$ | _$$/ $$ |__$$ |$$ |__$$ |$$$  \ /$$$ | //
    // $$    $$< $$ |  $$ |$$$$  $$ |   /$$/   $$ |  $$ |$$    |   $$    $$< $$ |  $$ |$$ |/    |      $$    $$/ $$    $$< $$ |  $$ |$$ |/    |$$    $$< $$    $$ |$$$$  /$$$$ | //
    // $$$$$$$  |$$ |  $$ |$$ $$ $$ |  /$$/    $$ |  $$ |$$$$$/    $$$$$$$  |$$ |  $$ |$$ |$$$$ |      $$$$$$$/  $$$$$$$  |$$ |  $$ |$$ |$$$$ |$$$$$$$  |$$$$$$$$ |$$ $$ $$/$$ | //
    // $$ |  $$ |$$ \__$$ |$$ |$$$$ | /$$/     $$ |__$$ |$$ |_____ $$ |__$$ |$$ \__$$ |$$ \__$$ |      $$ |      $$ |  $$ |$$ \__$$ |$$ \__$$ |$$ |  $$ |$$ |  $$ |$$ |$$$/ $$ | //
    // $$ |  $$ |$$    $$/ $$ | $$$ |/$$/      $$    $$/ $$       |$$    $$/ $$    $$/ $$    $$/       $$ |      $$ |  $$ |$$    $$/ $$    $$/ $$ |  $$ |$$ |  $$ |$$ | $/  $$ | //
    // $$/   $$/  $$$$$$/  $$/   $$/ $$/       $$$$$$$/  $$$$$$$$/ $$$$$$$/   $$$$$$/   $$$$$$/        $$/       $$/   $$/  $$$$$$/   $$$$$$/  $$/   $$/ $$/   $$/ $$/      $$/  //
    //                                                                                                                                                                           //
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(new Runnable() 
        {
            public void run() 
            {
                try 
                {
                    fileHandler = new FileHandler("Logs.log"); // Initialize the file handler
                    fileHandler.setFormatter(new SimpleFormatter()); // Set the formatter for the file handler
                    LOGGER.addHandler(fileHandler); // Add the file handler to the logger
                    LOGGER.info("Logging started"); // Log some messages
                    // LOGGER.warning("This is a warning message");
                    // LOGGER.severe("This is a severe error message");
                } 
                catch (IOException e) 
                {
                    e.printStackTrace();
                }
                new DBMSApp();
            }
        });
    }
}
