package shunanglers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import simple.robot.api.ClientContext;

public class GUI extends JFrame {
  private static final long serialVersionUID = 1L;
  
  private JPanel contentPane;
  
  private ClientContext ctx;
  
  private Anglers main;
  
  public static JComboBox<String> combobox;
  
  public GUI(ClientContext ctx, Anglers main) {
    setBackground(Color.BLACK);
    setFont(new Font("Georgia", 1, 15));
    this.main = main;
    this.ctx = ctx;
    InitComponents();
  }
  
  private void InitComponents() {
    setDefaultCloseOperation(2);
    setBounds(100, 100, 316, 249);
    this.contentPane = new JPanel();
    this.contentPane.setBackground(Color.DARK_GRAY);
    this.contentPane.setForeground(Color.BLACK);
    this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(this.contentPane);
    this.contentPane.setLayout((LayoutManager)null);
    setLocationRelativeTo((Component)null);
    JLabel lblNewLabel = new JLabel("Catch dem Anglers");
    lblNewLabel.setFont(new Font("Georgia", 1, 15));
    lblNewLabel.setBackground(Color.WHITE);
    lblNewLabel.setForeground(Color.WHITE);
    lblNewLabel.setBounds(76, 11, 178, 67);
    this.contentPane.add(lblNewLabel);
    combobox = new JComboBox<>();
    combobox.setForeground(Color.WHITE);
    combobox.setBackground(Color.BLACK);
    combobox.setFont(new Font("Georgia", 1, 15));
    combobox.setModel(new DefaultComboBoxModel<>(new String[] { "Anglerfish" }));
    combobox.setBounds(32, 80, 236, 46);
    this.contentPane.add(combobox);
    JButton btnNewButton = new JButton("Start");
    btnNewButton.setBackground(Color.BLACK);
    btnNewButton.setFont(new Font("Georgia", 1, 15));
    btnNewButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            Anglers.started = true;
            GUI.this.dispose();
          }
        });
    btnNewButton.setBounds(102, 176, 89, 23);
    this.contentPane.add(btnNewButton);
    setTitle("ShunAnglers");
    setLocationRelativeTo((Component)null);
  }
}
