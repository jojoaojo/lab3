package lab3.clients;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

import lab3.clients.rest.RestUsersClient;

public class GetUserClient {

	private static Logger Log = Logger.getLogger(GetUserClient.class.getName());

	public static void main(String[] args) throws IOException {

		if (args.length != 3) {
			System.err.println("Use: java " + CreateUserClient.class.getCanonicalName() + " url name password");
			return;
		}

		String serverUrl = args[0];
		String name = args[1];
		String password = args[2];

		var client = new RestUsersClient(URI.create(serverUrl));

		var result = client.getUser(name, password);
		if (result.isOK())
			Log.info("Get user:" + result.value());
		else
			Log.info("Get user failed with error: " + result.error());

	}

}
