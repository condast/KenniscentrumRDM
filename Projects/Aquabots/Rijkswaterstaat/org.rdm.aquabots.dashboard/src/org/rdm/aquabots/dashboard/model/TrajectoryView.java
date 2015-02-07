package org.rdm.aquabots.dashboard.model;

import org.rdm.aquabots.dashboard.utils.StringStyler;

public class TrajectoryView {
	public enum Types{
		POINT,
		LINE_STRING,
		POLYGON;

		public static String[] getTypes(){
			String[] strarray = new String[3];
			int index = 0;
			for( Types type: values() ){
				strarray[index++] = type.toString();
			}
			return strarray;
		}

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
	}
	
	private Types type = Types.POINT;

	public Types getType() {
		return type;
	}

	public void setType(Types type) {
		this.type = type;
	}

	public void setType( String type) {
		this.type = Types.valueOf( StringStyler.styleToEnum( type ));
	}

	public String setTrajectory(){
		return "typeSelect1();";//('Point');";//" + this.type.toString() + ");";
	}
	
}
