package al.exe;


import java.io.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
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
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
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
// Hibernate
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
// Necessary classes
import al.exe.ClassCPU;
import al.exe.ClassGPU;
import al.exe.ClassPCB;
import al.exe.ClassBrand;
import al.exe.ClassSocket;
import al.exe.ClassChipset;
import al.exe.ClassSocketToChipset;
import al.exe.HibernateUtil;


///////////////////////////////////////////////////////////////////////////////
//  _______   _______   __       __   ______    ______   _______   _______   //
// /       \ /       \ /  \     /  | /      \  /      \ /       \ /       \  //
// $$$$$$$  |$$$$$$$  |$$  \   /$$ |/$$$$$$  |/$$$$$$  |$$$$$$$  |$$$$$$$  | //
// $$ |  $$ |$$ |__$$ |$$$  \ /$$$ |$$ \__$$/ $$ |__$$ |$$ |__$$ |$$ |__$$ | //
// $$ |  $$ |$$    $$< $$$$  /$$$$ |$$      \ $$    $$ |$$    $$/ $$    $$/  //
// $$ |  $$ |$$$$$$$  |$$ $$ $$/$$ | $$$$$$  |$$$$$$$$ |$$$$$$$/  $$$$$$$/   //
// $$ |__$$ |$$ |__$$ |$$ |$$$/ $$ |/  \__$$ |$$ |  $$ |$$ |      $$ |       //
// $$    $$/ $$    $$/ $$ | $/  $$ |$$    $$/ $$ |  $$ |$$ |      $$ |       //
// $$$$$$$/  $$$$$$$/  $$/      $$/  $$$$$$/  $$/   $$/ $$/       $$/        //
//                                                                           //
///////////////////////////////////////////////////////////////////////////////
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
    private JTable brandTable, chipsetTable, cpuTable, gpuTable, pcbTable, socketTable, socketToChipsetTable;
    private DefaultTableModel brandTableModel, chipsetTableModel, cpuTableModel, gpuTableModel, pcbTableModel, socketTableModel, socketToChipsetTableModel;
    // Buttons
    private JButton addCPUButton, deleteCPUButton, updateCPUButton, pdfExportCPUButton, commitCPUButton;
    private JButton addGPUButton, deleteGPUButton, updateGPUButton, pdfExportGPUButton, commitGPUButton;
    private JButton addPCBButton, deletePCBButton, updatePCBButton, pdfExportPCBButton, commitPCBButton;
    private JButton addBrandButton, deleteBrandButton, updateBrandButton, pdfExportBrandButton, commitBrandButton;
    private JButton addSocketButton, deleteSocketButton, updateSocketButton, pdfExportSocketButton, commitSocketButton;
    private JButton addChipsetButton, deleteChipsetButton, updateChipsetButton, pdfExportChipsetButton, commitChipsetButton;
    private JButton addSocketToChipsetButton, deleteSocketToChipsetButton, updateSocketToChipsetButton, pdfExportSocketToChipsetButton, commitSocketToChipsetButton;
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
    // SocketToShipset Fields
    private JComboBox<String> compatibilitySocketComboBox;
    private JComboBox<String> compatibilityChipsetComboBox;

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
        socketToChipsetTableModel = new DefaultTableModel(new Object[]{"Socket ID", "Chipset"}, 0);

        // Create tables
        cpuTable = new JTable(cpuTableModel);
        gpuTable = new JTable(gpuTableModel);
        pcbTable = new JTable(pcbTableModel);
        brandTable = new JTable(brandTableModel);
        socketTable = new JTable(socketTableModel);
        chipsetTable = new JTable(chipsetTableModel);
        socketToChipsetTable = new JTable(socketToChipsetTableModel);
        cpuTable.setDefaultEditor(Object.class, null);
        gpuTable.setDefaultEditor(Object.class, null);
        pcbTable.setDefaultEditor(Object.class, null);
        brandTable.setDefaultEditor(Object.class, null);
        socketTable.setDefaultEditor(Object.class, null);
        chipsetTable.setDefaultEditor(Object.class, null);
        socketToChipsetTable.setDefaultEditor(Object.class, null);

        // Add tables to scroll panes
        JScrollPane cpuScrollPane = new JScrollPane(cpuTable);
        JScrollPane gpuScrollPane = new JScrollPane(gpuTable);
        JScrollPane pcbScrollPane = new JScrollPane(pcbTable);
        JScrollPane brandScrollPane = new JScrollPane(brandTable);
        JScrollPane socketScrollPane = new JScrollPane(socketTable);
        JScrollPane chipsetScrollPane = new JScrollPane(chipsetTable);
        JScrollPane socketToChipsetScrollPane = new JScrollPane(socketToChipsetTable);

        // Create buttons
        addBrandButton = new JButton("Add");
        deleteBrandButton = new JButton("Delete");
        updateBrandButton = new JButton("Update");
        pdfExportBrandButton = new JButton("PDF");
        commitBrandButton = new JButton("Commit changes");


        addChipsetButton = new JButton("Add");
        deleteChipsetButton = new JButton("Delete");
        updateChipsetButton = new JButton("Update");
        pdfExportChipsetButton = new JButton("PDF");
        commitChipsetButton = new JButton("Commit changes");

        addCPUButton = new JButton("Add");
        deleteCPUButton = new JButton("Delete");
        updateCPUButton = new JButton("Update");
        pdfExportCPUButton = new JButton("PDF");
        commitCPUButton = new JButton("Commit changes");

        addGPUButton = new JButton("Add");
        deleteGPUButton = new JButton("Delete");
        updateGPUButton = new JButton("Update");
        pdfExportGPUButton = new JButton("PDF");
        commitGPUButton = new JButton("Commit changes");

        addPCBButton = new JButton("Add");
        deletePCBButton = new JButton("Delete");
        updatePCBButton = new JButton("Update");
        pdfExportPCBButton = new JButton("PDF");
        commitPCBButton = new JButton("Commit changes");

        addSocketButton = new JButton("Add");
        deleteSocketButton = new JButton("Delete");
        updateSocketButton = new JButton("Update");
        pdfExportSocketButton = new JButton("PDF");
        commitSocketButton = new JButton("Commit changes");

        addSocketToChipsetButton = new JButton("Add");
        deleteSocketToChipsetButton = new JButton("Delete");
        updateSocketToChipsetButton = new JButton("Update");
        pdfExportSocketToChipsetButton = new JButton("PDF");
        commitSocketToChipsetButton = new JButton("Commit changes");

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
        // Create SocketToChipset fields
        compatibilitySocketComboBox = new JComboBox<>();
        compatibilityChipsetComboBox = new JComboBox<>();

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
        // compatibility panel
        JPanel socketToChipsetInputPanel = new JPanel(new GridLayout(2, 2));
        socketToChipsetInputPanel.add(new JLabel("Socket Name:"));
        socketToChipsetInputPanel.add(compatibilitySocketComboBox);
        socketToChipsetInputPanel.add(new JLabel("Chipset Name:"));
        socketToChipsetInputPanel.add(compatibilityChipsetComboBox);
        for (ClassSocket tempsocket : tempsockets) 
        {
            compatibilitySocketComboBox.addItem(tempsocket.getName());
        }
        for (ClassChipset tempchipset : tempchipsets) 
        {
            compatibilityChipsetComboBox.addItem(tempchipset.getName());
        }

        /////////////////////////
        //                     //
        //    Button panels    //
        //                     //
        /////////////////////////
        JPanel cpuButtonPanel = createButtonPanel(addCPUButton, deleteCPUButton, updateCPUButton, pdfExportCPUButton, commitCPUButton);
        JPanel gpuButtonPanel = createButtonPanel(addGPUButton, deleteGPUButton, updateGPUButton, pdfExportGPUButton, commitGPUButton);
        JPanel pcbButtonPanel = createButtonPanel(addPCBButton, deletePCBButton, updatePCBButton, pdfExportPCBButton, commitPCBButton);
        JPanel brandButtonPanel = createButtonPanel(addBrandButton, deleteBrandButton, updateBrandButton, pdfExportBrandButton, commitBrandButton);
        JPanel socketButtonPanel = createButtonPanel(addSocketButton, deleteSocketButton, updateSocketButton, pdfExportSocketButton, commitSocketButton);
        JPanel chipsetButtonPanel = createButtonPanel(addChipsetButton, deleteChipsetButton, updateChipsetButton, pdfExportChipsetButton, commitChipsetButton);
        JPanel socketToChipsetButtonPanel = createButtonPanel(addSocketToChipsetButton, deleteSocketToChipsetButton, updateSocketToChipsetButton, pdfExportSocketToChipsetButton, commitSocketToChipsetButton);

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
        // Add Compatibility panel
        JPanel socketToChipsetPanel = new JPanel(new BorderLayout());
        socketToChipsetPanel.add(socketToChipsetScrollPane, BorderLayout.CENTER);
        socketToChipsetPanel.add(socketToChipsetButtonPanel, BorderLayout.SOUTH);
        socketToChipsetPanel.add(socketToChipsetInputPanel, BorderLayout.NORTH);

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
        tabbedPane.addTab("Compatible", socketToChipsetPanel);
        // Add tabbed pane to content pane
        add(tabbedPane);
        // Populate tables
        populateTables();


        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //   ______   ________  ______   _______   ________                   ______    ______   ________  ______   ______   __    __        __        ______   ______   ________  ________  __    __  ________  _______    ______   //
        //  /      \ /        |/      \ /       \ /        |                 /      \  /      \ /        |/      | /      \ /  \  /  |      /  |      /      | /      \ /        |/        |/  \  /  |/        |/       \  /      \  //
        // /$$$$$$  |$$$$$$$$//$$$$$$  |$$$$$$$  |$$$$$$$$/        __       /$$$$$$  |/$$$$$$  |$$$$$$$$/ $$$$$$/ /$$$$$$  |$$  \ $$ |      $$ |      $$$$$$/ /$$$$$$  |$$$$$$$$/ $$$$$$$$/ $$  \ $$ |$$$$$$$$/ $$$$$$$  |/$$$$$$  | //
        // $$ \__$$/    $$ |  $$ |__$$ |$$ |__$$ |   $$ |         /  |      $$ |__$$ |$$ |  $$/    $$ |     $$ |  $$ |  $$ |$$$  \$$ |      $$ |        $$ |  $$ \__$$/    $$ |   $$ |__    $$$  \$$ |$$ |__    $$ |__$$ |$$ \__$$/  //
        // $$      \    $$ |  $$    $$ |$$    $$<    $$ |         $$/       $$    $$ |$$ |         $$ |     $$ |  $$ |  $$ |$$$$  $$ |      $$ |        $$ |  $$      \    $$ |   $$    |   $$$$  $$ |$$    |   $$    $$< $$      \  //
        //  $$$$$$  |   $$ |  $$$$$$$$ |$$$$$$$  |   $$ |          __       $$$$$$$$ |$$ |   __    $$ |     $$ |  $$ |  $$ |$$ $$ $$ |      $$ |        $$ |   $$$$$$  |   $$ |   $$$$$/    $$ $$ $$ |$$$$$/    $$$$$$$  | $$$$$$  | //
        // /  \__$$ |   $$ |  $$ |  $$ |$$ |  $$ |   $$ |         /  |      $$ |  $$ |$$ \__/  |   $$ |    _$$ |_ $$ \__$$ |$$ |$$$$ |      $$ |_____  _$$ |_ /  \__$$ |   $$ |   $$ |_____ $$ |$$$$ |$$ |_____ $$ |  $$ |/  \__$$ | //
        // $$    $$/    $$ |  $$ |  $$ |$$ |  $$ |   $$ |         $$/       $$ |  $$ |$$    $$/    $$ |   / $$   |$$    $$/ $$ | $$$ |      $$       |/ $$   |$$    $$/    $$ |   $$       |$$ | $$$ |$$       |$$ |  $$ |$$    $$/  //
        //  $$$$$$/     $$/   $$/   $$/ $$/   $$/    $$/                    $$/   $$/  $$$$$$/     $$/    $$$$$$/  $$$$$$/  $$/   $$/       $$$$$$$$/ $$$$$$/  $$$$$$/     $$/    $$$$$$$$/ $$/   $$/ $$$$$$$$/ $$/   $$/  $$$$$$/   //
        //                                                                                                                                                                                                                           //
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


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
        addCPUButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                LOGGER.addHandler(fileHandler);
                LOGGER.info("Trying to add new data to the table");
                if (cpuModelField.getText().isBlank() || cpuPriceField.getText().isBlank() || cpuFrequencyField.getText().isBlank() || cpuCoresField.getText().isBlank() || cpuThreadsField.getText().isBlank())
                {
                    JOptionPane.showMessageDialog(cpuPanel, "You must fill all text fields first!");
                }
                else
                {
                    String model = cpuModelField.getText();
                    double price = Double.parseDouble(cpuPriceField.getText());
                    int cores = Integer.parseInt(cpuCoresField.getText());
                    int threads = Integer.parseInt(cpuThreadsField.getText());
                    int frequency = Integer.parseInt(cpuFrequencyField.getText());
                    String brand = cpuBrandComboBox.getSelectedItem().toString();
                    String socket = cpuSocketComboBox.getSelectedItem().toString();
                    Object[] rowData = {model, price, cores, threads, frequency, brand, socket};
                    cpuTableModel.addRow(rowData);
                }
            }
        });

        updateCPUButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                LOGGER.addHandler(fileHandler);
                LOGGER.info("Trying to update data");
                int row = cpuTable.getSelectedRow();
                if (row != -1) 
                {
                    // create the popup window with yes/no options
                    int choice = JOptionPane.showConfirmDialog(cpuPanel, "Do you wish to continue?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    // handle the user's choice
                    if (choice == JOptionPane.YES_OPTION) 
                    {
                        try
                        {
                            checkIfEmpty(cpuModelField);
                            checkIfEmpty(cpuPriceField);
                            checkIfEmpty(cpuCoresField);
                            checkIfEmpty(cpuThreadsField);
                            checkIfEmpty(cpuFrequencyField);
                            String model = cpuModelField.getText();
                            String brand = cpuBrandComboBox.getSelectedItem().toString();
                            double price = Double.parseDouble(cpuPriceField.getText());
                            int cores = Integer.parseInt(cpuCoresField.getText());
                            int threads = Integer.parseInt(cpuThreadsField.getText());
                            int frequency = Integer.parseInt(cpuFrequencyField.getText());
                            String socket = cpuSocketComboBox.getSelectedItem().toString();
                            cpuTableModel.setValueAt(model, row, 0);
                            cpuTableModel.setValueAt(price, row, 1);
                            cpuTableModel.setValueAt(cores, row, 2);
                            cpuTableModel.setValueAt(threads, row, 3);
                            cpuTableModel.setValueAt(frequency, row, 4);
                            cpuTableModel.setValueAt(brand, row, 5);
                            cpuTableModel.setValueAt(socket, row, 6);
                        }
                        catch(NullPointerException ex) 
                        {
                            JOptionPane.showMessageDialog(cpuPanel, "You must fill all text fields first!");
                        }
                        catch(TextFieldException myEx) 
                        {
                            JOptionPane.showMessageDialog(cpuPanel, "You must fill all text fields first!");
                        }
                    } 
                    else 
                    {
                        System.out.println("User clicked NO");
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(cpuPanel, "Сan't update any record! Please select one!", "Error", row);
                }
            }
        });

        deleteCPUButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                LOGGER.addHandler(fileHandler);
                LOGGER.info("Trying to delete record in your table");
                int row = cpuTable.getSelectedRow();
                if (row != -1) 
                {
                    // create the popup window with yes/no options
                    int choice = JOptionPane.showConfirmDialog(cpuPanel, "Do you wish to continue?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    // handle the user's choice
                    if (choice == JOptionPane.YES_OPTION) 
                    {
                        System.out.println("User clicked YES");
                        cpuTableModel.removeRow(row);
                    } 
                    else 
                    {
                        System.out.println("User clicked NO");
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(cpuPanel, "Сan't delete record! Please select one!", "Error", row);
                }
            }
        });

        pdfExportCPUButton.addActionListener(new ActionListener() 
        {
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
                    fileChooser.setSelectedFile(new File("../../../../../exported_CPUs.pdf"));
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

        commitCPUButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
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
        addGPUButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                LOGGER.addHandler(fileHandler);
                LOGGER.info("Trying to add new data to the table GPU");
                if (gpuModelField.getText().isBlank() || gpuPriceField.getText().isBlank() || gpuFrequencyField.getText().isBlank() || gpuCoresField.getText().isBlank() || gpuMemoryField.getText().isBlank())
                {
                    JOptionPane.showMessageDialog(gpuPanel, "You must fill all text fields first!");
                }
                else
                {
                    String model = gpuModelField.getText();
                    double price = Double.parseDouble(gpuPriceField.getText());
                    int cores = Integer.parseInt(gpuCoresField.getText());
                    int memory = Integer.parseInt(gpuMemoryField.getText());
                    int frequency = Integer.parseInt(gpuFrequencyField.getText());
                    String brand = gpuBrandComboBox.getSelectedItem().toString();
                    Object[] rowData = {model, price, cores, memory, frequency, brand};
                    gpuTableModel.addRow(rowData);
                }
            }
        });

        updateGPUButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                LOGGER.addHandler(fileHandler);
                LOGGER.info("Trying to update data");
                int row = gpuTable.getSelectedRow();
                if (row != -1) 
                {
                    // create the popup window with yes/no options
                    int choice = JOptionPane.showConfirmDialog(gpuPanel, "Do you wish to continue?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    // handle the user's choice
                    if (choice == JOptionPane.YES_OPTION) 
                    {
                        try 
                        {
                            checkIfEmpty(gpuModelField);
                            checkIfEmpty(gpuPriceField);
                            checkIfEmpty(gpuCoresField);
                            checkIfEmpty(gpuMemoryField);
                            checkIfEmpty(gpuFrequencyField);
                            String model = gpuModelField.getText();
                            String brand = gpuBrandComboBox.getSelectedItem().toString();
                            double price = Double.parseDouble(gpuPriceField.getText());
                            int cores = Integer.parseInt(gpuCoresField.getText());
                            int memory = Integer.parseInt(gpuMemoryField.getText());
                            int frequency = Integer.parseInt(gpuFrequencyField.getText());
                            gpuTable.setValueAt(model, row, 0);
                            gpuTable.setValueAt(price, row, 1);
                            gpuTable.setValueAt(cores, row, 2);
                            gpuTable.setValueAt(memory, row, 3);
                            gpuTable.setValueAt(frequency, row, 4);
                            gpuTable.setValueAt(brand, row, 5);
                        }
                        catch(NullPointerException ex) 
                        {
                            JOptionPane.showMessageDialog(gpuPanel, "You must fill all text fields first!");
                        }
                        catch(TextFieldException myEx) 
                        {
                            JOptionPane.showMessageDialog(gpuPanel, "You must fill all text fields first!");
                        }
                    } 
                    else 
                    {
                        System.out.println("User clicked NO");
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(gpuPanel, "Сan't update any record! Please select one!", "Error", row);
                }
            }
        });

        deleteGPUButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                LOGGER.addHandler(fileHandler);
                LOGGER.info("Trying to delete record in your table");
                int row = gpuTable.getSelectedRow();
                if (row != -1) 
                {
                    // create the popup window with yes/no options
                    int choice = JOptionPane.showConfirmDialog(gpuPanel, "Do you wish to continue?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    // handle the user's choice
                    if (choice == JOptionPane.YES_OPTION) 
                    {
                        System.out.println("User clicked YES");
                        gpuTableModel.removeRow(row);
                    } 
                    else 
                    {
                        System.out.println("User clicked NO");
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(gpuPanel, "Сan't delete record! Please select one!", "Error", row);
                }
            }
        });

        pdfExportGPUButton.addActionListener(new ActionListener() 
        {
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
                    fileChooser.setSelectedFile(new File("../../../../../exported_GPUs.pdf"));
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

        
        commitGPUButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
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
        addPCBButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                LOGGER.addHandler(fileHandler);
                LOGGER.info("Trying to add new data to the table PCB");
                if (pcbModelField.getText().isBlank() || pcbPriceField.getText().isBlank())
                {
                    JOptionPane.showMessageDialog(pcbPanel, "You must fill all text fields first!");
                }
                else
                {
                    String model = pcbModelField.getText();
                    double price = Double.parseDouble(pcbPriceField.getText());
                    String brand = pcbBrandComboBox.getSelectedItem().toString();
                    String socket = pcbSocketComboBox.getSelectedItem().toString();
                    String chipset= pcbChipsetComboBox.getSelectedItem().toString();
                    Object[] rowData = {model, price, brand, socket, chipset};
                    pcbTableModel.addRow(rowData);
                }
            }
        });

        updatePCBButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                LOGGER.addHandler(fileHandler);
                LOGGER.info("Trying to update data");
                int row = pcbTable.getSelectedRow();
                if (row != -1) 
                {
                    // create the popup window with yes/no options
                    int choice = JOptionPane.showConfirmDialog(pcbPanel, "Do you wish to continue?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    // handle the user's choice
                    if (choice == JOptionPane.YES_OPTION) 
                    {
                        try 
                        {
                            checkIfEmpty(pcbModelField);
                            checkIfEmpty(pcbPriceField);
                            String model = pcbModelField.getText();
                            double price = Double.parseDouble(pcbPriceField.getText());
                            String brand = pcbBrandComboBox.getSelectedItem().toString();
                            String socket = pcbSocketComboBox.getSelectedItem().toString();
                            String chipset = pcbChipsetComboBox.getSelectedItem().toString();
                            pcbTable.setValueAt(model, row, 0);
                            pcbTable.setValueAt(price, row, 1);
                            pcbTable.setValueAt(brand, row, 2);
                            pcbTable.setValueAt(socket, row, 3);
                            pcbTable.setValueAt(chipset, row, 4);
                        }
                        catch(NullPointerException ex) 
                        {
                            JOptionPane.showMessageDialog(pcbPanel, "You must fill all text fields first!");
                        }
                        catch(TextFieldException myEx) 
                        {
                            JOptionPane.showMessageDialog(pcbPanel, "You must fill all text fields first!");
                        }
                    } 
                    else 
                    {
                        System.out.println("User clicked NO");
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(pcbPanel, "Сan't update any record! Please select one!", "Error", row);
                }
            }
        });

        deletePCBButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                LOGGER.addHandler(fileHandler);
                LOGGER.info("Trying to delete record in your table");
                int row = pcbTable.getSelectedRow();
                if (row != -1) 
                {
                    // create the popup window with yes/no options
                    int choice = JOptionPane.showConfirmDialog(pcbPanel, "Do you wish to continue?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    // handle the user's choice
                    if (choice == JOptionPane.YES_OPTION) 
                    {
                        System.out.println("User clicked YES");
                        pcbTableModel.removeRow(row);
                    } 
                    else 
                    {
                        System.out.println("User clicked NO");
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(pcbPanel, "Сan't delete record! Please select one!", "Error", row);
                }
            }
        });

        pdfExportPCBButton.addActionListener(new ActionListener() 
        {
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
                    fileChooser.setSelectedFile(new File("../../../../../exported_PCBs.pdf"));
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
        
        commitPCBButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
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
        addBrandButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                LOGGER.addHandler(fileHandler);
                LOGGER.info("Trying to add new data to the table PCB");
                if (brandNameField.getText().isBlank())
                {
                    JOptionPane.showMessageDialog(brandPanel, "You must fill all text fields first!");
                }
                else
                {
                    String name = brandNameField.getText();
                    Object[] rowData = {name};
                    brandTableModel.addRow(rowData);
                }
            }
        });

        updateBrandButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                LOGGER.addHandler(fileHandler);
                LOGGER.info("Trying to update data");
                int row = brandTable.getSelectedRow();
                if (row != -1) 
                {
                    // create the popup window with yes/no options
                    int choice = JOptionPane.showConfirmDialog(brandPanel, "Do you wish to continue?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    // handle the user's choice
                    if (choice == JOptionPane.YES_OPTION) 
                    {
                        try 
                        {
                            checkIfEmpty(brandNameField);
                            String name = brandNameField.getText();
                            brandTable.setValueAt(name, row, 0);
                        }
                        catch(NullPointerException ex) 
                        {
                            JOptionPane.showMessageDialog(brandPanel, "You must fill all text fields first!");
                        }
                        catch(TextFieldException myEx) 
                        {
                            JOptionPane.showMessageDialog(brandPanel, "You must fill all text fields first!");
                        }
                    } 
                    else 
                    {
                        System.out.println("User clicked NO");
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(brandPanel, "Сan't update any record! Please select one!", "Error", row);
                }
            }
        });

        deleteBrandButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                LOGGER.addHandler(fileHandler);
                LOGGER.info("Trying to delete record in your table");
                int row = brandTable.getSelectedRow();
                if (row != -1) 
                {
                    // create the popup window with yes/no options
                    int choice = JOptionPane.showConfirmDialog(brandPanel, "Do you wish to continue?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    // handle the user's choice
                    if (choice == JOptionPane.YES_OPTION) 
                    {
                        System.out.println("User clicked YES");
                        brandTableModel.removeRow(row);
                    } 
                    else 
                    {
                        System.out.println("User clicked NO");
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(brandPanel, "Сan't delete record! Please select one!", "Error", row);
                }
            }
        });

        pdfExportBrandButton.addActionListener(new ActionListener() 
        {
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
                    fileChooser.setSelectedFile(new File("../../../../../exported_Brands.pdf"));
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
        
        commitBrandButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
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
        addSocketButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                LOGGER.addHandler(fileHandler);
                LOGGER.info("Trying to add new data to the table PCB");
                if (socketNameField.getText().isBlank())
                {
                    JOptionPane.showMessageDialog(socketPanel, "You must fill all text fields first!");
                }
                else
                {
                    String name = socketNameField.getText();
                    Object[] rowData = {name};
                    socketTableModel.addRow(rowData);
                }
            }
        });

        updateSocketButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                LOGGER.addHandler(fileHandler);
                LOGGER.info("Trying to update data");
                int row = socketTable.getSelectedRow();
                if (row != -1) 
                {
                    // create the popup window with yes/no options
                    int choice = JOptionPane.showConfirmDialog(socketPanel, "Do you wish to continue?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    // handle the user's choice
                    if (choice == JOptionPane.YES_OPTION) 
                    {
                        try 
                        {
                            checkIfEmpty(socketNameField);
                            String name = socketNameField.getText();
                            socketTable.setValueAt(name, row, 0);
                        }
                        catch(NullPointerException ex) 
                        {
                            JOptionPane.showMessageDialog(socketPanel, "You must fill all text fields first!");
                        }
                        catch(TextFieldException myEx) 
                        {
                            JOptionPane.showMessageDialog(socketPanel, "You must fill all text fields first!");
                        }
                    } 
                    else 
                    {
                        System.out.println("User clicked NO");
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(socketPanel, "Сan't update any record! Please select one!", "Error", row);
                }
            }
        });

        deleteSocketButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                LOGGER.addHandler(fileHandler);
                LOGGER.info("Trying to delete record in your table");
                int row = socketTable.getSelectedRow();
                if (row != -1) 
                {
                    // create the popup window with yes/no options
                    int choice = JOptionPane.showConfirmDialog(socketPanel, "Do you wish to continue?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    // handle the user's choice
                    if (choice == JOptionPane.YES_OPTION) 
                    {
                        System.out.println("User clicked YES");
                        socketTableModel.removeRow(row);
                    } 
                    else 
                    {
                        System.out.println("User clicked NO");
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(socketPanel, "Сan't delete record! Please select one!", "Error", row);
                }
            }
        });

        pdfExportSocketButton.addActionListener(new ActionListener() 
        {
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
                    fileChooser.setSelectedFile(new File("../../../../../exported_Sockets.pdf"));
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
        
        commitSocketButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
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
        addChipsetButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                LOGGER.addHandler(fileHandler);
                LOGGER.info("Trying to add new data to the table PCB");
                if (chipsetNameField.getText().isBlank())
                {
                    JOptionPane.showMessageDialog(chipsetPanel, "You must fill all text fields first!");
                }
                else
                {
                    String name = chipsetNameField.getText();
                    Object[] rowData = {name};
                    chipsetTableModel.addRow(rowData);
                }
            }
        });

        updateChipsetButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                LOGGER.addHandler(fileHandler);
                LOGGER.info("Trying to update data");
                int row = chipsetTable.getSelectedRow();
                if (row != -1) 
                {
                    // create the popup window with yes/no options
                    int choice = JOptionPane.showConfirmDialog(chipsetPanel, "Do you wish to continue?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    // handle the user's choice
                    if (choice == JOptionPane.YES_OPTION) 
                    {
                        try 
                        {
                            checkIfEmpty(chipsetNameField);
                            String name = chipsetNameField.getText();
                            chipsetTable.setValueAt(name, row, 0);
                        }
                        catch(NullPointerException ex) 
                        {
                            JOptionPane.showMessageDialog(chipsetPanel, "You must fill all text fields first!");
                        }
                        catch(TextFieldException myEx) 
                        {
                            JOptionPane.showMessageDialog(chipsetPanel, "You must fill all text fields first!");
                        }
                    } 
                    else 
                    {
                        System.out.println("User clicked NO");
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(chipsetPanel, "Сan't update any record! Please select one!", "Error", row);
                }
            }
        });

        deleteChipsetButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                LOGGER.addHandler(fileHandler);
                LOGGER.info("Trying to delete record in your table");
                int row = chipsetTable.getSelectedRow();
                if (row != -1) 
                {
                    // create the popup window with yes/no options
                    int choice = JOptionPane.showConfirmDialog(chipsetPanel, "Do you wish to continue?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    // handle the user's choice
                    if (choice == JOptionPane.YES_OPTION) 
                    {
                        System.out.println("User clicked YES");
                        chipsetTableModel.removeRow(row);
                    } 
                    else 
                    {
                        System.out.println("User clicked NO");
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(chipsetPanel, "Сan't delete record! Please select one!", "Error", row);
                }
            }
        });

        pdfExportChipsetButton.addActionListener(new ActionListener() 
        {
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
                    fileChooser.setSelectedFile(new File("../../../../../exported_Chipsets.pdf"));
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
        
        commitChipsetButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
            }
        });



        
        addPCBButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                LOGGER.addHandler(fileHandler);
                LOGGER.info("Trying to add new data to the table PCB");
                if (pcbModelField.getText().isBlank() || pcbPriceField.getText().isBlank())
                {
                    JOptionPane.showMessageDialog(pcbPanel, "You must fill all text fields first!");
                }
                else
                {
                    String model = pcbModelField.getText();
                    double price = Double.parseDouble(pcbPriceField.getText());
                    String brand = pcbBrandComboBox.getSelectedItem().toString();
                    String socket = pcbSocketComboBox.getSelectedItem().toString();
                    String chipset= pcbChipsetComboBox.getSelectedItem().toString();
                    Object[] rowData = {model, price, brand, socket, chipset};
                    pcbTableModel.addRow(rowData);
                }
            }
        });

        updatePCBButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                LOGGER.addHandler(fileHandler);
                LOGGER.info("Trying to update data");
                int row = pcbTable.getSelectedRow();
                if (row != -1) 
                {
                    // create the popup window with yes/no options
                    int choice = JOptionPane.showConfirmDialog(pcbPanel, "Do you wish to continue?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    // handle the user's choice
                    if (choice == JOptionPane.YES_OPTION) 
                    {
                        try 
                        {
                            checkIfEmpty(pcbModelField);
                            checkIfEmpty(pcbPriceField);
                            String model = pcbModelField.getText();
                            double price = Double.parseDouble(pcbPriceField.getText());
                            String brand = pcbBrandComboBox.getSelectedItem().toString();
                            String socket = pcbSocketComboBox.getSelectedItem().toString();
                            String chipset = pcbChipsetComboBox.getSelectedItem().toString();
                            pcbTable.setValueAt(model, row, 0);
                            pcbTable.setValueAt(price, row, 1);
                            pcbTable.setValueAt(brand, row, 2);
                            pcbTable.setValueAt(socket, row, 3);
                            pcbTable.setValueAt(chipset, row, 4);
                        }
                        catch(NullPointerException ex) 
                        {
                            JOptionPane.showMessageDialog(pcbPanel, "You must fill all text fields first!");
                        }
                        catch(TextFieldException myEx) 
                        {
                            JOptionPane.showMessageDialog(pcbPanel, "You must fill all text fields first!");
                        }
                    } 
                    else 
                    {
                        System.out.println("User clicked NO");
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(pcbPanel, "Сan't update any record! Please select one!", "Error", row);
                }
            }
        });

        deletePCBButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                LOGGER.addHandler(fileHandler);
                LOGGER.info("Trying to delete record in your table");
                int row = pcbTable.getSelectedRow();
                if (row != -1) 
                {
                    // create the popup window with yes/no options
                    int choice = JOptionPane.showConfirmDialog(pcbPanel, "Do you wish to continue?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    // handle the user's choice
                    if (choice == JOptionPane.YES_OPTION) 
                    {
                        System.out.println("User clicked YES");
                        pcbTableModel.removeRow(row);
                    } 
                    else 
                    {
                        System.out.println("User clicked NO");
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(pcbPanel, "Сan't delete record! Please select one!", "Error", row);
                }
            }
        });

        pdfExportPCBButton.addActionListener(new ActionListener() 
        {
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
                    fileChooser.setSelectedFile(new File("../../../../../exported_PCBs.pdf"));
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
        
        commitPCBButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
            }
        });
        

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //   ______    ______    ______   __    __  ________  ________        ________  ______          ______   __    __  ______  _______    ______   ________  ________        __        ______   ______   ________  ________  __    __  ________  _______    ______   //
        //  /      \  /      \  /      \ /  |  /  |/        |/        |      /        |/      \        /      \ /  |  /  |/      |/       \  /      \ /        |/        |      /  |      /      | /      \ /        |/        |/  \  /  |/        |/       \  /      \  //
        // /$$$$$$  |/$$$$$$  |/$$$$$$  |$$ | /$$/ $$$$$$$$/ $$$$$$$$/       $$$$$$$$//$$$$$$  |      /$$$$$$  |$$ |  $$ |$$$$$$/ $$$$$$$  |/$$$$$$  |$$$$$$$$/ $$$$$$$$/       $$ |      $$$$$$/ /$$$$$$  |$$$$$$$$/ $$$$$$$$/ $$  \ $$ |$$$$$$$$/ $$$$$$$  |/$$$$$$  | //
        // $$ \__$$/ $$ |  $$ |$$ |  $$/ $$ |/$$/  $$ |__       $$ |            $$ |  $$ |  $$ |      $$ |  $$/ $$ |__$$ |  $$ |  $$ |__$$ |$$ \__$$/ $$ |__       $$ |         $$ |        $$ |  $$ \__$$/    $$ |   $$ |__    $$$  \$$ |$$ |__    $$ |__$$ |$$ \__$$/  //
        // $$      \ $$ |  $$ |$$ |      $$  $$<   $$    |      $$ |            $$ |  $$ |  $$ |      $$ |      $$    $$ |  $$ |  $$    $$/ $$      \ $$    |      $$ |         $$ |        $$ |  $$      \    $$ |   $$    |   $$$$  $$ |$$    |   $$    $$< $$      \  //
        //  $$$$$$  |$$ |  $$ |$$ |   __ $$$$$  \  $$$$$/       $$ |            $$ |  $$ |  $$ |      $$ |   __ $$$$$$$$ |  $$ |  $$$$$$$/   $$$$$$  |$$$$$/       $$ |         $$ |        $$ |   $$$$$$  |   $$ |   $$$$$/    $$ $$ $$ |$$$$$/    $$$$$$$  | $$$$$$  | //
        // /  \__$$ |$$ \__$$ |$$ \__/  |$$ |$$  \ $$ |_____    $$ |            $$ |  $$ \__$$ |      $$ \__/  |$$ |  $$ | _$$ |_ $$ |      /  \__$$ |$$ |_____    $$ |         $$ |_____  _$$ |_ /  \__$$ |   $$ |   $$ |_____ $$ |$$$$ |$$ |_____ $$ |  $$ |/  \__$$ | //
        // $$    $$/ $$    $$/ $$    $$/ $$ | $$  |$$       |   $$ |            $$ |  $$    $$/       $$    $$/ $$ |  $$ |/ $$   |$$ |      $$    $$/ $$       |   $$ |         $$       |/ $$   |$$    $$/    $$ |   $$       |$$ | $$$ |$$       |$$ |  $$ |$$    $$/  //
        //  $$$$$$/   $$$$$$/   $$$$$$/  $$/   $$/ $$$$$$$$/    $$/             $$/    $$$$$$/         $$$$$$/  $$/   $$/ $$$$$$/ $$/        $$$$$$/  $$$$$$$$/    $$/          $$$$$$$$/ $$$$$$/  $$$$$$/     $$/    $$$$$$$$/ $$/   $$/ $$$$$$$$/ $$/   $$/  $$$$$$/   //
        //                                                                                                                                                                                                                                                               //
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        addSocketToChipsetButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                LOGGER.addHandler(fileHandler);
                LOGGER.info("Trying to add new data to the table PCB");
                String socket = compatibilitySocketComboBox.getSelectedItem().toString();
                String chipset= compatibilityChipsetComboBox.getSelectedItem().toString();
                Object[] rowData = {socket, chipset};
                socketToChipsetTableModel.addRow(rowData);
            }
        });

        updateSocketToChipsetButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                LOGGER.addHandler(fileHandler);
                LOGGER.info("Trying to update data");
                int row = socketToChipsetTable.getSelectedRow();
                if (row != -1) 
                {
                    // create the popup window with yes/no options
                    int choice = JOptionPane.showConfirmDialog(socketToChipsetPanel, "Do you wish to continue?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    // handle the user's choice
                    if (choice == JOptionPane.YES_OPTION) 
                    {
                        try 
                        {
                            String socket = compatibilitySocketComboBox.getSelectedItem().toString();
                            String chipset = compatibilityChipsetComboBox.getSelectedItem().toString();
                            socketToChipsetTable.setValueAt(socket, row, 0);
                            socketToChipsetTable.setValueAt(chipset, row, 1);
                        }
                        catch(NullPointerException ex) 
                        {
                            JOptionPane.showMessageDialog(socketToChipsetPanel, "You must fill all text fields first!");
                        }
                    } 
                    else 
                    {
                        System.out.println("User clicked NO");
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(socketToChipsetPanel, "Сan't update any record! Please select one!", "Error", row);
                }
            }
        });

        deleteSocketToChipsetButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                LOGGER.addHandler(fileHandler);
                LOGGER.info("Trying to delete record in your table");
                int row = socketToChipsetTable.getSelectedRow();
                if (row != -1) 
                {
                    // create the popup window with yes/no options
                    int choice = JOptionPane.showConfirmDialog(socketToChipsetPanel, "Do you wish to continue?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    // handle the user's choice
                    if (choice == JOptionPane.YES_OPTION) 
                    {
                        System.out.println("User clicked YES");
                        socketToChipsetTableModel.removeRow(row);
                    } 
                    else 
                    {
                        System.out.println("User clicked NO");
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(socketToChipsetPanel, "Сan't delete record! Please select one!", "Error", row);
                }
            }
        });

        pdfExportSocketToChipsetButton.addActionListener(new ActionListener() 
        {
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
                    fileChooser.setSelectedFile(new File("../../../../../exported_Compatibilities.pdf"));
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
                        PdfPTable pdfTable = new PdfPTable(socketToChipsetTable.getColumnCount());
                        
                        // Create font for table headers
                        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK);
                        String[] headersPdfExport = {"\nSocket\n\n", "\nChipset"};

                        // Set column headers
                        for (int i = 0; i < socketToChipsetTable.getColumnCount(); i++) 
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
                        float[] columnWidths = {0.5f, 0.5f};
                        pdfTable.setWidths(columnWidths);
                        
                        // Add table data
                        for (int i = 0; i < socketToChipsetTable.getRowCount(); i++) 
                        {
                            for (int j = 0; j < socketToChipsetTable.getColumnCount(); j++) 
                            {
                                PdfPCell data = new PdfPCell(new Phrase(socketToChipsetTable.getValueAt(i, j).toString(), dataFont));
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
                        JOptionPane.showMessageDialog(socketToChipsetPanel, "Exported table data to " + fileName);
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(socketToChipsetPanel, "Error exporting table data to PDF");
                }
            }
        });
        
        commitSocketToChipsetButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
            }
        });


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //  ________  __    __  _______                    ______    ______   ________  ______   ______   __    __        __        ______   ______   ________  ________  __    __  ________  _______    ______   //
        // /        |/  \  /  |/       \                  /      \  /      \ /        |/      | /      \ /  \  /  |      /  |      /      | /      \ /        |/        |/  \  /  |/        |/       \  /      \  //
        // $$$$$$$$/ $$  \ $$ |$$$$$$$  |       __       /$$$$$$  |/$$$$$$  |$$$$$$$$/ $$$$$$/ /$$$$$$  |$$  \ $$ |      $$ |      $$$$$$/ /$$$$$$  |$$$$$$$$/ $$$$$$$$/ $$  \ $$ |$$$$$$$$/ $$$$$$$  |/$$$$$$  | //
        // $$ |__    $$$  \$$ |$$ |  $$ |      /  |      $$ |__$$ |$$ |  $$/    $$ |     $$ |  $$ |  $$ |$$$  \$$ |      $$ |        $$ |  $$ \__$$/    $$ |   $$ |__    $$$  \$$ |$$ |__    $$ |__$$ |$$ \__$$/  //
        // $$    |   $$$$  $$ |$$ |  $$ |      $$/       $$    $$ |$$ |         $$ |     $$ |  $$ |  $$ |$$$$  $$ |      $$ |        $$ |  $$      \    $$ |   $$    |   $$$$  $$ |$$    |   $$    $$< $$      \  //
        // $$$$$/    $$ $$ $$ |$$ |  $$ |       __       $$$$$$$$ |$$ |   __    $$ |     $$ |  $$ |  $$ |$$ $$ $$ |      $$ |        $$ |   $$$$$$  |   $$ |   $$$$$/    $$ $$ $$ |$$$$$/    $$$$$$$  | $$$$$$  | //
        // $$ |_____ $$ |$$$$ |$$ |__$$ |      /  |      $$ |  $$ |$$ \__/  |   $$ |    _$$ |_ $$ \__$$ |$$ |$$$$ |      $$ |_____  _$$ |_ /  \__$$ |   $$ |   $$ |_____ $$ |$$$$ |$$ |_____ $$ |  $$ |/  \__$$ | //
        // $$       |$$ | $$$ |$$    $$/       $$/       $$ |  $$ |$$    $$/    $$ |   / $$   |$$    $$/ $$ | $$$ |      $$       |/ $$   |$$    $$/    $$ |   $$       |$$ | $$$ |$$       |$$ |  $$ |$$    $$/  //
        // $$$$$$$$/ $$/   $$/ $$$$$$$/                  $$/   $$/  $$$$$$/     $$/    $$$$$$/  $$$$$$/  $$/   $$/       $$$$$$$$/ $$$$$$/  $$$$$$/     $$/    $$$$$$$$/ $$/   $$/ $$$$$$$$/ $$/   $$/  $$$$$$/   //
        //                                                                                                                                                                                                        //
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


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

        brandTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() 
        {
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
        
        socketTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() 
        {
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

        chipsetTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() 
        {
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

        socketToChipsetTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() 
        {
            @Override
            public void valueChanged(ListSelectionEvent e) 
            {
                int selectedRow = socketToChipsetTable.getSelectedRow();
                if (selectedRow != -1) 
                {
                    String socket = socketToChipsetTableModel.getValueAt(selectedRow, 0).toString();
                    String chipset = socketToChipsetTableModel.getValueAt(selectedRow, 1).toString();
                    // Set the selected item in the comboboxes
                    for (int i = 0; i < compatibilitySocketComboBox.getItemCount(); i++) 
                    {
                        String item = compatibilitySocketComboBox.getItemAt(i).toString();
                        if (item.equals(socket)) 
                        {
                            compatibilitySocketComboBox.setSelectedItem(item);
                            break;
                        }
                    }
                    for (int i = 0; i < compatibilityChipsetComboBox.getItemCount(); i++) 
                    {
                        String item = compatibilityChipsetComboBox.getItemAt(i).toString();
                        if (item.equals(chipset)) 
                        {
                            compatibilityChipsetComboBox.setSelectedItem(item);
                            break;
                        }
                    }
                }
            }
        });

        cpuTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() 
        {
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

        gpuTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() 
        {
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

        pcbTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() 
        {
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

        // Set the selected tab to the first tab
        tabbedPane.setSelectedIndex(0);
        setVisible(true);
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //  ________  __    __  _______          ______   ________         ______   __         ______    ______    ______   //
    // /        |/  \  /  |/       \        /      \ /        |       /      \ /  |       /      \  /      \  /      \  //
    // $$$$$$$$/ $$  \ $$ |$$$$$$$  |      /$$$$$$  |$$$$$$$$/       /$$$$$$  |$$ |      /$$$$$$  |/$$$$$$  |/$$$$$$  | //
    // $$ |__    $$$  \$$ |$$ |  $$ |      $$ |  $$ |$$ |__          $$ |  $$/ $$ |      $$ |__$$ |$$ \__$$/ $$ \__$$/  //
    // $$    |   $$$$  $$ |$$ |  $$ |      $$ |  $$ |$$    |         $$ |      $$ |      $$    $$ |$$      \ $$      \  //
    // $$$$$/    $$ $$ $$ |$$ |  $$ |      $$ |  $$ |$$$$$/          $$ |   __ $$ |      $$$$$$$$ | $$$$$$  | $$$$$$  | //
    // $$ |_____ $$ |$$$$ |$$ |__$$ |      $$ \__$$ |$$ |            $$ \__/  |$$ |_____ $$ |  $$ |/  \__$$ |/  \__$$ | //
    // $$       |$$ | $$$ |$$    $$/       $$    $$/ $$ |            $$    $$/ $$       |$$ |  $$ |$$    $$/ $$    $$/  //
    // $$$$$$$$/ $$/   $$/ $$$$$$$/         $$$$$$/  $$/              $$$$$$/  $$$$$$$$/ $$/   $$/  $$$$$$/   $$$$$$/   //
    //                                                                                                                  //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




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
    private void populateTables() {
        // Populate the tables with data from the database
        // You need to implement the necessary methods to retrieve data from the database
        List<ClassBrand> brands = retrieveBrands();
        for (ClassBrand brand : brands) {
            brandTableModel.addRow(new Object[]{brand.getName()});
        }
    
        List<ClassChipset> chipsets = retrieveChipsets();
        for (ClassChipset chipset : chipsets) {
            chipsetTableModel.addRow(new Object[]{chipset.getName()});
        }
    
        List<ClassSocket> sockets = retrieveSockets();
        for (ClassSocket socket : sockets) {
            socketTableModel.addRow(new Object[]{socket.getName()});
        }
    
        List<ClassSocketToChipset> socketToChipsets = retrieveSocketToChipsets();
        for (ClassSocketToChipset socketToChipset : socketToChipsets) {
            socketToChipsetTableModel.addRow(new Object[]{socketToChipset.getSocket().getName(), socketToChipset.getChipset().getName()});
        }
    
        List<ClassCPU> cpus = retrieveCPUs();
        for (ClassCPU cpu : cpus) {
            cpuTableModel.addRow(new Object[]{cpu.getModel(), cpu.getPrice(), cpu.getCores(), cpu.getThreads(), cpu.getFrequency(), cpu.getBrand().getName(), cpu.getSocket().getName()});
        }

        List<ClassGPU> gpus = retrieveGPUs();
        for (ClassGPU gpu : gpus) {
            gpuTableModel.addRow(new Object[]{gpu.getModel(), gpu.getPrice(), gpu.getCores(), gpu.getMemory(), gpu.getFrequency(), gpu.getBrand().getName()});
        }
    
        List<ClassPCB> motherboards = retrievePCBs();
        for (ClassPCB motherboard : motherboards) {pcbTableModel.addRow(new Object[]{motherboard.getModel(), motherboard.getPrice(), motherboard.getBrand().getName(), motherboard.getSocket().getName(), motherboard.getChipset().getName()});
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
            brands = session.createQuery("FROM ClassBrand").list();
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
            chipsets = session.createQuery("FROM ClassChipset").list();
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
            cpus = session.createQuery("FROM ClassCPU").list();
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
            gpus = session.createQuery("FROM ClassGPU").list();
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
            pcbs = session.createQuery("FROM ClassPCB").list();
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
            sockets = session.createQuery("FROM ClassSocket").list();
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

    private List<ClassSocketToChipset> retrieveSocketToChipsets() 
    {
        List<ClassSocketToChipset> socketToChipsets = null;
        try 
        {
            transaction = session.beginTransaction();
            socketToChipsets = session.createQuery("FROM ClassSocketToChipset").list();
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
        return socketToChipsets;
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
    private JPanel createButtonPanel(JButton addButton, JButton deleteButton, JButton updateButton,  JButton pdfExportButton, JButton commitButton) 
    {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(pdfExportButton);
        // buttonPanel.add(commitButton);
        return buttonPanel;
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
