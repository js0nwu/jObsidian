import java.io.*;
import java.net.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JTextArea; 
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; 
import javax.swing.JOptionPane;
import java.lang.Runnable;
import java.lang.Object;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List; 
import java.util.HashSet; 
import java.util.Random;
import java.lang.ProcessBuilder; 

public class IRCConnection extends JFrame implements ActionListener{
	String serveraddress;
	String nick;
	String loginID;
	String channeltext; 
	String rnickname;
	String rmessage;
	int delay = 300; 
	Socket socket; 
	BufferedWriter writer;
	BufferedReader reader;
	String line; 
	JTextField server, nickname, login, channel, sendtext;
	JTextField nickpass; 
	JButton connect, send, quit; 
	static JTextArea rmsg; 
	static HashSet<String> exeCommands = new HashSet<String>(); 
	static HashSet<String> classCommands = new HashSet<String>();
	static HashSet<String> jarCommands = new HashSet<String>();
	static HashSet<String> csCommands = new HashSet<String>(); 
	String password; 
	IRCConnection(String title)
	{
		super(title);
		this.init();
		 this.setSize(800,500);
		 this.setVisible(true);
		 this.setResizable(false); 
	}
	void init()
	{
		JLabel lServer = new JLabel("Server:");
		JLabel lNick = new JLabel("Nick:");
		JLabel lChannel = new JLabel("Channel:");
		JLabel lPassword = new JLabel("Password:"); 
		server = new JTextField(10); 
		nickname = new JTextField(10); 
		login = new JTextField(10); 
		channel = new JTextField(10);
		sendtext = new JTextField(60); 
		nickpass = new JTextField(10); 
		JPanel panel = new JPanel();
		panel.add(lServer);
		panel.add(server);
		panel.add(lNick);
		panel.add(nickname);
		panel.add(lChannel);
		panel.add(channel);
		panel.add(lPassword);
		panel.add(nickpass); 
		connect = new JButton("Connect");
		send = new JButton("Send");
		quit = new JButton("Quit");
		rmsg = new JTextArea("", 20, 60);
		rmsg.setEditable(false);
		rmsg.setWrapStyleWord(true); 
		rmsg.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
		connect.addActionListener(this);
		send.addActionListener(this); 
		panel.add(connect);
		panel.add(rmsg); 
		JScrollPane ScrollPanel = new JScrollPane(rmsg); 
		panel.add(ScrollPanel); 
		panel.add(sendtext); 
		panel.add(send); 
		this.add(panel);
	}
	
	@Override
	public void actionPerformed (ActionEvent e)
	{
		if (e.getSource() == connect)
		{
			connectMethod(); 
			password = nickpass.getText().toString().trim();
			new Timer(delay, taskPerformer).start();
			sendtext.setText("PRIVMSG " + channeltext + " :"); 
			 
		}
		else if (e.getSource() == send)
		{
			String toSend = sendtext.getText(); 
			sendData(toSend);
			sendtext.setText("PRIVMSG " + channeltext + " :"); 
		}
	}
	
