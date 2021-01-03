package org.fisco.bcos.asset.client;

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;

import java.math.BigInteger;

public class MyChainApp extends JFrame {
	MyChainControl myChain;

	public MyChainApp() {
		super("My Financial Supply Chain App");
		this.setSize(700, 900);
		this.setBackground(new Color(189,252,200));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(22, 5));

		myAccountPanel();
		queryReceiptPanel();
		createReceiptPanel();
		transferReceiptPanel();
		financingPanel();
		clearReceiptPanel();

		this.setVisible(true);
		myChain = new MyChainControl();
		myChain.myChainInit();
	}

	void myAccountPanel() {
		JPanel jp00 = new JPanel();
		JLabel jb00 = new JLabel("My Financial Supply Chain App");
		jb00.setFont(new java.awt.Font("Dialog", 1, 20));
		jp00.add(jb00);
		jp00.setBackground(new Color(130,202,156));

		JPanel jp0 = new JPanel();
		JLabel jb0 = new JLabel("—————————————————————————————————————————————————————————");
		jp0.add(jb0);
		jp0.setBackground(new Color(189,252,200));

		JPanel jp1 = new JPanel();
		JLabel jb1 = new JLabel("My Account: ");
		jb1.setFont(new java.awt.Font("Dialog", 1, 20));
		jp1.add(jb1);
		jp1.setBackground(new Color(189,252,200));

		JPanel jp2 = new JPanel();
		jp2.setLayout(new FlowLayout());
		JLabel jb2_1 = new JLabel("Company: ");
		JTextField jt2_2 = new JTextField("");
		jt2_2.setColumns(10);
		JButton jbt2_3 = new JButton("Login");
		jbt2_3.setBorderPainted(false);
		jp2.add(jb2_1);
		jp2.add(jt2_2);
		jp2.add(jbt2_3);
		jp2.setBackground(new Color(189,252,200));

		JPanel jp3 = new JPanel();
		jp3.setLayout(new FlowLayout());
		JLabel jb3 = new JLabel("Company Fund: ");
		jp3.add(jb3);
		jp3.setBackground(new Color(189,252,200));

		jbt2_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myChain.loginControl(jt2_2.getText());
				jb3.setText("Company Fund: " + myChain.getCompanyFundControl());
			}
		});
		this.add(jp00);
		this.add(jp0);
		this.add(jp1);
		this.add(jp2);
		this.add(jp3);
	}

	void queryReceiptPanel() {
		JPanel jp0 = new JPanel();
		JLabel jb0 = new JLabel("—————————————————————————————————————————————————————————");
		jp0.add(jb0);
		jp0.setBackground(new Color(189,252,200));

		JPanel jp1 = new JPanel();
		JLabel jb1 = new JLabel("Query Receipt: ");
		jb1.setFont(new java.awt.Font("Dialog", 1, 20));
		jp1.add(jb1);
		jp1.setBackground(new Color(189,252,200));

		JPanel jp2 = new JPanel();
		jp2.setLayout(new FlowLayout());
		JLabel jb2_1 = new JLabel("From Company: ");
		JTextField jt2_2 = new JTextField("");
		jt2_2.setColumns(10);
		JButton jbt2_3 = new JButton("Find");
		jbt2_3.setBorderPainted(false);
		jp2.add(jb2_1);
		jp2.add(jt2_2);
		jp2.add(jbt2_3);
		jp2.setBackground(new Color(189,252,200));

		JPanel jp3 = new JPanel();
		jp3.setLayout(new FlowLayout());
		JLabel jb3 = new JLabel("Receipt Money: ");
		jp3.add(jb3);
		jp3.setBackground(new Color(189,252,200));

		jbt2_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String from_company = jt2_2.getText();
				jb3.setText("Receipt Money: " + myChain.getReceiptMoneyControl(from_company));
			}
		});

		this.add(jp0);
		this.add(jp1);
		this.add(jp2);
		this.add(jp3);
	}

	void createReceiptPanel() {
		JPanel jp0 = new JPanel();
		JLabel jb0 = new JLabel("—————————————————————————————————————————————————————————");
		jp0.add(jb0);
		jp0.setBackground(new Color(189,252,200));

		JPanel jp1 = new JPanel();
		JLabel jb1 = new JLabel("Create Receipt: ");
		jb1.setFont(new java.awt.Font("Dialog", 1, 20));
		jp1.add(jb1);
		jp1.setBackground(new Color(189,252,200));

		JPanel jp2 = new JPanel();
		jp2.setLayout(new FlowLayout());
		JLabel jb2_1 = new JLabel("To Company: ");
		JTextField jt2_2 = new JTextField("");
		jt2_2.setColumns(10);
		JLabel jb3_1 = new JLabel("Money: ");
		JTextField jt3_2 = new JTextField("");
		jt3_2.setColumns(10);
		JButton jbt2_3 = new JButton("Create");
		jbt2_3.setBorderPainted(false);
		jp2.add(jb2_1);
		jp2.add(jt2_2);
		jp2.add(jb3_1);
		jp2.add(jt3_2);
		jp2.add(jbt2_3);
		jp2.setBackground(new Color(189,252,200));

		jbt2_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String to_company = jt2_2.getText();
				int money = Integer.valueOf(jt3_2.getText());
				myChain.createReceiptControl(to_company, money);
				jt2_2.setText("");
				jt3_2.setText("");
			}
		});
		this.add(jp0);
		this.add(jp1);
		this.add(jp2);
	}

	void transferReceiptPanel() {
		JPanel jp0 = new JPanel();
		JLabel jb0 = new JLabel("—————————————————————————————————————————————————————————");
		jp0.add(jb0);
		jp0.setBackground(new Color(189,252,200));

        JPanel jp1 = new JPanel();
        JLabel jb1 = new JLabel("Transfer Receipt: ");
		jb1.setFont(new java.awt.Font("Dialog", 1, 20));
        jp1.add(jb1);
        jp1.setBackground(new Color(189,252,200));

        JPanel jp2 = new JPanel();
        jp2.setLayout(new FlowLayout());
        JLabel CompanyB = new JLabel("Company B: ");
        JTextField CBTextF = new JTextField("");
        CBTextF.setColumns(8);
        JLabel CompanyC = new JLabel("Company C: ");
        JTextField CCTextF = new JTextField("");
        CCTextF.setColumns(8);
        JLabel MoneyLB = new JLabel("Money ");
        JTextField MoneyTF = new JTextField("");
        MoneyTF.setColumns(5);
        JButton jb = new JButton("Transfer");
        jb.setBorderPainted(false);
        jp2.setBackground(new Color(189,252,200));

        jp2.add(CompanyB);
        jp2.add(CBTextF);
        jp2.add(CompanyC);
        jp2.add(CCTextF);
        jp2.add(MoneyLB);
        jp2.add(MoneyTF);
        jp2.add(jb);

        jb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String company_B = CBTextF.getText();
                String company_C = CCTextF.getText();
                int money = Integer.valueOf(MoneyTF.getText());
                myChain.transferReceiptControl(company_B, company_C, money);
                CBTextF.setText("");
                CCTextF.setText("");
                MoneyTF.setText("");
            }
        });
        this.add(jp0);
        this.add(jp1);
        this.add(jp2);
	}

	void financingPanel() {
		JPanel jp0 = new JPanel();
		JLabel jb0 = new JLabel("—————————————————————————————————————————————————————————");
		jp0.add(jb0);
		jp0.setBackground(new Color(189,252,200));

		JPanel jp1 = new JPanel();
        JLabel jb1 = new JLabel("Financing With Receipt: ");
		jb1.setFont(new java.awt.Font("Dialog", 1, 20));
        jp1.add(jb1);
        jp1.setBackground(new Color(189,252,200));

        JPanel jp2 = new JPanel();
        jp2.setLayout(new FlowLayout());
        JLabel MoneyLB = new JLabel("Money: ");
        JTextField MoneyTF = new JTextField("");
        MoneyTF.setColumns(10);
        JButton jb = new JButton("Financing");
        jb.setBorderPainted(false);
        jp2.add(MoneyLB);
        jp2.add(MoneyTF);
        jp2.add(jb);
        jp2.setBackground(new Color(189,252,200));

        jb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int money = Integer.valueOf(MoneyTF.getText());
                String flag = myChain.financingWithReceiptControl(money);
                MoneyTF.setText(flag);
            }
        });
        this.add(jp0);
        this.add(jp1);
        this.add(jp2);
	}

	void clearReceiptPanel() {
		JPanel jp0 = new JPanel();
		JLabel jb0 = new JLabel("—————————————————————————————————————————————————————————");
		jp0.add(jb0);
		jp0.setBackground(new Color(189,252,200));

		JPanel jp1 = new JPanel();
        JLabel jb1 = new JLabel("Clear Receipt: ");
		jb1.setFont(new java.awt.Font("Dialog", 1, 20));
        jp1.add(jb1);
        jp1.setBackground(new Color(189,252,200));

        JPanel jp00 = new JPanel();
		JLabel jb00 = new JLabel("—————————————————————————————————————————————————————————");
		jp00.add(jb00);
		jp00.setBackground(new Color(189,252,200));

        JPanel jp2 = new JPanel();
        jp2.setLayout(new FlowLayout());
        JLabel FCompanyLB = new JLabel("From Company: ");
        JTextField FCompanyTF = new JTextField("");
        FCompanyTF.setColumns(10);
        JButton jb = new JButton("Clear");
        jb.setBorderPainted(false);
        jp2.add(FCompanyLB);
        jp2.add(FCompanyTF);
        jp2.add(jb);
        jp2.setBackground(new Color(189,252,200));
        
        jb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String from_company = FCompanyTF.getText();
                String flag = myChain.clearReceiptControl(from_company);
                FCompanyTF.setText(flag);
            }
        });
        this.add(jp0);
        this.add(jp1);
        this.add(jp2);
        this.add(jp00);
	}

	public static void main(String[] args) {
		MyChainApp myApp = new MyChainApp();
	}
}