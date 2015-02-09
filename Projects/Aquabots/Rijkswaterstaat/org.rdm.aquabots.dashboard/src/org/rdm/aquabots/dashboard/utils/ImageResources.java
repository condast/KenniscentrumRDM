package org.rdm.aquabots.dashboard.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.service.ResourceManager;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class ImageResources extends AbstractImages{

	public static final String S_BUNDLE_ID = "org.rdm.aquabots.dashboard";
	public static final String S_RESOURCES = "resources/";
	public static final String S_DOUBLE_ARROW = "double-arrow-";

	public enum Images{
		UP,
		DOWN,
		LEFT,
		RIGHT;
		
		public static String getFile( Images image ){
			StringBuffer buffer = new StringBuffer();
			buffer.append( S_RESOURCES );
			buffer.append( S_DOUBLE_ARROW );
			switch( image ){		
			case UP:
				buffer.append( image.name().toLowerCase());
				break;
			case LEFT:
				buffer.append( image.name().toLowerCase());
				break;
			case DOWN:
				buffer.append( image.name().toLowerCase());
				break;
			case RIGHT:
				buffer.append( image.name().toLowerCase());
				break;
			default:
				break;
			}
			buffer.append( "-64.png" );
			return buffer.toString();
		}
	}
	
	private static ImageResources resources = new ImageResources();
	
	private static Logger logger = Logger.getLogger( ImageResources.class.getName() );
	
	private ImageResources() {
		super( S_RESOURCES, S_BUNDLE_ID);
	}

	public static ImageResources getInstance() {
		return resources;
	}

	@Override
	public void initialise(){
		setImage( Images.UP );
		setImage( Images.LEFT );
		setImage( Images.DOWN );
		setImage( Images.RIGHT );
	}

	public Image getImage( Images image ){
		return super.getImageFromName( image.name() );
	}

	protected void setImage( Images image ){
		super.setImage( Images.getFile(image));
	}
	
	/**
	 * Register the resource with the given name
	 * @param name
	 */
	public static void registerImage( Images image ){
		registerImage( image.name().toLowerCase(), Images.getFile(image));
	}
	
	/**
	 * Register the resource with the given name
	 * @param name
	 */
	public static void registerImage( String name, String file ){
		ResourceManager resourceManager = RWT.getResourceManager();
		if( !resourceManager.isRegistered( name ) ) {
			InputStream inputStream = ImageResources.class.getClassLoader().getResourceAsStream( file );
			try {
				resourceManager.register( name, inputStream );
			} finally {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.log( Level.SEVERE, name + ": " + file );
					e.printStackTrace();
				}
			}
		}		
	}
	
	/**
	 * Get the image with the given name
	 * @param name
	 * @return
	 */
	public static String getImageString( Images image ){
		return Images.getFile(image); 
	}
	
	/**
	 * Set the image for the given control
	 * @param widget
	 * @param name
	 */
	public static void setImage( Control widget, Images image ){
		widget.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
		//registerImage( image );
		if( widget instanceof Label ){
			  Label label = (Label) widget;
			  String src = getImageString( image );
			  label.setText( "Hello<img width='24' height='24' src='" + src + "'/> there " );
			}
		if( widget instanceof Button ){
		  Button button = (Button) widget;
		  String src = getImageString( image );
		  button.setText( "<img width='24' height='24' src='" + src + "'/>" );
		}
	}
}
