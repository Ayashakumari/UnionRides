package mx.bigapps.unionrides.Adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import mx.bigapps.unionrides.R;


/**
 * Created by admin on 21-08-2017.
 */
public class PopupAdapterMarker implements GoogleMap.InfoWindowAdapter {
    LayoutInflater inflater = null;

    public PopupAdapterMarker(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View popup = inflater.inflate(R.layout.popupmarker, null);
        // TextView rate = (TextView) popup.findViewById(R.id.rate);
        TextView tv = (TextView) popup.findViewById(R.id.titleMarker);
        TextView snippet = (TextView) popup.findViewById(R.id.snippet);
        String str = marker.getTitle();
        final String[] str2 = str.split("_");
        tv.setText(str);
        snippet.setText(marker.getSnippet());
       /* rate.setText(str2[2]);
        minoredr.setText("Min order Accept SR "+str2[5]);*/
        //  double rating = Double.parseDouble(String.valueOf(str2[2]));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // tv.setText(new DecimalFormat("##.##").format(rating));
        }


        return (popup);
    }


    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}