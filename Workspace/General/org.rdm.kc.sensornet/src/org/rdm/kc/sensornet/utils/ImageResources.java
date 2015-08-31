package org.rdm.kc.sensornet.utils;

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

	public static final String S_BUNDLE_ID = "org.rdm.kc.sensornet";
	public static final String S_RESOURCES = "/resources/";

	public static final String S_AQUABOTS = "aquabots.png";
	public static final String S_AQUABOTS2 = "aquabots2.png";
	public static final String S_AQUABOTS3 = "aquabots3.png";

	public static final String S_DOUBLE_ARROW = "double-arrow-";
	public static final String S_BATHYMETRY = "depth.jpg";

	public static final String S_ZOOM_IN = "zoom-in";
	public static final String S_ZOOM_OUT = "zoom-out";

	public enum Images{
		AQUABOTS,
		AQUABOTS2,
		AQUABOTS3,
		UP,
		DOWN,
		LEFT,
		RIGHT,
		ZOOM_IN,
		ZOOM_OUT,
		DEPTH;
		
		public static String getFile( Images image ){
			StringBuffer buffer = new StringBuffer();
			switch( image ){		
			case AQUABOTS:
				buffer.append( S_AQUABOTS );
				break;
			case AQUABOTS2:
				buffer.append( S_AQUABOTS2 );
				break;
			case AQUABOTS3:
				buffer.append( S_AQUABOTS3 );
				break;
			case UP:
				buffer.append( S_DOUBLE_ARROW );
				buffer.append( image.name().toLowerCase());
				buffer.append( "-32.png" );
				break;
			case LEFT:
				buffer.append( S_DOUBLE_ARROW );
				buffer.append( image.name().toLowerCase());
				buffer.append( "-32.png" );
				break;
			case DOWN:
				buffer.append( S_DOUBLE_ARROW );
				buffer.append( image.name().toLowerCase());
				buffer.append( "-32.png" );
				break;
			case RIGHT:
				buffer.append( S_DOUBLE_ARROW );
				buffer.append( image.name().toLowerCase());
				buffer.append( "-32.png" );
				break;
			case ZOOM_IN:
				buffer.append( S_ZOOM_IN );
				buffer.append( "-32.png" );
				break;
			case ZOOM_OUT:
				buffer.append( S_ZOOM_OUT );
				buffer.append( "-32.png" );
				break;
			case DEPTH:
				buffer.append( S_BATHYMETRY );
				break;
			default:
				break;
			}
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
		setImage( Images.AQUABOTS );
		setImage( Images.AQUABOTS2 );
		setImage( Images.AQUABOTS3 );
		setImage( Images.UP );
		setImage( Images.LEFT );
		setImage( Images.DOWN );
		setImage( Images.RIGHT );
		setImage( Images.DEPTH );
	}

	public Image getImage( Images image ){
		return super.getImageFromName( Images.getFile( image ));
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
