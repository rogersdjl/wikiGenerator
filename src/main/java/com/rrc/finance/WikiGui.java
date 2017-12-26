package com.rrc.finance;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.rrc.finance.http.HttpMethod;
import com.rrc.finance.wiki.WikiGenerator;
import com.rrc.finance.wiki.WikiParam;
/**
 * wiki界面类，用eclipse上的swing插件生成。部分修改。
 * @author doujinlong
 *
 */
public class WikiGui {

	private List<String> history = Lists.newArrayList();
	private List<WikiParam> wikiResults = Lists.newArrayList();
	private JFrame frame;
	private JTextField cookie;
	private JTextField url;
	private JTextArea result;
	private Color temp;
	/**
	 * 启动界面
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WikiGui window = new WikiGui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public WikiGui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1200, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		
		JComboBox method = new JComboBox(new String[]{"GET","PUT","POST"});
		springLayout.putConstraint(SpringLayout.WEST, method, 99, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(method);
		
		cookie = new JTextField();
		springLayout.putConstraint(SpringLayout.SOUTH, method, -28, SpringLayout.NORTH, cookie);
		springLayout.putConstraint(SpringLayout.NORTH, cookie, 77, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, cookie, 0, SpringLayout.WEST, method);
		springLayout.putConstraint(SpringLayout.EAST, cookie, 407, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(cookie);
		cookie.setColumns(10);
		
		JTextArea json = new JTextArea();
		json.setLineWrap(true);
		springLayout.putConstraint(SpringLayout.NORTH, json, 13, SpringLayout.SOUTH, cookie);
		springLayout.putConstraint(SpringLayout.WEST, json, 0, SpringLayout.WEST, method);
		springLayout.putConstraint(SpringLayout.SOUTH, json, -208, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, json, 0, SpringLayout.EAST, cookie);
		frame.getContentPane().add(json);
		
		JLabel lblNewLabel = new JLabel("请求方式:");
		springLayout.putConstraint(SpringLayout.NORTH, lblNewLabel, 3, SpringLayout.NORTH, method);
		springLayout.putConstraint(SpringLayout.WEST, lblNewLabel, 10, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(lblNewLabel);
		
		JLabel label = new JLabel("URL:");
		springLayout.putConstraint(SpringLayout.WEST, label, 0, SpringLayout.WEST, lblNewLabel);
		frame.getContentPane().add(label);
		
		JLabel label_1 = new JLabel("JSON(post/put):");
		springLayout.putConstraint(SpringLayout.NORTH, label_1, 17, SpringLayout.SOUTH, cookie);
		springLayout.putConstraint(SpringLayout.WEST, label_1, 0, SpringLayout.WEST, lblNewLabel);
		springLayout.putConstraint(SpringLayout.EAST, label_1, 73, SpringLayout.WEST, lblNewLabel);
		frame.getContentPane().add(label_1);
		
		JLabel label_2 = new JLabel("Cookie:");
		springLayout.putConstraint(SpringLayout.SOUTH, label, -8, SpringLayout.NORTH, label_2);
		springLayout.putConstraint(SpringLayout.NORTH, label_2, 0, SpringLayout.NORTH, cookie);
		springLayout.putConstraint(SpringLayout.WEST, label_2, 0, SpringLayout.WEST, lblNewLabel);
		frame.getContentPane().add(label_2);
		JList historyRecord = new JList();
		springLayout.putConstraint(SpringLayout.NORTH, historyRecord, 53, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, historyRecord, 432, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, historyRecord, -91, SpringLayout.SOUTH, frame.getContentPane());
//		springLayout.putConstraint(SpringLayout.EAST, list, 592, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(historyRecord);
		JButton button = new JButton("提交");
		springLayout.putConstraint(SpringLayout.NORTH, button, -1, SpringLayout.NORTH, method);
		springLayout.putConstraint(SpringLayout.EAST, button, 0, SpringLayout.EAST, cookie);
		frame.getContentPane().add(button);
		
		url = new JTextField();
		springLayout.putConstraint(SpringLayout.WEST, url, 0, SpringLayout.WEST, method);
		springLayout.putConstraint(SpringLayout.SOUTH, url, -6, SpringLayout.NORTH, cookie);
		springLayout.putConstraint(SpringLayout.EAST, url, 0, SpringLayout.EAST, cookie);
		url.setColumns(10);
		frame.getContentPane().add(url);
		
		JLabel label_3 = new JLabel("历史记录");
		springLayout.putConstraint(SpringLayout.NORTH, label_3, 0, SpringLayout.NORTH, method);
		springLayout.putConstraint(SpringLayout.WEST, label_3, 0, SpringLayout.WEST, historyRecord);
		frame.getContentPane().add(label_3);
		
		JButton btnwiki = new JButton("导出wiki");
		springLayout.putConstraint(SpringLayout.WEST, btnwiki, 6, SpringLayout.EAST, label_3);
		springLayout.putConstraint(SpringLayout.SOUTH, btnwiki, 0, SpringLayout.SOUTH, lblNewLabel);
		
		
		result = new JTextArea();
		result.setLineWrap(true);
		result.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(result);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 24, SpringLayout.SOUTH, json);
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 0, SpringLayout.WEST, method);
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, 148, SpringLayout.SOUTH, json);
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, 0, SpringLayout.EAST, cookie);
		frame.getContentPane().add(scrollPane);
		
		JLabel label_4 = new JLabel("返回:");
		springLayout.putConstraint(SpringLayout.NORTH, label_4, 0, SpringLayout.NORTH, result);
		springLayout.putConstraint(SpringLayout.WEST, label_4, 0, SpringLayout.WEST, label_1);
		frame.getContentPane().add(label_4);
		frame.getContentPane().add(btnwiki);
		
		btnwiki.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int []is= historyRecord.getSelectedIndices();
				List<WikiParam> wiki = Lists.newArrayList();
				for (int i : is) {
					if (wikiResults.get(i).getStatus().intValue()==200
							||wikiResults.get(i).getStatus().intValue()==201) {
						wiki.add(wikiResults.get(i));
					}
				}
				String ret = WikiGenerator.generateWiki(wiki);
				try {
					WikiGenerator.createWikiFile(ret, "C:/Users/Administrator/Desktop/");
					JOptionPane.showMessageDialog(frame, "已经导出到桌面,wiki.html,双击打开看看~");
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(frame, "错误"+e1.getMessage(),"错误！！！",JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		});
		
		url.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				if (StringUtils.isNotBlank(url.getText())&&!url.getText().startsWith("http://")) {
					JOptionPane.showMessageDialog(frame, "错误！！！url必须以http://开始","错误！！！",JOptionPane.ERROR_MESSAGE);
					url.setText("");
				}
			}
			@Override
			public void focusGained(FocusEvent e) {
			}
		});
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				result.setBackground(temp!=null?temp:result.getBackground());
				WikiParam ret = null;
				try {
					ret = WikiGenerator.httpRequest(HttpMethod.resolve((String)method.getSelectedItem()), url.getText(), cookie.getText(), json.getText());
					result.setText(ret.getResult());
					if (ret.getStatus()==null||ret.getStatus()<=0) {
						temp = result.getBackground();
						result.setBackground(Color.RED);
						return;
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					temp = result.getBackground();
					result.setBackground(Color.RED);
					result.setText("请求异常，"+e1.getMessage());
					return;
				}
				history.add(url.getText());
				historyRecord.setListData(history.toArray());
				wikiResults.add(ret);
			}
		});
		
	}
}