	public void sendData(String data)
	{
		try {
			writer.write(data + "\r\n");
			writer.flush(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sendtext.setText("PRIVMSG " + channeltext + " :"); 
		rmsg.append("<sent>" + data + "\n"); 
	}
	public String exeExec(String exename, String channel, String rnick, String rmsg)
	{
		try {
			Process p = new ProcessBuilder(exename, channel, rnick, rmsg).start();
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String output = input.readLine();
			System.out.println(output); 
			return output; 

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.toString(); 
		}
	}
	public String csExec(String channel, String rnick, String rmsg, String csname)
	{
		try {
			Process p = new ProcessBuilder("cscompilerun.exe", channel, rnick, rmsg, csname).start();
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String output = input.readLine();
			System.out.println(output); 
			return output; 

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.toString(); 
		}
	}
	public String classExec(String classname, String channel, String rnick, String rmsg)
	{
		try {
			Process p = new ProcessBuilder("java.exe", classname.replace(".class", ""), channel, rnick, rmsg).start();
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String output = input.readLine();
			System.out.println(output); 
			return output; 

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.toString(); 
		}
	}
	public String jarExec(String jarname, String channel, String rnick, String rmsg)
	{
		try {
			Process p = new ProcessBuilder("java.exe", "-jar", jarname, channel, rnick, rmsg).start();
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String output = input.readLine();
			System.out.println(output); 
			return output; 

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.toString(); 
		}
	}
	public void connectMethod()
	{
		try
		{
			connectServer(); 
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(this, ex);
		}
	}
	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {  
            public void run() {
            	IRCConnection guiWin = new IRCConnection("Java IRC");
        		guiWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
		configHashSet(); 
    }
	public static void nullHashSet()
	{
		exeCommands = null;
		classCommands = null;
		jarCommands = null;
		csCommands = null; 
	}
	public static void configHashSet()
	{
		
		String path = ".";
		String files; 
		File folder = new File(path);
		File[] listFiles = folder.listFiles();
		for (int i = 0; i < listFiles.length; i++)
		{
			System.out.println(listFiles[i]); 
			if (listFiles[i].toString().substring(2).endsWith(".exe"))
			{
				String exeFile = listFiles[i].toString().substring(2); 
				System.out.println(exeFile); 
				exeCommands.add(exeFile); 
			
			}
			else if (listFiles[i].toString().substring(2).endsWith(".class"))
			{
				String classFile = listFiles[i].toString().substring(2);
				System.out.println(classFile);
				classCommands.add(classFile); 
			}
			else if (listFiles[i].toString().substring(2).endsWith(".jar"))
			{
				String jarFile = listFiles[i].toString().substring(2);
				System.out.println(jarFile);
				jarCommands.add(jarFile); 
			}
			else if (listFiles[i].toString().substring(2).endsWith(".cs"))
			{
				String csFile = listFiles[i].toString().substring(2);
				System.out.println(csFile);
				csCommands.add(csFile); 
			}
		}
	}
	public void connectServer() throws Exception
	{
		serveraddress = server.getText();
		nick = nickname.getText(); 
		loginID = login.getText();
		channeltext = channel.getText();
		socket = new Socket(serveraddress, 6667);
		socket.setSoTimeout(100);
		writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream( )));
		writer.write("NICK " + nick + "\r\n");
		writer.write("USER " + nick + " 8 * : Java IRC Client\r\n");
		writer.flush(); 
		writer.write("JOIN " + channeltext + "\r\n");
		writer.flush();
		//JOptionPane.showMessageDialog(this, "Connected!");
	}
    public void updateServer() throws Exception 
    {
    	try
    	{
		line = reader.readLine(); 
		
		System.out.println(line); 
		rmsg.append(line + "\n"); 
		if (line.contains(" "))
		{
		{
			if (line.startsWith("PING ")) {
				sendData("PONG " + line.substring(5));
			}
			else if (line.substring(line.indexOf(' ') + 1).toLowerCase().startsWith("privmsg") == true)
				{
				line = line.substring(1).trim();
					String[] tmparr = null;
					tmparr = line.split("!");
					rnickname = tmparr[0].trim();
					tmparr = line.split(":");
					rmessage = tmparr[1].trim();
					System.out.println(rnickname + ">" + rmessage);
					rmsg.append(rnickname + ">" + rmessage + "\n");
					String sayingto = line.split("PRIVMSG ")[1].split(":")[0].substring(0, line.split("PRIVMSG ")[1].split(":")[0].length() - 1).trim();
					System.out.println(sayingto); 
					for (String command : exeCommands)
					{
						if (rmessage.contains("!" + command.replace(".exe", "")))
						{
							sendData(exeExec(command, channeltext, rnickname, rmessage));
						}
					}
					for (String command : classCommands)
					{
						if (rmessage.contains("!" + command.replace(".class", "")))
						{
							sendData(classExec(command, channeltext, rnickname, rmessage)); 
						}
					}
					for (String command : jarCommands)
					{
						if (rmessage.contains("!" + command.replace(".jar", "")))
						{
							sendData(jarExec(command, channeltext, rnickname, rmessage));
						}
					}
					for (String command : csCommands)
					{
						if (rmessage.contains("!" + command.replace(".cs", "")) && exeCommands.contains(command.replace(".cs", ".exe")) == false)
						{
							sendData(csExec(channeltext, rnickname, rmessage, command));
							configHashSet(); 
						}
					}
					if (sayingto.contains(nick) && line.startsWith("!") == false && rnickname != nick)
					{
						System.out.println("Botchat"); 
						sendData(exeExec("botchat.exe", channeltext, rnickname, rmessage));
					}
					String log = exeExec("logmsg.exe", channeltext, rnickname, line + "\n"); 
				}
			else if (line.substring(line.indexOf(' ') + 1).toLowerCase().startsWith("notice") == true)
			{
				line = line.substring(1);
				String[] tmparr = null; 
				tmparr = line.split(":");
				rmessage = tmparr[1].trim();
				System.out.println("NOTICE " + rnickname + ">" + rmessage);
				rmsg.append("NOTICE " + rnickname + ">" + rmessage + "\n");
				String password = nickpass.getText().toString().trim(); 
				if (rmessage.contains("NickServ IDENTIFY"))
				{
					System.out.println("Password: " + "\"" + password + "\""); 
					sendData("PRIVMSG " + "NickServ" + " :IDENTIFY " + password);
				}
			}
			else if (line.substring(line.indexOf(' ') + 1).toLowerCase().startsWith("join") == true)
			{
				line = line.substring(1);
				String[] tmparr = null; 
				tmparr = line.split("!");
				rnickname = tmparr[0].trim();
				System.out.println(rnickname + " joined!");
				rmsg.append(rnickname + " joined!" + "\n");
				int greetindex;
				Random number = new Random();
				greetindex = number.nextInt(18);
				sendData("PRIVMSG " + nick + " :!greet " + greetindex);
			}
			else if (line.substring(line.indexOf(' ') + 1).toLowerCase().startsWith("part") == true || line.substring(line.indexOf(' ') + 1).toLowerCase().startsWith("quit") == true)
			{
				line = line.substring(1);
				String[] tmparr = null; 
				tmparr = line.split("!");
				rnickname = tmparr[0].trim();
				System.out.println(rnickname + " left!");
				rmsg.append(rnickname + " left!" + "\n");
				int greetindex;
				Random number = new Random();
				greetindex = number.nextInt(18);
				sendData("PRIVMSG " + nick + " :!farewell " + greetindex);
			}
				
		}
		}
		else
		{
			System.out.println("None"); 
		}
    	}
    	catch (Exception ex)
    	{
    		//System.out.println(ex); 
    	}
		//if ((line = reader.readLine( )) != null) {
		
		//}
    }
	public void updatefeed() {
		try
		{
			
			updateServer();
		}
		catch (Exception ex)
		{
			System.out.println(ex); 
		}
	}
	ActionListener taskPerformer = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			updatefeed(); 
			
		}
	}; 


}