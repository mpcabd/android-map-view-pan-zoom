// This work is licensed under the GNU Public License (GPL).
// To view a copy of this license, visit http://www.gnu.org/copyleft/gpl.html

// Written by Abd Allah Diab (mpcabd)
// Email: mpcabd ^at^ gmail ^dot^ com
// Website: http://mpcabd.igeex.biz

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public class ObservableMapView extends MapView {
	public ObservableMapView(Context context, String apiKey) { super(context, apiKey); }
	public ObservableMapView(Context context, AttributeSet attrs) { super(context, attrs); }
	public ObservableMapView(Context context, AttributeSet attrs, int defStyle) { super(context, attrs, defStyle); }
	
	private GeoPoint mOldTopLeft;
	private GeoPoint mOldCenter;
	private GeoPoint mOldBottomRight;
	private int mOldZoomLevel = -1;
	
	private MapViewListener mMapViewListener;
	public MapViewListener getMapViewListener() { return mMapViewListener; }
	public void setMapViewListener(MapViewListener value) { mMapViewListener = value; }
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_UP) {
			GeoPoint newCenter = this.getMapCenter();
			GeoPoint newTopLeft = this.getProjection().fromPixels(0, 0);
			GeoPoint newBottomRight = this.getProjection().fromPixels(this.getWidth(), this.getHeight());
			
			if (this.mMapViewListener != null &&
				newTopLeft.getLatitudeE6() == mOldTopLeft.getLatitudeE6() &&
				newTopLeft.getLongitudeE6() == mOldTopLeft.getLongitudeE6()) {
				mMapViewListener.onClick(this.getProjection().fromPixels((int)ev.getX(), (int)ev.getY()));
			}
		}
		return super.onTouchEvent(ev);
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		
		GeoPoint newCenter = this.getMapCenter();
		GeoPoint newTopLeft = this.getProjection().fromPixels(0, 0);
		GeoPoint newBottomRight = this.getProjection().fromPixels(this.getWidth(), this.getHeight());
		int newZoomLevel = this.getZoomLevel();
		
		if (mOldCenter == null)
			mOldCenter = newCenter;
		
		if (mOldTopLeft == null)
			mOldTopLeft = newTopLeft;

		if (mOldBottomRight == null)
			mOldBottomRight = newBottomRight;

		if (newTopLeft.getLatitudeE6() != mOldTopLeft.getLatitudeE6() || newTopLeft.getLongitudeE6() != mOldTopLeft.getLongitudeE6()) {
			if (this.mMapViewListener != null) {
				GeoPoint oldTopLeft, oldCenter, oldBottomRight;
				
				oldTopLeft = mOldTopLeft;
				oldCenter = mOldCenter;
				oldBottomRight = mOldBottomRight;
		
				mOldBottomRight = newBottomRight;
				mOldTopLeft = newTopLeft;
				mOldCenter = newCenter;
				
				mMapViewListener.onPan(oldTopLeft,
									   oldCenter,
									   oldBottomRight,
									   newTopLeft,
									   newCenter,
									   newBottomRight);
			}
		}
		
		if (mOldZoomLevel == -1)
			mOldZoomLevel = newZoomLevel;
		else if (mOldZoomLevel != newZoomLevel && mMapViewListener != null) {
			int oldZoomLevel = mOldZoomLevel;
			GeoPoint oldTopLeft, oldCenter, oldBottomRight;
			oldTopLeft = mOldTopLeft;
			oldCenter = mOldCenter;
			oldBottomRight = mOldBottomRight;
	
			mOldZoomLevel = newZoomLevel;
			mOldBottomRight = newBottomRight;
			mOldTopLeft = newTopLeft;
			mOldCenter = newCenter;
			
			mMapViewListener.onZoom(oldTopLeft,
									oldCenter,
									oldBottomRight,
									newTopLeft,
									newCenter,
									newBottomRight,
									oldZoomLevel,
									newZoomLevel);
		}
	}
}
