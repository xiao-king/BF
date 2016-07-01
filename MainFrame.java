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
	
	private JTextArea textArea;//д����ĵط�
	private JTextArea resultLabel;//д����ĵط�
	private JTextArea inputArea;//����ĵط�
	private JMenuItem loginItem;
	private String currentUser;
	private Font font;
	private ArrayList<String> history;
	private int ctry; 
	private String currentFilename; 
	
	public MainFrame() {
		history=new ArrayList<String>();
		font=new Font("΢���ź�",Font.BOLD,15);
		
		JFrame frame = new JFrame("BF Client");
		frame.setLayout(null);

		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");//file��menu
		menuBar.add(fileMenu);
		JMenu editMenu = new JMenu("Edit");
		menuBar.add(editMenu);
		JMenu loginMenu=new JMenu("login");
		menuBar.add(loginMenu);
		
		//���file�ϵ�menuitem
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
		
		//���login�ϵ�item
		loginItem    = new JMenuItem("Sign in");
		loginMenu   .add(loginItem);
		JMenuItem    registerItem = new JMenuItem("Register");
		loginMenu   .add(registerItem);
		registerItem.addActionListener(new registerActionListener());
		loginItem   .addActionListener(new LoginButtonListener());
       
		
		
		//�����������İ�ť
		JMenuItem undoItem=new JMenuItem("Undo");
		JMenuItem redoItem=new JMenuItem("Redo");
		editMenu.add(undoItem);
		editMenu.add(redoItem);
		
		//��menuBar�ӵ�frame��
		frame.setJMenuBar(menuBar);
        
		//ע�����
		newMenuItem  .addActionListener(new newActionListener());
		saveMenuItem .addActionListener(new SaveActionListener());
		runMenuItem  .addActionListener(new runActionListener());
        openItem     .addActionListener(new openActionListener());
		undoItem     .addActionListener(new undoActionListener());
		redoItem     .addActionListener(new redoActionListener());
		versionItem  .addActionListener(new versionActionListener());
        
		//���ô������Ĳ���
		textArea = new JTextArea();
		textArea.setFont(new Font("΢���ź�",Font.BOLD,18));
		textArea.setMargin(new Insets(10, 10, 10, 10));
		textArea.setBackground(Color.LIGHT_GRAY);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setBorder (BorderFactory.createLineBorder(Color.DARK_GRAY,2));
		textArea.setBounds(0, 0, 784, 400);
		frame.getContentPane().add(textArea);

		//�����������Ĳ���
		inputArea=new JTextArea();
		inputArea.setFont(new Font("΢���ź�",Font.BOLD,16));
		inputArea.setMargin(new Insets(10, 10, 10, 10));
		inputArea.setBackground(Color.LIGHT_GRAY);
		inputArea.setLineWrap(true);
		inputArea.setWrapStyleWord(true);
		inputArea.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,2));
		inputArea.setBounds(0,402,400,300);
		frame.getContentPane().add(inputArea);
		
		//���ý�����Ĳ���
		resultLabel = new JTextArea();
		resultLabel.setFont(new Font("����",Font.BOLD,15));
		resultLabel.setBounds(402,402,382,300);
		resultLabel.setBackground(Color.lightGray);
		resultLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,2));
		frame.getContentPane().add(resultLabel);

		//���ô���Ĳ���
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setLocation(390, 100);
		frame.setVisible(true);
		
		currentFilename=new String();
	}
    
	
    
	//����new�ļ���
	class newActionListener implements ActionListener{

		public void actionPerformed(ActionEvent event) {
			if(textArea.getText().equals("")&&inputArea.getText().equals("")){
				JOptionPane.showMessageDialog(null, "����չ����ռ�","����BF����Ϣ",JOptionPane.PLAIN_MESSAGE);
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
						
						String file=(String)JOptionPane.showInputDialog(null,"��ѡ��: \n","����BF���ʺ�",JOptionPane.PLAIN_MESSAGE,null,version,version[0]);

                        if(file!=null&&!file.equals("")){
						    code=RemoteHelper.getInstance().getIOService().readFile(currentUser, currentFilename+"__"+file);
		    		    }
		    		} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	}else{
		    		JOptionPane.showMessageDialog(null, "�㻹û���κΰ汾", "����BF���ʺ�",  JOptionPane.ERROR_MESSAGE);
		    	}
		    }else{
		    	JOptionPane.showMessageDialog(null, "�㻹û�е�¼", "����BF���ʺ�",  JOptionPane.ERROR_MESSAGE);
		    }
		    textArea.setText(code);
		}
		
	}
	
	
	//����run�ļ���
	class runActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String code =textArea .getText();     //����
			String input=inputArea.getText();     //����
			
			try {
				String s=RemoteHelper.getInstance().getExecuteService().execute(code, input);
				resultLabel.setText(s);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	//����open�ļ���
	class openActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
            
			if(currentUser!=null){
			
			    //��ȡ���û�ѡ����ļ���
			    String file=new String();
			    try {
				    file=RemoteHelper.getInstance().getIOService().readFileList(currentUser);
			        if(file==null||file.equals("")){
			    	    JOptionPane.showMessageDialog(null, "�㻹û���κ��ļ�","����BF����ʾ",JOptionPane.ERROR_MESSAGE);
			        }else {
			        	String chooser[]=file.split("__");
			        	file=(String)JOptionPane.showInputDialog(null,"��ѡ��: \n","����BF����ʾ",JOptionPane.PLAIN_MESSAGE,null,chooser,chooser[0]);
			        }
			    } catch (RemoteException e1) {
			    	e1.printStackTrace();
			    }
			    
			    //������汾
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
				JOptionPane.showMessageDialog(null, "�㻹û�е�¼","����BF����Ϣ",JOptionPane.ERROR_MESSAGE);
			}
		}	
	}
	
	//��¼��ť�ļ���
	class LoginButtonListener implements ActionListener{
		private JFrame loginFrame;    //��¼�Ĵ���
    	private JButton sure;    //ȷ�ϰ�ť
    	private JButton notSure;	//ȡ����ť
    	private JTextField userName;	//���������û����ĵط�
    	private JPasswordField passwordField;	//������������ĵط�
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
				
				
				sure=new JButton("ȷ��");
				notSure=new JButton("ȡ��");
				sure.setFont(font);
				notSure.setFont(font);
				sure.setFocusPainted(false);
				notSure.setFocusPainted(false);
				
				sure.addActionListener(new twoButtonListener());
				notSure.addActionListener(new twoButtonListener());
				
				panels[2].add(sure);
				panels[2].add(notSure);
				
				//��������
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
	    //��¼���ڴ���������ť�ļ���
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
		
		private JFrame registerFrame;    //��¼�Ĵ���
    	private JButton sure;    //ȷ�ϰ�ť
    	private JButton notSure;	//ȡ����ť
    	private JTextField userName;	//���������û����ĵط�
    	private JPasswordField passwordField1;	//������������ĵط�
    	private JPasswordField passwordField2;	//����ȷ������ĵط�
    	
		public void actionPerformed(ActionEvent action) {
		
			    registerFrame=new JFrame();
				registerFrame.setLayout(new GridLayout(4,1));
				
				JPanel panels[]=new JPanel[4];
				for(int i=0;i<4;i++){
					panels[i]=new JPanel();
					registerFrame.add(panels[i]);
				}
				
				JLabel user=new JLabel("�û���    ");
				user.setFont(font);
				userName=new JTextField(10);
				
				panels[0].add(user);
				panels[0].add(userName);
				
				JLabel password1=new JLabel("��������");
				password1.setFont(font);
				passwordField1=new JPasswordField(10);
				panels[1].add(password1);
				panels[1].add(passwordField1);
				
				JLabel password2=new JLabel("ȷ������");
				password2.setFont(font);
				passwordField2=new JPasswordField(10);
				panels[2].add(password2);
				panels[2].add(passwordField2);
				
				sure=new JButton("ȷ��");
				notSure=new JButton("ȡ��");
				sure.setFont(font);
				notSure.setFont(font);
				sure.setFocusPainted(false);
				notSure.setFocusPainted(false);
				
				sure.addActionListener(new rTwoButtonListener());
				notSure.addActionListener(new rTwoButtonListener());
				
				panels[3].add(sure);
				panels[3].add(notSure);
				
				//��������
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
							JOptionPane.showMessageDialog(registerFrame,"�������벻һ��", "������Ϣ",JOptionPane.ERROR_MESSAGE);
						}
						
						username=userName.getText();
						try {
							isPasswordTrue=RemoteHelper.getInstance().getUserService(). Register(username, password1);
						    
						} catch (RemoteException e) {
							e.printStackTrace();
						}
						if(isPasswordTrue){
						    JOptionPane.showMessageDialog(registerFrame, "ע��ɹ�","����BF���ʺ�",JOptionPane.PLAIN_MESSAGE);
						    registerFrame.dispose();
					    }else{
					    	JOptionPane.showMessageDialog(registerFrame, "���û����Ѵ���","����BF���ʺ�",JOptionPane.ERROR_MESSAGE);
					    }
					}
					
					if(event.getSource()==notSure){
						registerFrame.dispose();
					}
				}
		    }
		
		
	}
	//����saveitem�ļ���
	class SaveActionListener implements ActionListener {
        private boolean label;
        private boolean currentLabel=false;
		public void actionPerformed(ActionEvent e) {
			if(currentUser!=null){
				if(currentFilename==null||currentFilename.equals("")){
						currentFilename=(String)JOptionPane.showInputDialog(null, "�������ļ�����\n", "����BF���ʺ�", JOptionPane.PLAIN_MESSAGE,null,null, "���������");
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
						JOptionPane.showMessageDialog(null,"����ɹ�","����BF����Ϣ",JOptionPane.PLAIN_MESSAGE);
						currentLabel=false;
					}else{
						JOptionPane.showMessageDialog(null, "�ļ����ظ������ļ���û�иĶ�","����BF����Ϣ",JOptionPane.ERROR_MESSAGE);
					}
				}else{
					JOptionPane.showMessageDialog(null, "�ļ�������Ϊ��","����BF����Ϣ",JOptionPane.ERROR_MESSAGE);
				}
			    
			}else{
				JOptionPane.showMessageDialog(null, "�㻹û�е�¼","����BF����Ϣ",JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	
	class undoActionListener implements ActionListener{

		public void actionPerformed(ActionEvent arg0) {
			if(ctry>=1){
			    ctry--;
			    textArea.setText(history.get(ctry));
			}else{
				JOptionPane.showMessageDialog(null, "�޷���ǰ","����BF����Ϣ",JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}
	
	class redoActionListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			if(ctry<history.size()-1){
				ctry++;
				textArea.setText(history.get(ctry));
			}else{
				JOptionPane.showMessageDialog(null, "�����������","����BF����Ϣ",JOptionPane.ERROR_MESSAGE);
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
	

