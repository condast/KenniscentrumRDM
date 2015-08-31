package org.rdm.kc.sensornet.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import org.rdm.kc.sensornet.utils.IOUtils;
import org.rdm.kc.sensornet.utils.ProjectFolderUtils;

public class DataStore {

	public static final String ENTRY = "entry";
	
	private static DataStore store = new DataStore();
	
	private DataStore() {
	}
	
	/**
	 * Get the data store
	 * @return
	 */
	public static DataStore getInstance(){
		return store;
	}
	
	public synchronized void storeData( String id, Map<String, String> data ){
		File file = new File( ProjectFolderUtils.getStoreDir( id ));
		if( !file.getParentFile().exists() ){
			file.getParentFile().mkdirs();
		}
		if(!file.exists() ){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		Calendar calendar = Calendar.getInstance();
		PrintWriter out = null;
		try{ 
			out = new PrintWriter(new BufferedWriter(new FileWriter( file, true)));
			out.println( ENTRY + ":" + calendar.getTimeInMillis());
		    Iterator<Map.Entry<String, String>> iterator = data.entrySet().iterator();
		    while( iterator.hasNext() ){
		    	Map.Entry<String, String> next = iterator.next();
		    	out.println( next.getKey() + "," + next.getValue());
		    }
		}catch (IOException e) {
		    e.printStackTrace();
		}
		finally{
			IOUtils.closeWriter( out );
		}
	}
	
	public synchronized Iterator<StoreObject<String, String>> retrieveData( String id, boolean remove ){
		File file = new File( ProjectFolderUtils.getStoreDir( id ));
		Scanner scanner = null;
		Collection<StoreObject<String,String>> results = new ArrayList<StoreObject<String, String>>();
		try {
			scanner = new Scanner( file );
			if( !scanner.hasNext(ENTRY + "*"))
				return results.iterator();
				
			long time = 0;
			String line = scanner.next( ENTRY + "*" );
			do{
				String[] split = line.split("[:]");
				time = Long.parseLong( split[1]);
				Map<String, String> data = new HashMap<String, String>();
				while( scanner.hasNext() ){
					line = scanner.nextLine();
					if( line.startsWith( ENTRY ))
						break;
					split = line.split("[,]");
					data.put(split[0], split[1]);
				}
				results.add(new StoreObject<String,String>( time, data ));
			}
			while( scanner.hasNext( ENTRY + "*" ) );
			if( remove ){
				new PrintWriter(file).close();
			}
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
		finally{
			if( scanner != null )
				scanner.close();
		}
		return results.iterator();
	}
	
}