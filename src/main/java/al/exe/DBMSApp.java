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
import javax.swing.*;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Element;
import com.itextpdf.text.Document;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
// Hibernate
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
// Necessary classes
import al.exe.ClassCPU;
import al.exe.ClassGPU;
import al.exe.ClassPCB;
import al.exe.ClassBrand;
import al.exe.ClassSocket;
import al.exe.ClassChipset;
import al.exe.ClassSocketToChipset;


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
    // Swing gui components
    private JTabbedPane tabbedPane;
    private JTable brandTable;
    private JTable chipsetTable;
    private JTable cpuTable;
    private JTable gpuTable;
    private JTable pcbTable;
    private JTable socketTable;
    private JTable socketToChipsetTable;
    private DefaultTableModel brandTableModel;
    private DefaultTableModel chipsetTableModel;
    private DefaultTableModel cpuTableModel;
    private DefaultTableModel gpuTableModel;
    private DefaultTableModel pcbTableModel;
    private DefaultTableModel socketTableModel;
    private DefaultTableModel socketToChipsetTableModel;
    private JButton addBrandButton;
    private JButton deleteBrandButton;
    private JButton updateBrandButton;
    private JButton addChipsetButton;
    private JButton deleteChipsetButton;
    private JButton updateChipsetButton;
    private JButton addCPUButton;
    private JButton deleteCPUButton;
    private JButton updateCPUButton;
    private JButton addGPUButton;
    private JButton deleteGPUButton;
    private JButton updateGPUButton;
    private JButton addPCBButton;
    private JButton deletePCBButton;
    private JButton updatePCBButton;
    private JButton addSocketButton;
    private JButton deleteSocketButton;
    private JButton updateSocketButton;
    private JButton addSocketToChipsetButton;
    private JButton deleteSocketToChipsetButton;
    private JButton updateSocketToChipsetButton;
    // CPU Fields
    private JTextField cpuModelField;
    private JTextField cpuPriceField;
    private JTextField cpuCoresField;
    private JTextField cpuThreadsField;
    private JTextField cpuFrequencyField;
    private JComboBox<String> brandComboBox;
    private JComboBox<String> socketComboBox;
    // Brand Fields
    private JTextField brandIdField;
    private JTextField brandNameField;
    // Hibernate
    private SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;


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
    {
        // Initialize window
        setTitle("DBMS App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 550);
        setLocationRelativeTo(null);
        setResizable(false);

        // Initialize components
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

        addChipsetButton = new JButton("Add Chipset");
        deleteChipsetButton = new JButton("Delete Chipset");
        updateChipsetButton = new JButton("Update Chipset");

        addCPUButton = new JButton("Add CPU");
        deleteCPUButton = new JButton("Delete CPU");
        updateCPUButton = new JButton("Update CPU");

        addGPUButton = new JButton("Add GPU");
        deleteGPUButton = new JButton("Delete GPU");
        updateGPUButton = new JButton("Update GPU");

        addPCBButton = new JButton("Add PCB");
        deletePCBButton = new JButton("Delete PCB");
        updatePCBButton = new JButton("Update PCB");

        addSocketButton = new JButton("Add Socket");
        deleteSocketButton = new JButton("Delete Socket");
        updateSocketButton = new JButton("Update Socket");

        addSocketToChipsetButton = new JButton("Add Socket to Chipset");
        deleteSocketToChipsetButton = new JButton("Delete Socket to Chipset");
        updateSocketToChipsetButton = new JButton("Update Socket to Chipset");

        // Create CPU Fields
        cpuModelField = new JTextField(255);
        cpuPriceField = new JTextField(10);
        cpuCoresField = new JTextField(5);
        cpuThreadsField = new JTextField(5);
        cpuFrequencyField = new JTextField(10);
        brandComboBox = new JComboBox<>();
        socketComboBox = new JComboBox<>();
        // Create Brand fields
        brandIdField = new JTextField();
        brandNameField = new JTextField();


        /////////////////////////
        //                     //
        //    cpuInputPanel    //
        //                     //
        /////////////////////////
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
        cpuInputPanel.add(brandComboBox);
        cpuInputPanel.add(new JLabel("Socket:"));
        cpuInputPanel.add(socketComboBox);

        // Add CPU button panel
        JPanel cpuButtonPanel = createButtonPanel(addCPUButton, deleteCPUButton, updateCPUButton);

        ////////////////////////////////////////////
        //                                        //
        //    Add tables and buttons to panels    //
        //                                        //
        ////////////////////////////////////////////
        JPanel brandPanel = new JPanel(new BorderLayout());
        brandPanel.add(brandScrollPane, BorderLayout.CENTER);
        brandPanel.add(createButtonPanel(addBrandButton, deleteBrandButton, updateBrandButton), BorderLayout.SOUTH);

        JPanel chipsetPanel = new JPanel(new BorderLayout());
        chipsetPanel.add(chipsetScrollPane, BorderLayout.CENTER);
        chipsetPanel.add(createButtonPanel(addChipsetButton, deleteChipsetButton, updateChipsetButton), BorderLayout.SOUTH);

        // JPanel cpuPanel = new JPanel(new BorderLayout());
        // cpuPanel.add(cpuScrollPane, BorderLayout.CENTER);
        // cpuPanel.add(createButtonPanel(addCPUButton, deleteCPUButton, updateCPUButton), BorderLayout.SOUTH);
        // Add CPU panel
        JPanel cpuPanel = new JPanel(new BorderLayout());
        cpuPanel.add(cpuScrollPane, BorderLayout.CENTER);
        cpuPanel.add(cpuButtonPanel, BorderLayout.SOUTH);
        cpuPanel.add(cpuInputPanel, BorderLayout.NORTH);

        JPanel gpuPanel = new JPanel(new BorderLayout());
        gpuPanel.add(gpuScrollPane, BorderLayout.CENTER);
        gpuPanel.add(createButtonPanel(addGPUButton, deleteGPUButton, updateGPUButton), BorderLayout.SOUTH);

        JPanel pcbPanel = new JPanel(new BorderLayout());
        pcbPanel.add(pcbScrollPane, BorderLayout.CENTER);
        pcbPanel.add(createButtonPanel(addPCBButton, deletePCBButton, updatePCBButton), BorderLayout.SOUTH);

        JPanel socketPanel = new JPanel(new BorderLayout());
        socketPanel.add(socketScrollPane, BorderLayout.CENTER);
        socketPanel.add(createButtonPanel(addSocketButton, deleteSocketButton, updateSocketButton), BorderLayout.SOUTH);

        JPanel socketToChipsetPanel = new JPanel(new BorderLayout());
        socketToChipsetPanel.add(socketToChipsetScrollPane, BorderLayout.CENTER);
        socketToChipsetPanel.add(createButtonPanel(addSocketToChipsetButton, deleteSocketToChipsetButton, updateSocketToChipsetButton), BorderLayout.SOUTH);
        
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
        brandComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Brand combo box selection logic
            }
        });

        socketComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Socket combo box selection logic
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

        // Add listeners
        // addBrandButton.addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         // Add brand logic
        //         // You need to implement the addBrand() method
        //         addBrand();
        //     }
        // });

        // deleteBrandButton.addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         // Delete brand logic
        //         // You need to implement the deleteBrand() method
        //         deleteBrand();
        //     }
        // });

        // updateBrandButton.addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         // Update brand logic
        //         // You need to implement the updateBrand() method
        //         updateBrand();
        //     }
        // });

         // Add listeners to buttons
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
        // Set table selection listener
        brandTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Brand table selection logic
                // You can implement the selection logic here or create separate methods
                int selectedRow = brandTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String id = brandTableModel.getValueAt(selectedRow, 0).toString();
                    String name = brandTableModel.getValueAt(selectedRow, 1).toString();
                    brandIdField.setText(id);
                    brandNameField.setText(name);
                }
            }
        });

        
        // Add selection listener to CPU table
        cpuTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = cpuTable.getSelectedRow();
                if (selectedRow != -1) {
                    // Update CPU fields with selected row data
                    cpuModelField.setText(cpuTableModel.getValueAt(selectedRow, 1).toString());
                    cpuPriceField.setText(cpuTableModel.getValueAt(selectedRow, 2).toString());
                    cpuCoresField.setText(cpuTableModel.getValueAt(selectedRow, 3).toString());
                    cpuThreadsField.setText(cpuTableModel.getValueAt(selectedRow, 4).toString());
                    cpuFrequencyField.setText(cpuTableModel.getValueAt(selectedRow, 5).toString());
                    brandComboBox.getSelectedItem();
                    socketComboBox.getSelectedItem();
                }
            }
        });

        // Set the selected tab to the first tab
        tabbedPane.setSelectedIndex(0);

        setVisible(true);
    }




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


    private void addChipset() {
        // Implement chipset addition logic
    }

    private void deleteChipset() {
        // Implement chipset deletion logic
    }

    private void updateChipset() {
        // Implement chipset update logic
    }

    private void addCPU() {
        // Get input values from text fields and combo boxes
        String model = cpuModelField.getText();
        double price = Double.parseDouble(cpuPriceField.getText());
        int cores = Integer.parseInt(cpuCoresField.getText());
        int threads = Integer.parseInt(cpuThreadsField.getText());
        int frequency = Integer.parseInt(cpuFrequencyField.getText());
        ClassBrand selectedBrand = getSelectedBrand();
        ClassSocket selectedSocket = getSelectedSocket();

        // Create a new CPU object with the input values
        ClassCPU cpu = new ClassCPU(model, price, cores, threads, frequency, selectedBrand, selectedSocket);

        // Add the CPU to the database (implement this based on your database access code)
        // ...

        // Clear input fields after adding the CPU
        clearCPUInputFields();

        // Refresh the CPU table
        refreshCPUTable();
    }
    private void deleteCPU() {
        int selectedRow = cpuTable.getSelectedRow();
        if (selectedRow != -1) {
            int cpuId = (int) cpuTableModel.getValueAt(selectedRow, 0);

            // Delete the CPU from the database (implement this based on your database access code)
            // ...

            // Refresh the CPU table
            refreshCPUTable();
        }
    }

    private void updateCPU() {
        int selectedRow = cpuTable.getSelectedRow();
        if (selectedRow != -1) {
            int cpuId = (int) cpuTableModel.getValueAt(selectedRow, 0);

            // Get input values from text fields and combo boxes
            String model = cpuModelField.getText();
            double price = Double.parseDouble(cpuPriceField.getText());
            int cores = Integer.parseInt(cpuCoresField.getText());
            int threads = Integer.parseInt(cpuThreadsField.getText());
            int frequency = Integer.parseInt(cpuFrequencyField.getText());
            ClassBrand selectedBrand = getSelectedBrand();
            ClassSocket selectedSocket = getSelectedSocket();

            // Create a new CPU object with the updated values
            ClassCPU cpu = new ClassCPU(model, price, cores, threads, frequency, selectedBrand, selectedSocket);

            // Update the CPU in the database (implement this based on your database access code)
            // ...

            // Clear input fields after updating the CPU
            clearCPUInputFields();

            // Refresh the CPU table
            refreshCPUTable();
        }
    }

    private void addGPU() {
        // Implement GPU addition logic
    }

    private void deleteGPU() {
        // Implement GPU deletion logic
    }

    private void updateGPU() {
        // Implement GPU update logic
    }

    private void addMotherboard() {
        // Implement motherboard addition logic
    }

    private void deleteMotherboard() {
        // Implement motherboard deletion logic
    }

    private void updateMotherboard() {
        // Implement motherboard update logic
    }

    private void addSocket() {
        // Implement socket addition logic
    }

    private void deleteSocket() {
        // Implement socket deletion logic
    }

    private void updateSocket() {
        // Implement socket update logic
    }

    private void addSocketToChipset() {
        // Implement socket-to-chipset addition logic
    }

    private void deleteSocketToChipset() {
        // Implement socket-to-chipset deletion logic
    }

    private void updateSocketToChipset() {
        // Implement socket-to-chipset update logic
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
    private void clearCPUInputFields() 
    {
        cpuModelField.setText("");
        cpuPriceField.setText("");
        cpuCoresField.setText("");
        cpuThreadsField.setText("");
        cpuFrequencyField.setText("");
        brandComboBox.setSelectedIndex(-1);
        socketComboBox.setSelectedIndex(-1);
    }

    private void refreshCPUTable() 
    {   // Clear the CPU table
        cpuTableModel.setRowCount(0);
        // Retrieve and populate the CPU table with updated data (implement this based on your database access code)
        List<ClassCPU> cpus = retrieveCPUs();
        for (ClassCPU cpu : cpus) 
        {
            cpuTableModel.addRow(new Object[]{cpu.getId(), cpu.getModel(), cpu.getPrice(), cpu.getCores(), cpu.getThreads(), cpu.getFrequency(), cpu.getBrand().getId(), cpu.getSocket().getId()});
        }
    }

    // Helper method to create a panel with buttons
    private JPanel createButtonPanel(JButton addButton, JButton deleteButton, JButton updateButton) 
    {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        return buttonPanel;
    }

    private ClassSocket getSelectedSocket() 
    {
        String selectedSocketName = (String) socketComboBox.getSelectedItem();
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
        String selectedBrandName = (String) brandComboBox.getSelectedItem();
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

    // public static void main(String[] args) 
    // {
    //     SwingUtilities.invokeLater(DBMSApp::new);
    // }
}
