package FurnitureRenovate.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import FurnitureRenovate.model.BeanBrand;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.*;

import FurnitureRenovate.util.*;
import FurnitureRenovate.ui.*;
import FurnitureRenovate.control.*;


public class FrmBrand extends JDialog implements ActionListener {
    private JPanel toolBar = new JPanel();
    private Button btnAdd = new Button("添加");
    private Button btnModify = new Button("修改");
    private Button btnDel = new Button("删除");
    private JTextField edtKeyword = new JTextField(10);
    private Button btnSearch = new Button("查询品牌名");
    private Object tblTitle[] = {"品牌名", "描述"};
    private Object tblData[][];
    List<BeanBrand> brands = null;
    DefaultTableModel tablmod = new DefaultTableModel();
    private JTable dataTable = new JTable(tablmod);

    private void reloadTable() {
        try {
            brands = (new BrandManager()).search(this.edtKeyword.getText());
            tblData = new Object[brands.size()][2];
            for (int i = 0; i < brands.size(); i++) {
                tblData[i][0] = brands.get(i).getBrandName();
                tblData[i][1] = brands.get(i).getBrandDescribe();
            }
            tablmod.setDataVector(tblData, tblTitle);
            this.dataTable.validate();
            this.dataTable.repaint();
        } catch (BaseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public FrmBrand(Frame f, String s, boolean b) {
        super(f, s, b);
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        toolBar.add(btnAdd);
        toolBar.add(btnModify);
        toolBar.add(btnDel);
        toolBar.add(edtKeyword);
        toolBar.add(btnSearch);


        this.getContentPane().add(toolBar, BorderLayout.NORTH);
        //提取现有数据
        this.reloadTable();
        this.getContentPane().add(new JScrollPane(this.dataTable), BorderLayout.CENTER);

        // 屏幕居中显示
        this.setSize(800, 600);
        double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        this.setLocation((int) (width - this.getWidth()) / 2,
                (int) (height - this.getHeight()) / 2);

        this.validate();

        this.btnAdd.addActionListener(this);
        this.btnModify.addActionListener(this);
        this.btnDel.addActionListener(this);
        this.btnSearch.addActionListener(this);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                //System.exit(0);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if (e.getSource() == this.btnAdd) {
            FrmAddBrand dlg = new FrmAddBrand(this, "添加品牌", true);
            dlg.setVisible(true);
            if(dlg.changeFlag == 0)
                this.reloadTable();
        }
        else if (e.getSource() == this.btnModify) {
            int i = this.dataTable.getSelectedRow();
            if (i < 0) {
                JOptionPane.showMessageDialog(null, "请选择品牌", "提示", JOptionPane.ERROR_MESSAGE);
                return;
            }
            BeanBrand brand = this.brands.get(i);

            FrmModifyBrand dlg = new FrmModifyBrand(this, "修改品牌", true);
            dlg.brand = brand;
            dlg.setVisible(true);
            if(dlg.brand != null)
                this.reloadTable();
        }
        else if (e.getSource() == this.btnDel) {
            int i = this.dataTable.getSelectedRow();
            if (i < 0) {
                JOptionPane.showMessageDialog(null, "请选择品牌", "提示", JOptionPane.ERROR_MESSAGE);
                return;
            }
            BeanBrand brand = brands.get(i);

            if (JOptionPane.showConfirmDialog(this, "确定删除" + brand.getBrandName() + "吗？", "确认", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    (new BrandManager()).DeleteBrand(brand);
                    this.reloadTable();
                } catch (BaseException e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        else if (e.getSource() == this.btnSearch) {
            this.reloadTable();
        }

    }
}
