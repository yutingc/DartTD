package dart.network;

import java.io.IOException;
import java.util.*;
/**
 * A group of {@link ClientHandler}s .
 */
public class ClientPool {
	private LinkedList<ClientHandler> _clients;
	
	/**
	 * Initialize a new {@link ClientPool}.
	 */
	public ClientPool() {
		_clients = new LinkedList<ClientHandler>();
	}
	
	/**
	 * Add a new client to the chat room.
	 * 
	 * @param client to add
	 */
	public void add(ClientHandler client) {
		_clients.add(client);
	}
	
	/**
	 * Remove a client from the pool. 
	 * 
	 * @param client to remove
	 * @return true if the client was removed, false if they were not there.
	 */
	public synchronized boolean remove(ClientHandler client) {
		return _clients.remove(client);
	}
	
	/**
	 * Send the packet to all clients in the pool
	 * 
	 * @param message to send
	 * @param sender the client _not_ to send the message to (send to everyone
	 *          if null)
	 */
	public void broadcast(String message) {
		for (ClientHandler client : _clients) {
			client.send(message);
		}
	}
	
	/**
	 * Close all {@link ClientHandler}s and empty the pool
	 */
	public synchronized void killall() {
		this.broadcast("The server is quitting now. Goodbye.");

		for (ClientHandler client : _clients) {
			try {
				client.kill();
			} catch (IOException e) {
				// There's nothing we can do here.
			}
		}

		_clients.clear();
	}
}

