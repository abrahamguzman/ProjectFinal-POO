package com.proyectofinal.poo;

import com.proyectofinal.poo.model.Category;
import com.proyectofinal.poo.model.Tool;
import com.proyectofinal.poo.repository.CategoryRepository;
import com.proyectofinal.poo.repository.ToolRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class ToolsForm {
    private JPanel Main;
    private JTextField txtName;
    private JTextField txtPrice;
    private JTextField txtCategory;
    private JButton btnSave;
    private JTable table1;
    private JTextField textField1;
    private JButton btnUpdate;
    private JButton btnFindById;
    private JButton btnDelete;
    private JComboBox comboBox1;


    private ToolRepository toolRepository = new ToolRepository();
    private CategoryRepository categoryRepository = new CategoryRepository();

    public static void main(String[] args) {
        JFrame frame = new JFrame("ToolsForm");
        ToolsForm toolsForm = new ToolsForm();
        frame.setContentPane(toolsForm.Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        toolsForm.loadTableData();
        toolsForm.loadCategories();
    }

    public ToolsForm() {
        table1.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && table1.getSelectedRow() != -1) {
                int selectedRow = table1.getSelectedRow();
                txtName.setText(table1.getModel().getValueAt(selectedRow, 1).toString());
                txtPrice.setText(table1.getModel().getValueAt(selectedRow, 2).toString());
                String categoryName = table1.getModel().getValueAt(selectedRow, 3).toString();
                for (int i = 0; i < comboBox1.getItemCount(); i++) {
                    ComboBoxItem item = (ComboBoxItem) comboBox1.getItemAt(i);
                    if (item.getName().equals(categoryName)) {
                        comboBox1.setSelectedIndex(i);
                        break;
                    }
                }
            }
        });
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = txtName.getText();
                double price = Double.parseDouble(txtPrice.getText());
                int categoryId = ((ComboBoxItem) Objects.requireNonNull(comboBox1.getSelectedItem())).getId();

                toolRepository.create(name, price, categoryId);
                loadTableData();
            }
        });
        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ComboBoxItem selectedItem = (ComboBoxItem) comboBox1.getSelectedItem();
                if (selectedItem != null) {
                    int selectedCategoryId = selectedItem.getId();
                    System.out.println("Selected Category ID: " + selectedCategoryId);
                }
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table1.getSelectedRow();
                if (selectedRow != -1) {
                    int toolId = (int) table1.getModel().getValueAt(selectedRow, 0);
                    int response = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas eliminar esta herramienta?", "Confirmación", JOptionPane.YES_NO_OPTION);
                    if (response == JOptionPane.YES_OPTION) {
                        toolRepository.delete(toolId);
                        loadTableData();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, selecciona una herramienta para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table1.getSelectedRow();
                if (selectedRow != -1) {
                    int toolId = (int) table1.getModel().getValueAt(selectedRow, 0);
                    String name = txtName.getText();
                    double price = Double.parseDouble(txtPrice.getText());
                    int categoryId = ((ComboBoxItem) Objects.requireNonNull(comboBox1.getSelectedItem())).getId();

                    toolRepository.update(toolId, name, price, categoryId);
                    loadTableData();
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, selecciona una herramienta para actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnFindById.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int toolId = Integer.parseInt(textField1.getText());
                Tool tool = toolRepository.findById(toolId);
                if (tool != null) {
                    txtName.setText(tool.getName());
                    txtPrice.setText(String.valueOf(tool.getPrice()));
                    String categoryName = tool.getCategoryName();

                    for (int i = 0; i < comboBox1.getItemCount(); i++) {
                        ComboBoxItem item = (ComboBoxItem) comboBox1.getItemAt(i);
                        if (item.getName().equals(categoryName)) {
                            comboBox1.setSelectedIndex(i);
                            break;
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró ninguna herramienta con el ID proporcionado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    void loadCategories() {
        comboBox1.removeAllItems();
        for (Category category : categoryRepository.findAll()) {
            comboBox1.addItem(new ComboBoxItem(category.getId(), category.getName()));
        }
    }

    class ComboBoxItem {
        private int id;
        private String name;

        public ComboBoxItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    void loadTableData() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Price");
        model.addColumn("Category");

        for (Tool tool : toolRepository.findAll()) {
            model.addRow(new Object[]{
                    tool.getId(),
                    tool.getName(),
                    tool.getPrice(),
                    tool.getCategoryName()
            });
        }

        table1.setModel(model);
    }
}
