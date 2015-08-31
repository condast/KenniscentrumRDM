package org.rdm.aquabots.nmea;

public class NMEAFormatException extends Exception {
	private static final long serialVersionUID = 1L;

	public NMEAFormatException() {
		super();
	}

	public NMEAFormatException(String arg0, Throwable arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public NMEAFormatException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public NMEAFormatException(String arg0) {
		super(arg0);
	}

	public NMEAFormatException( int index, String line, String first, String last ) {
		super(printError( index, line, first, last ));
	}

	public NMEAFormatException( int index, String line, String first ) {
		super(printError( index, line, first, null ));
	}

	public NMEAFormatException(Throwable arg0) {
		super(arg0);
	}	
	
	protected static final String printError( int index, String line, String first, String last ){
		StringBuffer buffer = new StringBuffer();
		buffer.append( first + "[" + index + ", " + line + "]" );
		if(( last != null ) && last.length() > 0  )
			buffer.append( last );
		return buffer.toString();
	}

}
