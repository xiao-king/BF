package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.*;
import rmi.RemoteHelper;


@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	
	private JTextArea textArea;//写代码的地方
	private JTextArea resultLabel;//写结果的地方
	private JTextArea inputArea;//输入的地方
	private JMenuItem loginItem;
	private String currentUser;
	private Font font;
	private ArrayList<String> history;
	private int ctry; 
	private String currentFilename; 
	
	public MainFrame() {
		history=new ArrayList<String>();
		font=new Font("微软雅黑",Font.BOLD,15);
		
		JFrame frame = new JFrame("BF Client");
		frame.setLayout(null);

		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");//file的menu
		menuBar.add(fileMenu);
		JMenu editMenu = new JMenu("Edit");
		menuBar.add(editMenu);
		JMenu loginMenu=new JMenu("login");
		menuBar.add(loginMenu);
		
		//添加file上的menuitem
		JMenuItem newMenuItem = new JMenuItem("New");
		fileMenu.add(newMenuItem);
		JMenuItem saveMenuItem = new JMenuItem("Save");
		fileMenu.add(saveMenuItem);
		JMenuItem runMenuItem = new JMenuItem("Run");
		fileMenu.add(runMenuItem);
		JMenuItem openItem=new JMenuItem("Open");
		fileMenu.add(openItem);
		JMenuItem versionItem=new JMenuItem("Version");
		fileMenu.add(versionItem);
		
		//添加login上的item
		loginItem    = new JMenuItem("Sign in");
		loginMenu   .add(loginItem);
		JMenuItem    registerItem = new JMenuItem("Register");
		loginMenu   .add(registerItem);
		registerItem.addActionListener(new registerActionListener());
		loginItem   .addActionListener(new LoginButtonListener());
       
		
		
		//撤销和重做的按钮
		JMenuItem undoItem=new JMenuItem("Undo");
		JMenuItem redoItem=new JMenuItem("Redo");
		editMenu.add(undoItem);
		editMenu.add(redoItem);
		
		//把menuBar加到frame上
		frame.setJMenuBar(menuBar);
        
		//注册监听
		newMenuItem  .addActionListener(new newActionListener());
		saveMenuItem .addActionListener(new SaveActionListener());
		runMenuItem  .addActionListener(new runActionListener());
        openItem     .addActionListener(new openActionListener());
		undoItem     .addActionListener(new undoActionListener());
		redoItem     .addActionListener(new redoActionListener());
		versionItem  .addActionListener(new versionActionListener());
        
		//设置代码区的参数
		textArea = new JTextArea();
		textArea.setFont(new Font("微软雅黑",Font.BOLD,18));
		textArea.setMargin(new Insets(10, 10, 10, 10));
		textArea.setBackground(Color.LIGHT_GRAY);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setBorder (BorderFactory.createLineBorder(Color.DARK_GRAY,2));
		textArea.setBounds(0, 0, 784, 400);
		frame.getContentPane().add(textArea);

		//设置输入区的参数
		inputArea=new JTextArea();
		inputArea.setFont(new Font("微软雅黑",Font.BOLD,16));
		inputArea.setMargin(new Insets(10, 10, 10, 10));
		inputArea.setBackground(Color.LIGHT_GRAY);
		inputArea.setLineWrap(true);
		inputArea.setWrapStyleWord(true);
		inputArea.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,2));
		inputArea.setBounds(0,402,400,300);
		frame.getContentPane().add(inputArea);
		
		//设置结果区的参数
		resultLabel = new JTextArea();
		resultLabel.setFont(new Font("楷体",Font.BOLD,15));
		resultLabel.setBounds(402,402,382,300);
		resultLabel.setBackground(Color.lightGray);
		resultLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,2));
		frame.getContentPane().add(resultLabel);

		//设置窗体的参数
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setLocation(390, 100);
		frame.setVisible(true);
		
		currentFilename=new String();
	}
    
	
    
	//对于new的监听
	class newActionListener implements ActionListener{

		public void actionPerformed(ActionEvent event) {
			if(textArea.getText().equals("")&&inputArea.getText().equals("")){
				JOptionPane.showMessageDialog(null, "已清空工作空间","来自BF的消息",JOptionPane.PLAIN_MESSAGE);
			}else{
				textArea.setText("");
				inputArea.setText("");
				resultLabel.setText("");
				history.removeAll(history);
				currentFilename=null;
			}
			
		}
	}
	
	class versionActionListener implements ActionListener{

		
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			String code=new String();
		    if(currentUser!=null){
		    	if(currentFilename!=null){
		    		try {
						String version[]=RemoteHelper.getInstance().getIOService().readVersionList(currentUser, currentFilename).split("__");
						
						String file=(String)JOptionPane.showInputDialog(null,"请选择: \n","来自BF的问候",JOptionPane.PLAIN_MESSAGE,null,version,version[0]);

                        if(file!=null&&!file.equals("")){
						    code=RemoteHelper.getInstance().getIOService().readFile(currentUser, currentFilename+"__"+file);
		    		    }
		    		} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	}else{
		    		JOptionPane.showMessageDialog(null, "你还没有任何版本", "来自BF的问候",  JOptionPane.ERROR_MESSAGE);
		    	}
		    }else{
		    	JOptionPane.showMessageDialog(null, "你还没有登录", "来自BF的问候",  JOptionPane.ERROR_MESSAGE);
		    }
		    textArea.setText(code);
		}
		
	}
	
	
	//对于run的监听
	class runActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String code =textArea .getText();     //代码
			String input=inputArea.getText();     //输入
			
			try {
				String s=RemoteHelper.getInstance().getExecuteService().execute(code, input);
				resultLabel.setText(s);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	//对于open的监听
	class openActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
            
			if(currentUser!=null){
			
			    //获取到用户选择的文件名
			    String file=new String();
			    try {
				    file=RemoteHelper.getInstance().getIOService().readFileList(currentUser);
			        if(file==null||file.equals("")){
			    	    JOptionPane.showMessageDialog(null, "你还没有任何文件","来自BF的提示",JOptionPane.ERROR_MESSAGE);
			        }else {
			        	String chooser[]=file.split("__");
			        	file=(String)JOptionPane.showInputDialog(null,"请选择: \n","来自BF的提示",JOptionPane.PLAIN_MESSAGE,null,chooser,chooser[0]);
			        }
			    } catch (RemoteException e1) {
			    	e1.printStackTrace();
			    }
			    
			    //打开最近版本
			    String version=new String();
			    String code=new String();
			    if(!file.equals("")&&file!=null){
			    	try {
			    		version=RemoteHelper.getInstance().getIOService().readVersionList(currentUser, file);
                        String s[]=version.split("__");		
			    		code=RemoteHelper.getInstance().getIOService().readFile(currentUser, file+"__"+s[s.length-1]);
			    	} catch (RemoteException e1) {
			    		// TODO Auto-generated catch block
			    		e1.printStackTrace();
			    	}
			    	textArea.setText(code);
			    }
			}else{
				JOptionPane.showMessageDialog(null, "你还没有登录","来自BF的消息",JOptionPane.ERROR_MESSAGE);
			}
		}	
	}
	
	//登录按钮的监听
	class LoginButtonListener implements ActionListener{
		private JFrame loginFrame;    //登录的窗体
    	private JButton sure;    //确认按钮
    	private JButton notSure;	//取消按钮
    	private JTextField userName;	//用来输入用户名的地方
    	private JPasswordField passwordField;	//用来输入密码的地方
		public void actionPerformed(ActionEvent twoButtonEvent) {
			
			if(currentUser==null){
				loginFrame=new JFrame();
				loginFrame.setLayout(new GridLayout(3,1));
				
				JPanel panels[]=new JPanel[3];
				for(int i=0;i<3;i++){
					panels[i]=new JPanel();
					loginFrame.add(panels[i]);
				}
				
				JLabel user=new JLabel("USER             ");
				user.setFont(font);
				userName=new JTextField(10);
				
				panels[0].add(user);
				panels[0].add(userName);
				
				JLabel password=new JLabel("PASSWORD");
				password.setFont(font);
				passwordField=new JPasswordField(10);
				panels[1].add(password);
				panels[1].add(passwordField);
				
				
				sure=new JButton("确定");
				notSure=new JButton("取消");
				sure.setFont(font);
				notSure.setFont(font);
				sure.setFocusPainted(false);
				notSure.setFocusPainted(false);
				
				sure.addActionListener(new twoButtonListener());
				notSure.addActionListener(new twoButtonListener());
				
				panels[2].add(sure);
				panels[2].add(notSure);
				
				//窗体属性
				loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				loginFrame.setSize(300, 150);
				loginFrame.setVisible(true);
				loginFrame.setResizable(false);
				
				loginFrame.setLocation(460, 340);
			    loginFrame.setTitle("Login");
				
			}else{
				loginItem.setText("Sign in");
				currentUser=null;
	    }
    }
	    //登录窗口处的两个按钮的监听
		class twoButtonListener implements ActionListener{
            boolean isPasswordTrue=true;
			String username;
			public void actionPerformed(ActionEvent event) {
				
				if(event.getSource()==sure&&!userName.getText().equals("")){
					username=userName.getText();
					char s[]=passwordField.getPassword();
					String password=new String();
					for(int i=0;i<s.length;i++){
						password=password+Character.toString(s[i]);
					}
					
					try {
						isPasswordTrue=RemoteHelper.getInstance().getUserService().login(username, password);
					    System.out.println(isPasswordTrue);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(isPasswordTrue){
					   currentUser=username;
					   loginItem.setText("Sign out");
					   loginFrame.dispose();
				    }
				}
				
				if(event.getSource()==notSure){
					loginFrame.dispose();
				}
			}
	    }
	}
	
	
	class registerActionListener implements ActionListener{
		
		private JFrame registerFrame;    //登录的窗体
    	private JButton sure;    //确认按钮
    	private JButton notSure;	//取消按钮
    	private JTextField userName;	//用来输入用户名的地方
    	private JPasswordField passwordField1;	//用来输入密码的地方
    	private JPasswordField passwordField2;	//用来确认密码的地方
    	
		public void actionPerformed(ActionEvent action) {
		
			    registerFrame=new JFrame();
				registerFrame.setLayout(new GridLayout(4,1));
				
				JPanel panels[]=new JPanel[4];
				for(int i=0;i<4;i++){
					panels[i]=new JPanel();
					registerFrame.add(panels[i]);
				}
				
				JLabel user=new JLabel("用户名    ");
				user.setFont(font);
				userName=new JTextField(10);
				
				panels[0].add(user);
				panels[0].add(userName);
				
				JLabel password1=new JLabel("设置密码");
				password1.setFont(font);
				passwordField1=new JPasswordField(10);
				panels[1].add(password1);
				panels[1].add(passwordField1);
				
				JLabel password2=new JLabel("确认密码");
				password2.setFont(font);
				passwordField2=new JPasswordField(10);
				panels[2].add(password2);
				panels[2].add(passwordField2);
				
				sure=new JButton("确定");
				notSure=new JButton("取消");
				sure.setFont(font);
				notSure.setFont(font);
				sure.setFocusPainted(false);
				notSure.setFocusPainted(false);
				
				sure.addActionListener(new rTwoButtonListener());
				notSure.addActionListener(new rTwoButtonListener());
				
				panels[3].add(sure);
				panels[3].add(notSure);
				
				//窗体属性
				registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				registerFrame.setSize(300, 200);
				registerFrame.setVisible(true);
				registerFrame.setResizable(false);
				registerFrame.setTitle("Register");
				registerFrame.setLocation(460, 340);
				
		}
		
		class rTwoButtonListener implements ActionListener{
			 boolean isPasswordTrue=true;
				String username;
				public void actionPerformed(ActionEvent event) {
					// TODO Auto-generated method stub
					if(event.getSource()==sure&&!userName.getText().equals("")){
						
						char s[]=passwordField1.getPassword();
						String password1=new String();
						for(int i=0;i<s.length;i++){
							password1=password1+Character.toString(s[i]);
						}
						
						String password2=new String();
						s=passwordField2.getPassword();
						for(int i=0;i<s.length;i++){
							password2=password2+Character.toString(s[i]);
						}
						
						if(!password1.equals(password2)){
							JOptionPane.showMessageDialog(registerFrame,"两次密码不一致", "错误消息",JOptionPane.ERROR_MESSAGE);
						}
						
						username=userName.getText();
						try {
							isPasswordTrue=RemoteHelper.getInstance().getUserService(). Register(username, password1);
						    
						} catch (RemoteException e) {
							e.printStackTrace();
						}
						if(isPasswordTrue){
						    JOptionPane.showMessageDialog(registerFrame, "注册成功","来自BF的问候",JOptionPane.PLAIN_MESSAGE);
						    registerFrame.dispose();
					    }else{
					    	JOptionPane.showMessageDialog(registerFrame, "该用户名已存在","来自BF的问候",JOptionPane.ERROR_MESSAGE);
					    }
					}
					
					if(event.getSource()==notSure){
						registerFrame.dispose();
					}
				}
		    }
		
		
	}
	//保存saveitem的监听
	class SaveActionListener implements ActionListener {
        private boolean label;
        private boolean currentLabel=false;
		public void actionPerformed(ActionEvent e) {
			if(currentUser!=null){
				if(currentFilename==null||currentFilename.equals("")){
						currentFilename=(String)JOptionPane.showInputDialog(null, "请输入文件名：\n", "来自BF的问候", JOptionPane.PLAIN_MESSAGE,null,null, "在这儿输入");
						currentLabel=true;
				}
				
				if(!currentFilename.equals("")||currentFilename!=null){
            
					String code = textArea.getText();
					String time=MainFrame.getTime();
						try {
							label=RemoteHelper.getInstance().getIOService().writeFile(code, currentUser, currentFilename+"__"+time,currentLabel);
							System.out.println(label);
						} catch (RemoteException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					
			    
					if(label){
						JOptionPane.showMessageDialog(null,"保存成功","来自BF的消息",JOptionPane.PLAIN_MESSAGE);
						currentLabel=false;
					}else{
						JOptionPane.showMessageDialog(null, "文件名重复或者文件并没有改动","来自BF的消息",JOptionPane.ERROR_MESSAGE);
					}
				}else{
					JOptionPane.showMessageDialog(null, "文件名不能为空","来自BF的消息",JOptionPane.ERROR_MESSAGE);
				}
			    
			}else{
				JOptionPane.showMessageDialog(null, "你还没有登录","来自BF的消息",JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	
	class undoActionListener implements ActionListener{

		public void actionPerformed(ActionEvent arg0) {
			if(ctry>=1){
			    ctry--;
			    textArea.setText(history.get(ctry));
			}else{
				JOptionPane.showMessageDialog(null, "无法向前","来自BF的消息",JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}
	
	class redoActionListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			if(ctry<history.size()-1){
				ctry++;
				textArea.setText(history.get(ctry));
			}else{
				JOptionPane.showMessageDialog(null, "不能再向后了","来自BF的消息",JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}
	
	class keepCode extends KeyAdapter{

		public void keyTyped(KeyEvent event) {
            char a =event.getKeyChar();
            if(a==','||a=='.'||a=='['||a==']'
               ||a=='<'||a=='>'||a=='+'||a=='-'){
            	history.add(textArea.getText());
            	ctry=history.size()-1;
            }
			
		}
	}	
	
	private static String getTime(){
		
		    Date a=new Date();
		    DateFormat format=new SimpleDateFormat("MMdd-HHmmss");
		    String time=format.format(a);
		    return time;
	}
}
	

