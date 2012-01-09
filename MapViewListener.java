// This work is licensed under the GNU Public License (GPL).
// To view a copy of this license, visit http://www.gnu.org/copyleft/gpl.html

// Written by Abd Allah Diab (mpcabd)
// Email: mpcabd ^at^ gmail ^dot^ com
// Website: http://mpcabd.igeex.biz

import com.google.android.maps.GeoPoint;

public interface MapViewListener {
	void onPan(GeoPoint oldTopLeft,
			   GeoPoint oldCenter,
			   GeoPoint oldBottomRight,
			   GeoPoint newTopLeft,
			   GeoPoint newCenter,
			   GeoPoint newBottomRight);
	void onZoom(GeoPoint oldTopLeft,
				GeoPoint oldCenter,
				GeoPoint oldBottomRight,
				GeoPoint newTopLeft,
				GeoPoint newCenter,
				GeoPoint newBottomRight,
				int oldZoomLevel,
				int newZoomLevel);
	void onClick(GeoPoint clickedPoint);
}
