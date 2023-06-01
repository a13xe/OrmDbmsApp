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
        setSize(900, 750);
        setLocationRelativeTo(null);
        setResizable(false);

        // Initialize components
        tabbedPane = new JTabbedPane();

        // Initialize Hibernate session factory
        sessionFactory = new Configuration().configure().buildSessionFactory();
        session = sessionFactory.openSession();
        transaction = null;

        // Create table models
        cpuTableModel = new DefaultTableModel(new Object[]{"Model", "Price", "Cores", "Threads", "Frequency", "Brand ID", "Socket ID"}, 0);
        gpuTableModel = new DefaultTableModel(new Object[]{"Model", "Price", "Cores", "Memory", "Frequency", "Brand ID"}, 0);
        pcbTableModel = new DefaultTableModel(new Object[]{"Model", "Price", "Brand ID", "Socket ID", "Chipset ID"}, 0);
        brandTableModel = new DefaultTableModel(new Object[]{"Name"}, 0);
        socketTableModel = new DefaultTableModel(new Object[]{"Name"}, 0);
        chipsetTableModel = new DefaultTableModel(new Object[]{"Name"}, 0);
        socketToChipsetTableModel = new DefaultTableModel(new Object[]{"Socket ID", "Chipset ID"}, 0);

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

        // Add tables and buttons to panels
        JPanel brandPanel = new JPanel(new BorderLayout());
        brandPanel.add(brandScrollPane, BorderLayout.CENTER);
        brandPanel.add(createButtonPanel(
                addBrandButton, deleteBrandButton, updateBrandButton), BorderLayout.SOUTH);

        JPanel chipsetPanel = new JPanel(new BorderLayout());
        chipsetPanel.add(chipsetScrollPane, BorderLayout.CENTER);
        chipsetPanel.add(createButtonPanel(
                addChipsetButton, deleteChipsetButton, updateChipsetButton), BorderLayout.SOUTH);

        JPanel cpuPanel = new JPanel(new BorderLayout());
        cpuPanel.add(cpuScrollPane, BorderLayout.CENTER);
        cpuPanel.add(createButtonPanel(
                addCPUButton, deleteCPUButton, updateCPUButton), BorderLayout.SOUTH);

        JPanel gpuPanel = new JPanel(new BorderLayout());
        gpuPanel.add(gpuScrollPane, BorderLayout.CENTER);
        gpuPanel.add(createButtonPanel(
                addGPUButton, deleteGPUButton, updateGPUButton), BorderLayout.SOUTH);

        JPanel pcbPanel = new JPanel(new BorderLayout());
        pcbPanel.add(pcbScrollPane, BorderLayout.CENTER);
        pcbPanel.add(createButtonPanel(
                addPCBButton, deletePCBButton, updatePCBButton), BorderLayout.SOUTH);

        JPanel socketPanel = new JPanel(new BorderLayout());
        socketPanel.add(socketScrollPane, BorderLayout.CENTER);
        socketPanel.add(createButtonPanel(
                addSocketButton, deleteSocketButton, updateSocketButton), BorderLayout.SOUTH);

        JPanel socketToChipsetPanel = new JPanel(new BorderLayout());
        socketToChipsetPanel.add(socketToChipsetScrollPane, BorderLayout.CENTER);
        socketToChipsetPanel.add(createButtonPanel(
                addSocketToChipsetButton, deleteSocketToChipsetButton, updateSocketToChipsetButton), BorderLayout.SOUTH);
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
        addBrandButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add brand logic
                // You need to implement the addBrand() method
                addBrand();
            }
        });

        deleteBrandButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Delete brand logic
                // You need to implement the deleteBrand() method
                deleteBrand();
            }
        });

        updateBrandButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update brand logic
                // You need to implement the updateBrand() method
                updateBrand();
            }
        });

        //////////////////////////////////////////////
        //                                          //
        //    // Add listeners for other buttons    //
        //                                          //
        //////////////////////////////////////////////
            
        /////////////////////////////////////////////
        //  ________  ______   _______    ______   //
        // /        |/      \ /       \  /      \  //
        // $$$$$$$$//$$$$$$  |$$$$$$$  |/$$$$$$  | //
        //    $$ |  $$ |  $$ |$$ |  $$ |$$ |  $$ | //
        //    $$ |  $$ |  $$ |$$ |  $$ |$$ |  $$ | //
        //    $$ |  $$ |  $$ |$$ |  $$ |$$ |  $$ | //
        //    $$ |  $$ \__$$ |$$ |__$$ |$$ \__$$ | //
        //    $$ |  $$    $$/ $$    $$/ $$    $$/  //
        //    $$/    $$$$$$/  $$$$$$$/   $$$$$$/   //
        //                                         //
        /////////////////////////////////////////////

        // Populate tables
        populateTables();

        // Set table selection listeners
        brandTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Brand table selection logic
                // You can implement the selection logic here or create separate methods
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
    private void populateTables() 
    {
        List<ClassBrand> brands = retrieveBrands();
        for (ClassBrand brand : brands) 
        {
            brandTableModel.addRow(new Object[]{brand.getId(), brand.getName()});
        }

        List<ClassChipset> chipsets = retrieveChipsets();
        for (ClassChipset chipset : chipsets) 
        {
            chipsetTableModel.addRow(new Object[]{chipset.getId(), chipset.getName()});
        }

        List<ClassCPU> cpus = retrieveCPUs();
        for (ClassCPU cpu : cpus) 
        {
            cpuTableModel.addRow(new Object[]{cpu.getId(), cpu.getModel(), cpu.getPrice(), cpu.getCores(), cpu.getThreads(),
                    cpu.getFrequency(), cpu.getBrand().getId(), cpu.getSocket().getId()});
        }

        List<ClassGPU> gpus = retrieveGPUs();
        for (ClassGPU gpu : gpus) 
        {
            gpuTableModel.addRow(new Object[]{gpu.getId(), gpu.getModel(), gpu.getPrice(), gpu.getCores(), gpu.getMemory(),
                    gpu.getFrequency(), gpu.getBrand().getId()});
        }

        List<ClassPCB> pcbs = retrievePCBs();
        for (ClassPCB pcb : pcbs) 
        {
            pcbTableModel.addRow(new Object[]{pcb.getId(), pcb.getModel(), pcb.getPrice(),
                    pcb.getBrand().getId(), pcb.getSocket().getId(), pcb.getChipset().getId()});
        }

        List<ClassSocket> sockets = retrieveSockets();
        for (ClassSocket socket : sockets) 
        {
            socketTableModel.addRow(new Object[]{socket.getId(), socket.getName()});
        }

        List<ClassSocketToChipset> socketToChipsets = retrieveSocketToChipsets();
        for (ClassSocketToChipset socketToChipset : socketToChipsets) 
        {
            socketToChipsetTableModel.addRow(new Object[]{socketToChipset.getId(), socketToChipset.getSocket().getId(),
                    socketToChipset.getChipset().getId()});
        }
    }

    private JPanel createButtonPanel(JButton addButton, JButton deleteButton, JButton updateButton) 
    {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.add(addButton);
        panel.add(deleteButton);
        panel.add(updateButton);
        return panel;
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

    /////////////////////////////////////////////////////////////////
    //                                                             //
    //    // Methods to add/delete/update data for other tables    //
    //                                                             //
    /////////////////////////////////////////////////////////////////
    
    /////////////////////////////////////////////
    //  ________  ______   _______    ______   //
    // /        |/      \ /       \  /      \  //
    // $$$$$$$$//$$$$$$  |$$$$$$$  |/$$$$$$  | //
    //    $$ |  $$ |  $$ |$$ |  $$ |$$ |  $$ | //
    //    $$ |  $$ |  $$ |$$ |  $$ |$$ |  $$ | //
    //    $$ |  $$ |  $$ |$$ |  $$ |$$ |  $$ | //
    //    $$ |  $$ \__$$ |$$ |__$$ |$$ \__$$ | //
    //    $$ |  $$    $$/ $$    $$/ $$    $$/  //
    //    $$/    $$$$$$/  $$$$$$$/   $$$$$$/   //
    //                                         //
    /////////////////////////////////////////////


    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(DBMSApp::new);
    }
}
