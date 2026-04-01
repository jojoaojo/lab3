package lab3.clients;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Logger;

import lab3.api.Result;
import lab3.api.User;
import lab3.clients.rest.RestUsersClient;

public class CreateUserClient {

	private static Logger Log = Logger.getLogger(CreateUserClient.class.getName());
	
	public static void main(String[] args) throws IOException {
		
		if( args.length < 6) {
			System.err.println( "Use: java " + CreateUserClient.class.getCanonicalName() + " url name pwd displayName domain phonenumber1 phonenumber2 ... ");
			return;
		}
		String serverUrl = args[0];
		String name = args[1];
		String pwd = args[2];
		String displayName = args[3];
		String domain = args[4];
		String[] phoneNumbers = Arrays.copyOfRange(args, 5, args.length);
		
		User usr = new User( name, pwd, displayName, domain, Set.of(phoneNumbers ));
		
		RestUsersClient client = new RestUsersClient( URI.create( serverUrl ) );
		
		Result<String> result = client.createUser( usr );
		if( result.isOK()  )
			Log.info("Created user:" + result.value() );
		else
			Log.info("Create user failed with error: " + result.error());

	}
	
}
