package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import service.ExecuteService;
import service.IOService;
import service.UserService;
import serviceImpl.ExecuteServiceImpl;
import serviceImpl.IOServiceImpl;
import serviceImpl.UserServiceImpl;

public class DataRemoteObject extends UnicastRemoteObject implements IOService, UserService, ExecuteService{
	
	private static final long serialVersionUID = 4029039744279087114L;//讲道理这个好像是注释
	
	private IOService iOService;
	private UserService userService;
	private ExecuteService executeService;
	protected DataRemoteObject() throws RemoteException {
		iOService = new IOServiceImpl();
		userService = new UserServiceImpl();
		executeService=new ExecuteServiceImpl();
	}

	@Override
	public boolean writeFile(String file, String userId, String fileName,boolean label) throws RemoteException{

		return iOService.writeFile(file, userId, fileName,label);
	}

	@Override
	public String readFile(String userId, String fileName) throws RemoteException{

		return iOService.readFile(userId, fileName);
	}

	@Override
	public String readFileList(String userId) throws RemoteException{
		
		return iOService.readFileList(userId);
	}
    
	public String readVersionList(String userId,String fileName)throws RemoteException{
		return iOService.readVersionList(userId, fileName);
	}
	@Override
	public boolean login(String username, String password) throws RemoteException {
		
		return userService.login(username, password);
	}

	@Override
	public boolean logout(String username) throws RemoteException {
		
		return userService.logout(username);
	}
    
	public boolean Register(String username,String password) throws RemoteException{
		
		return userService.Register(username,password);
	}

	@Override
	public String execute(String code, String param) throws RemoteException {
		// TODO Auto-generated method stub
		return executeService.execute(code, param);
	}
}
