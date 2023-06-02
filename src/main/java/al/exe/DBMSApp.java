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
{   // Hibernate
    private Session session;
    private Transaction transaction;
    private SessionFactory sessionFactory;
    // Swing gui components
    private JTabbedPane tabbedPane;
    private JTable brandTable, chipsetTable, cpuTable, gpuTable, pcbTable, socketTable, socketToChipsetTable;
    private DefaultTableModel brandTableModel, chipsetTableModel, cpuTableModel, gpuTableModel, pcbTableModel, socketTableModel, socketToChipsetTableModel;
    // Buttons
    private JButton addCPUButton, deleteCPUButton, updateCPUButton, commitCPUButton;
    private JButton addGPUButton, deleteGPUButton, updateGPUButton, commitGPUButton;
    private JButton addPCBButton, deletePCBButton, updatePCBButton, commitPCBButton;
    private JButton addBrandButton, deleteBrandButton, updateBrandButton, commitBrandButton;
    private JButton addSocketButton, deleteSocketButton, updateSocketButton, commitSocketButton;
    private JButton addChipsetButton, deleteChipsetButton, updateChipsetButton, commitChipsetButton;
    private JButton addSocketToChipsetButton, deleteSocketToChipsetButton, updateSocketToChipsetButton, commitSocketToChipsetButton;
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
        addBrandButton = new JButton("Add Brand");
        deleteBrandButton = new JButton("Delete Brand");
        updateBrandButton = new JButton("Update Brand");
        commitBrandButton = new JButton("Commit changes");


        addChipsetButton = new JButton("Add Chipset");
        deleteChipsetButton = new JButton("Delete Chipset");
        updateChipsetButton = new JButton("Update Chipset");
        commitChipsetButton = new JButton("Commit changes");

        addCPUButton = new JButton("Add CPU");
        deleteCPUButton = new JButton("Delete CPU");
        updateCPUButton = new JButton("Update CPU");
        commitCPUButton = new JButton("Commit changes");

        addGPUButton = new JButton("Add GPU");
        deleteGPUButton = new JButton("Delete GPU");
        updateGPUButton = new JButton("Update GPU");
        commitGPUButton = new JButton("Commit changes");

        addPCBButton = new JButton("Add PCB");
        deletePCBButton = new JButton("Delete PCB");
        updatePCBButton = new JButton("Update PCB");
        commitPCBButton = new JButton("Commit changes");

        addSocketButton = new JButton("Add Socket");
        deleteSocketButton = new JButton("Delete Socket");
        updateSocketButton = new JButton("Update Socket");
        commitSocketButton = new JButton("Commit changes");

        addSocketToChipsetButton = new JButton("Add Socket to Chipset");
        deleteSocketToChipsetButton = new JButton("Delete Socket to Chipset");
        updateSocketToChipsetButton = new JButton("Update Socket to Chipset");
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
        JPanel cpuButtonPanel = createButtonPanel(addCPUButton, deleteCPUButton, updateCPUButton, commitCPUButton);
        JPanel gpuButtonPanel = createButtonPanel(addGPUButton, deleteGPUButton, updateGPUButton, commitGPUButton);
        JPanel pcbButtonPanel = createButtonPanel(addPCBButton, deletePCBButton, updatePCBButton, commitPCBButton);
        JPanel brandButtonPanel = createButtonPanel(addBrandButton, deleteBrandButton, updateBrandButton, commitBrandButton);
        JPanel socketButtonPanel = createButtonPanel(addSocketButton, deleteSocketButton, updateSocketButton, commitSocketButton);
        JPanel chipsetButtonPanel = createButtonPanel(addChipsetButton, deleteChipsetButton, updateChipsetButton, commitChipsetButton);
        JPanel socketToChipsetButtonPanel = createButtonPanel(addSocketToChipsetButton, deleteSocketToChipsetButton, updateSocketToChipsetButton, commitSocketToChipsetButton);

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
        

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //   ______    ______   __       __  _______    ______         _______    ______   __    __        __        ______   ______   ________  ________  __    __  ________  _______    ______         //
        //  /      \  /      \ /  \     /  |/       \  /      \       /       \  /      \ /  |  /  |      /  |      /      | /      \ /        |/        |/  \  /  |/        |/       \  /      \        //
        // /$$$$$$  |/$$$$$$  |$$  \   /$$ |$$$$$$$  |/$$$$$$  |      $$$$$$$  |/$$$$$$  |$$ |  $$ |      $$ |      $$$$$$/ /$$$$$$  |$$$$$$$$/ $$$$$$$$/ $$  \ $$ |$$$$$$$$/ $$$$$$$  |/$$$$$$  |       //
        // $$ |  $$/ $$ |  $$ |$$$  \ /$$$ |$$ |__$$ |$$ |  $$ |      $$ |__$$ |$$ |  $$ |$$  \/$$/       $$ |        $$ |  $$ \__$$/    $$ |   $$ |__    $$$  \$$ |$$ |__    $$ |__$$ |$$ \__$$/        //
        // $$ |      $$ |  $$ |$$$$  /$$$$ |$$    $$< $$ |  $$ |      $$    $$< $$ |  $$ | $$  $$<        $$ |        $$ |  $$      \    $$ |   $$    |   $$$$  $$ |$$    |   $$    $$< $$      \        //
        // $$ |   __ $$ |  $$ |$$ $$ $$/$$ |$$$$$$$  |$$ |  $$ |      $$$$$$$  |$$ |  $$ |  $$$$  \       $$ |        $$ |   $$$$$$  |   $$ |   $$$$$/    $$ $$ $$ |$$$$$/    $$$$$$$  | $$$$$$  |       //
        // $$ \__/  |$$ \__$$ |$$ |$$$/ $$ |$$ |__$$ |$$ \__$$ |      $$ |__$$ |$$ \__$$ | $$ /$$  |      $$ |_____  _$$ |_ /  \__$$ |   $$ |   $$ |_____ $$ |$$$$ |$$ |_____ $$ |  $$ |/  \__$$ |       //
        // $$    $$/ $$    $$/ $$ | $/  $$ |$$    $$/ $$    $$/       $$    $$/ $$    $$/ $$ |  $$ |      $$       |/ $$   |$$    $$/    $$ |   $$       |$$ | $$$ |$$       |$$ |  $$ |$$    $$/        //
        //  $$$$$$/   $$$$$$/  $$/      $$/ $$$$$$$/   $$$$$$/        $$$$$$$/   $$$$$$/  $$/   $$/       $$$$$$$$/ $$$$$$/  $$$$$$/     $$/    $$$$$$$$/ $$/   $$/ $$$$$$$$/ $$/   $$/  $$$$$$/         //
        //                                                                                                                                                                                               //
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Add listener for brand combo box
        cpuBrandComboBox.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
            }
        });
        

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //   ______    ______   ________  ______   ______   __    __        __        ______   ______   ________  ________  __    __  ________  _______    ______   //
        //  /      \  /      \ /        |/      | /      \ /  \  /  |      /  |      /      | /      \ /        |/        |/  \  /  |/        |/       \  /      \  //
        // /$$$$$$  |/$$$$$$  |$$$$$$$$/ $$$$$$/ /$$$$$$  |$$  \ $$ |      $$ |      $$$$$$/ /$$$$$$  |$$$$$$$$/ $$$$$$$$/ $$  \ $$ |$$$$$$$$/ $$$$$$$  |/$$$$$$  | //
        // $$ |__$$ |$$ |  $$/    $$ |     $$ |  $$ |  $$ |$$$  \$$ |      $$ |        $$ |  $$ \__$$/    $$ |   $$ |__    $$$  \$$ |$$ |__    $$ |__$$ |$$ \__$$/  //
        // $$    $$ |$$ |         $$ |     $$ |  $$ |  $$ |$$$$  $$ |      $$ |        $$ |  $$      \    $$ |   $$    |   $$$$  $$ |$$    |   $$    $$< $$      \  //
        // $$$$$$$$ |$$ |   __    $$ |     $$ |  $$ |  $$ |$$ $$ $$ |      $$ |        $$ |   $$$$$$  |   $$ |   $$$$$/    $$ $$ $$ |$$$$$/    $$$$$$$  | $$$$$$  | //
        // $$ |  $$ |$$ \__/  |   $$ |    _$$ |_ $$ \__$$ |$$ |$$$$ |      $$ |_____  _$$ |_ /  \__$$ |   $$ |   $$ |_____ $$ |$$$$ |$$ |_____ $$ |  $$ |/  \__$$ | //
        // $$ |  $$ |$$    $$/    $$ |   / $$   |$$    $$/ $$ | $$$ |      $$       |/ $$   |$$    $$/    $$ |   $$       |$$ | $$$ |$$       |$$ |  $$ |$$    $$/  //
        // $$/   $$/  $$$$$$/     $$/    $$$$$$/  $$$$$$/  $$/   $$/       $$$$$$$$/ $$$$$$/  $$$$$$/     $$/    $$$$$$$$/ $$/   $$/ $$$$$$$$/ $$/   $$/  $$$$$$/   //
        //                                                                                                                                                          //
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /////////////////
        //             //
        //    BRAND    //
        //             //
        /////////////////
         addBrandButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBrand();
            }
        });

        deleteBrandButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteBrand();
            }
        });

        updateBrandButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBrand();
            }
        });

        ///////////////////
        //               //
        //    CHIPSET    //
        //               //
        ///////////////////
        addChipsetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addChipset();
            }
        });

        deleteChipsetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteChipset();
            }
        });

        updateChipsetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateChipset();
            }
        });

        ///////////////
        //           //
        //    CPU    //
        //           //
        ///////////////
        addCPUButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCPU();
            }
        });

        deleteCPUButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCPU();
            }
        });

        updateCPUButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCPU();
            }
        });

        ///////////////
        //           //
        //    GPU    //
        //           //
        ///////////////
        addGPUButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addGPU();
            }
        });

        deleteGPUButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteGPU();
            }
        });

        updateGPUButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGPU();
            }
        });

        ///////////////////////
        //                   //
        //    Motherboard    //
        //                   //
        ///////////////////////
        addPCBButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMotherboard();
            }
        });

        deletePCBButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteMotherboard();
            }
        });

        updatePCBButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMotherboard();
            }
        });

        //////////////////
        //              //
        //    SOCKET    //
        //              //
        //////////////////
        addSocketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSocket();
            }
        });

        deleteSocketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSocket();
            }
        });

        updateSocketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSocket();
            }
        });

        /////////////////////////////
        //                         //
        //    SOCKET TO CHIPSET    //
        //                         //
        /////////////////////////////
        addSocketToChipsetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSocketToChipset();
            }
        });

        deleteSocketToChipsetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSocketToChipset();
            }
        });

        updateSocketToChipsetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSocketToChipset();
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

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //   ______   _______   _______        __  _______   ________  __        ________  ________  ________        __  __    __  _______   _______    ______   ________  ________        __       __  ________  ________  __    __   ______   _______    ______   //
    //  /      \ /       \ /       \      /  |/       \ /        |/  |      /        |/        |/        |      /  |/  |  /  |/       \ /       \  /      \ /        |/        |      /  \     /  |/        |/        |/  |  /  | /      \ /       \  /      \  //
    // /$$$$$$  |$$$$$$$  |$$$$$$$  |    /$$/ $$$$$$$  |$$$$$$$$/ $$ |      $$$$$$$$/ $$$$$$$$/ $$$$$$$$/      /$$/ $$ |  $$ |$$$$$$$  |$$$$$$$  |/$$$$$$  |$$$$$$$$/ $$$$$$$$/       $$  \   /$$ |$$$$$$$$/ $$$$$$$$/ $$ |  $$ |/$$$$$$  |$$$$$$$  |/$$$$$$  | //
    // $$ |__$$ |$$ |  $$ |$$ |  $$ |   /$$/  $$ |  $$ |$$ |__    $$ |      $$ |__       $$ |   $$ |__        /$$/  $$ |  $$ |$$ |__$$ |$$ |  $$ |$$ |__$$ |   $$ |   $$ |__          $$$  \ /$$$ |$$ |__       $$ |   $$ |__$$ |$$ |  $$ |$$ |  $$ |$$ \__$$/  //
    // $$    $$ |$$ |  $$ |$$ |  $$ |  /$$/   $$ |  $$ |$$    |   $$ |      $$    |      $$ |   $$    |      /$$/   $$ |  $$ |$$    $$/ $$ |  $$ |$$    $$ |   $$ |   $$    |         $$$$  /$$$$ |$$    |      $$ |   $$    $$ |$$ |  $$ |$$ |  $$ |$$      \  //
    // $$$$$$$$ |$$ |  $$ |$$ |  $$ | /$$/    $$ |  $$ |$$$$$/    $$ |      $$$$$/       $$ |   $$$$$/      /$$/    $$ |  $$ |$$$$$$$/  $$ |  $$ |$$$$$$$$ |   $$ |   $$$$$/          $$ $$ $$/$$ |$$$$$/       $$ |   $$$$$$$$ |$$ |  $$ |$$ |  $$ | $$$$$$  | //
    // $$ |  $$ |$$ |__$$ |$$ |__$$ |/$$/     $$ |__$$ |$$ |_____ $$ |_____ $$ |_____    $$ |   $$ |_____  /$$/     $$ \__$$ |$$ |      $$ |__$$ |$$ |  $$ |   $$ |   $$ |_____       $$ |$$$/ $$ |$$ |_____    $$ |   $$ |  $$ |$$ \__$$ |$$ |__$$ |/  \__$$ | //
    // $$ |  $$ |$$    $$/ $$    $$//$$/      $$    $$/ $$       |$$       |$$       |   $$ |   $$       |/$$/      $$    $$/ $$ |      $$    $$/ $$ |  $$ |   $$ |   $$       |      $$ | $/  $$ |$$       |   $$ |   $$ |  $$ |$$    $$/ $$    $$/ $$    $$/  //
    // $$/   $$/ $$$$$$$/  $$$$$$$/ $$/       $$$$$$$/  $$$$$$$$/ $$$$$$$$/ $$$$$$$$/    $$/    $$$$$$$$/ $$/        $$$$$$/  $$/       $$$$$$$/  $$/   $$/    $$/    $$$$$$$$/       $$/      $$/ $$$$$$$$/    $$/    $$/   $$/  $$$$$$/  $$$$$$$/   $$$$$$/   //
    //                                                                                                                                                                                                                                                          //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // add/delete/update methods
    private void addBrand() 
    {
        // Create a new brand object
        ClassBrand brand = new ClassBrand();
        brand.setName("New Brand");
        try 
        {
            transaction = session.beginTransaction();
            session.save(brand);
            transaction.commit();
            // Refresh the brand table
            brandTableModel.addRow(new Object[]{brand.getId(), brand.getName()});
        } 
        catch (Exception e) 
        {
            if (transaction != null) 
            {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    private void deleteBrand() 
    {
        // Get the selected brand ID from the brand table
        int selectedRow = brandTable.getSelectedRow();
        int brandId = Integer.parseInt(brandTableModel.getValueAt(selectedRow, 0).toString());

        try 
        {
            transaction = session.beginTransaction();
            ClassBrand brand = session.get(ClassBrand.class, brandId);
            session.delete(brand);
            transaction.commit();
            // Remove the brand from the brand table
            brandTableModel.removeRow(selectedRow);
        } 
        catch (Exception e) 
        {
            if (transaction != null) 
            {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    private void updateBrand() 
    {
        // Get the selected brand ID from the brand table
        int selectedRow = brandTable.getSelectedRow();
        int brandId = Integer.parseInt(brandTableModel.getValueAt(selectedRow, 0).toString());

        try 
        {
            transaction = session.beginTransaction();
            ClassBrand brand = session.get(ClassBrand.class, brandId);
            brand.setName("Updated Brand");
            session.update(brand);
            transaction.commit();
            // Update the brand in the brand table
            brandTableModel.setValueAt(brand.getName(), selectedRow, 1);
        } 
        catch (Exception e) 
        {
            if (transaction != null) 
            {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }


    private void addChipset() 
    {
    }

    private void deleteChipset() 
    {
    }

    private void updateChipset() 
    {
    }

    private void addCPU() 
    {
    }

    private void deleteCPU() 
    {
    }

    private void updateCPU() 
    {
    }

    private void addGPU() 
    {
    }

    private void deleteGPU() 
    {
    }

    private void updateGPU() 
    {
    }

    private void addMotherboard() 
    {
    }

    private void deleteMotherboard() 
    {
    }

    private void updateMotherboard() 
    {
    }

    private void addSocket() 
    {
    }

    private void deleteSocket() 
    {
    }

    private void updateSocket() 
    {
    }

    private void addSocketToChipset() 
    {
    }

    private void deleteSocketToChipset() 
    {
    }

    private void updateSocketToChipset() 
    {
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //   ______    ______   __       __  _______    ______   _______    ______   __    __        __        ______   ______   ________  ________  __    __  ________  _______    ______   //
    //  /      \  /      \ /  \     /  |/       \  /      \ /       \  /      \ /  |  /  |      /  |      /      | /      \ /        |/        |/  \  /  |/        |/       \  /      \  //
    // /$$$$$$  |/$$$$$$  |$$  \   /$$ |$$$$$$$  |/$$$$$$  |$$$$$$$  |/$$$$$$  |$$ |  $$ |      $$ |      $$$$$$/ /$$$$$$  |$$$$$$$$/ $$$$$$$$/ $$  \ $$ |$$$$$$$$/ $$$$$$$  |/$$$$$$  | //
    // $$ |  $$/ $$ |  $$ |$$$  \ /$$$ |$$ |__$$ |$$ |  $$ |$$ |__$$ |$$ |  $$ |$$  \/$$/       $$ |        $$ |  $$ \__$$/    $$ |   $$ |__    $$$  \$$ |$$ |__    $$ |__$$ |$$ \__$$/  //
    // $$ |      $$ |  $$ |$$$$  /$$$$ |$$    $$< $$ |  $$ |$$    $$< $$ |  $$ | $$  $$<        $$ |        $$ |  $$      \    $$ |   $$    |   $$$$  $$ |$$    |   $$    $$< $$      \  //
    // $$ |   __ $$ |  $$ |$$ $$ $$/$$ |$$$$$$$  |$$ |  $$ |$$$$$$$  |$$ |  $$ |  $$$$  \       $$ |        $$ |   $$$$$$  |   $$ |   $$$$$/    $$ $$ $$ |$$$$$/    $$$$$$$  | $$$$$$  | //
    // $$ \__/  |$$ \__$$ |$$ |$$$/ $$ |$$ |__$$ |$$ \__$$ |$$ |__$$ |$$ \__$$ | $$ /$$  |      $$ |_____  _$$ |_ /  \__$$ |   $$ |   $$ |_____ $$ |$$$$ |$$ |_____ $$ |  $$ |/  \__$$ | //
    // $$    $$/ $$    $$/ $$ | $/  $$ |$$    $$/ $$    $$/ $$    $$/ $$    $$/ $$ |  $$ |      $$       |/ $$   |$$    $$/    $$ |   $$       |$$ | $$$ |$$       |$$ |  $$ |$$    $$/  //
    //  $$$$$$/   $$$$$$/  $$/      $$/ $$$$$$$/   $$$$$$/  $$$$$$$/   $$$$$$/  $$/   $$/       $$$$$$$$/ $$$$$$/  $$$$$$/     $$/    $$$$$$$$/ $$/   $$/ $$$$$$$$/ $$/   $$/  $$$$$$/   //
    //                                                                                                                                                                                   //
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////





    ////////////////////////////////////////////////////////////////////////////////////////////
    //  _______   _______         ________  __    __  _______    ______   _______   ________  //
    // /       \ /       \       /        |/  |  /  |/       \  /      \ /       \ /        | //
    // $$$$$$$  |$$$$$$$  |      $$$$$$$$/ $$ |  $$ |$$$$$$$  |/$$$$$$  |$$$$$$$  |$$$$$$$$/  //
    // $$ |  $$ |$$ |__$$ |      $$ |__    $$  \/$$/ $$ |__$$ |$$ |  $$ |$$ |__$$ |   $$ |    //
    // $$ |  $$ |$$    $$<       $$    |    $$  $$<  $$    $$/ $$ |  $$ |$$    $$<    $$ |    //
    // $$ |  $$ |$$$$$$$  |      $$$$$/      $$$$  \ $$$$$$$/  $$ |  $$ |$$$$$$$  |   $$ |    //
    // $$ |__$$ |$$ |__$$ |      $$ |_____  $$ /$$  |$$ |      $$ \__$$ |$$ |  $$ |   $$ |    //
    // $$    $$/ $$    $$/       $$       |$$ |  $$ |$$ |      $$    $$/ $$ |  $$ |   $$ |    //
    // $$$$$$$/  $$$$$$$/        $$$$$$$$/ $$/   $$/ $$/        $$$$$$/  $$/   $$/    $$/     //
    //                                                                                        //
    ////////////////////////////////////////////////////////////////////////////////////////////
    private void commitChanges() 
    {
        commitBrandChanges();
    }

    private void commitBrandChanges() 
    {   // Iterate through the brand table and export changes to the database
        for (int row = 0; row < brandTableModel.getRowCount(); row++) 
        {
            int id = (int) brandTableModel.getValueAt(row, 0);
            String name = (String) brandTableModel.getValueAt(row, 1);
            // Update or insert the brand based on the ID
            if (id > 0) 
            {   // Update existing brand with ID
                updateBrand(id, name);
            } 
            else 
            {   // Insert new brand
                insertBrand(name);
            }
        }
    }


    // Implement similar export methods for other tables
    private void updateBrand(int id, String name) {
        Session session = null;
        Transaction transaction = null;
    
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
    
            ClassBrand brand = session.get(ClassBrand.class, id);
            if (brand != null) {
                brand.setName(name);
                session.update(brand);
            }
    
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    private void insertBrand(String name) {
        Session session = null;
        Transaction transaction = null;
    
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
    
            ClassBrand brand = new ClassBrand();
            brand.setName(name);
            session.save(brand);
    
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
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
    private void clearCPUFields() 
    {
        cpuModelField.setText("");
        cpuPriceField.setText("");
        cpuCoresField.setText("");
        cpuThreadsField.setText("");
        cpuFrequencyField.setText("");
        cpuBrandComboBox.setSelectedIndex(-1);
        cpuSocketComboBox.setSelectedIndex(-1);
        // cpuBrandComboBox.setSelectedIndex(0);
        // cpuSocketComboBox.setSelectedIndex(0);
    }

    private void refreshCPUTable() 
    {   // Clear the CPU table
        cpuTableModel.setRowCount(0);
        // Retrieve and populate the CPU table with updated data (implement this based on your database access code)
        List<ClassCPU> cpus = retrieveCPUs();
        for (ClassCPU cpu : cpus) 
        {
            cpuTableModel.addRow(new Object[]{cpu.getModel(), cpu.getPrice(), cpu.getCores(), cpu.getThreads(), cpu.getFrequency(), cpu.getBrand().getName(), cpu.getSocket().getName()});
        }
    }

    // Helper method to create a panel with buttons
    private JPanel createButtonPanel(JButton addButton, JButton deleteButton, JButton updateButton, JButton commitButton) 
    {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(commitButton);
        return buttonPanel;
    }

    private ClassSocket getSelectedSocket() 
    {
        String selectedSocketName = (String) cpuSocketComboBox.getSelectedItem();
        // Retrieve the selected socket from the database based on the socket name
        ClassSocket selectedSocket = null; // Replace with your actual database access code
        List<ClassSocket> sockets = retrieveSockets();
        for (ClassSocket socket : sockets) 
        {
            if (socket.getName().equals(selectedSocketName)) 
            {
                selectedSocket = socket;
                break;
            }
        }
        return selectedSocket;
    }
    
    private ClassBrand getSelectedBrand() 
    {
        String selectedBrandName = (String) cpuBrandComboBox.getSelectedItem();
        // Retrieve the selected socket from the database based on the socket name
        ClassBrand selectedBrand = null; // Replace with your actual database access code
        List<ClassBrand> brands = retrieveBrands();
        for (ClassBrand brand : brands) 
        {
            if (brand.getName().equals(selectedBrandName)) 
            {
                selectedBrand = brand;
                break;
            }
        }
        return selectedBrand;
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
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DBMSApp();
            }
        });
    }
}
