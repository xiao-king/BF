//鏈嶅姟鍣║serService鐨凷tub锛屽唴瀹圭浉鍚�
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UserService extends Remote{
	public boolean login(String username, String password) throws RemoteException;

	public boolean logout(String username) throws RemoteException;
	
	public boolean register(String username,String pass) throws RemoteException;
}
